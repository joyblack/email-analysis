package com.sunrun.emailanalysis.data.request.account.warning;

public class WarningAccountData {

    private Long id;

    private String nickName;

    private String address;

    public WarningAccountData() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
