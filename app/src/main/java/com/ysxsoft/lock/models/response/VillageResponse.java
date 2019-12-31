package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class VillageResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":"18e16dba09914f05bd737181546aa661","quarters_name":"上南花苑（三期）","address":"浦东新区齐河路257弄","lat":"31.18663","lng":"121.50854"},{"id":"2c414c74a21d4333ab4d83ad5d47909a","quarters_name":"测试小区二","address":"长宁区","lat":"31.24916","lng":"121.4879"},{"id":"3d7f07ca57514013a80b45758f850ed1","quarters_name":"溢翠苑","address":"浦东新区南码头路1288弄","lat":"31.18749","lng":"121.52034"},{"id":"41a0e1514bd240e99c38332f41a9f5b4","quarters_name":"泉州市悦达文化传播有限公司","address":"上号升级","lat":"0.0","lng":"0.0"},{"id":"426d287dd353468da887b618e170be1d","quarters_name":"盼盼小区","address":"上海市长宁区临虹路168号","lat":"31.2369","lng":"121.36398"},{"id":"52c8956df8144a459a4aba69fe1b173b","quarters_name":"东明家园","address":"浦东新区东明路560弄","lat":"31.1852","lng":"121.51565"},{"id":"54b3997eaa3643e4a790c0d4c5856a04","quarters_name":"绿苑小区","address":"老沪太路1291弄","lat":"31.24916","lng":"121.4879"},{"id":"5b3c3fceee724c20aaf6d02e743eeb8c","quarters_name":"测试小区","address":"长宁区通协路558弄","lat":"31.23203","lng":"121.36244"},{"id":"5b62cd2496f5411db2b4ccd6064d5819","quarters_name":"测试六","address":"上海市长宁区","lat":"31.24916","lng":"121.4879"},{"id":"62d4f0ef6fb24ee1b27d45d2192bdeff","quarters_name":"卓施小区","address":"浦东","lat":"30.88439","lng":"121.40865"},{"id":"85a6accd9f7940119322f4826258bd30","quarters_name":"毛毛小区","address":"上海嘉定","lat":"31.36434","lng":"121.25101"},{"id":"9d2ec6b9c0194f34a391f9448cc024c5","quarters_name":"测试三","address":"上海市长宁区金钟路999号D栋402","lat":"31.22648","lng":"121.35773"},{"id":"a80d26524a2f439aa1767e1fa6ba89a2","quarters_name":"金南新苑","address":"陆家浜路413弄","lat":null,"lng":null},{"id":"b6195e94df454e3dbdf278c460703b37","quarters_name":"山姆御花园","address":"通协路558","lat":"31.23269","lng":"121.35608"},{"id":"b8d9bfb317d5419a9145a01602e954a3","quarters_name":"名盛苑","address":"上海","lat":"31.24916","lng":"121.4879"},{"id":"d2d415b409404628a48adfb1462782f7","quarters_name":"上海测试小区","address":"测试小区","lat":null,"lng":null},{"id":"d30a51d40ea145eaab9241c46880e099","quarters_name":"山姆邻里邻外","address":"上海市长宁区金钟路999号","lat":"31.2264","lng":"121.35763"},{"id":"dc9de83ddf8f4276b17e335033f65ae0","quarters_name":"飘鹰公寓","address":"虹口区恒业路365弄","lat":"31.23686","lng":"121.36037"},{"id":"de8441c4bbc34e2a900b3116c1d2b9dc","quarters_name":"金象大厦","address":"上海市虹口区华昌路9号","lat":"31.26729","lng":"121.48485"},{"id":"eb10ad6e73e6406d9b8ade5de0ba678d","quarters_name":"九州公寓","address":"旭华","lat":"31.24916","lng":"121.4879"},{"id":"f0218479cbc34b0cbb95592fed64eafe","quarters_name":"雍景园","address":"天山支路","lat":"31.24916","lng":"121.4879"}]
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
         * id : 18e16dba09914f05bd737181546aa661
         * quarters_name : 上南花苑（三期）
         * address : 浦东新区齐河路257弄
         * lat : 31.18663
         * lng : 121.50854
         */

        private String id;
        private String quarters_name;
        private String address;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        private String img;
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
