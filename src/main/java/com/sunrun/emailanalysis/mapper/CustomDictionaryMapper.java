package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.common.KeywordData;
import com.sunrun.emailanalysis.po.CustomDictionary;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface CustomDictionaryMapper extends Mapper<CustomDictionary> {

    List<HashMap<String, Object>> getCustomDictionaryList(@Param("data") KeywordData data);

    Long getCustomDictionaryListCount(@Param("data") KeywordData data);

    HashMap<String, Object> getCustomDictionaryById(@Param("id") Long id);

    Long clear();
}