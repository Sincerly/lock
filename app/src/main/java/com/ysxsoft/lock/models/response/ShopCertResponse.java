package com.ysxsoft.lock.models.response;

public class ShopCertResponse {



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
        if (data == null){
            data = new DataBean();
        }
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String attach;
        private String id;
        private String member_id;
        private String status;
        private String type;
        private String typetxt;

        public String getAttach() {
            return attach == null ? "" : attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMember_id() {
            return member_id == null ? "" : member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getStatus() {
            return status == null ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type == null ? "" : type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypetxt() {
            return typetxt == null ? "" : typetxt;
        }

        public void setTypetxt(String typetxt) {
            this.typetxt = typetxt;
        }
    }
}
