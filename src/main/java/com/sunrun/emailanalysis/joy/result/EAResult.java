package com.sunrun.emailanalysis.joy.result;

import java.io.Serializable;

public class EAResult implements Serializable {
    private Boolean state;	//操作结果：bool
    private String message;	//返回信息：string
    private String detailMessage;	//返回信息：开发人员关注的信息
    private Object data = null;	//返回数据：object
    private int returnCode;		//状态码：int

    private EAResult(Notice notice, String detailMessage, Boolean state, Object data) {
        this.state = state;
        this.message = notice.getMessage();
        this.returnCode = notice.getReturnCode();
        this.data = data;
        this.detailMessage = detailMessage;
    }

    public static EAResult buildSuccessResult(Object data){
        return new EAResult(Notice.EXECUTE_IS_SUCCESS,
                "",
                true,
                data
        );
    }

    public static EAResult buildFailedResult(Notice notice, String detailMessage){
        return new EAResult(notice,
                detailMessage,
                false,
                null
        );
    }

    public static EAResult buildFailedResult(Notice notice, String detailMessage, Object data){
        return new EAResult(notice,
                detailMessage,
                false,
                data
        );
    }

    public static EAResult buildDefaultResult(){
        return new EAResult(Notice.EXECUTE_IS_FAILED,
                "",
                false,
                null
                );
    }

    public static EAResult buildSuccessResult(){
        return new EAResult(Notice.EXECUTE_IS_SUCCESS,
                "",
                true,
                null
        );
    }

    public static EAResult buildFailedResult(){
        return new EAResult(Notice.EXECUTE_IS_FAILED,
                "",
                false,
                null
        );
    }


    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String toString() {
        return "JoyResult{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", returnCode=" + returnCode +
                '}';
    }

}
