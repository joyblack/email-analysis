package com.sunrun.emailanalysis.service.mysql;

import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.factory.JoyFileFactory;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.*;
import com.sunrun.emailanalysis.po.*;
import com.sunrun.emailanalysis.service.CaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {

    private static Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Autowired
    private CaseTypeMapper caseTypeMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailEntityMapper emailEntityMapper;

    @Autowired
    private EmailRelationMapper emailRelationMapper;

    @Override
    public List<CaseType> getAllCaseType(){
        try{
            return caseTypeMapper.selectAll();
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    @Override
    public EmailCase getCaseByName(String caseName){
        try{
            EmailCase condition = new EmailCase();
            condition.setCaseName(caseName);
            return emailCaseMapper.selectOne(condition);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    @Override
    public EmailCase getCaseById(Long caseId){
        try{
            if(caseId == null){
                return  null;
            }else{
                return emailCaseMapper.selectByPrimaryKey(caseId);
            }
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    @Override
    public EAResult clearCase(Long caseId) {

        try{
            // Search Case Information
            EmailCase emailCase = emailCaseMapper.selectByPrimaryKey(caseId);
            if(emailCase == null){
                log.error("项目信息不存在,id = {}" , caseId);
                throw new EAException("项目信息不存在, id = " + caseId, Notice.CASE_IS_NOT_EXIST);
            }

            // Delete Case id
            log.info("Delete the email case...");
            emailCaseMapper.deleteByPrimaryKey(caseId);

            // Delete email.
            log.info("Delete the email...");
            Email email = new Email();
            email.setCaseId(caseId);
            emailMapper.delete(email);

            // Delete attach
            log.info("Delete the attach of case...");
            EmailAttach attach = new EmailAttach();
            attach.setCaseId(caseId);
            emailAttachMapper.delete(attach);

            // Delete entity
            log.info("Delete the entity of case...");
            EmailEntity entity = new EmailEntity();
            entity.setCaseId(caseId);
            emailEntityMapper.delete(entity);

            // Delete relation
            log.info("Delete the relation of case...");
            EmailRelation relation = new EmailRelation();
            relation.setCaseId(caseId);
            emailRelationMapper.delete(relation);

            // Delete Directory
            log.info("Delete the directory(index/email/attach) of case...");
            JoyFile joyFile = JoyFileFactory.factory(FileProtocol.valueOf(emailCase.getProtocol()));
            joyFile.deleteDir(emailCase.getIndexPath());
            joyFile.deleteDir(emailCase.getAttachPath());
            joyFile.deleteDir(emailCase.getEmailPath());
        }catch (EAException ea){
            log.error(ea.getMessage());
            throw ea;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
        return EAResult.buildSuccessResult();
    }


    public HashMap<String, Object> getCaseList(CaseListData inputData) {
        try {
            HashMap<String, Object> result = new HashMap<>();
            // Get total.
            Long total = emailCaseMapper.getCaseListTotal(inputData);
            log.info("The total count is {}", total);
            result.put(ResultDictionary.TOTAL, total);
            if(total == null || total.equals(0L)){
                result.put(ResultDictionary.DATA, null);
                result.put(ResultDictionary.COUNT, 0);
            }else{
                List<HashMap<String, Object>> list = emailCaseMapper.getCaseList(inputData);
                result.put(ResultDictionary.DATA,list);
                result.put(ResultDictionary.COUNT, list.size());
            }
            return result;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    @Override
    public EmailCase getStatisticCase(Long caseId){
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
