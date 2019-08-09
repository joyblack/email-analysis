package com.sunrun.emailanalysis.data.request.tool;

public class AddressData {
    private String ip;

    private String zipCode;

    private String address;

    public AddressData() {
    }

    @Override
    public String toString() {
        return "AddressData{" +
                "ip='" + ip + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
