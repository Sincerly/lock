package com.ysxsoft.lock.models.response.resp;

public class FaceResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"id":3,"member_id":"9","attach":"faceimg/20200102/17tup5czxcwxgcqn.png","type":1,"typetxt":"人脸识别","status":1}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
         * id : 3
         * member_id : 9
         * attach : faceimg/20200102/17tup5czxcwxgcqn.png
         * type : 1
         * typetxt : 人脸识别
         * status : 1
         */

        private int id;
        private String member_id;
        private String attach;
        private int type;
        private String typetxt;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypetxt() {
            return typetxt;
        }

        public void setTypetxt(String typetxt) {
            this.typetxt = typetxt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
