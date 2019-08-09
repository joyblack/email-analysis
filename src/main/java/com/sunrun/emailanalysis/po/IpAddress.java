package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ea_ip_address")
public class IpAddress {

    @Id
    private Long id;

    private String startIp;

    private String endIp;

    private String address1;

    private String address2;

    private Long startIpValue;

    private Long endIpValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Long getStartIpValue() {
        return startIpValue;
    }

    public void setStartIpValue(Long startIpValue) {
        this.startIpValue = startIpValue;
    }

    public Long getEndIpValue() {
        return endIpValue;
    }

    public void setEndIpValue(Long endIpValue) {
        this.endIpValue = endIpValue;
    }
}