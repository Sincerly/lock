package com.ysxsoft.lock.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.ysxsoft.common_base.R;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.models.response.MobileResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * create by Sincerly on 2019/1/3 0003
 **/
public class SplashActivity extends BaseActivity {

    private ImageView splashImage;

    @Override
    public void doWork() {
        super.doWork();
        splashImage = findViewById(R.id.splashImage);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!SharedPreferencesUtils.isFirst(SplashActivity.this)){
                    ARouter.getInstance().build("/main/GuideActivity").navigation();
                    finish();
                }else{
                    if (!TextUtils.isEmpty(SharedPreferencesUtils.getToken(SplashActivity.this))) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //已登录 跳转到主页
                                ARouter.getInstance().build("/main/MainActivity").navigation();
                                finish();
                            }
                        }, 500);
                    } else {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //未登录 跳转到登录页面  Tips:宿主工程必须依赖 annotationProcessor 'com.alibaba:arouter-compiler:1.2.2'
//                                ARouter.getInstance().build("/main/LoginActivity").navigation();
//                                finish();
                                call();
                            }
                        }, 500);
                    }
                }
            }
        }, 500);
//        request();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    PhoneNumberAuthHelper helper;
    private void call() {
        helper = PhoneNumberAuthHelper.getInstance(this, tokenResultListener);
        helper.setAuthSDKInfo("isgu8Z+e5PJrU4I19s1OUByrgrcXD2aZswJ66jWoD/VRTW7umKLhbR1AAGGcMP9epo/v5LniY45VHEkbVRupMffyzUfTjpWZQyuuMnO/7r66hu/TDpjBKSAB8MqFh+F9FxUpx6+eUn75ZH1RLvJ3VIBRl/5qmu1gIWGFs9dgNFQJtWt6jVw8jfK6ZyXLjakfI5HmV2d2ekoxwDOjacGAeQdR+NobYAwBbCFP2sXB/ouESJd/Pko2aBzODZc1H0+/UWWyPmkNo8M2WTuwa4rT3A0v1/zR7D/b");
        helper.setLoggerEnable(true);
        if (helper.checkEnvAvailable()) {
            //检查终端是否支持号码认证
            helper.setAuthUIConfig(new AuthUIConfig.Builder()
                    .setLogBtnText("手机号码一键登录")
                    .setNavHidden(true)
                    .setNavColor(getResources().getColor(R.color.colorPrimary))
                    .setLogoImgPath("ic_launcher")
                    .setLogoWidth(DisplayUtils.dp2px(SplashActivity.this, 32))
                    .setLogoHeight(DisplayUtils.dp2px(SplashActivity.this, 32))
                    .setSloganText(" ")
                    .setLogBtnBackgroundPath("shape_btn_bg")
                    .setLogBtnWidth(DisplayUtils.dp2px(SplashActivity.this, 122))
                    .setLogBtnHeight(DisplayUtils.dp2px(SplashActivity.this, 16))
                    .setLogBtnTextSize(16)
                    .setSwitchAccText("其他方式登录")
                    .setSwitchAccTextSize(12)
                    .setSwitchAccTextColor(Color.parseColor("#666666"))

                    .setPrivacyBefore("登录即同意我们的")
                    .setCheckboxHidden(true)
                    .setAppPrivacyOne("《服务协议》","http://www.baidu.com")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());
            helper.getLoginToken(SplashActivity.this, 30000);
        }
    }

    private TokenResultListener tokenResultListener = new TokenResultListener() {
        @Override
        public void onTokenSuccess(final String token) {
            Log.e("tag", "onTokenSuccess" + token);
            helper.setUIClickListener(new AuthUIControlClickListener() {
                @Override
                public void onClick(String s, Context context, JSONObject jsonObject) {
                    Log.e("tag",s+" "+jsonObject);
                    switch (s){
                        case "700000":
                            //点击返回 用户取消免密登录
                            break;
                        case "700001":
                            //点击切换 用户取消免密登录
                            OtherLoginActivity.start();
                            break;
                        case "700002":
                            //点击登录按钮事件
                            break;
                        case "700003":
                            //点击check box事件
                            break;
                        case "700004":
                            //点击协议富文本文字事件
                            break;
                    }
                }
            });

            TokenRet tokenRet = JSON.parseObject(token, TokenRet.class);
            if (tokenRet != null) {
                switch (tokenRet.getCode()){
                    case "600000":
                        //获取token成功
                        String tok = tokenRet.getToken();
                        getMobile(tok);
                        break;
                    case "600001":
                        //唤起授权页成功
                        //showToast(tokenRet.getMsg());
                        break;
                    case "600002":
                        //唤起授权页失败
                        showToast(tokenRet.getMsg());
                        break;
                    case "600004":
                        //获取运营商配置信息失败
                        showToast(tokenRet.getMsg());
                        break;
                    case "600005":
                        //手机终端不安全
                        showToast(tokenRet.getMsg());
                        break;
                }
            }
        }

        @Override
        public void onTokenFailed(String s) {
            Log.e("tag", "onTokenFailed" + s);
        }
    };

    public void getMobile(String accessToken) {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url("http://47.99.219.208:8080/api/auth/getmobile?accessToken="+accessToken)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag","返回值:"+response);
                        hideLoadingDialog();
                        MobileResponse resp = JsonUtils.parseByGson(response, MobileResponse.class);
                        if (resp != null) {
                            SharedPreferencesUtils.saveToken(SplashActivity.this,resp.getApitoken());
                            MainActivity.start();
                            helper.quitAuthActivity();
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
    }

    public class LoginResponse{

        /**
         * token :
         * phone :
         */

        private String token;
        private String phone;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
