package com.ysxsoft.lock.ui.activity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.AppManager;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.StringUtils;
import com.ysxsoft.common_base.utils.action.GetCodeTimerUtils;
import com.ysxsoft.lock.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.OtherLoginResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 其他方式登录
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/OtherLoginActivity")
public class OtherLoginActivity extends BaseActivity {
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.backWithText)
    TextView backWithText;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customCenterTabView)
    LinearLayout customCenterTabView;
    @BindView(R.id.rightWithIcon)
    TextView rightWithIcon;
    @BindView(R.id.bg)
    LinearLayout bg;
    @BindView(R.id.bottomLineView)
    View bottomLineView;
    @BindView(R.id.parent)
    LinearLayout parent;

    @BindView(R.id.inputLoginPhone)
    EditText inputLoginPhone;
    @BindView(R.id.inputLoginPwd)
    EditText inputLoginPwd;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tvOk)
    TextView tvOk;

    @BindView(R.id.forgetPwd)
    TextView forgetPwd;
    @BindView(R.id.regist)
    TextView regist;


    private boolean isRunning = false;
    private GetCodeTimerUtils utils;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getOtherLoginActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_login;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        utils = GetCodeTimerUtils.getInstance();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("登录/注册");
    }

    @OnClick({R.id.backLayout, R.id.ivClose, R.id.forgetPwd, R.id.regist, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.ivClose:
                inputLoginPhone.setText("");
                break;

            case R.id.forgetPwd:
                ForgetPwdActivity.start();
                break;

            case R.id.regist:
                RegActivity.start();
                break;

            case R.id.tvOk:
                if (TextUtils.isEmpty(inputLoginPhone.getText().toString().trim())) {
                    showToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(inputLoginPwd.getText().toString().trim())) {
                    showToast("密码不能为空");
                    return;
                }
                request();
                break;


        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_OTHER_LOGIN)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("username", inputLoginPhone.getText().toString().trim())
                .addParams("password", inputLoginPwd.getText().toString().trim())
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "json====" + response);
                        hideLoadingDialog();
                        OtherLoginResponse resp = JsonUtils.parseByGson(response, OtherLoginResponse.class);
                        if (resp != null) {

                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                SharedPreferencesUtils.saveToken(mContext,resp.getApitoken());
                                MainActivity.start();
                                AppManager.getAppManager().finishAllActivity();
                                finish();
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取其他方式登录失败");
                        }
                    }
                });
    }
}
