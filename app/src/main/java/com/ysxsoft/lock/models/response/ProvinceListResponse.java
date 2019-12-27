package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class ProvinceListResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"0c9976468d1811e5be75eca86ba4ba05","name":"北京市","code":"110000"},{"id":"0c9978228d1811e5be75eca86ba4ba05","name":"江苏省","code":"320000"},{"id":"0c9979598d1811e5be75eca86ba4ba05","name":"浙江省","code":"330000"},{"id":"0c997a558d1811e5be75eca86ba4ba05","name":"安徽省","code":"340000"},{"id":"0c997b258d1811e5be75eca86ba4ba05","name":"福建省","code":"350000"},{"id":"0c997ba88d1811e5be75eca86ba4ba05","name":"江西省","code":"360000"},{"id":"0c997c2e8d1811e5be75eca86ba4ba05","name":"山东省","code":"370000"},{"id":"0c997caa8d1811e5be75eca86ba4ba05","name":"河南省","code":"410000"},{"id":"0c997d308d1811e5be75eca86ba4ba05","name":"湖北省","code":"420000"},{"id":"0c997dae8d1811e5be75eca86ba4ba05","name":"湖南省","code":"430000"},{"id":"0c997e268d1811e5be75eca86ba4ba05","name":"广东省","code":"440000"},{"id":"0c997ea68d1811e5be75eca86ba4ba05","name":"天津市","code":"120000"},{"id":"0c997f268d1811e5be75eca86ba4ba05","name":"广  西","code":"450000"},{"id":"0c997fa18d1811e5be75eca86ba4ba05","name":"海南省","code":"460000"},{"id":"0c99801c8d1811e5be75eca86ba4ba05","name":"重庆市","code":"500000"},{"id":"0c9980968d1811e5be75eca86ba4ba05","name":"四川省","code":"510000"},{"id":"0c9981128d1811e5be75eca86ba4ba05","name":"贵州省","code":"520000"},{"id":"0c9981908d1811e5be75eca86ba4ba05","name":"云南省","code":"530000"},{"id":"0c9982108d1811e5be75eca86ba4ba05","name":"西  藏","code":"540000"},{"id":"0c9982898d1811e5be75eca86ba4ba05","name":"陕西省","code":"610000"},{"id":"0c9983038d1811e5be75eca86ba4ba05","name":"甘肃省","code":"620000"},{"id":"0c99837f8d1811e5be75eca86ba4ba05","name":"青海省","code":"630000"},{"id":"0c9983fa8d1811e5be75eca86ba4ba05","name":"河北省","code":"130000"},{"id":"0c9984758d1811e5be75eca86ba4ba05","name":"宁  夏","code":"640000"},{"id":"0c9984f08d1811e5be75eca86ba4ba05","name":"新  疆","code":"650000"},{"id":"0c99856d8d1811e5be75eca86ba4ba05","name":"台湾省","code":"710000"},{"id":"0c9985f18d1811e5be75eca86ba4ba05","name":"香  港","code":"810000"},{"id":"0c99866b8d1811e5be75eca86ba4ba05","name":"澳  门","code":"820000"},{"id":"0c9986e58d1811e5be75eca86ba4ba05","name":"山西省","code":"140000"},{"id":"0c9987648d1811e5be75eca86ba4ba05","name":"内蒙古","code":"150000"},{"id":"0c9987e58d1811e5be75eca86ba4ba05","name":"辽宁省","code":"210000"},{"id":"0c99885c8d1811e5be75eca86ba4ba05","name":"吉林省","code":"220000"},{"id":"0c9988d68d1811e5be75eca86ba4ba05","name":"黑龙江","code":"230000"},{"id":"0c9989538d1811e5be75eca86ba4ba05","name":"上海市","code":"310000"}]
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
         * id : 0c9976468d1811e5be75eca86ba4ba05
         * name : 北京市
         * code : 110000
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
