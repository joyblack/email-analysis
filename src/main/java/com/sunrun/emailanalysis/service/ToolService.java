package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.returned.ComplexAddress;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.mapper.AreaMapper;
import com.sunrun.emailanalysis.mapper.IpAddressMapper;
import com.sunrun.emailanalysis.po.Area;
import com.sunrun.emailanalysis.po.IpAddress;
import com.sunrun.emailanalysis.tool.common.IPTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolService {
    private static Logger log = LoggerFactory.getLogger(ToolService.class);

    @Autowired
    private IpAddressMapper ipAddressMapper;

    @Autowired
    private AreaMapper areaMapper;

    public ComplexAddress getAddressByIP(String ip) {
        try {
            ComplexAddress address = new ComplexAddress();
            // You should change.
            long ipValue = IPTool.ipToLong(ip);
            log.info("long value is : {}", ipValue);
            IpAddress record = ipAddressMapper.getIpAddress(ipValue);
            if(record == null){
                log.info("Can't find any information...");

            }else{
                log.info("Find information: {}", record);
                // 运营商
                address.setIsp(record.getAddress2());
                // 地址
                address.setAddress(record.getAddress1());
                // 所属起始止IP
                address.setStartIP(record.getStartIp());
                address.setEndIP(record.getEndIp());
                String mergerName = record.getAddress1();
                Area area = areaMapper.getAreaByMergerName(mergerName);
                if(area != null){
                    // 使用该地址
                    address.setAddress(area.getMergerName2());
                    // 经度
                    address.setLongitude(area.getLongitude());
                    // 纬度
                    address.setLatitude(area.getLatitude());
                    // 长途区号
                    address.setCode(area.getCode());
                    // 邮编
                    address.setZipCode(area.getZipCode());
                }

            }
            return address;
        } catch (Exception e) {
            throw new EAException(e.getMessage());
        }

    }

    public ComplexAddress getAddressByAddress(String add) {
        try {
            ComplexAddress address = new ComplexAddress();
            Area area = areaMapper.getAreaByMergerName(add);
            if (area != null) {
                // 使用该地址
                address.setAddress(area.getMergerName2());
                // 经度
                address.setLongitude(area.getLongitude());
                // 纬度
                address.setLatitude(area.getLatitude());
                // 长途区号
                address.setCode(area.getCode());
                // 邮编
                address.setZipCode(area.getZipCode());
            }
            return address;
        } catch (Exception e) {
            throw new EAException(e.getMessage());
        }
    }

    public ComplexAddress getAddressByZipCode(String zipCode) {
        try {
            ComplexAddress address = new ComplexAddress();
            Area area = areaMapper.getAreaByZipCode(zipCode);
            if (area != null) {
                // 使用该地址
                address.setAddress(area.getMergerName2());
                // 经度
                address.setLongitude(area.getLongitude());
                // 纬度
                address.setLatitude(area.getLatitude());
                // 长途区号
                address.setCode(area.getCode());
                // 邮编
                address.setZipCode(area.getZipCode());
            }
            return address;
        } catch (Exception e) {
            throw new EAException(e.getMessage());
        }
    }


}
