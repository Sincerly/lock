package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * 商铺详情
 * create by Sincerly on 9999/9/9 0009
 **/
public class ShopDetailResponse {

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;

    public class DataBean {
        private String id;
        private String member_id;
        private String logo;
        private String name;
        private String mainbusiness;
        private String week1;
        private String week2;
        private String time1;
        private String time2;
        private String address;
        private String tel;
        private String distance;
        private String lat;
        private String lng;
        private String status;
        private String create_time;
        private String xl;
        private List<ItemBean> attachList;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public List<ItemBean> getAttachList() {
            return attachList;
        }

        public void setAttachList(List<ItemBean> attachList) {
            this.attachList = attachList;
        }

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
            return lat==null?"0.0":"".equals(lat)?"0.0":lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng==null?"0.0":"".equals(lng)?"0.0":lng;
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

        private String ticket;

        public class ItemBean{
            private String attach;

            public String getAttach() {
                return attach;
            }

            public void setAttach(String attach) {
                this.attach = attach;
            }
        }

    }

}
