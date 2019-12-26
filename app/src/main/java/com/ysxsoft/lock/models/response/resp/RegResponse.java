package com.ysxsoft.lock.models.response.resp;

public class RegResponse {

    /**
     * msg : 操作成功
     * code : 200
     * token : eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjM5ODY1M2I5LWRjMDUtNDk0OS1hZTJjLWM1ZTAwMzAxZTAxYSJ9.AHfxHL8m4hZzCRCeTsj-c5viWEbehcROEW6oxouv-70idR_Q9iPjs6iR1b6_KUSurQgRglmcOgHN5gSqU4EfTQ
     */

    private String msg;
    private int code;
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
