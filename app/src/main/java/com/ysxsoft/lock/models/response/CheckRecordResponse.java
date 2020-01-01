package com.ysxsoft.lock.models.response;

import java.util.ArrayList;

/**
 * 核销记录
 * create by Sincerly on 9999/9/9 0009
 **/
public class CheckRecordResponse {

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    private ArrayList<DataBean> data;

    public class DataBean {
        String id;
        String cost_time;
        String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCost_time() {
            return cost_time;
        }

        public void setCost_time(String cost_time) {
            this.cost_time = cost_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        String price;
    }
}
