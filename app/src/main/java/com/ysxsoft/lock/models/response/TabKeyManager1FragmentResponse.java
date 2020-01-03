package com.ysxsoft.lock.models.response;

import java.util.List;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class TabKeyManager1FragmentResponse {


    /**
     * msg : 操作成功
     * code : 200
     * data : [{"member_id":"9","requ_id":null,"status":1,"id":"066b101a2ab34a9883d761e556388b9d","quarters_name":"紫轩公寓","address":"云东路","isdefault":1,"listkey":[{"equ_id":null,"equ_pass":null,"equ_name":"单元门钥匙","equ_status":0,"equ_type":2,"member_id":null,"requ_id":null}]}]
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
         * member_id : 9
         * requ_id : null
         * status : 1
         * id : 066b101a2ab34a9883d761e556388b9d
         * quarters_name : 紫轩公寓
         * address : 云东路
         * isdefault : 1
         * listkey : [{"equ_id":null,"equ_pass":null,"equ_name":"单元门钥匙","equ_status":0,"equ_type":2,"member_id":null,"requ_id":null}]
         */

        private String member_id;
        private String requ_id;
        private int status;
        private String id;
        private String quarters_name;
        private String address;
        private boolean isExpanded;

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        private int isdefault;
        private List<ListkeyBean> listkey;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getRequ_id() {
            return requ_id;
        }

        public void setRequ_id(String requ_id) {
            this.requ_id = requ_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

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

        public int getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }

        public List<ListkeyBean> getListkey() {
            return listkey;
        }

        public void setListkey(List<ListkeyBean> listkey) {
            this.listkey = listkey;
        }

        public static class ListkeyBean {
            /**
             * equ_id : null
             * equ_pass : null
             * equ_name : 单元门钥匙
             * equ_status : 0
             * equ_type : 2
             * member_id : null
             * requ_id : null
             */

            private String equ_id;
            private String equ_pass;
            private String equ_name;
            private int equ_status;
            private int equ_type;
            private String member_id;
            private String requ_id;

            public String getEqu_id() {
                return equ_id;
            }

            public void setEqu_id(String equ_id) {
                this.equ_id = equ_id;
            }

            public String getEqu_pass() {
                return equ_pass;
            }

            public void setEqu_pass(String equ_pass) {
                this.equ_pass = equ_pass;
            }

            public String getEqu_name() {
                return equ_name;
            }

            public void setEqu_name(String equ_name) {
                this.equ_name = equ_name;
            }

            public int getEqu_status() {
                return equ_status;
            }

            public void setEqu_status(int equ_status) {
                this.equ_status = equ_status;
            }

            public int getEqu_type() {
                return equ_type;
            }

            public void setEqu_type(int equ_type) {
                this.equ_type = equ_type;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getRequ_id() {
                return requ_id;
            }

            public void setRequ_id(String requ_id) {
                this.requ_id = requ_id;
            }
        }
    }
}
