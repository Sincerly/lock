package com.ysxsoft.lock.models.response;

import java.util.ArrayList;

/**
 * Create By 胡
 * on 2019/12/31 0031
 */
public class PacketRechargeListResponse {

    String code;
    String msg;

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

    ArrayList<DataBean>  data;

    public class DataBean{
        String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        String num;

        public String getYprice() {
            return yprice;
        }

        public void setYprice(String yprice) {
            this.yprice = yprice;
        }

        String yprice;
    }
}
