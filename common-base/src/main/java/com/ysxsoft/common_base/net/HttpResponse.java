package com.ysxsoft.common_base.net;

/**
 * create by Sincerly on 2019/1/21 0021
 **/
public interface HttpResponse {
    String SUCCESS = "200";
    String ERROR = "1";
    String NONE = "2";
    String NO_RESPONSE = "500";//系统内部错误
    String EMPTY="204";//操作已经执行成功，但是没有返回数据
}
