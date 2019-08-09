package com.sunrun.emailanalysis.data.request.email;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class SearchEmailData {
    // Case id
    private Long caseId;

    // Search key word
    private String content;

    // Pagination information.
    private Pagination pagination;

    // Use exact.
    private Integer isExact;

    // Use exact.
    private Integer isWarning;

    // Send user.
    private String from;

    // Receive user.
    private String to;

    // Subject
    private String subject;

    // Start SendTime
    private String sendTimeStart;

    // End SendTime
    private String sendTimeEnd;

    // Send User IP
    private String fromIp;

    public String getSendTimeStart() {
        return sendTimeStart;
    }

    public void setSendTimeStart(String sendTimeStart) {
        this.sendTimeStart = sendTimeStart;
    }

    public String getSendTimeEnd() {
        return sendTimeEnd;
    }

    public void setSendTimeEnd(String sendTimeEnd) {
        this.sendTimeEnd = sendTimeEnd;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public SearchEmailData(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Integer getIsExact() {
        return isExact;
    }

    public void setIsExact(Integer isExact) {
        this.isExact = isExact;
    }

    public Integer getIsWarning() {
        return isWarning;
    }

    public void setIsWarning(Integer isWarning) {
        this.isWarning = isWarning;
    }

    @Override
    public String toString() {
        return "SearchEmailData{" +
                "caseId=" + caseId +
                ", content='" + content + '\'' +
                ", pagination=" + pagination +
                ", isExact=" + isExact +
                ", isWarning=" + isWarning +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", sendTimeStart='" + sendTimeStart + '\'' +
                ", sendTimeEnd='" + sendTimeEnd + '\'' +
                ", fromIp='" + fromIp + '\'' +
                '}';
    }
}
