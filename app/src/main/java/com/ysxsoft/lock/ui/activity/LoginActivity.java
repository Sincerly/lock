package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.PreLoginResultListener;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.LoginResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * 登录
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/LoginActivity")
public class LoginActivity extends BaseActivity {
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.Phone)
    TextView Phone;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.otherLogin)
    TextView otherLogin;
    @BindView(R.id.LL)
    LinearLayout LL;

    private final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String TAG = "LoginActivity";
    PhoneNumberAuthHelper helper = null;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getLoginActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void doWork() {
        super.doWork();
        requestPermissions();
    }

    @OnClick({R.id.login, R.id.otherLogin, R.id.LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                break;
            case R.id.otherLogin:
                OtherLoginActivity.start();
                break;
            case R.id.LL:
                break;
        }
    }

    /**
     * 获取手机号
     * @param accessToken
     */
    public void getMobile(String accessToken) {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.GET_MOBILE)
                .addParams("accessToken",accessToken)
                .addParams("outId", System.currentTimeMillis()+"")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        LoginResponse resp = JsonUtils.parseByGson(response, LoginResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<LoginResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                            Phone.setText(resp.getPhone());
                            helper.quitAuthActivity();
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
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

            if (token.contains("6000")) {
                return;
            }
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
                        break;
                     case "600002":
                        //唤起授权页失败
                        break;
                     case "600004":
                        //获取运营商配置信息失败
                        break;
                     case "600005":
                        //手机终端不安全
                        break;
                }
            }
        }

        @Override
        public void onTokenFailed(String s) {
            Log.e("tag", "onTokenFailed" + s);
        }
    };

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
                    .setLogoWidth(DisplayUtils.dp2px(LoginActivity.this, 32))
                    .setLogoHeight(DisplayUtils.dp2px(LoginActivity.this, 32))
                    .setSloganText(" ")
                    .setLogBtnBackgroundPath("shape_btn_bg")
                    .setLogBtnWidth(DisplayUtils.dp2px(LoginActivity.this, 122))
                    .setLogBtnHeight(DisplayUtils.dp2px(LoginActivity.this, 16))
                    .setLogBtnTextSize(16)
                    .setSwitchAccText("其他方式登录")
                    .setSwitchAccTextSize(12)
                    .setSwitchAccTextColor(Color.parseColor("#666666"))

                    .setPrivacyBefore("登录即同意我们的")
                    .setCheckboxHidden(true)
                    .setAppPrivacyOne("《服务协议》","http://www.baidu.com")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());

            helper.getLoginToken(LoginActivity.this, 30000);
        }
    }

    private void requestPermissions() {
        RxPermissions re = new RxPermissions(this);
        re.requestEach(BASIC_PERMISSIONS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }
}
