package com.ysxsoft.lock.models.response;

/**
* 其他方式登录
* create by Sincerly on 9999/9/9 0009
**/
public class OtherLoginResponse{

    /**
     * msg : 操作成功
     * apitoken : eyJhbGciOiJIUzUxMiJ9.eyJhcGlfbG9naW5fdXNlcl9rZXkiOiI2NjQwMTg5Yy1iMzIwLTQzODktYmNlOC1hOWYxYTRlYTE4OTkifQ.K5XGXnoNdN58vP2-n6BDo6gfCCBJPh3_Sn1Stv0MMK1fEyxYiVg6b7NBOdO11hE4RChgAZtd7UsN_83V54qRQA
     * code : 200
     */

    private String msg;
    private String apitoken;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getApitoken() {
        return apitoken;
    }

    public void setApitoken(String apitoken) {
        this.apitoken = apitoken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
