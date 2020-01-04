package com.ysxsoft.lock.ui.activity;

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
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.StringUtils;
import com.ysxsoft.common_base.utils.action.GetCodeTimerUtils;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.models.response.VerfyCodeRes;
import com.ysxsoft.lock.models.response.resp.SendMsgResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.RegResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 注册
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/RegActivity")
public class RegActivity extends BaseActivity {
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
    @BindView(R.id.inputLoginCode)
    EditText inputLoginCode;
    @BindView(R.id.inputLoginPwd)
    EditText inputLoginPwd;
    @BindView(R.id.inputSecondLoginPwd)
    EditText inputSecondLoginPwd;
    @BindView(R.id.sendMsg)
    TextView sendMsg;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tvOk)
    TextView tvOk;
    private boolean isRunning = false;
    private GetCodeTimerUtils utils;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getRegActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reg;
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
        title.setText("注册");
    }

    @OnClick({R.id.backLayout, R.id.sendMsg, R.id.ivClose, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.sendMsg:
                //获取验证码
                if (StringUtils.isEmpty(inputLoginPhone.getText().toString().trim())) {
                    showToast("请填写手机号码！");
                    return;
                }
                if (!StringUtils.checkPhone(inputLoginPhone.getText().toString().trim())) {
                    showToast("手机号码格式不正确！");
                    return;
                }
                if (!isRunning) {
                    utils.initDelayTime(60);
                    utils.initStepTime(1);
                    utils.setOnGetCodeListener(new GetCodeTimerUtils.OnGetCodeListener() {
                        @Override
                        public void onRunning(int totalTime) {
                            sendMsg.setText(totalTime + "s后重新获取");
                            isRunning = true;
                        }

                        @Override
                        public void onFinish() {
                            utils.stopDelay();
                            sendMsg.setText("重新获取");
                            isRunning = false;
                        }
                    });
                    utils.startDelay();
                }
                SendMsg(inputLoginPhone.getText().toString().trim());
                break;
            case R.id.ivClose:
                inputLoginPhone.setText("");
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(inputLoginPhone.getText().toString().trim())) {
                    showToast("手机号号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(inputLoginCode.getText().toString().trim())) {
                    showToast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(inputLoginPwd.getText().toString().trim())) {
                    showToast("密码不能为空");
                    return;
                }
                if (inputLoginPwd.getText().toString().trim().length() < 6) {
                    showToast("密码不能少于六位");
                    return;
                }

                if (TextUtils.isEmpty(inputSecondLoginPwd.getText().toString().trim())) {
                    showToast("再次输入密码不能为空");
                    return;
                }

                if (!TextUtils.equals(inputLoginPwd.getText().toString().trim(), inputSecondLoginPwd.getText().toString().trim())) {
                    showToast("两次输入密码不一致");
                    return;
                }
                request();
                break;
        }
    }

    private void SendMsg(String phone) {
        OkHttpUtils.post()
                .url(Api.GET_PHONE)
                .addParams("phone", phone)
                .addParams("businesstype", "MEMBER_REGISTER")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", "e=====" + e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SendMsgResponse resp = JsonUtils.parseByGson(response, SendMsgResponse.class);
                        if (resp != null) {
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(String.valueOf(resp.getCode()))) {
                                String key = resp.getKey();
                                SharedPreferencesUtils.saveSp(mContext,"Regkey",key);
                                CheckCode(key);
                            }
                        }
                    }
                });


    }

    private void CheckCode(String key) {
        OkHttpUtils.post()
                .url(Api.VERFY_CODE)
                .addParams("key", key)
                .addParams("code", inputLoginCode.getText().toString().trim())
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "response==" + response);
                        VerfyCodeRes verfyCodeRes = JsonUtils.parseByGson(response, VerfyCodeRes.class);
                        if (verfyCodeRes != null) {
                            showToast(verfyCodeRes.getMsg());
                        }
                    }
                });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.REG)
                .addParams("phone", inputLoginPhone.getText().toString().trim())
                .addParams("password", inputLoginPwd.getText().toString().trim())
                .addParams("key", SharedPreferencesUtils.getSp(mContext,"Regkey"))
                .addParams("code", inputLoginCode.getText().toString().trim())
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
                        RegResponse resp = JsonUtils.parseByGson(response, RegResponse.class);
                        if (resp != null) {
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(String.valueOf(resp.getCode()))) {
//                                SharedPreferencesUtils.saveToken(mContext, resp.getToken());
//                                MainActivity.start();
                                finish();
                            }
                        } else {
                            showToast("获取注册失败");
                        }
                    }
                });
    }
}
