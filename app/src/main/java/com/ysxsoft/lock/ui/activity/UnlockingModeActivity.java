package com.ysxsoft.lock.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.event.UnlockEvent;
import com.ysxsoft.lock.models.event.UnlockSuccessEvent;
import com.ysxsoft.lock.models.response.ActionResponse;
import com.ysxsoft.lock.models.response.MessageEvent;
import com.ysxsoft.lock.models.response.PwdResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.ysxsoft.lock.ui.dialog.CouponSuccessDialog;
import com.ysxsoft.lock.ui.dialog.OpenLockPwdDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
@Route(path = "/main/UnlockingModeActivity")
public class UnlockingModeActivity extends BaseActivity {
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

    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv10)
    TextView tv10;

    @Autowired
    String pass;
    @Autowired
    String equ_id;//门禁设备id
    @Autowired
    String requ_id;//小区id

    public static void start(String pass, String equ_id, String requ_id) {
        ARouter.getInstance().build(ARouterPath.getUnlockingModeActivity()).withString("pass", pass).withString("equ_id", equ_id).withString("requ_id", requ_id).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlocking_mode;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        initTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private CouponSuccessDialog dialog;

    @Subscriber
    public void showLockSuccessDialog(UnlockSuccessEvent event){
        hideLoadingDialog();
        dialog=CouponSuccessDialog.show(UnlockingModeActivity.this, true, false,
                ""
                ,""
                , ""
                , "", "", new CouponSuccessDialog.OnDialogClickListener() {
                    @Override
                    public void sure() {
                    }
                });
    }

    @Subscriber
    public void hideLockProgressDialog(UnlockEvent event){
        Log.e("tag","收到回调！");
        hideLoadingDialog();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("开锁方式");
    }

    @OnClick({R.id.backLayout, R.id.tv2, R.id.tv4, R.id.tv6, R.id.tv8, R.id.tv10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv2:
                showLoadingDialog("开门中");
                EventBus.getDefault().post(new MessageEvent(pass,requ_id,equ_id));
                break;
            case R.id.tv4:
                getPwd(equ_id);
                break;
            case R.id.tv6:
                openRemote(equ_id);
                break;
            case R.id.tv8:
                Intent intent = new Intent("SELECT");
                intent.putExtra("type", 1);
                sendBroadcast(intent);
                finish();
                break;
            case R.id.tv10:
                break;
        }
    }

    private void getPwd(String equid){
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.GET_PASSWORD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(this))
                .addParams("equid", equid)//设备锁id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "response:" + response);
                        hideLoadingDialog();
                        PwdResponse resp = JsonUtils.parseByGson(response, PwdResponse.class);
                        if (resp != null) {
                            if ("0".equals(resp.getResult())) {
                                //showToast(resp.getMsg());
                                OpenLockPwdDialog dialog = new OpenLockPwdDialog(UnlockingModeActivity.this, R.style.CenterDialogStyle);
                                dialog.setData(resp.getData());
                                dialog.showDialog();
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("生成密码失败");
                        }
                    }
                });
    }

    private void openRemote(String equid) {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.REMOTDOOR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(this))
                .addParams("equid", equid)//设备锁id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "response:" + response);
                        hideLoadingDialog();
                        ActionResponse resp = JsonUtils.parseByGson(response, ActionResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                showToast(resp.getMsg());
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("开门失败");
                        }
                    }
                });
    }
}
