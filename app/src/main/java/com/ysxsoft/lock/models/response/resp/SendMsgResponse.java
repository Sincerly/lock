package com.ysxsoft.lock.models.response.resp;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class SendMsgResponse {

    /**
     * msg : 操作成功
     * code : 200
     * key : 127uhx4nwgm98yhw9v
     */

    private String msg;
    private int code;
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
