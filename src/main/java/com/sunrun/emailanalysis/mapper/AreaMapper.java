package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.Area;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AreaMapper extends Mapper<Area> {

    Area getAreaByMergerName(@Param("mergerName") String mergerName);

    Area getAreaByZipCode(@Param("zipCode") String zipCode);
}