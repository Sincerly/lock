package com.ysxsoft.lock.models.response;

import java.util.List;

public class ScanHistoryResponse {

    /**
     * total : 3
     * rows : [{"id":"6716720614221487","business_id":"2auwdir81cdle107","member_id":"4","card_id":"6461478651930091","put_id":"czjw6954bvgsegfp","type":1,"price":"100.00","oprice":"200.00","start_time":"2020-02-17 09:33:26","start_time_str":"2020-02-17","end_time":"2020-05-31 00:00:00","end_time_str":"2020-05-31","status":"1","title":"现金券测试","img":"business/product/cptyatv60pjc9j6g.jpg","remark":null,"name":"测试店铺1","logo":"business/headimg/6e6bss672i8a0m5k.png","lng":"116.397590","lat":"39.908776","tel":"18605195009"},{"id":"6665349612057484","business_id":"2auwdir81cdle107","member_id":"4","card_id":"6461478651930091","put_id":"czjw6954bvgsegfp","type":1,"price":"100.00","oprice":"200.00","start_time":"2020-02-11 10:51:36","start_time_str":"2020-02-11","end_time":"2020-05-31 00:00:00","end_time_str":"2020-05-31","status":"1","title":"现金券测试","img":"business/product/cptyatv60pjc9j6g.jpg","remark":null,"name":"测试店铺1","logo":"business/headimg/6e6bss672i8a0m5k.png","lng":"116.397590","lat":"39.908776","tel":"18605195009"},{"id":"6461632293355663","business_id":"2auwdir81cdle107","member_id":"1","card_id":"6461478651930091","put_id":"czjw6954bvgsegfp","type":1,"price":"100.00","oprice":"200.00","start_time":"2020-01-18 20:58:42","start_time_str":"2020-01-18","end_time":"2020-05-31 00:00:00","end_time_str":"2020-05-31","status":"2","title":"现金券测试","img":"business/product/cptyatv60pjc9j6g.jpg","remark":"现金券测试核销","name":"测试店铺1","logo":"business/headimg/6e6bss672i8a0m5k.png","lng":"116.397590","lat":"39.908776","tel":"18605195009"}]
     * code : 200
     * msg : 0
     */

    private String total;
    private String code;
    private String msg;
    private List<RowsBean> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 6716720614221487
         * business_id : 2auwdir81cdle107
         * member_id : 4
         * card_id : 6461478651930091
         * put_id : czjw6954bvgsegfp
         * type : 1
         * price : 100.00
         * oprice : 200.00
         * start_time : 2020-02-17 09:33:26
         * start_time_str : 2020-02-17
         * end_time : 2020-05-31 00:00:00
         * end_time_str : 2020-05-31
         * status : 1
         * title : 现金券测试
         * img : business/product/cptyatv60pjc9j6g.jpg
         * remark : null
         * name : 测试店铺1
         * logo : business/headimg/6e6bss672i8a0m5k.png
         * lng : 116.397590
         * lat : 39.908776
         * tel : 18605195009
         */

        private String id;
        private String business_id;
        private String member_id;
        private String card_id;
        private String put_id;
        private String type;
        private String price;
        private String oprice;
        private String start_time;
        private String start_time_str;
        private String end_time;
        private String end_time_str;
        private String status;
        private String title;
        private String img;
        private Object remark;
        private String name;
        private String logo;
        private String lng;
        private String lat;
        private String tel;

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

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getPut_id() {
            return put_id;
        }

        public void setPut_id(String put_id) {
            this.put_id = put_id;
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

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getStart_time_str() {
            return start_time_str;
        }

        public void setStart_time_str(String start_time_str) {
            this.start_time_str = start_time_str;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_time_str() {
            return end_time_str;
        }

        public void setEnd_time_str(String end_time_str) {
            this.end_time_str = end_time_str;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
