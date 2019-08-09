package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.mapper.CaseTypeMapper;
import com.sunrun.emailanalysis.po.CaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseTypeService {
    @Autowired
    private CaseTypeMapper caseTypeMapper;

    public List<CaseType> getAllCaseType(){
        return caseTypeMapper.selectAll();
    }
}
