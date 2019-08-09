package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.statistic.StatisticRelationData;
import com.sunrun.emailanalysis.po.StatisticRelation;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface StatisticRelationMapper extends Mapper<StatisticRelation> {
    StatisticRelation getSingleByCaseId(@Param("caseId") Long caseId);

    List<HashMap<String, Object>> getStatisticRelationList(@Param("data") StatisticRelationData data);

    Long getStatisticRelationListCount(@Param("data") StatisticRelationData data);

    Long getStatisticRelationTotal(@Param("data")StatisticRelationData data);
}