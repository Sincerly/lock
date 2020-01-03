package com.ysxsoft.lock.net;

import com.ysxsoft.lock.config.AppConfig;


/**
 * 接口地址
 */
public interface Api {

    public final String BASE_URL="http://47.99.219.208:8080";//手机号注册

    //登录
    public final String GET_LOGIN= BASE_URL+"";
    //注册
    public final String GET_REG= BASE_URL+"";
    //忘记密码
    public final String GET_FORGET_PWD= BASE_URL+"";
    //卡包
    public final String GET_PACKET= BASE_URL+"";
    //商铺详情
    public final String GET_SHOP_DETAIL= BASE_URL+"";
    //商圈
    public final String GET_SHOP_LIST= BASE_URL+"";
    //团购详情
    public final String GET_TUAN_DETAIL= BASE_URL+"";
    //使用券
    public final String GET_USE_COUPON= BASE_URL+"";
    //我的
    public final String GET_USER_INFO= BASE_URL+"";
    //设置
    public final String GET_SETTING= BASE_URL+"";
    //信息编辑
    public final String GET_EDIT_INFO= BASE_URL+"";
    //关于我们
    public final String GET_ABOUT_ME= BASE_URL+"";
    //添加新小区
    public final String GET_ADD_PLACE= BASE_URL+"";
    //申请钥匙
    public final String GET_APPLY_KEY= BASE_URL+"";
    //钥匙管理
    public final String GET_KEY_MANAGER= BASE_URL+"";
    //实名认证
    public final String GET_IDCARD_CERT= BASE_URL+"";
    //物业认证
    public final String GET_PROPERTY_CERT= BASE_URL+"";
    //意见反馈
    public final String GET_FEED_BACK= BASE_URL+"";

    //商户中心
    public final String GET_SHOP_CENTER= BASE_URL+"";
    //
    public final String GET_SHOP_AUTHENTICATION= BASE_URL+"";
    //商户认证审核通过
    public final String GET_SHOP_EGIS= BASE_URL+"";
    //商户认证审核失败
    public final String GET_SHOP_AUDIT_FAILED= BASE_URL+"";
    //商户信息
    public final String GET_SHOP_INFO= BASE_URL+"";
    //店铺管理
    public final String GET_SHOP_MANAGER= BASE_URL+"";
    //核销成功
    public final String GET_CHECK_SUCESS= BASE_URL+"";
    //核销记录
    public final String GET_CHECK_RECORD= BASE_URL+"";
    //添加卡卷现金券
    public final String GET_ADD_PACKET_MONEY= BASE_URL+"";
    //添加卡卷团购券
    public final String GET_ADD_PACKET_GROUP= BASE_URL+"";
    //添加卡卷会员卡券
    public final String GET_ADD_PACKET_VIP= BASE_URL+"";
    //添加卡卷体验券
    public final String GET_ADD_PACKET_EXPERIENCE= BASE_URL+"";
    //开始投放
    public final String GET_START_AD_SERVING_LIST= BASE_URL+"";
    //卡劵投放
    public final String GET_PACKET_SERVING_LIST= BASE_URL+"";
    //卡劵投放充值
    public final String GET_PACKET_RECHARGE= BASE_URL+"";
    //充值成功
    public final String GET_RECHARGE_SUCESS= BASE_URL+"";
    //帮助
    public final String GET_HELP_LIST= BASE_URL+"";


    ///////////////////////////////////////////////////////////////////////////
    // 接口对接
    ///////////////////////////////////////////////////////////////////////////



    public final String GET_OTHER_LOGIN= BASE_URL+"/api/auth/login";    //其他方式登录
    public final String REG=BASE_URL+"/api/auth/register";//手机号注册
    public final String FORGET_PWD=BASE_URL+"/api/auth/repass";//找回密码
    public final String LOGOUT=BASE_URL+"/logout";//退出登录
    public final String LOGIN=BASE_URL+"/api/auth/login";//用户名/密码登录
    public final String GET_MOBILE=BASE_URL+"/api/auth/getmobile";//阿里云一键登录取号
    public final String PROVINCE_LIST=BASE_URL+"/api/pcd/listp";//省列表
    public final String CITY_LIST=BASE_URL+"/api/pcd/listc";//市列表
    public final String DISTRICT_LIST=BASE_URL+"/api/pcd/lista";//区县列表
    public final String VERFY_CODE=BASE_URL+"/api/sms/verifycode";//校验验证码
    public final String GET_PHONE=BASE_URL+"/api/sms/sendsms";//获取手机验证码

