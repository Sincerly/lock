package com.ysxsoft.lock.models.response;

/**
 * Create By 胡
 * on 2019/12/31 0031
 */
public class CheckAddressResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"id":"5b3c3fceee724c20aaf6d02e743eeb8c","quarters_name":"测试小区","address":"长宁区通协路558弄","lat":"31.23203","lng":"121.36244"}
     */

    private String msg;
    private String code;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5b3c3fceee724c20aaf6d02e743eeb8c
         * quarters_name : 测试小区
         * address : 长宁区通协路558弄
         * lat : 31.23203
         * lng : 121.36244
         */

        private String id;
        private String quarters_name;
        private String address;
        private String lat;
        private String lng;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuarters_name() {
            return quarters_name;
        }

        public void setQuarters_name(String quarters_name) {
            this.quarters_name = quarters_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
    }
}
