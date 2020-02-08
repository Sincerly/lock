package com.ysxsoft.lock.models.response;

/**
 * Create By 胡
 * on 2020/1/6 0006
 */
public class PersonCenterResponse {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"member_id":"9","user_name":"18605195008","nickname":null,"autograph":null,"icon":"headimg/20191227/8qsw54ht0yuh4q3q.png","card1":1,"card2":1,"card3":1,"card4":1}
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
         * member_id : 9
         * user_name : 18605195008
         * nickname : null
         * autograph : null
         * icon : headimg/20191227/8qsw54ht0yuh4q3q.png
         * card1 : 1
         * card2 : 1
         * card3 : 1
         * card4 : 1
         */

        private String member_id;
        private String user_name;
        private String nickname;
        private String autograph;
        private String icon;
        private String card1;
        private String card2;
        private String card3;
        private String card4;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getNickname() {
            return nickname==null?"邻里邻外":nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAutograph() {
            return autograph==null?"我为邻里邻外代言，我的地盘我做主":autograph;
        }

        public void setAutograph(String autograph) {
            this.autograph = autograph;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCard1() {
            return card1;
        }

        public void setCard1(String card1) {
            this.card1 = card1;
        }

        public String getCard2() {
            return card2;
        }

        public void setCard2(String card2) {
            this.card2 = card2;
        }

        public String getCard3() {
            return card3;
        }

        public void setCard3(String card3) {
            this.card3 = card3;
        }

        public String getCard4() {
            return card4;
        }

        public void setCard4(String card4) {
            this.card4 = card4;
        }
    }
}
