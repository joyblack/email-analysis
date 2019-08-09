package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.IpAddress;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IpAddressMapper extends Mapper<IpAddress> {

    IpAddress getIpAddress(@Param("ipValue") long ipValue);
}