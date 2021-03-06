package com.ysxsoft.lock.models.response;

import java.util.List;

/**
* 帮助
* create by Sincerly on 9999/9/9 0009
**/
public class HelpResponse{


    /**
     * msg : 操作成功
     * code : 200
     * data : [{"id":3,"title":"帮助列表","content":"帮助列表","create_time":"2020-01-18 17:49:55"},{"id":4,"title":"帮助列表1","content":"帮助列表1","create_time":"2020-01-18 17:50:03"}]
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
         * id : 3
         * title : 帮助列表
         * content : 帮助列表
         * create_time : 2020-01-18 17:49:55
         */

        private String id;
        private String title;
        private String content;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
