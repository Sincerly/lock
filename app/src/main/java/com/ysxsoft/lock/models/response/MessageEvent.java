package com.ysxsoft.lock.models.response;

/**
 * Create By 胡
 * on 2020/1/7 0007
 */
public class MessageEvent {
    private String pass;
    private String requ_id;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRequ_id() {
        return requ_id;
    }

    public void setRequ_id(String requ_id) {
        this.requ_id = requ_id;
    }

    public String getEqu_id() {
        return equ_id;
    }

    public void setEqu_id(String equ_id) {
        this.equ_id = equ_id;
    }

    private String equ_id;

    public MessageEvent(String pass,String requ_id,String equ_id) {
        this.pass = pass;//密码
        this.requ_id = requ_id;//小区id
        this.equ_id = equ_id; //门禁设备id
    }
}
