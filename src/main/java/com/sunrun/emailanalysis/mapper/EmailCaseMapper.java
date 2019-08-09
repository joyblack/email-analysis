package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.po.EmailCase;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface EmailCaseMapper extends Mapper<EmailCase> {
    Long getCaseListTotal(@Param("data") CaseListData inputData);

    List<HashMap<String, Object>> getCaseList(@Param("data") CaseListData inputData);

    Long getCaseTotal();

    Long getEmailTotal(@Param("caseId") Long caseId);


    EmailCase getNewestCase();
}