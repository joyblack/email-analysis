package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.RequestParameterValue;
import com.sunrun.emailanalysis.data.request.attach.AttachListData;
import com.sunrun.emailanalysis.data.request.entity.EditEntityData;
import com.sunrun.emailanalysis.data.request.RequestParameter;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.ea.recognition.AppearPosition;
import com.sunrun.emailanalysis.ea.recognition.dictionary.EmailIndexDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.common.FileCombineExtension;
import com.sunrun.emailanalysis.joy.file.factory.JoyFileFactory;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.EmailAttachMapper;
import com.sunrun.emailanalysis.mapper.EmailCaseMapper;
import com.sunrun.emailanalysis.mapper.EmailEntityMapper;
import com.sunrun.emailanalysis.mapper.SysConfigMapper;
import com.sunrun.emailanalysis.parser.CommonParser;
import com.sunrun.emailanalysis.po.EmailAttach;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.po.EmailEntity;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Service
public class AttachService {
    private static Logger log = LoggerFactory.getLogger(AttachService.class);

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private EmailEntityMapper emailEntityMapper;

    public List<HashMap<String, Object>> getEmailAttach(Long id) {
        return emailAttachMapper.getEmailAttach(id);
    }

    public HashMap<String, Object> getAttach(Long id) {
        return emailAttachMapper.getAttach(id);
    }

