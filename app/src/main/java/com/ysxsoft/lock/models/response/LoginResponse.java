package com.ysxsoft.lock.models.response;

/**
* 登录
* create by Sincerly on 9999/9/9 0009
**/
public class LoginResponse{

    /**
     * token :
     * phone :
     */

    private String token;
    private String phone;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
