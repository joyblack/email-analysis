package com.sunrun.emailanalysis.joy.exception;

import com.sunrun.emailanalysis.joy.result.Notice;
public class EAException extends RuntimeException {

    private static final long serialVersionUID = 8182981230289783930L;

    private Notice notice = Notice.EXECUTE_IS_FAILED;

    public EAException() {
        super();
    }

    public EAException(String message, Throwable cause) {
        super(message, cause);
    }

    public EAException(String message) {
        super(message);
    }

    public EAException(Throwable cause) {
        super(cause);
    }

    public EAException(Notice notice) {
        super(notice.getMessage());
        this.notice = notice;
    }

    public EAException(String message, Notice notice) {
        super(message);
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}

