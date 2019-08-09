package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.account.AccountListData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.AccountMapper;
import com.sunrun.emailanalysis.po.EmailCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private CaseService caseService;

    @Autowired
    private AccountMapper accountMapper;

    private static Logger log = LoggerFactory.getLogger(AccountService.class);



    public List<HashMap<String, Object>> getAccountList(AccountListData inputData) {
        try {
            Long caseId = inputData.getCaseId();
            // 检测项目是否存在
            EmailCase emailCase = caseService.getStatisticCase(caseId);
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }
            // 正常获取数据
            return accountMapper.getAccountList(inputData);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public HashMap<String, Object> getAccountListCount(AccountListData inputData) {
        try {
            Long caseId = inputData.getCaseId();
            // 检测项目是否存在
            EmailCase emailCase = caseService.getStatisticCase(caseId);
            if(emailCase == null){
                throw new EAException("没有该项目的信息", Notice.CASE_IS_NOT_EXIST);
            }


            HashMap<String, Object> result = new HashMap<>();
            // Get total.
            Long total = accountMapper.getAccountListCount(inputData);
            log.info("The total count is {}", total);
            result.put(ResultDictionary.TOTAL, total);
            if(total == null || total.equals(0L)){
                result.put(ResultDictionary.TOTAL, 0);
            }
            return result;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

}