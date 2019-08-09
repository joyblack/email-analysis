package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.analysis.EmailAnalysisData;
import com.sunrun.emailanalysis.data.request.analysis.MonitorData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailByAccountData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailByEntityData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.dictionary.common.AnalysisDictionary;
import com.sunrun.emailanalysis.dictionary.common.JudgeDictionary;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.database.CommonDictionary;
import com.sunrun.emailanalysis.dictionary.database.SysConfigDictionary;
import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.dictionary.file.FileTypeExtension;
import com.sunrun.emailanalysis.ea.config.AnalysisConfig;
import com.sunrun.emailanalysis.ea.extract.Extract;
import com.sunrun.emailanalysis.ea.extract.ExtractFactory;
import com.sunrun.emailanalysis.ea.extract.ExtractResult;
import com.sunrun.emailanalysis.ea.recognition.AppearPosition;
import com.sunrun.emailanalysis.ea.recognition.dictionary.EmailIndexDictionary;
import com.sunrun.emailanalysis.ea.recognition.index.EAIndex;
import com.sunrun.emailanalysis.ea.recognition.index.EAIndexFactory;
import com.sunrun.emailanalysis.ea.recognition.ner.Entity;
import com.sunrun.emailanalysis.ea.recognition.ner.Recognition;
import com.sunrun.emailanalysis.ea.recognition.ner.RecognitionFactory;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.factory.JoyFileFactory;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.*;
import com.sunrun.emailanalysis.parser.CommonParser;
import com.sunrun.emailanalysis.parser.html.EAHtmlParser;
import com.sunrun.emailanalysis.po.*;
import com.sunrun.emailanalysis.service.common.RefreshTableService;
import com.sunrun.emailanalysis.task.info.TaskInfo;
import com.sunrun.emailanalysis.task.info.TaskState;
import com.sunrun.emailanalysis.tool.excel.ExcelTool;
import com.sunrun.emailanalysis.tool.tika.TiKaTool;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class EmailService {

    private static Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailRelationMapper emailRelationMapper;

    @Autowired
    private EmailEntityMapper emailEntityMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private Executor asyncExecutor;

    @Autowired
    private HashMap<Long, TaskInfo> analysisTask;

    @Autowired
    private RefreshTableService refreshTableService;

    @Autowired
    private WarningAccountMapper warningAccountMapper;


    public EAResult analysisCheck(EmailAnalysisData data) {
        // 处理输入路径，解析出所需要分析的邮件
        File inputFile = new File(data.getFilePath());
        if(inputFile.isDirectory() || (inputFile.isFile() && (inputFile.getName().endsWith(FileTypeExtension.EMAIL_1) || inputFile.getName().endsWith(FileTypeExtension.EMAIL_2)))){
        }else{
            return EAResult.buildFailedResult(Notice.PATH_IS_NOT_EXIST, "提供的解析路径不是一个有效的路径信息");
        }
        HashMap<String, Long> result = new HashMap<>();
        Long taskId = JoyUUID.getUUId();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setState(TaskState.PREPARE);
        taskInfo.setTaskId(taskId);
        analysisTask.put(taskId,taskInfo);
        return EAResult.buildSuccessResult(taskInfo);
    }

    public EAResult analysis(EmailAnalysisData data){
        HashMap<String, Object> eaResultDataMap = new HashMap<>();
        long start = System.nanoTime();
        TaskInfo taskInfo = null;
        try {
            Long taskId = data.getTaskId();
            // Check task info
            if(taskId == null){
                throw new EAException("请先预检测分析(call 'analysisCheck')获取任务ID识别码", Notice.TASK_NOT_EXSIST);
            }
            taskInfo = analysisTask.get(taskId);
            if(taskInfo == null){
                throw new EAException("不存在该任务的检测信息，请先预检测分析(call 'email/analysisCheck')。", Notice.TASK_NOT_CHECK);
            }
            // Must be prepare state.
            if(!taskInfo.getState().equals(TaskState.PREPARE)){
                switch (taskInfo.getState()){
                    case RUNNING: throw new EAException("任务正在执行，请勿重复请求", Notice.TASK_IS_RUNNING);
                    case COMPLETE: throw new EAException("任务正在完成，切勿重复请求，可以选择删除该任务相关信息(call 'task/deleteTask')", Notice.TASK_IS_COMPLETE);
                    default:
                }
            }

            // Check case information.
            EmailCase condition = new EmailCase();
            condition.setCaseName(data.getCaseName());
            EmailCase oldCase = emailCaseMapper.selectOne(condition);
            final EmailCase emailCase = new EmailCase();
            // 如果是新项目，则新生成项目信息
            if(oldCase == null){
                // Set the email info.
                Long caseId = JoyUUID.getUUId();
                emailCase.setCaseId(caseId);
                emailCase.setCaseName(data.getCaseName());
                emailCase.setEditTime(new Date());
                emailCase.setCreateTime(new Date());
                emailCase.setCaseCode(caseId.toString());
                emailCase.setCreateId(1L);
                emailCase.setCaseType(data.getCaseType());
            }else{
                log.info("Project is already exist, append it: {}", data.getCaseName());
                emailCase.setCaseId(oldCase.getCaseId());
                emailCase.setCaseName(oldCase.getCaseName());
                emailCase.setEditTime(new Date());
                emailCase.setCreateTime(oldCase.getCreateTime());
                emailCase.setCaseCode(oldCase.getCaseCode());
                emailCase.setCreateId(oldCase.getCreateId());
                emailCase.setCaseType(oldCase.getCaseType());
            }
            // == Create the analysis result file store root path(mail,index and attachment)
            FileProtocol protocol = FileProtocol.valueOf(data.getProtocol().toUpperCase());
            JoyFile JoyFile = JoyFileFactory.factory(protocol);
            if(oldCase == null){
                String rootPath = sysConfigMapper.getSysConfigValue(SysConfigDictionary.ANALYSIS_RESULT_PATH);
                emailCase.setProtocol(data.getProtocol().toUpperCase());
                emailCase.setIndexPath(rootPath + File.separator + emailCase.getCaseId() + File.separator + AnalysisDictionary.FILENAME_OF_INDEX);
                emailCase.setAttachPath(rootPath + File.separator + emailCase.getCaseId() + File.separator + AnalysisDictionary.FILENAME_OF_ATTACH);
                emailCase.setEmailPath(rootPath + File.separator + emailCase.getCaseId() + File.separator + AnalysisDictionary.FILENAME_OF_EMAIL);
                // Create the three result directory.
                createAnalysisDirectory(emailCase.getAttachPath(), JoyFile);
                createAnalysisDirectory(emailCase.getEmailPath(), JoyFile);
                createAnalysisDirectory(emailCase.getIndexPath(), JoyFile);
            }else{
                emailCase.setProtocol(oldCase.getProtocol());
                emailCase.setIndexPath(oldCase.getIndexPath());
                emailCase.setAttachPath(oldCase.getAttachPath());
                emailCase.setEmailPath(oldCase.getEmailPath());
            }
            // Handler file path, get the all mail file list.
            List<File> emailFiles = new ArrayList<>();
            File inputFile = new File(data.getFilePath());
            if(inputFile.isDirectory()){
                // filter the email file to analysis.
                emailFiles = JoyFile.listFile(inputFile,
                        f -> f.getName().endsWith(FileTypeExtension.EMAIL_1) || f.getName().endsWith(FileTypeExtension.EMAIL_2),
                        true);
            }else if(inputFile.isFile() && (inputFile.getName().endsWith(FileTypeExtension.EMAIL_1) || inputFile.getName().endsWith(FileTypeExtension.EMAIL_2))){
                // single email: when think this file is email file, then I maybe need to check her type.
                emailFiles.add(inputFile);
            }else{
                log.error("This isn't a path information: {}", data.getFilePath());
                throw new EAException("不是一个有效的路径信息", Notice.PATH_IS_NOT_EXIST);
            }
            // Email total count.
            taskInfo.setTotal((long) emailFiles.size());;
            log.info("Email file check over, total {}, now start analysis: ", taskInfo.getTotal());
            // Set the total email count.
            if(oldCase != null){
                emailCase.setTotalEmail(oldCase.getTotalEmail() + taskInfo.getTotal());
            }else{
                emailCase.setTotalEmail(taskInfo.getTotal());
            }
            // Insert email case information to database.
            if(oldCase == null){
                emailCaseMapper.insertSelective(emailCase);
            }else{
                emailCaseMapper.updateByPrimaryKeySelective(emailCase);
            }

            // Recognition(Index and NER), the two component can used every task rather than every email file.
            EAIndex index = EAIndexFactory.buildDefaultEAIndex(emailCase.getIndexPath(), protocol);
            Recognition recognition = RecognitionFactory.buildDefaultRecognition();

            // Change task state.
            taskInfo.setState(TaskState.RUNNING);
            analysisTask.put(taskId, taskInfo);

            // start word
            log.info("***************************************");
            log.info("开始解析任务，本次任务信息如下：");
            log.info("任务ID：{}", taskInfo.getTaskId());
            log.info("开始时间: {}", new Date());
            log.info("所属项目ID：{}", emailCase.getCaseId());
            log.info("所属项目名称：{}", emailCase.getCaseName());
            log.info("解析邮件的总数：{}", taskInfo.getTotal());
            log.info("***************************************");


            // 多线程解析并加锁
            CountDownLatch countDownLatch = new CountDownLatch(taskInfo.getTotal().intValue());
            // Every email file used a thread.
            for (File emailFile : emailFiles) {
                asyncExecutor.execute(() -> {
                    try {
                        asyncAnalysis(emailCase, emailFile, index, recognition, data.getAnalysisConfig());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                log.info("block now main thread.");
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error("block error...", e);
            }

            // 关闭lucene索引
            log.info("Close lucene index.");
            index.close();// Last must be call.

            // 计算耗时
            long usedTime = (System.nanoTime() - start) / 1000 / 1000;
            eaResultDataMap.put("usedTime", usedTime);

            // 计算本次解析账户生成的域名、账户等信息，更新域名、账户表
            log.info("Analysis success, Now refresh table information...");
            refreshTableService.updateAccountAndDomain(emailCase);
            log.info("Refresh table success, all is end...");

            // 如果开启了告警邮件配置，则计算告警邮件，并更新被标注为告警邮件的列表
            if(data.getAnalysisConfig().getEnableWarningAccount().equals(Boolean.TRUE)){
                // 获取告警账户列表
                List<WarningAccount> accounts = warningAccountMapper.getAccountListByState(CommonDictionary.STATE_ENABLE);
                if(accounts != null && accounts.size() > 0){
                    emailMapper.updateWarningState(accounts, emailCase.getCaseId());
                }
                // 初始化告警账户的状态，全部设置为0（待考虑）
            }
            // 修改任务状态
            if(taskInfo != null){
                taskInfo.setState(TaskState.COMPLETE);
                analysisTask.put(taskInfo.getTaskId(), taskInfo);
            }
            // 清空线程池
            ((ThreadPoolTaskExecutor)asyncExecutor).initialize();
            log.info("Analysis end, used time(ms):  " + usedTime);
            return EAResult.buildSuccessResult(eaResultDataMap);
        } catch (EAException e) {
            throw new EAException(e.getMessage(), e.getNotice());
        }catch (Exception e) {
            throw new EAException(e.getMessage());
        }
    }

    private void asyncAnalysis(EmailCase emailCase, File emailFile, EAIndex index, Recognition recognition, AnalysisConfig config) throws Exception{
        log.info("*************************************");
        log.info("解析邮件: {}", emailFile.getAbsolutePath());
        long t1 = System.nanoTime();
        // ==== 提取邮件相关信息
        Extract extract = ExtractFactory.buildExtract(emailCase, emailFile);
        ExtractResult extractResult = extract.extract();
        log.info("{}：提取邮件信息耗时(ms): {}", emailFile.getAbsolutePath(), (System.nanoTime() - t1)/1000/1000);

        // ==== 存储邮件的信息
        storeEmailInfo(emailCase, extractResult);

        // ==== 实体识别: textContent和htmlTextContent部分
        Email email = extractResult.getEmail();
        if(config.getEnableEntity().equals(Boolean.TRUE)){
            // subject
            long t2 = System.nanoTime();
            HashMap<String, Entity> subjectContentEntity = recognition.ner(email.getEmailSubject());
            insertEntityToDB(emailCase, email, null, subjectContentEntity, AppearPosition.SUBJECT);
            log.info("{}:实体识别(subject)耗时(ms): {}", emailFile.getAbsolutePath(),(System.nanoTime() - t2)/1000/1000);

            // textContent
            long t3 = System.nanoTime();
            HashMap<String, Entity> textContentEntity = recognition.ner(email.getTextContent());
            insertEntityToDB(emailCase, email, null, textContentEntity, AppearPosition.CONTENT);
            log.info("{}:邮件正文识别(textContent)耗时(ms): {}",  emailFile.getAbsolutePath(),(System.nanoTime() - t3)/1000/1000);
        }
        // ==== 邮件信息全文索引: 内容索引可以从extractResult中提取所需内容，无需传入
        long t4 = System.nanoTime();
        try{
            index.indexEmail(extractResult);
        }catch (Exception e){
            log.info("Index error: " + e.getMessage());
        }
        log.info("{}:建立邮件信息全文索引耗时(ms): {}",  emailFile.getAbsolutePath(), (System.nanoTime() - t4)/1000/1000);


        // == 处理邮件附件
        List<EmailAttach> attaches = extractResult.getAttaches();
        for (EmailAttach attach : attaches) {
            log.info("{}:Handler attach ({})...",  emailFile.getAbsolutePath(), attach.getFileName());
            String attachContent = getAttachContent(attach);
            if(attachContent == null || attachContent.isEmpty()){
                continue;
            }
            // ==== 实体识别
            if(config.getEnableEntity().equals(Boolean.TRUE)) {
                long t8 = System.nanoTime();
                HashMap<String, Entity> attachEntity = recognition.ner(attachContent);
                log.info("{}：识别附件内容实体耗时(ms): {}",  emailFile.getAbsolutePath(),(System.nanoTime() - t8) / 1000 / 1000);

                long t9 = System.nanoTime();
                insertEntityToDB(emailCase, email, attach, attachEntity, AppearPosition.ATTACH);
                log.info("{}：插入附件识别的实体到数据库耗时(ms): {}",  emailFile.getAbsolutePath(),(System.nanoTime() - t9) / 1000 / 1000);
            }

            // ==== 附件信息（信息、内容）全文索引
            long t11 = System.nanoTime();
            index.indexAttach(extractResult, attach, attachContent);
            log.info("{}：建立附件索引耗时(ms): {}",  emailFile.getAbsolutePath(),(System.nanoTime() - t11) / 1000 / 1000);
        }
        log.info("{}：整个邮件共花费(ms):{} ",  emailFile.getAbsolutePath(),(System.nanoTime() - t1) / 1000 / 1000);
    }

    // Insert email information to database.
    private void storeEmailInfo(EmailCase emailCase, ExtractResult extractResult){
        System.out.println("Start: 存储邮件信息...");
        // 1.email info
        Email email = extractResult.getEmail();
        email.setCreateTime(new Date());
        email.setCaseId(emailCase.getCaseId());

        emailMapper.insertSelective(email);

        // 2.receiver info(to,bc,bcc)
        List<EmailRelation> relations = extractResult.getRelations();
        for (EmailRelation relation : relations) {
            emailRelationMapper.insertSelective(relation);
        }

        // 3.attach and content info
        List<EmailAttach> attaches = extractResult.getAttaches();
        for (EmailAttach attach : attaches) {
            emailAttachMapper.insertSelective(attach);
        }

    }


    // Insert extract Entity to database.
    private void insertEntityToDB(EmailCase emailCase, Email email,  EmailAttach attach, HashMap<String, Entity> data, AppearPosition position){
        List<EmailEntity> list = new ArrayList<>();
        for (Map.Entry<String, Entity> entry : data.entrySet()) {
            EmailEntity ef = new EmailEntity();
            // id
            ef.setId(JoyUUID.getUUId());
            ef.setAppearPosition(position.getId());
            ef.setEmailId(email.getEmailId());
            ef.setEntityType(entry.getValue().name());
            ef.setEntityValue(entry.getKey());
            ef.setCaseId(emailCase.getCaseId());
            // [problem] We not save the the attach id, we did.
            ef.setAttachId(attach == null? 0L: attach.getAttachId());
            list.add(ef);
        }

        if(list.size() > 0){
            try{
                emailEntityMapper.batchInsert(list);
            }catch (Exception e){
                log.error("Batch insert entity error: {}", e.getMessage());
            }
        }
    }

    private String getAttachContent(EmailAttach attach){
        // get the attach content.
        String storePath = attach.getStorePath();
        // the parser content.
        String content = null;
        // get parser type.
        CommonParser.ParserType parserType = CommonParser.getParserType(attach.getFileType());
        switch (parserType) {
            case TYPE_EXCEL:
                // use easy excel to parse
                content = ExcelTool.getContent(storePath);
                break;
            case TYPE_NOT_SUPPORT: return  null;
            case TYPE_HTML:
                content = EAHtmlParser.getTextFromHtmlContent(storePath);
                break;
            case TYPE_EMAIL:
                System.out.println("Find a email attach file, Now you not handler it...");
                break;
            case TYPE_OTHER:
                content = TiKaTool.getContent(storePath);
                break;
        }
        return content;
    }

    // 获取当前分析的状态
    public HashMap<String, Object> monitor(MonitorData data){
        HashMap<String, Object> result = new HashMap<>();
        // task information
        TaskInfo taskInfo = analysisTask.get(data.getTaskId());
        if(taskInfo == null){
            throw new EAException("不存在该任务的信息");
        }
        result.put("taskId", taskInfo.getTaskId());
        result.put("taskState", taskInfo.getState());
        // 邮件总数
        result.put("total", taskInfo.getTotal());
        ThreadPoolExecutor tpe = ((ThreadPoolTaskExecutor) asyncExecutor).getThreadPoolExecutor();
        // 排队邮件总数
        result.put("queue", tpe.getQueue().size());
        // 已经完成的任务数量
        long completed = tpe.getCompletedTaskCount();
        // == 如果已经完成了，tpe.getCompletedTaskCount()可能会被初始化为0，因此设置为任务总数是最好的。
        if(taskInfo.getState().equals(TaskState.COMPLETE)){
            completed = taskInfo.getTotal();
        }
        result.put("complete", completed);
        // 活动线程数
        result.put("active", tpe.getActiveCount());
        // 总线程数
        result.put("totalTask", tpe.getTaskCount());
        // 百分比
        DecimalFormat df = new DecimalFormat(".00");
        result.put("percent", completed == 0? 0 : df.format(((double)completed) / taskInfo.getTotal() * 100));

        int corePoolSize = tpe.getCorePoolSize();
        result.put("corePoolSize", corePoolSize);

        int largestPoolSize = tpe.getLargestPoolSize();
        result.put("largestPoolSize", largestPoolSize);

        int maximumPoolSize = tpe.getMaximumPoolSize();
        result.put("maximumPoolSize", maximumPoolSize);

        return result;
    }


    public HashMap<String, Object> getBasicInfo(Long id) {
        return emailMapper.getBasicInfo(id);
    }

    public HashMap<String, Object> getContentInfo(Long id) {
        return emailMapper.getContentInfo(id);
    }



    public HashMap<String, Object> getEmailListCountByAccount(SearchEmailByAccountData input) {
        HashMap<String, Object> result = new HashMap<>();
        try{
            Long total = emailMapper.getEmailListCountByAccount(input);
            if(total == null){
                total = 0L;
            }
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public List<HashMap<String, Object>> getEmailListByAccount(SearchEmailByAccountData input){
        try{
            return emailMapper.getEmailListByAccount(input);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public HashMap<String, Object> getEmailListCountByEntityValue(SearchEmailByEntityData input) {
        HashMap<String, Object> result = new HashMap<>();
        try{
            Long total = emailMapper.getEmailListCountByEntityValue(input);
            if(total == null){
                total = 0L;
            }
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public List<HashMap<String, Object>> getEmailListByEntityValue(SearchEmailByEntityData input){
        try{
            return emailMapper.getEmailListByEntityValue(input);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }



    public HashMap<String, Object> getEmailListCount(SearchEmailData input) {
        HashMap<String, Object> result = new HashMap<>();
        Long total;
        try{
            log.info("Search case id：{}", input.getCaseId());

            // 是否是全文检索
            if(input.getContent() != null){
                log.info("Full text search count...");
                total = getEmailCountByFullText(input);
            }else{
                log.info("Simple search count...");
                // 查询总条数
                total = emailMapper.getEmailListCount(input);
                if(total == null){
                    total = 0L;
                }
            }
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public List<HashMap<String, Object>> getEmailList(SearchEmailData input){
        try{
            log.info("Search case id：{}", input.getCaseId());
            // Judge whether if full text search.
            if(input.getContent() != null){
                log.info("Full text search...");
                return getEmailByFullText(input);
            }else{
                log.info("Simple search...");
                return emailMapper.getEmailList(input);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    // 从邮件信息中搜索
    private List<HashMap<String, Object>> getEmailByFullText(SearchEmailData input) {
        List<HashMap<String, Object>> data = new ArrayList<>();
        String content = input.getContent();
        Long caseId = input.getCaseId();
        log.info("Search content: {}", content);
        try {
            // 打开索引库：如果没提供 caseId 则检索当前的所有库
            List<EmailCase> emailCases = new ArrayList<>();
            // == 创建multiReader对象
            if(caseId == null){
                emailCases.addAll(emailCaseMapper.selectAll());
            }else{
                emailCases.add(emailCaseMapper.selectByPrimaryKey(caseId));
            }
            IndexReader[] indexReaders = new IndexReader[emailCases.size()];
            log.info("共联合了{}个索引库", indexReaders.length);
            for (int i = 0; i< emailCases.size(); i++ ) {
                indexReaders[i] = DirectoryReader.open(FSDirectory.open(Paths.get(new File(emailCases.get(i).getIndexPath()).getPath())));
            }
            MultiReader multiReader = new MultiReader(indexReaders);
            // 创建Searcher.
            IndexSearcher indexSearcher = new IndexSearcher(multiReader);
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            // 精确和非精确（对输入关键字进行分词）查询:精准查询则将当前词汇前后加上 " 号。
            // TermQuery 是旧方式，但是和业务中的精准查询有问题
            if(input.getIsExact().equals(JudgeDictionary.YES)){
                content = "\"" + content + "\"";
            }
            try {
                if(input.getSubject() != null){
                    builder.add(new QueryParser(EmailIndexDictionary.SUBJECT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.MUST);
                }
                if(input.getContent() != null){
                    builder.add(new QueryParser(EmailIndexDictionary.ATTACH_CONTENT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.SHOULD);
                    builder.add(new QueryParser(EmailIndexDictionary.TEXT_CONTENT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.SHOULD);
                }
                if(input.getSendTimeStart() != null || input.getSendTimeEnd() != null){
                    builder.add(TermRangeQuery.newStringRange(EmailIndexDictionary.SEND_TIME, input.getSendTimeStart(), input.getSendTimeEnd(), true, false), BooleanClause.Occur.MUST);
                }
                if(input.getFromIp() != null){
                    builder.add(new TermQuery(new Term(EmailIndexDictionary.FROM_IP,input.getFromIp())), BooleanClause.Occur.MUST);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new EAException("创建查询对象时发生错误.", Notice.EXECUTE_IS_FAILED);
            }

            // 起始记录ID
            int start = input.getPagination().getPageIndex() * input.getPagination().getPageSize();

            int num = (input.getPagination().getPageIndex() + 1) * input.getPagination().getPageSize();

            // CollectionStatistics
            // Not be set Integer.MAX_VALUE replaced use num, the totalHits already be get.
            BooleanQuery booleanQuery = builder.build();
            log.info("查询表达式为:" + booleanQuery.toString());
            TopDocs topDocs = indexSearcher.search(booleanQuery, num);
            // total
            long totalHits = topDocs.totalHits;
            log.info("本次查询共搜索到邮件数据: {}条.", totalHits);
            long end = Math.min(num, totalHits);
            log.info("预备获取编号{} - {} 的数据.", start + 1, end);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = start; i < end; i++) {
                Document doc = indexSearcher.doc(scoreDocs[i].doc);
                data.add(contentDoc2EmailInfo(doc));
            }
            // 关闭reader
            multiReader.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    // 在索引写入的时候是否考虑存放一个字段表明该条索引是来源email还是attach
    private HashMap<String, Object> contentDoc2EmailInfo(Document doc){
        HashMap<String, Object> result = new HashMap<>();
        result.put(EmailIndexDictionary.EMAIL_ID, doc.get(EmailIndexDictionary.EMAIL_ID));
        result.put(EmailIndexDictionary.SUBJECT, doc.get(EmailIndexDictionary.SUBJECT));
        result.put(EmailIndexDictionary.CASE_ID, doc.get(EmailIndexDictionary.CASE_ID));
        result.put(EmailIndexDictionary.FROM_NAME, doc.get(EmailIndexDictionary.FROM_NAME));
        result.put(EmailIndexDictionary.FROM_ADDRESS, doc.get(EmailIndexDictionary.FROM_ADDRESS));
        result.put(EmailIndexDictionary.TO_NAME, doc.get(EmailIndexDictionary.TO_NAME));
        result.put(EmailIndexDictionary.TO_ADDRESS, doc.get(EmailIndexDictionary.TO_ADDRESS));
        result.put(EmailIndexDictionary.CREATE_TIME, doc.get(EmailIndexDictionary.CREATE_TIME));
        result.put(EmailIndexDictionary.SEND_TIME, doc.get(EmailIndexDictionary.SEND_TIME));
        result.put(EmailIndexDictionary.OLD_FILE_NAME, doc.get(EmailIndexDictionary.OLD_FILE_NAME));
        result.put(EmailIndexDictionary.NEW_FILE_NAME, doc.get(EmailIndexDictionary.NEW_FILE_NAME));
        //result.put(EmailIndexDictionary.HTML_TEXT_CONTENT, doc.get(EmailIndexDictionary.HTML_TEXT_CONTENT));
        //result.put(EmailIndexDictionary.TEXT_CONTENT, doc.get(EmailIndexDictionary.TEXT_CONTENT));
        //result.put(EmailIndexDictionary.ATTACH_CONTENT, doc.get(EmailIndexDictionary.ATTACH_CONTENT));
        //result.put(EmailIndexDictionary.ATTACH_ID, doc.get(EmailIndexDictionary.ATTACH_ID));
        return result;
    }

    // Get the data record count from index library.
    private Long getEmailCountByFullText(SearchEmailData input) {
        String content = input.getContent();
        Long caseId = input.getCaseId();
        log.info("Search content: {}", content);
        try {
            // 打开索引库：如果没提供 caseId 则检索当前的所有库
            List<EmailCase> emailCases = new ArrayList<>();
            // == 创建multiReader对象
            if(caseId == null){
                emailCases.addAll(emailCaseMapper.selectAll());
            }else{
                emailCases.add(emailCaseMapper.selectByPrimaryKey(caseId));
            }
            IndexReader[] indexReaders = new IndexReader[emailCases.size()];
            log.info("共联合了{}个索引库", indexReaders.length);
            for (int i = 0; i< emailCases.size(); i++ ) {
                indexReaders[i] = DirectoryReader.open(FSDirectory.open(Paths.get(new File(emailCases.get(i).getIndexPath()).getPath())));
            }
            MultiReader multiReader = new MultiReader(indexReaders);
            // 创建Searcher.
            IndexSearcher indexSearcher = new IndexSearcher(multiReader);
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            // 精确和非精确（对输入关键字进行分词）查询:精准查询则将当前词汇前后加上 " 号。
            // TermQuery 是旧方式，但是和业务中的精准查询有问题
            if(input.getIsExact().equals(JudgeDictionary.YES)){
                content = "\"" + content + "\"";
            }
            try {
                if(input.getSubject() != null){
                    builder.add(new QueryParser(EmailIndexDictionary.SUBJECT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.SHOULD);
                }
                if(input.getContent() != null){
                    builder.add(new QueryParser(EmailIndexDictionary.ATTACH_CONTENT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.SHOULD);
                    builder.add(new QueryParser(EmailIndexDictionary.TEXT_CONTENT, new SmartChineseAnalyzer()).parse(content), BooleanClause.Occur.SHOULD);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new EAException("创建查询对象时发生错误.", Notice.EXECUTE_IS_FAILED);
            }
            // CollectionStatistics
            // Not be set Integer.MAX_VALUE replaced use num, the totalHits already be get.
            TopDocs topDocs = indexSearcher.search(builder.build(), 1);
            // total
            long totalHits = topDocs.totalHits;
            // 关闭reader
            multiReader.close();
            return totalHits;

        } catch (Exception e) {
            log.error(e.getMessage());
            return 0L; // 总条数
        }
    }



    private void createAnalysisDirectory(String path, JoyFile file){
        boolean result = file.mkdir(path);
        if(!result){
            throw new EAException("创建文件夹'" + path  +"'出错，请检查系统配置: analysis_result_path");
        }
    }

    public void download(Long emailId, HttpServletResponse response) {
        // Get attach information.
        try{
            log.info("Download email id = " + emailId);
            // Check
            Email email = emailMapper.selectByPrimaryKey(emailId);
            if(email == null){
                log.error("Email is not exist,id = " + emailId);
                throw new EAException("没有该邮件的信息: id = " + emailId, Notice.ATTACH_NOT_EXSIT);
            }
            log.info("Email info: " + email);
            FileProtocol protocol = FileProtocol.valueOf(email.getFileProtocol());
            // Get inputStream
            JoyFile joyFile = JoyFileFactory.factory(protocol);
            joyFile.download(email.getStorePath(),email.getFileName(), response);
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }


    public HashMap<String,String> getRelationData(IDData data) {
        try {
            Long emailId = data.getId();
            // 获取邮件收发列表
            EmailRelation condition = new EmailRelation();
            condition.setEmailId(emailId);
            List<EmailRelation> relations = emailRelationMapper.select(condition);

            List<String> from = new ArrayList<>();
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();

            log.info("该邮件的收发关系数据为:{}", relations);

            if(relations != null && relations.size() > 0){
                from.add(relations.get(0).getFromAddress());
                for (EmailRelation relation : relations) {
                    switch (relation.getSendType()) {
                        case "TO":
                            to.add(relation.getToAddress());
                            break;
                        case "CC":
                            cc.add(relation.getToAddress());
                            break;
                        case "BCC":
                            bcc.add(relation.getToAddress());
                            break;
                    }
                }
            }

            HashMap<String,String> result = new HashMap<>();
            result.put("from", from.toString().replace("[","").replace("]",""));
            result.put("to", to.toString().replace("[","").replace("]",""));
            result.put("cc", cc.toString().replace("[","").replace("]",""));
            result.put("bcc", bcc.toString().replace("[","").replace("]",""));
            return  result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }
}