    public final String SAVE_INFO=BASE_URL+"/api/members/saveunit";//保存住户信息
    public final String GET_DEVICE_INFO=BASE_URL+"/api/members/getunitequ";//获取该单元门禁设备
    public final String KEY_APPLY=BASE_URL+"/api/members/bindkey";//钥匙申请
    public final String BIND_PLACE=BASE_URL+"/api/members/bindresq";//用户绑定小区
    public final String DELETE_PLACE=BASE_URL+"/api/members/delrequ";//删除小区
    public final String SET_DEFAULT_PLACE=BASE_URL+"/api/members/setdefault";//设置默认小区
    public final String GET_DEFAULT_PLACE_INFO=BASE_URL+"/api/members/getresq_default";//获取默认小区信息
    public final String GET_BIND_PLACE_LIST=BASE_URL+"/api/members/listresq";//获取用户绑定小区信息列表
    public final String GET_ADDRESS_LIST=BASE_URL+"/api/members/listresq_cd";//按市/区县 获取小区列表
    public final String GET_FLOOR_INFO=BASE_URL+"/api/members/listfloor";//获取小区楼栋信息
    public final String GET_FLOOR_CHILD_INFO=BASE_URL+"/api/members/listunit";//获取小区楼栋单元信息
    public final String GET_NEAR_FLOOR_INFO=BASE_URL+"/api/members/getnear";//获取附近小区信息

    public final String TOU_HISTORY=BASE_URL+"/api/business/listputin";//会员卡券投放记录
    public final String HX=BASE_URL+"/api/business/costcard";//会员卡券核销
    public final String HX_HISTORY=BASE_URL+"/api/business/listcostcard";//会员卡券核销记录
    public final String CARD_INFO_LIST=BASE_URL+"/api/business/listtickethis";//卡券信息列表
    public final String PUBLISH_CARD=BASE_URL+"/api/business/putin";//卡券投放
    public final String SHOP_CERT=BASE_URL+"/api/business/businessauth";//商户认证
    public final String UPLOAD_SHOP_LOGO=BASE_URL+"/api/business/businesslogo";//店铺Logo上传
    public final String IS_SHOP_CERT=BASE_URL+"/api/business/isbusinessauth";//是否通过商户认证
    public final String EDIT_INFO=BASE_URL+"/api/business/savebusiness";//添加/修改 店铺信息
    public final String ADD_CARD=BASE_URL+"/api/business/savecard";//添加卡券
    public final String CARD_RECHARGE_LIST=BASE_URL+"/api/business/listrecharge";//点券充值列表
    public final String CARD_INFO=BASE_URL+"/api/business/getcard";//获取卡券信息
    public final String CARD_LIST=BASE_URL+"/api/business/listcard";//获取卡券列表
    public final String CARD_BANLANCE=BASE_URL+"/api/business/getticket";//获取店点券余额
    public final String SHOP_INFO=BASE_URL+"/api/business/getbusiness";//获取店铺信息

    public final String IS_AUTH=BASE_URL+"/api/members/isauth";//用户是否已通过实名认证
    public final String FEED_BACK=BASE_URL+"/api/members/feedback";//意见反馈
    public final String TENANT=BASE_URL+"/api/members/tenant";//物业认证--租户认证
    public final String OWNER=BASE_URL+"/api/members/owner";//物业认证--业主认证
    public final String SAVE_AUTH_INFO=BASE_URL+"/api/members/saveauth";//新增用户实名认证信息
    public final String UPLOAD_IMG=BASE_URL+"/api/members/faceimg";//人脸识别上传
    public final String EDIT_SIGN=BASE_URL+"/api/members/setgrap";//修改签名
    public final String EDIT_NICKNAME=BASE_URL+"/api/members/setnickname";//修改昵称
    public final String EDIT_LOGO=BASE_URL+"/api/members/headimg";//上传用户头像
    public final String IS_FACE=BASE_URL+"/api/members/isface";//人脸识别 认证状态
}
