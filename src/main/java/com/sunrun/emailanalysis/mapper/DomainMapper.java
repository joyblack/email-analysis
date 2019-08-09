package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.domain.DomainListData;
import com.sunrun.emailanalysis.po.Domain;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface DomainMapper extends Mapper<Domain> {
    int clearCaseInfo(@Param("caseId") Long caseId);

    List<HashMap<String, Object>> getDomainList(@Param("data") DomainListData data);

    Long getDomainListCount(@Param("data") DomainListData data);
}