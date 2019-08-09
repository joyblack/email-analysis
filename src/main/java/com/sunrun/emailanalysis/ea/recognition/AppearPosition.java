package com.sunrun.emailanalysis.ea.recognition;

import java.util.HashMap;

// The feature appear position, please, do not change the type declare order.
public enum AppearPosition {
    SUBJECT(0,"subject","主题"),
    CONTENT(1,"content","正文"),
    ATTACH(2,"attach","附件"),
    ATTACH_REMARK(3,"attach_remark","附件备注"),
    EMAIL_REMARK(4,"email_remark","邮件备注");

    private Integer id;
    private String name;
    private String remark;

    AppearPosition(Integer id, String name, String remark){
        this.id = id;
        this.name = name;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public HashMap<String, Object> hashMapResult(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("remark", remark);
        return result;
    }
}
