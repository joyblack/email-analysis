package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.EmailRelation;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface EmailRelationMapper extends Mapper<EmailRelation> {
    // getGroupFromAddressCount is write in EmailMapper.
    List<HashMap<String, Object>> getGroupToAddressCount(@Param("caseId") Long caseId);

    List<HashMap<String, Object>> getGroupCcAddressCount(@Param("caseId") Long caseId);

}