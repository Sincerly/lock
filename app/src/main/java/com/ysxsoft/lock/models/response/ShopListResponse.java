package com.ysxsoft.lock.models.response;

import java.util.ArrayList;
import java.util.List;

/**
* 商圈
* create by Sincerly on 9999/9/9 0009
**/
public class ShopListResponse{


    /**
     * total : 2
     * rows : [{"id":"23z935yl1gsb6rv1","member_id":"9","requ_id":null,"logo":"business/headimg/2npdh8f0lafvjhgw.jpeg","name":"晴天超市","mainbusiness":"超市","week1":"周一","week2":"周三","time1":"9:00","time2":"23:00","address":"无锡市堰桥路","tel":"18605195008","lng":"120.2823795500","status":1,"recommend":1,"create_time":"2020-01-08 23:33:02","ticket":0,"distance":"0.0"},{"id":"23z935yl1gsb6rv2","member_id":"9","requ_id":null,"logo":"business/headimg/2npdh8f0lafvjhgw.jpeg","name":"肯德基","mainbusiness":"快餐","week1":"周一","week2":"周三","time1":"9:00","time2":"23:00","address":"江苏省无锡市梁溪区北大街1号","tel":"18605195008","lat":"31.6063772100","lng":"120.2397302300","status":1,"recommend":1,"create_time":"2020-01-08 23:35:37","ticket":0,"distance":"11.0"}]
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
        return rows==null?new ArrayList<>():rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 23z935yl1gsb6rv1
         * member_id : 9
         * requ_id : null
         * logo : business/headimg/2npdh8f0lafvjhgw.jpeg
         * name : 晴天超市
         * mainbusiness : 超市
         * week1 : 周一
         * week2 : 周三
         * time1 : 9:00
         * time2 : 23:00
         * address : 无锡市堰桥路
         * tel : 18605195008
         * lng : 120.2823795500
         * status : 1
         * recommend : 1
         * create_time : 2020-01-08 23:33:02
         * ticket : 0
         * distance : 0.0
         * lat : 31.6063772100
         */

        private String id;
        private String member_id;
        private String requ_id;
        private String logo;
        private String name;
        private String mainbusiness;
        private String week1;
        private String week2;
        private String time1;
        private String time2;
        private String address;
        private String tel;
        private String lng;
        private String status;
        private String recommend;
        private String create_time;
        private String ticket;
        private String distance;
        private String lat;
        private String xl;

        public String getXl() {
            return xl;
        }

        public void setXl(String xl) {
            this.xl = xl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getRequ_id() {
            return requ_id;
        }

        public void setRequ_id(String requ_id) {
            this.requ_id = requ_id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMainbusiness() {
            return mainbusiness;
        }

        public void setMainbusiness(String mainbusiness) {
            this.mainbusiness = mainbusiness;
        }

        public String getWeek1() {
            return week1;
        }

        public void setWeek1(String week1) {
            this.week1 = week1;
        }

        public String getWeek2() {
            return week2;
        }

        public void setWeek2(String week2) {
            this.week2 = week2;
        }

        public String getTime1() {
            return time1;
        }

        public void setTime1(String time1) {
            this.time1 = time1;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
}
