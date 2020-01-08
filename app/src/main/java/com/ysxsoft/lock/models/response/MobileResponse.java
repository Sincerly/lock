package com.ysxsoft.lock.models.response;

public class MobileResponse {

    /**
     * msg : 操作成功
     * apitoken : eyJhbGciOiJIUzUxMiJ9.eyJhcGlfbG9naW5fdXNlcl9rZXkiOiIzNzM4ZDI0NS04YWRkLTQwMDQtYjIyMi1lNWRkOTNkZmQ5NGYifQ.daHTU7hHJTQkcK77ebaDDoJXpeMNOFI5EjrbUbJ1XV5m2xQT3dfm-w2G5Oogjt0_0TT4mZABejqRG5GxGjaCOQ
     * code : 200
     * phone : 18595756729
     */

    private String msg;
    private String apitoken;
    private int code;
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
