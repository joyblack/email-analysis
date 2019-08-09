package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.data.request.query.BasicData;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.po.CaseType;
import com.sunrun.emailanalysis.po.EmailCase;

import java.util.HashMap;
import java.util.List;

public interface CaseService {
    // The basic query.
    List<CaseType> getAllCaseType();

    EmailCase getCaseByName(String caseName);

    EmailCase getCaseById(Long caseId);

    EAResult clearCase(Long id);

    HashMap<String, Object> getCaseList(CaseListData data);

    EmailCase getStatisticCase(Long caseId);
}
