package com.sunrun.emailanalysis.service.common;

import com.sunrun.emailanalysis.mapper.AccountMapper;
import com.sunrun.emailanalysis.mapper.DomainMapper;
import com.sunrun.emailanalysis.po.Account;
import com.sunrun.emailanalysis.po.Domain;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.tool.common.EmailTool;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
// 解析完成之后，需要对数据进行再刷新
@Service
public class RefreshTableService {
    // 刷新域名表、账户表
    private static Logger log = LoggerFactory.getLogger(RefreshTableService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DomainMapper domainMapper;

    public void updateAccountAndDomain(EmailCase emailCase){
        try{
            // 删除所有旧数据
            accountMapper.clearCaseInfo(emailCase.getCaseId());
            domainMapper.clearCaseInfo(emailCase.getCaseId());
            // 账户列表
            List<HashMap<String, Object>> accountByRelation = accountMapper.getAccountByRelation(emailCase.getCaseId());
            // 域名列表
            HashSet<String> domains = new HashSet<>();
            if(accountByRelation != null && accountByRelation.size() > 0){
                for (HashMap<String, Object> map : accountByRelation) {
                    Object address = map.get("address");
                    if(address != null){
                        Account data = new Account();
                        data.setAccountId(JoyUUID.getUUId());
                        data.setAddress(address.toString());
                        data.setCaseId(emailCase.getCaseId());
                        String domainName = EmailTool.getDomainName(data.getAddress());
                        data.setDomainName(domainName);
                        data.setNickName("");
                        accountMapper.insertSelective(data);
                        // 添加到set中
                        domains.add(domainName);
                    }
                }
            }
            // 插入域名列表
            if(domains.size() > 0){
                for (String d : domains) {
                    Domain domain = new Domain();
                    domain.setDomainId(JoyUUID.getUUId());
                    domain.setCaseId(emailCase.getCaseId());
                    domain.setDomainName(d);
                    domainMapper.insertSelective(domain);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新账户、域名表出错: {}", e.getMessage());
        }finally {
            log.info("更新表完毕...");
        }
    }

}
