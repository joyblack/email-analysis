package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.statistic.AttachTypeData;
import com.sunrun.emailanalysis.data.request.statistic.StatisticRelationData;

import java.util.HashMap;
import java.util.List;

public interface StatisticService {
    Long getCaseTotal();

    Long getEmailTotal(Long caseId);

    List<HashMap<String, Object>> getAttachType(AttachTypeData data);

    List<HashMap<String, Object>> getStatisticRelationList(StatisticRelationData data);

    HashMap<String, Long> getStatisticRelationListCount(StatisticRelationData data);

    HashMap<String, Long> getStatisticRelationTotal(StatisticRelationData data);
}
