package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.SysConfig;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysConfigMapper extends Mapper<SysConfig> {
    String getSysConfigValue(@Param("configName") String configName);
}