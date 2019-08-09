package com.sunrun.emailanalysis.joy.result;

public enum Notice{

    REQUEST_PARAMETER_IS_ERROR("请求参数错误", 201),

    EXECUTE_IS_SUCCESS("操作成功", 233),
    EXECUTE_IS_FAILED("操作失败", 234),




    // 解析邮件相关 250-300
    CASE_IS_EXIST("项目已经存在", 251),
    PATH_IS_NOT_EXIST("路径不存在", 252),
    CASE_IS_NOT_EXIST("项目信息不存在", 253),
    CASE_ID_NOT_EXIST("项目ID不存在", 254),

    // 附件相关 300-399
    ATTACH_NOT_EXSIT("附件信息不存在",300),

    // 任务执行相关
    TASK_IS_COMPLETE("任务处于完成状态",400),
    TASK_NOT_CHECK("未检查的任务",401),
    TASK_IS_PREPARE("任务处于准备状态",402),
    TASK_IS_RUNNING("任务处于运行状态",403),
    TASK_NOT_EXSIST("不存在的任务",404),

    // 自定义词典
    CUSTOM_DICTIONARY_VALUE_IS_NULL("关键字的值为空",802),
    CUSTOM_DICTIONARY_IS_NOT_EXIST("关键字条目不存在",801),

    // 邮件相关
    ADDRESS_IS_ALREADY_EXIST("账户信息已存在", 901)
    ;

    private String message;
    private int returnCode;

    private Notice(String message, int returnCode) {
        this.message = message;
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

}
