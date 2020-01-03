package com.ysxsoft.lock.models.response;

import java.util.ArrayList;

/**
 * Create By 胡
 * on 2019/12/31 0031
 */
public class CardListResponse {
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
        String business_id;
        String type;
        String price;
        String oprice;
        String collar;
        String img;
        String title;
        String remark;
        String status;
        String snum;
        String totalnum;
        String ysnum;

        public String getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }

        public String getYsnum() {
            return ysnum;
        }

        public void setYsnum(String ysnum) {
            this.ysnum = ysnum;
        }

        public String getCostnum() {
            return costnum;
        }

        public void setCostnum(String costnum) {
            this.costnum = costnum;
        }

        String costnum
                ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOprice() {
            return oprice;
        }

        public void setOprice(String oprice) {
            this.oprice = oprice;
        }

        public String getCollar() {
            return collar;
        }

        public void setCollar(String collar) {
            this.collar = collar;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSnum() {
            return snum;
        }

        public void setSnum(String snum) {
            this.snum = snum;
        }

    }

}
