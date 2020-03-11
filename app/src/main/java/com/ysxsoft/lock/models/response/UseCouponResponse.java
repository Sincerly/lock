package com.ysxsoft.lock.models.response;

/**
* 使用券
* create by Sincerly on 9999/9/9 0009
**/
public class UseCouponResponse{


    /**
     * code : 200
     * data : {"card_id":"6322516921151787","end_time":"2020-01-03 04:51:46","end_time_str":"2020-01-03","id":"6326242284255590","img":"business/product/26q3dxit6jf4luq4.png","lat":"39.952556","lng":"116.463485","logo":"business/headimg/2npdh8f0lafvjhgw.jpeg","member_id":"9","name":"亿生信","oprice":"600.00","price":"500.00","put_id":"3gtlbp9ki4neh99f","remark":"现金券","start_time":"2020-01-03 04:53:42","start_time_str":"2020-01-03","status":"1","tel":"18605195008","title":"测试现金券","type":1}
     * msg : 操作成功
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * card_id : 6322516921151787
         * end_time : 2020-01-03 04:51:46
         * end_time_str : 2020-01-03
         * id : 6326242284255590
         * img : business/product/26q3dxit6jf4luq4.png
         * lat : 39.952556
         * lng : 116.463485
         * logo : business/headimg/2npdh8f0lafvjhgw.jpeg
         * member_id : 9
         * name : 亿生信
         * oprice : 600.00
         * price : 500.00
         * put_id : 3gtlbp9ki4neh99f
         * remark : 现金券
         * start_time : 2020-01-03 04:53:42
         * start_time_str : 2020-01-03
         * status : 1
         * tel : 18605195008
         * title : 测试现金券
         * type : 1
         */

        private String card_id;
        private String end_time;
        private String end_time_str;
        private String id;
        private String img;
        private String lat;
        private String lng;
        private String logo;
        private String member_id;
        private String business_id;

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        private String name;
        private String oprice;
        private String price;
        private String put_id;
        private String remark;
        private String start_time;
        private String start_time_str;
        private String status;
        private String tel;
        private String distance;
        private String title;
        private int type;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOprice() {
            return oprice;
        }

        public void setOprice(String oprice) {
            this.oprice = oprice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPut_id() {
            return put_id;
        }

        public void setPut_id(String put_id) {
            this.put_id = put_id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
