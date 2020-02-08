package com.ysxsoft.lock.models.response;

public class CheckPermissionResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"data":{"id":"6326242284255590","card_id":"6322516921151787","put_id":"3gtlbp9ki4neh99f","type":1,"price":"500.00","oprice":"600.00","start_time":"2020-01-03 04:53:42","end_time":"2020-01-03 04:51:46","status":"1","title":"测试现金券","img":"business/product/26q3dxit6jf4luq4.png","remark":"现金券"},"jur":1}
     */

    private String msg;
    private String code;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : {"id":"6326242284255590","card_id":"6322516921151787","put_id":"3gtlbp9ki4neh99f","type":1,"price":"500.00","oprice":"600.00","start_time":"2020-01-03 04:53:42","end_time":"2020-01-03 04:51:46","status":"1","title":"测试现金券","img":"business/product/26q3dxit6jf4luq4.png","remark":"现金券"}
         * jur : 1
         */

        private DataBean data;
        private int jur;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getJur() {
            return jur;
        }

        public void setJur(int jur) {
            this.jur = jur;
        }

        public static class DataBean {
            /**
             * id : 6326242284255590
             * card_id : 6322516921151787
             * put_id : 3gtlbp9ki4neh99f
             * type : 1
             * price : 500.00
             * oprice : 600.00
             * start_time : 2020-01-03 04:53:42
             * end_time : 2020-01-03 04:51:46
             * status : 1
             * title : 测试现金券
             * img : business/product/26q3dxit6jf4luq4.png
             * remark : 现金券
             */

            private String id;
            private String card_id;
            private String put_id;
            private int type;
            private String price;
            private String oprice;
            private String start_time;
            private String end_time;
            private String status;
            private String title;
            private String img;
            private String remark;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCard_id() {
                return card_id;
            }

            public void setCard_id(String card_id) {
                this.card_id = card_id;
            }

            public String getPut_id() {
                return put_id;
            }

            public void setPut_id(String put_id) {
                this.put_id = put_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getOprice() {
                return oprice;
            }

            public void setOprice(String oprice) {
                this.oprice = oprice;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }
}
