package com.sunrun.emailanalysis.data.request.attach;

import com.sunrun.emailanalysis.data.request.common.Pagination;
import com.sunrun.emailanalysis.joy.file.common.FileCombineExtension;

import java.util.List;

public class AttachListData {
    private Long caseId;
    private Pagination pagination;
    private String fileName;
    // include 值为负数时表示获取所有类型附件
    private Integer include;
    private List<String> fileType;

    public AttachListData() {
    }

    public AttachListData(Long caseId, String fileName, Integer include, List<String> fileType, Pagination pagination) {
        this.caseId = caseId;
        this.pagination = pagination;
        this.fileName = fileName;
        this.include = include;
        this.fileType = fileType;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }


    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getInclude() {
        return include;
    }

    public void setInclude(Integer include) {
        this.include = include;
    }

    public List<String> getFileType() {
        return fileType;
    }

    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "AttachListData{" +
                "caseId=" + caseId +
                ", fileName=" + fileName +
                ", include=" + include +
                ", pagination=" + pagination +
                ", fileType=" + fileType +
                '}';
    }
}
