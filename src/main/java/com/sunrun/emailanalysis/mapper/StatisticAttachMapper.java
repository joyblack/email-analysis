package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.StatisticAttach;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface StatisticAttachMapper extends Mapper<StatisticAttach> {

    StatisticAttach getSingleByCaseId(@Param("caseId") Long caseId);

    Long statisticAttachType(@Param("caseId") Long caseId, @Param("include") Integer include, @Param("extensionList") List<String> extensionList);
}