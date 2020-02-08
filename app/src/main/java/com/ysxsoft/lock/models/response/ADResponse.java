package com.ysxsoft.lock.models.response;

public class ADResponse {


    /**
     * msg : 操作成功
     * code : 200
     * data : {"requId":null,"advertisePosition":null,"url":"http://web.linlilinwai.com/h5/download","picturePath":"news/4249837658227712.jpg"}
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
         * requId : null
         * advertisePosition : null
         * url : http://web.linlilinwai.com/h5/download
         * picturePath : news/4249837658227712.jpg
         */
        private String requId;
        private String advertisePosition;
        private String url;
        private String picturePath;

        public String getRequId() {
            return requId;
        }

        public void setRequId(String requId) {
            this.requId = requId;
        }

        public String getAdvertisePosition() {
            return advertisePosition;
        }

        public void setAdvertisePosition(String advertisePosition) {
            this.advertisePosition = advertisePosition;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
        }
    }
}
