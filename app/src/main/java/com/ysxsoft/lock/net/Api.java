package com.ysxsoft.lock.net;

import com.ysxsoft.lock.config.AppConfig;

/**
 * 接口地址
 */
public interface Api {
    //登录
    public final String GET_LOGIN= AppConfig.BASE_URL+"";
    //其他方式登录
    public final String GET_OTHER_LOGIN= AppConfig.BASE_URL+"";
    //注册
    public final String GET_REG= AppConfig.BASE_URL+"";
    //忘记密码
    public final String GET_FORGET_PWD= AppConfig.BASE_URL+"";
    //卡包
    public final String GET_PACKET= AppConfig.BASE_URL+"";
    //商铺详情
    public final String GET_SHOP_DETAIL= AppConfig.BASE_URL+"";
    //商圈
    public final String GET_SHOP_LIST= AppConfig.BASE_URL+"";
    //团购详情
    public final String GET_TUAN_DETAIL= AppConfig.BASE_URL+"";
    //使用券
    public final String GET_USE_COUPON= AppConfig.BASE_URL+"";
    //我的
    public final String GET_USER_INFO= AppConfig.BASE_URL+"";
    //设置
    public final String GET_SETTING= AppConfig.BASE_URL+"";
    //信息编辑
    public final String GET_EDIT_INFO= AppConfig.BASE_URL+"";
    //关于我们
    public final String GET_ABOUT_ME= AppConfig.BASE_URL+"";
    //添加新小区
    public final String GET_ADD_PLACE= AppConfig.BASE_URL+"";
    //申请钥匙
    public final String GET_APPLY_KEY= AppConfig.BASE_URL+"";
    //钥匙管理
    public final String GET_KEY_MANAGER= AppConfig.BASE_URL+"";
    //实名认证
    public final String GET_IDCARD_CERT= AppConfig.BASE_URL+"";
    //物业认证
    public final String GET_PROPERTY_CERT= AppConfig.BASE_URL+"";
    //意见反馈
    public final String GET_FEED_BACK= AppConfig.BASE_URL+"";

    //商户中心
    public final String GET_SHOP_CENTER= AppConfig.BASE_URL+"";
    //商户认证
    public final String GET_SHOP_AUTHENTICATION= AppConfig.BASE_URL+"";
    //商户认证审核通过
    public final String GET_SHOP_EGIS= AppConfig.BASE_URL+"";
    //商户认证审核失败
    public final String GET_SHOP_AUDIT_FAILED= AppConfig.BASE_URL+"";
    //商户信息
    public final String GET_SHOP_INFO= AppConfig.BASE_URL+"";
    //店铺管理
    public final String GET_SHOP_MANAGER= AppConfig.BASE_URL+"";
    //核销成功
    public final String GET_CHECK_SUCESS= AppConfig.BASE_URL+"";
    //核销记录
    public final String GET_CHECK_RECORD= AppConfig.BASE_URL+"";
    //添加卡卷现金券
    public final String GET_ADD_PACKET_MONEY= AppConfig.BASE_URL+"";
    //添加卡卷团购券
    public final String GET_ADD_PACKET_GROUP= AppConfig.BASE_URL+"";
    //添加卡卷会员卡券
    public final String GET_ADD_PACKET_VIP= AppConfig.BASE_URL+"";
    //添加卡卷体验券
    public final String GET_ADD_PACKET_EXPERIENCE= AppConfig.BASE_URL+"";
    //开始投放
    public final String GET_START_AD_SERVING_LIST= AppConfig.BASE_URL+"";
    //卡劵投放
    public final String GET_PACKET_SERVING_LIST= AppConfig.BASE_URL+"";
    //卡劵投放充值
    public final String GET_PACKET_RECHARGE= AppConfig.BASE_URL+"";
    //充值成功
    public final String GET_RECHARGE_SUCESS= AppConfig.BASE_URL+"";
    //帮助
    public final String GET_HELP_LIST= AppConfig.BASE_URL+"";
}
