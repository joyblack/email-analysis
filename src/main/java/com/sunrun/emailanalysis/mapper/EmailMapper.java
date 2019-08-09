package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.email.SearchEmailByAccountData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailByEntityData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailData;
import com.sunrun.emailanalysis.po.Email;
import com.sunrun.emailanalysis.po.WarningAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface EmailMapper extends Mapper<Email> {

    Long getEmailListCount(@Param("data") SearchEmailData data);

    List<HashMap<String, Object>> getEmailList(@Param("data")  SearchEmailData data);

    Long getEmailListCountByEntityValue(@Param("data") SearchEmailByEntityData data);

    List<HashMap<String, Object>> getEmailListByEntityValue(@Param("data")  SearchEmailByEntityData data);

    HashMap<String, Object> getBasicInfo(@Param("emailId")Long emailId);

    HashMap<String, Object> getContentInfo(@Param("emailId")Long id);

    List<HashMap<String, Object>> getGroupFromAddressCount(@Param("caseId") Long caseId);

    Long getEmailListCountByAccount(@Param("data")SearchEmailByAccountData input);

    List<HashMap<String, Object>> getEmailListByAccount(@Param("data") SearchEmailByAccountData input);

    void updateWarningState(@Param("accounts") List<WarningAccount> accounts, @Param("caseId") Long caseId);
}