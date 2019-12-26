package com.ysxsoft.lock.models.response.resp;

public class LoginResponse {

    /**
     * msg : 操作成功
     * apitoken : eyJhbGciOiJIUzUxMiJ9.eyJhcGlfbG9naW5fdXNlcl9rZXkiOiI1ZGQ3YTAwYi0wYmJjLTRkNGMtOWQ2NS1mZmI2YTkwNWVhNTYifQ.P5idlaSVT0lR0PXV10x0SSXD0_8_SDUVu8obF9Rr0gtFiVX8owl4ffXyQ_W12MBE2S22j9Z4SnwJD6nhBVOVRQ
     * code : 200
     */

    private String msg;
    private String apitoken;
    private int code;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
