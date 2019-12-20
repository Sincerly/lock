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
import com.alibaba.fastjson.JSONObject;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.PreLoginResultListener;
import com.mobile.auth.gatewayauth.TokenResultListener;
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
        initTitle();
        initConfig();
    }

    private void initTitle() {
//        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//        backLayout.setVisibility(View.VISIBLE);
//        back.setImageResource(R.mipmap.icon_gray_back);
//        title.setText("");
    }

    @OnClick({R.id.login, R.id.otherLogin, R.id.LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                call();
                break;
            case R.id.otherLogin:
                OtherLoginActivity.start();
                break;
            case R.id.LL:
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_LOGIN)
                .addParams("uid", SharedPreferencesUtils.getUid(LoginActivity.this))
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
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
    }

    private TokenResultListener tokenResultListener = new TokenResultListener() {
        @Override
        public void onTokenSuccess(String s) {
            Log.e("tag", "onTokenSuccess" + s);
            helper.setAuthUIConfig(new AuthUIConfig.Builder()
                    .setLogBtnText("手机号码一键登录")
                    .setNavHidden(true)
                    .setLogoImgPath("ic_launcher")
                    .setLogoWidth(DisplayUtils.dp2px(LoginActivity.this, 90))
                    .setLogoHeight(DisplayUtils.dp2px(LoginActivity.this, 90))
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
                    .setAppPrivacyOne("《服务协议》","11")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());

            helper.accelerateLoginPage(30000, new PreLoginResultListener() {
                @Override
                public void onTokenSuccess(String s) {
                    Log.e("tag", "login onTokenSuccess" + s);
                }

                @Override
                public void onTokenFailed(String type, String s1) {
                    Log.e("tag", "login onTokenFailed" + type + " " + s1);
                }
            });
        }

        @Override
        public void onTokenFailed(String s) {
            Log.e("tag", "onTokenFailed" + s);
        }
    };
    PhoneNumberAuthHelper helper = null;

    private void call() {
        helper = PhoneNumberAuthHelper.getInstance(this, tokenResultListener);
        helper.setAuthSDKInfo("isgu8Z+e5PJrU4I19s1OUByrgrcXD2aZswJ66jWoD/VRTW7umKLhbR1AAGGcMP9epo/v5LniY45VHEkbVRupMffyzUfTjpWZQyuuMnO/7r66hu/TDpjBKSAB8MqFh+F9FxUpx6+eUn75ZH1RLvJ3VIBRl/5qmu1gIWGFs9dgNFQJtWt6jVw8jfK6ZyXLjakfI5HmV2d2ekoxwDOjacGAeQdR+NobYAwBbCFP2sXB/ouESJd/Pko2aBzODZc1H0+/UWWyPmkNo8M2WTuwa4rT3A0v1/zR7D/b");
        helper.setLoggerEnable(true);
        if (helper.checkEnvAvailable()) {
            //检查终端是否支持号码认证
            helper.getLoginToken(LoginActivity.this, 30000);
        }
        helper.setUIClickListener(new AuthUIControlClickListener() {
            @Override
            public void onClick(String s, Context context, JSONObject jsonObject) {
                Log.e("tag","onClick "+s+" context"+context+" jsonObject:");
                switch (s){
                    case "700004":
                        //隐私协议
                        break;
                    case "700001":
                        //切换登录方式 CUCC中国联通
                        break;
                }
            }
        });
    }

    private void initConfig() {
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
