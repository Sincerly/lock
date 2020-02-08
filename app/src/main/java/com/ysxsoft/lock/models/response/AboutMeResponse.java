package com.ysxsoft.lock.models.response;

/**
* 关于我们
* create by Sincerly on 9999/9/9 0009
**/
public class AboutMeResponse{

    /**
     * msg : 操作成功
     * code : 200
     * data : {"id":1,"title":null,"content":"服务协议","create_time":"2020-01-09 00:08:32"}
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
         * id : 1
         * title : null
         * content : 服务协议
         * create_time : 2020-01-09 00:08:32
         */

        private int id;
        private String title;
        private String content;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
