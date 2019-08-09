package com.sunrun.emailanalysis.ea.recognition.dictionary;

public class EmailIndexDictionary {
    public static final String CASE_ID;
    public static final String EMAIL_ID;
    public static final String FROM_NAME;
    public static final String FROM_ADDRESS;
    public static final String TO_NAME;
    public static final String TO_ADDRESS;
    public static final String SUBJECT;
    public static final String TEXT_CONTENT;
    public static final String ATTACH_ID;
    public static final String ATTACH_CONTENT;
    public static final String CREATE_TIME;
    public static final String SEND_TIME;
    public static final String NEW_FILE_NAME;
    public static final String OLD_FILE_NAME;
    public static final String FROM_IP;

    static {
        CASE_ID = "caseId";
        EMAIL_ID = "emailId";
        FROM_NAME = "fromName";
        FROM_ADDRESS = "fromAddress";
        TO_NAME = "toName";
        TO_ADDRESS = "toAddress";
        SUBJECT = "subject";
        TEXT_CONTENT = "textContent";
        ATTACH_ID = "attachId";
        ATTACH_CONTENT = "attachContent";
        CREATE_TIME = "createTime";
        SEND_TIME = "sendTime";
        NEW_FILE_NAME = "newFileName";
        OLD_FILE_NAME = "oldFileName";
        FROM_IP = "fromIp";
    }
}
