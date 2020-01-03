package com.ysxsoft.lock.models.response;

import java.util.List;

/**
* 商户信息
* create by Sincerly on 9999/9/9 0009
**/
public class ShopInfoResponse{
    private String msg;
    private String code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;

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
        String member_id;
        String logo;
        String name;
        String mainbusiness;
        String week1;
        String week2;
        String time1;
        String time2;
        String address;
        String tel;
        String lat;
        String lng;
        String status;
        String create_time;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        String ticket;
    }
}
