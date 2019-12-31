package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class CityResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"4ed4768aa3fb11e599b3eca86ba4ba05","name":"北京市","code":"110100"}]
     */

    private String msg;
    private String code;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * id : 4ed4768aa3fb11e599b3eca86ba4ba05
         * name : 北京市
         * code : 110100
         */

        private String id;
        private String name;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
