package com.ysxsoft.lock.models.response.resp;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class CommentResponse {


    /**
     * msg : 操作成功
     * code : 200
     * data : false
     */

    private String msg;
    private String code;
    private boolean data;

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

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
