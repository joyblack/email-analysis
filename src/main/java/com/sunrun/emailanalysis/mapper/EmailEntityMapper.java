package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.entity.EntityListData;
import com.sunrun.emailanalysis.ea.recognition.ner.Entity;
import com.sunrun.emailanalysis.po.EmailEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface EmailEntityMapper extends Mapper<EmailEntity> {
    List<HashMap<String, Object>> getEntityList(@Param("data") EntityListData data);

    Long getEntityListCount(@Param("data") EntityListData data);

    List<HashMap<String, Object>> getAttachEditEntities(@Param("attachId") Long attachId);

    void batchInsert(@Param("data")List<EmailEntity> data);
}