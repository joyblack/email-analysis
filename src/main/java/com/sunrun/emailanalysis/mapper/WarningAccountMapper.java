package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountListData;
import com.sunrun.emailanalysis.po.WarningAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface WarningAccountMapper extends Mapper<WarningAccount> {
    HashMap<String, Object> getWarningAccount(@Param("accountId") Long accountId);

    Long getAccountListCount(@Param("data") WarningAccountListData data);

    List<HashMap<String, Object>> getAccountList(@Param("data") WarningAccountListData data);

    void setState(@Param("state") Integer state, @Param("ids") List<Long> ids);

    List<WarningAccount> getAccountListByState(@Param("state") Integer state);
}