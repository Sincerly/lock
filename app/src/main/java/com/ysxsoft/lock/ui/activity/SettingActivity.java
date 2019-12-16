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
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.SettingResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/SettingActivity")
public class SettingActivity extends BaseActivity {
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

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.autograph)
    TextView autograph;
    @BindView(R.id.tvMemery)
    TextView tvMemery;
    @BindView(R.id.LoginOut)
    TextView LoginOut;

    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;
    @BindView(R.id.LL3)
    LinearLayout LL3;
    @BindView(R.id.LL4)
    LinearLayout LL4;
    @BindView(R.id.LL5)
    LinearLayout LL5;
    @BindView(R.id.LL6)
    LinearLayout LL6;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getSettingActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("设置");
    }

    @OnClick({R.id.backLayout, R.id.LL1, R.id.LL2, R.id.LL3, R.id.LL4, R.id.LL5, R.id.LL6, R.id.LoginOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                break;
            case R.id.LL2:
                EditInfoActivity.start();
                break;
            case R.id.LL3:
                break;
            case R.id.LL4:
                break;
            case R.id.LL5:
                break;
            case R.id.LL6:
                break;
            case R.id.LoginOut:
                toLogin();
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_SETTING)
                .addParams("uid", SharedPreferencesUtils.getUid(SettingActivity.this))
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
                        SettingResponse resp = JsonUtils.parseByGson(response, SettingResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<SettingResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取设置失败");
                        }
                    }
                });
    }
}
