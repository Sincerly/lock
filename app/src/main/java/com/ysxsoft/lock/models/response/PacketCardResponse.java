package com.ysxsoft.lock.models.response;

import java.util.ArrayList;
import java.util.List;

public class PacketCardResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"6326242284255590","card_id":"6322516921151787","put_id":"3gtlbp9ki4neh99f","type":1,"price":"500.00","oprice":"600.00","start_time":"2020-01-03 04:53:42","end_time":"2020-01-03 04:51:46","status":"1","title":"测试现金券","img":"business/product/26q3dxit6jf4luq4.png","remark":"现金券"}]
     */

    private String msg;
    private String code;
    private List<DataBean> data;

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 6326242284255590
         * card_id : 6322516921151787
         * put_id : 3gtlbp9ki4neh99f
         * type : 1
         * price : 500.00
         * oprice : 600.00
         * start_time : 2020-01-03 04:53:42
         * end_time : 2020-01-03 04:51:46
         * status : 1
         * title : 测试现金券
         * img : business/product/26q3dxit6jf4luq4.png
         * remark : 现金券
         */

        private String id;
        private String card_id;
        private String put_id;
        private int type;
        private String price;
        private String oprice;
        private String start_time;
        private String start_time_str;
        private String end_time;
        private String end_time_str;
        private String status;
        private String title;
        private String img;
        private String remark;
        private String name;
        private String logo;
        private String tel;

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }

        private boolean isClick;

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo == null ? "" : logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTel() {
            return tel == null ? "" : tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getStart_time_str() {
            return start_time_str == null ? "" : start_time_str;
        }

        public void setStart_time_str(String start_time_str) {
            this.start_time_str = start_time_str;
        }

        public String getEnd_time_str() {
            return end_time_str == null ? "" : end_time_str;
        }

        public void setEnd_time_str(String end_time_str) {
            this.end_time_str = end_time_str;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCard_id() {
            return card_id == null ? "" : card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getPut_id() {
            return put_id == null ? "" : put_id;
        }

        public void setPut_id(String put_id) {
            this.put_id = put_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPrice() {
            return price == null ? "" : price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOprice() {
            return oprice == null ? "" : oprice;
        }

        public void setOprice(String oprice) {
            this.oprice = oprice;
        }

        public String getStart_time() {
            return start_time == null ? "" : start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time == null ? "" : end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStatus() {
            return status == null ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img == null ? "" : img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getRemark() {
            return remark == null ? "暂无" : remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