    public HashMap<String, Object> getAttachContent(Long id) {
        HashMap<String, Object> attach = emailAttachMapper.getAttach(id);
        if(attach == null){
            throw new EAException("没有该附件的信息", Notice.ATTACH_NOT_EXSIT);
        }
        Object fileType = attach.get("fileType");
        String attachContent = null;
        if(fileType != null){
            String trimType = fileType.toString().trim();
            if(!trimType.isEmpty() || CommonParser.isSupportExtractContent(trimType)){
                log.info("The trimType is: {}", trimType);
                // 获取索引目录信息
                Long caseId = Long.valueOf(attach.get("caseId").toString());
                EmailCase emailCase = emailCaseMapper.selectByPrimaryKey(caseId);
                String indexPath = sysConfigMapper.getSysConfigValue(emailCase.getAttachPath());
                // 从索引库中检索出该附件的内容信息
                Directory directory;
                try {
                    directory = FSDirectory.open(Paths.get(new File(indexPath).getPath()));
                    // 创建indexReader对象
                    IndexReader indexReader = DirectoryReader.open(directory);
                    // 创建indexSearcher对象
                    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                    // 创建查询
                    Query query = new TermQuery(new Term(EmailIndexDictionary.ATTACH_ID, id.toString()));
                    //执行查询
                    // 参数一  查询对象
                    // 参数二  查询结果返回的最大值
                    TopDocs topDocs = indexSearcher.search(query, 1);
                    log.info("The getAttachContent get hits number: {}", topDocs.totalHits);
                    // 遍历查询结果
                    for (ScoreDoc scoreDoc: topDocs.scoreDocs){
                        //scoreDoc.doc 属性就是document对象的id
                        Document doc = indexSearcher.doc(scoreDoc.doc);
                        log.info("The search result is: {}", doc);
                        attachContent = doc.get(EmailIndexDictionary.ATTACH_CONTENT);
                    }
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        attach.put("attachContent", attachContent);
        return attach;
    }

    public void download(Long attachId, HttpServletResponse response) {
        // Get attach information.
        try{
            log.info("Download attach id = " + attachId);
            // Check
            EmailAttach attach = emailAttachMapper.selectByPrimaryKey(attachId);
            if(attach == null){
                throw new EAException("没有该附件的信息: id = " + attachId, Notice.ATTACH_NOT_EXSIT);
            }
            log.info("Attach info: " + attach);
            FileProtocol protocol = FileProtocol.valueOf(attach.getFileProtocol());
            // Get inputStream
            JoyFile joyFile = JoyFileFactory.factory(protocol);
            joyFile.download(attach.getStorePath(),attach.getFileName(), response);
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public HashMap<String, Object> getAttachListCount(AttachListData attachListData) {
        // Get attach information.
        try {
            HashMap<String, Object> result = new HashMap<>();
            List<String> extList = FileCombineExtension.getSmallFileTypeList(attachListData.getFileType());
            // If the file type list include the `all` type.
            if(extList == null){
                attachListData.setInclude(RequestParameterValue.INCLUDE_ALL);
            }
            // Get total.
            Long total = emailAttachMapper.getAttachListCount(attachListData, extList);
            result.put(ResultDictionary.TOTAL, total == null? 0L : total);
            return result;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }

    }

    public List<HashMap<String, Object>> getAttachList(AttachListData attachListData) {
        // Get attach information.
        try {
            HashMap<String, Object> result = new HashMap<>();
            List<String> extList = FileCombineExtension.getSmallFileTypeList(attachListData.getFileType());
            // If the file type list include the `all` type.
            if(extList == null){
                attachListData.setInclude(RequestParameterValue.INCLUDE_ALL);
            }
            return emailAttachMapper.getAttachList(attachListData, extList);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }

    }

    public EAResult editEntities(EditEntityData data) {
        try{
            // Check attach id.
            Long attachId = data.getId();
            if(attachId == null || attachId.equals(0L)){
                throw new EAException("请输入有效的附件ID信息", Notice.EXECUTE_IS_FAILED);
            }

            // Check attach information.
            EmailAttach attach = emailAttachMapper.selectByPrimaryKey(attachId);
            if(attach == null){
                throw new EAException("无法找到该附件的信息.", Notice.ATTACH_NOT_EXSIT);
            }
            // Update,delete need to consider? If this way, first need to delete old attach's remark entity.
            log.info("Clear all attach remark entity...");
            int num = clearAttachRemarkEntity(attachId);
            log.info("Cleared total {} record.", num);

            // Add new information, if this is null or empty, just clear.
            List<EmailEntity> entities = data.getEntities();
            log.info("Add {} record in this attach remark information...",entities.size());
            if(entities != null){
                for (EmailEntity entity : entities) {
                    entity.setCaseId(attach.getCaseId());
                    entity.setEmailId(attach.getEmailId());
                    entity.setId(JoyUUID.getUUId());
                    entity.setAttachId(attachId);
                    entity.setAppearPosition(AppearPosition.ATTACH_REMARK.getId());
                    // insert
                    emailEntityMapper.insertSelective(entity);
                }
            }
        }catch (EAException e){
            throw e;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
        return EAResult.buildSuccessResult();

    }

    public List<HashMap<String, Object>> getEditEntities(IDData data) {
        try{
            // Check attach id.
            Long attachId = data.getId();
            if(attachId == null || attachId.equals(0L)){
                throw new EAException("请输入有效的附件ID信息", Notice.EXECUTE_IS_FAILED);
            }

            // Check attach information.
            EmailAttach attach = emailAttachMapper.selectByPrimaryKey(attachId);
            if(attach == null){
                throw new EAException("无法找到该附件的信息.", Notice.ATTACH_NOT_EXSIT);
            }
            return emailEntityMapper.getAttachEditEntities(attachId);
        }catch (EAException e){
            throw e;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    // Clear attach all entity(remark entity, appearPosition = attachRemark)
    private int clearAttachRemarkEntity(Long attachId){
        EmailEntity condition = new EmailEntity();
        condition.setAttachId(attachId);
        condition.setAppearPosition(AppearPosition.ATTACH_REMARK.getId());
        return emailEntityMapper.delete(condition);
    }


    public EAResult deleteRemarkEntity(IDData data) {
        try{
            // Check attach id.
            Long id = data.getId();
            if(id == null || id.equals(0L)){
                throw new EAException("请输入有效的实体ID信息", Notice.EXECUTE_IS_FAILED);
            }
            // Check attach information.
            emailEntityMapper.deleteByPrimaryKey(id);
            return EAResult.buildSuccessResult();
        }catch (EAException e){
            throw e;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public EAResult clearRemarkEntities(IDData data) {
        try{
            // Check attach id.
            Long id = data.getId();
            if(id == null || id.equals(0L)){
                throw new EAException("请输入有效的附件ID信息", Notice.EXECUTE_IS_FAILED);
            }
            // Check attach information.
            EmailEntity condition = new EmailEntity();
            condition.setAttachId(id);
            // 出现位置为邮件的备注:AppearPosition.ATTACH_REMARK.ID = 4
            condition.setAppearPosition(AppearPosition.ATTACH_REMARK.getId());
            emailEntityMapper.delete(condition);
            return EAResult.buildSuccessResult();
        }catch (EAException e){
            throw e;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }
}
