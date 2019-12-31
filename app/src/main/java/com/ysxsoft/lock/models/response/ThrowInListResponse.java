package com.ysxsoft.lock.models.response;

import java.util.ArrayList;

/**
 * Create By 胡
 * on 2019/12/31 0031
 */
public class ThrowInListResponse {

    private String msg;
    private String code;


    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    private ArrayList<DataBean> data;

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


    public static class DataBean {
        String id;
        String total_num;
        String ys_num;
        String totalcost;
        String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTotal_num() {
            return total_num;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
        }

        public String getYs_num() {
            return ys_num;
        }

        public void setYs_num(String ys_num) {
            this.ys_num = ys_num;
        }

        public String getTotalcost() {
            return totalcost;
        }

        public void setTotalcost(String totalcost) {
            this.totalcost = totalcost;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReqname() {
            return reqname;
        }

        public void setReqname(String reqname) {
            this.reqname = reqname;
        }

        String reqname;
    }

}
