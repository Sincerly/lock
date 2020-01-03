package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/31 0031
 */
public class ListUnitResponse {
    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"00ad0e9de72a4deb9d9f6862caa062d7","requ_id":"2c414c74a21d4333ab4d83ad5d47909a","floor_name":"2号楼"},{"id":"036b71ac9bc346e595f6889a44819f11","requ_id":"2c414c74a21d4333ab4d83ad5d47909a","floor_name":"2"},{"id":"8a870c66a5944bcda0c4721fec877020","requ_id":"2c414c74a21d4333ab4d83ad5d47909a","floor_name":"4号楼"},{"id":"afeb2be4f6ae4029b3b0ec094d73f38d","requ_id":"2c414c74a21d4333ab4d83ad5d47909a","floor_name":"5号楼"},{"id":"e17602704c1e473ba5ac74399102e2fc","requ_id":"2c414c74a21d4333ab4d83ad5d47909a","floor_name":"1"}]
     */

    private String msg;
    private String code;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    private List<DataBean> data;

    public static class DataBean {
        String id;
        String requ_id;
        String floor_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getFloor_name() {
            return floor_name;
        }

        public void setFloor_name(String floor_name) {
            this.floor_name = floor_name;
        }

        String floor_name;

    }
}
