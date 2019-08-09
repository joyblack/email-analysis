package com.sunrun.emailanalysis.service.mysql;

import com.sunrun.emailanalysis.data.request.RequestParameterValue;
import com.sunrun.emailanalysis.data.request.email.SearchEmailData;
import com.sunrun.emailanalysis.data.request.statistic.AttachTypeData;
import com.sunrun.emailanalysis.data.request.statistic.StatisticRelationData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.database.StatisticAttachDictionary;
import com.sunrun.emailanalysis.dictionary.database.StatisticRelationDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.common.FileCombineExtension;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.*;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.po.StatisticAttach;
import com.sunrun.emailanalysis.po.StatisticRelation;
import com.sunrun.emailanalysis.service.StatisticService;
import com.sunrun.emailanalysis.tool.common.EmailTool;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    private static Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailRelationMapper emailRelationMapper;

    @Autowired
    private StatisticAttachMapper statisticAttachMapper;

    @Autowired
    private StatisticRelationMapper statisticRelationMapper;

    @Autowired
    private EmailMapper emailMapper;

    @Override
    public Long getCaseTotal() {
        try{
            Long total = emailCaseMapper.getCaseTotal();
            log.info("The total value is {}", total);
            if(total == null){
                return 0L;
            }
            return total;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    @Override
    public Long getEmailTotal(Long caseId) {
        try{
            Long total = emailCaseMapper.getEmailTotal(caseId);
            log.info("The total value is {}", total);
            if(total == null){
                return 0L;
            }
            return total;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }



    @Override
    public HashMap<String, Long> getStatisticRelationListCount(StatisticRelationData data) {
        try{
            EmailCase emailCase = getStatisticCase(data.getCaseId());
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }
            updateStatisticRelation(emailCase);
            Long total = statisticRelationMapper.getStatisticRelationListCount(data);
            if(total == null){
                total = 0L;
            }
            HashMap<String, Long> result = new HashMap<>();
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }


    @Override
    public List<HashMap<String, Object>> getStatisticRelationList(StatisticRelationData data) {
        try{
            EmailCase emailCase = getStatisticCase(data.getCaseId());
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }
            updateStatisticRelation(emailCase);
            return statisticRelationMapper.getStatisticRelationList(data);
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    @Override
    public HashMap<String, Long> getStatisticRelationTotal(StatisticRelationData data) {
        try{
            EmailCase emailCase = getStatisticCase(data.getCaseId());
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }
            updateStatisticRelation(emailCase);
            Long total = statisticRelationMapper.getStatisticRelationTotal(data);
            if(total == null){
                total = 0L;
            }
            HashMap<String, Long> result = new HashMap<>();
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    @Override
    public List<HashMap<String, Object>> getAttachType(AttachTypeData data) {
        try{
            // 最终结果
            List<HashMap<String, Object>> result = new ArrayList<>();
            EmailCase emailCase = getStatisticCase(data.getCaseId());
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }
            // 获取该批次数据的计算时间
            StatisticAttach record = statisticAttachMapper.getSingleByCaseId(emailCase.getCaseId());
            if(record == null || ! record.getCaseUpdateTime().equals(emailCase.getEditTime())){
                if(record == null){
                    log.info("还没有进行过统计...");
                }else{
                    log.info("项目进行过更新，统计数据需要更新...");
                }
                statisticAttachTypeInfo(emailCase);
            }else{
                log.info("该项目的统计数据已经为最新状态，无需更新...");
            }

            // divide or combine?
            if(data.getStatisticType().equals(RequestParameterValue.STATISTIC_TYPE_DIVIDE)){
                log.info("选择分别统计...");
                for (String fileType : data.getFileType()) {
                    List<String> extensionList = FileCombineExtension.getExtensionList(fileType);
                    Long count;
                    // Show include all file type, just set the include to be -1.
                    if (extensionList == null) {
                        count = statisticAttachMapper.statisticAttachType(emailCase.getCaseId(), RequestParameterValue.INCLUDE_ALL, null);
                    }else{
                        count = statisticAttachMapper.statisticAttachType(emailCase.getCaseId(), data.getInclude(), extensionList);
                    }
                    if(count == null){
                        count = 0L;
                    }
                    log.info("类型 {} 统计的数量为: {}", fileType, count);
                    HashMap<String, Object> r = new HashMap<>();
                    r.put(ResultDictionary.FILE_TYPE, fileType);
                    r.put(ResultDictionary.APPEAR_TIME, count);
                    result.add(r);
                }
            }else{
                // Add all type list and just one search is ok.
                log.info("选择组合统计...");
                List<String> extensionList = FileCombineExtension.getSmallFileTypeList(data.getFileType());
                // If the file type list include the `all` type.
                if(extensionList == null){
                    data.setInclude(RequestParameterValue.INCLUDE_ALL);
                }
                Long count = statisticAttachMapper.statisticAttachType(emailCase.getCaseId(), data.getInclude(), extensionList);
                if(count == null){
                    count = 0L;
                }
                log.info("类型 {} 统计的数量为: {}", extensionList, count);
                HashMap<String, Object> r = new HashMap<>();
                r.put(ResultDictionary.FILE_TYPE, ResultDictionary.FILE_TYPE_COMBINE);
                r.put(ResultDictionary.APPEAR_TIME, count);
                result.add(r);
            }
            return result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    // 预先处理，是否需要统计收发关系，并入库.
    public void updateStatisticRelation(EmailCase emailCase){
        StatisticRelation record = statisticRelationMapper.getSingleByCaseId(emailCase.getCaseId());
        if(record == null || !record.getCaseUpdateTime().equals(emailCase.getEditTime())){
            if(record == null){
                log.info("还没有进行过统计...");
            }else{
                log.info("项目进行过更新，统计数据需要更新...");
            }
            statisticRelationInfo(emailCase);
        }else{
            log.info("该项目的统计数据已经为最新状态，无需更新...");
        }
    }

    private void statisticAttachTypeInfo(EmailCase emailCase) {
        try{
            // Delete old case statistic information.
            log.info("开始删除旧统计数据...");
            StatisticAttach condition = new StatisticAttach();
            condition.setCaseId(emailCase.getCaseId());
            int delete = statisticAttachMapper.delete(condition);
            log.info("共删除旧统计数据{}条", delete);

            log.info("开始更新项目{}的附件类型统计信息", emailCase.getCaseName());
            List<HashMap<String, Object>> groupTypeCount = emailAttachMapper.getGroupTypeCount(emailCase.getCaseId());
            log.info("统计的结果为: {}", groupTypeCount);
            log.info("将其更新到统计表...");
            for (HashMap<String, Object> map : groupTypeCount) {
                StatisticAttach record = new StatisticAttach();
                record.setStatisticId(JoyUUID.getUUId());
                record.setCaseId(emailCase.getCaseId());
                record.setCaseUpdateTime(emailCase.getEditTime());
                record.setExtension(map.getOrDefault(StatisticAttachDictionary.FILE_TYPE, "").toString());
                record.setAppearTime(Long.valueOf(map.getOrDefault(StatisticAttachDictionary.APPEAR_TIME, 0).toString()));
                statisticAttachMapper.insertSelective(record);
            }
        }catch (EAException ea){
            log.error("更新统计表时出错: {}", ea.getMessage() );
            throw ea;
        }catch (Exception e){
            log.error("更新统计表时出错: {}", e.getMessage() );
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    private void statisticRelationInfo(EmailCase emailCase) {
        try{
            // Delete old case statistic information.
            log.info("开始删除旧统计数据...");
            StatisticRelation condition = new StatisticRelation();
            condition.setCaseId(emailCase.getCaseId());
            int delete = statisticRelationMapper.delete(condition);
            log.info("共删除旧统计数据{}条", delete);

            log.info("开始更新项目{}的发件人统计信息...", emailCase.getCaseName());
            // 收件人信息只需从email表中统计即可，如果是relation表作为from的记录可能会出现重复
            List<HashMap<String, Object>> data = emailMapper.getGroupFromAddressCount(emailCase.getCaseId());
            log.info("发件人统计记录数: {}", data.size());
            log.info("将其更新到统计表...");
            insertToStatisticRelation(data, emailCase, StatisticRelationDictionary.SEND_TYPE_FROM);

            // 收件人以及抄送者可直接从Relation中进行统计
            log.info("开始更新项目{}的收件人统计信息...", emailCase.getCaseName());
            data = emailRelationMapper.getGroupToAddressCount(emailCase.getCaseId());
            log.info("收件人统计记录数: {}", data.size());
            log.info("将其更新到统计表...");
            insertToStatisticRelation(data, emailCase, StatisticRelationDictionary.SEND_TYPE_TO);

            log.info("开始更新项目{}的抄送人统计信息...", emailCase.getCaseName());
            data = emailRelationMapper.getGroupCcAddressCount(emailCase.getCaseId());
            log.info("抄送人统计记录数: {}", data.size());
            log.info("将其更新到统计表...");
            insertToStatisticRelation(data, emailCase, StatisticRelationDictionary.SEND_TYPE_CC);

            log.info("更新关系统计表完毕...");
        }catch (EAException ea){
            log.error("更新统计表时出错: {}", ea.getMessage() );
            throw ea;
        }catch (Exception e){
            log.error("更新统计表时出错: {}", e.getMessage() );
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    private void insertToStatisticRelation(List<HashMap<String, Object>> data, EmailCase emailCase, String sendType){
        if(data == null){
            log.info("不含有任何数据...");
            return;
        }
        for (HashMap<String, Object> map : data) {
            StatisticRelation record = new StatisticRelation();
            record.setStatisticId(JoyUUID.getUUId());
            record.setCaseId(emailCase.getCaseId());
            record.setCaseUpdateTime(emailCase.getEditTime());
            record.setNickName(map.getOrDefault(StatisticRelationDictionary.NICK_NAME, "").toString());
            record.setAddress(map.getOrDefault(StatisticRelationDictionary.ADDRESS, "").toString());
            record.setAppearTime(Long.valueOf(map.getOrDefault(StatisticRelationDictionary.APPEAR_TIME, 0).toString()));
            record.setSendType(sendType);
            // domainName
            record.setDomainName(EmailTool.getDomainName(record.getAddress()));
            statisticRelationMapper.insertSelective(record);
        }
    }

    private EmailCase getStatisticCase(Long caseId){
        EmailCase emailCase = new EmailCase();
        // All case information.
        if(caseId == null || caseId.equals(0L)){
            emailCase.setEmailPath("NONE");
            emailCase.setCaseId(0L);
            emailCase.setCaseName("all case");

            // 获取当前系统中编辑时间最新的那个项目，将其作为所有项目的统计基准时间.
            EmailCase newestCase = emailCaseMapper.getNewestCase();
            if(newestCase == null){
                // Set null , then throw exception show no case information.
                emailCase = null;
            }else{
                emailCase.setEditTime(newestCase.getEditTime());
            }
        }else{
            // By id to get emailCase information.
            emailCase = emailCaseMapper.selectByPrimaryKey(caseId);
        }
        return emailCase;
    }


}
