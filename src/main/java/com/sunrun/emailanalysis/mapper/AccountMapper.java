package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.account.AccountListData;
import com.sunrun.emailanalysis.po.Account;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface AccountMapper extends Mapper<Account> {
    int clearCaseInfo(@Param("caseId") Long caseId);

    List<HashMap<String, Object>> getAccountByRelation(@Param("caseId") Long caseId);

    List<HashMap<String, Object>> getAccountList(@Param("data") AccountListData data);

    Long getAccountListCount(@Param("data") AccountListData data);
}