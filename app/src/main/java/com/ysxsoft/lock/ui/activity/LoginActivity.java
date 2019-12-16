package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.LoginResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 登录
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/LoginActivity")
public class LoginActivity extends BaseActivity {
//    @BindView(R.id.statusBar)
//    View statusBar;
//    @BindView(R.id.backWithText)
//    TextView backWithText;
//    @BindView(R.id.back)
//    ImageView back;
//    @BindView(R.id.backLayout)
//    LinearLayout backLayout;
//    @BindView(R.id.title)
//    TextView title;
//    @BindView(R.id.customCenterTabView)
//    LinearLayout customCenterTabView;
//    @BindView(R.id.rightWithIcon)
//    TextView rightWithIcon;
//    @BindView(R.id.bg)
//    LinearLayout bg;
//    @BindView(R.id.bottomLineView)
//    View bottomLineView;
//    @BindView(R.id.parent)
//    LinearLayout parent;

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
        initTitle();
    }

    private void initTitle() {
//        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//        backLayout.setVisibility(View.VISIBLE);
//        back.setImageResource(R.mipmap.icon_gray_back);
//        title.setText("");
    }

    @OnClick({R.id.backLayout, R.id.login, R.id.otherLogin, R.id.LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.login:
                MainActivity.start();
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
}
