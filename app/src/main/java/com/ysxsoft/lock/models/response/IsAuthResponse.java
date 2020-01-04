package com.ysxsoft.lock.models.response;

/**
 * Create By 胡
 * on 2020/1/3 0003
 */
public class IsAuthResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"id":1,"member_id":"11","realname":"黑芝麻胡","cardno":"412728199301186551","phone":"13253565026","nationality":1,"status":1}
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
         * id : 1
         * member_id : 11
         * realname : 黑芝麻胡
         * cardno : 412728199301186551
         * phone : 13253565026
         * nationality : 1
         * status : 1
         */

        private int id;
        private String member_id;
        private String realname;
        private String cardno;
        private String phone;
        private int nationality;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getNationality() {
            return nationality;
        }

        public void setNationality(int nationality) {
            this.nationality = nationality;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
