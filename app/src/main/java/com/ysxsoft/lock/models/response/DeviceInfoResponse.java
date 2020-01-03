package com.ysxsoft.lock.models.response;

/**
 * Create By 胡
 * on 2020/1/3 0003
 */
public class DeviceInfoResponse {

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
        private String equ_pass;
        private String equ_type;
        private String equ_name;
        private String requ_id;
        private String floor_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEqu_pass() {
            return equ_pass;
        }

        public void setEqu_pass(String equ_pass) {
            this.equ_pass = equ_pass;
        }

        public String getEqu_type() {
            return equ_type;
        }

        public void setEqu_type(String equ_type) {
            this.equ_type = equ_type;
        }

        public String getEqu_name() {
            return equ_name;
        }

        public void setEqu_name(String equ_name) {
            this.equ_name = equ_name;
        }

        public String getRequ_id() {
            return requ_id;
        }

        public void setRequ_id(String requ_id) {
            this.requ_id = requ_id;
        }

        public String getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(String floor_id) {
            this.floor_id = floor_id;
        }

        public String getUnit_id() {
            return unit_id;
        }

        public void setUnit_id(String unit_id) {
            this.unit_id = unit_id;
        }

        private String unit_id;
    }

}
