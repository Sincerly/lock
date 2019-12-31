package com.ysxsoft.lock.models.response;

/**
* 忘记密码
* create by Sincerly on 9999/9/9 0009
**/
public class ForgetPwdResponse{

    /**
     * msg : 操作成功
     * code : 200
     */

    private String msg;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
