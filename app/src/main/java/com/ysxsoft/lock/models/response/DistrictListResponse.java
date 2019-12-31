package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class DistrictListResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"2d2eb52ca1d711e599b3eca86ba4ba05","name":"东城区","code":"110101"},{"id":"2d2ebb67a1d711e599b3eca86ba4ba05","name":"房山区","code":"110111"},{"id":"2d2efc0ea1d711e599b3eca86ba4ba05","name":"通州区","code":"110112"},{"id":"2d2f3938a1d711e599b3eca86ba4ba05","name":"顺义区","code":"110113"},{"id":"2d341609a1d711e599b3eca86ba4ba05","name":"昌平区","code":"110114"},{"id":"2d344ac8a1d711e599b3eca86ba4ba05","name":"大兴区","code":"110115"},{"id":"2d389b38a1d711e599b3eca86ba4ba05","name":"怀柔区","code":"110116"},{"id":"2d38cecba1d711e599b3eca86ba4ba05","name":"平谷区","code":"110117"},{"id":"2d44000ca1d711e599b3eca86ba4ba05","name":"西城区","code":"110102"},{"id":"2d5db865a1d711e599b3eca86ba4ba05","name":"崇文区","code":"110103"},{"id":"2d67a1fda1d711e599b3eca86ba4ba05","name":"宣武区","code":"110104"},{"id":"2d67e36ca1d711e599b3eca86ba4ba05","name":"朝阳区","code":"110105"},{"id":"2d732f9aa1d711e599b3eca86ba4ba05","name":"丰台区","code":"110106"},{"id":"2d73734ea1d711e599b3eca86ba4ba05","name":"石景山区","code":"110107"},{"id":"2d784494a1d711e599b3eca86ba4ba05","name":"海淀区","code":"110108"},{"id":"2d787dd4a1d711e599b3eca86ba4ba05","name":"门头沟区","code":"110109"}]
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
         * id : 2d2eb52ca1d711e599b3eca86ba4ba05
         * name : 东城区
         * code : 110101
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
