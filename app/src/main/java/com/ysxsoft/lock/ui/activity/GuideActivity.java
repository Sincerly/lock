package com.ysxsoft.lock.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerViewAdapter;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.LoginResponse;
import com.ysxsoft.lock.models.response.MobileResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

@Route(path = "/main/GuideActivity")
public class GuideActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.into)
    Button into;
    @BindView(R.id.menu1)
    TextView menu1;
    @BindView(R.id.menu2)
    TextView menu2;
    @BindView(R.id.menu3)
    TextView menu3;
    PhoneNumberAuthHelper helper = null;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getGuideActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void doWork() {
        super.doWork();
        List<TextView> points=new ArrayList<>();
        menu1.setSelected(true);
        points.add(menu1);
        points.add(menu2);
        points.add(menu3);
        List<Integer> list=new ArrayList<>();
        list.add(R.mipmap.icon_setupp1);
        list.add(R.mipmap.icon_setupp2);
        list.add(R.mipmap.icon_setupp3);
        list.add(R.mipmap.icon_setupp4);
        ViewPagerViewAdapter adapter=new ViewPagerViewAdapter<Integer>(GuideActivity.this,list,R.layout.view_gui) {
            @Override
            protected void fillView(View view, int position, Integer resource) {
                ImageView pic=view.findViewById(R.id.pic);
                pic.setImageResource(resource);
            }
        };
        viewPager.setOffscreenPageLimit(4);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i <points.size() ; i++) {
                    if(i==position){
                        points.get(i).setSelected(true);
                    }else{
                        points.get(i).setSelected(false);
                    }
                }
                if(position==3){
                    into.setAlpha(0);
                    into.setVisibility(View.VISIBLE);
                    ViewCompat.animate(into).alpha(0f).alpha(1f).setDuration(600).start();
                }else{
                    into.setAlpha(1);
                    ViewCompat.animate(into).alpha(1f).alpha(0f).setDuration(600).start();
                    into.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.into)
    public void onViewClicked() {
        SharedPreferencesUtils.saveFirst(GuideActivity.this,true);
        if("".equals(SharedPreferencesUtils.getToken(GuideActivity.this))){
            //调用一键取号
            call();
        }else{
//            ARouter.getInstance().build("/main/MainActivity").navigation();
        }
    }
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
                    .setLogoWidth(DisplayUtils.dp2px(GuideActivity.this, 32))
                    .setLogoHeight(DisplayUtils.dp2px(GuideActivity.this, 32))
                    .setSloganText(" ")
                    .setLogBtnBackgroundPath("shape_btn_bg")
                    .setLogBtnWidth(DisplayUtils.dp2px(GuideActivity.this, 122))
                    .setLogBtnHeight(DisplayUtils.dp2px(GuideActivity.this, 16))
                    .setLogBtnTextSize(16)
                    .setSwitchAccText("其他方式登录")
                    .setSwitchAccTextSize(12)
                    .setSwitchAccTextColor(Color.parseColor("#666666"))

                    .setPrivacyBefore("登录即同意我们的")
                    .setCheckboxHidden(true)
                    .setAppPrivacyOne("《服务协议》","http://www.baidu.com")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());
            helper.getLoginToken(GuideActivity.this, 30000);
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
                .url(Api.GET_MOBILE+"?accessToken="+accessToken)
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
                            SharedPreferencesUtils.saveToken(GuideActivity.this,resp.getApitoken());
                            MainActivity.start();
                            helper.quitAuthActivity();
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
    }
}
