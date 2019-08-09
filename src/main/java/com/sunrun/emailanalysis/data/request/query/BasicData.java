package com.sunrun.emailanalysis.data.request.query;

import com.sunrun.emailanalysis.data.request.CommonData;

public class BasicData extends CommonData {
    // 查询内容
    private String content;

    public BasicData() {
    }

    public BasicData(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
