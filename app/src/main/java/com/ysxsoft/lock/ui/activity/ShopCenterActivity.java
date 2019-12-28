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
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CertificationDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ShopCenterResponse;
import com.ysxsoft.lock.net.Api;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 商户中心
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ShopCenterActivity")
public class ShopCenterActivity extends BaseActivity {
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
    ConstraintLayout parent;

    @BindView(R.id.tvApplyShop)
    TextView tvApplyShop;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopCenterActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_center;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.transparent));
        backLayout.setVisibility(View.VISIBLE);
        bottomLineView.setVisibility(View.GONE);
        back.setImageResource(R.mipmap.icon_white_back);
        title.setText("");
    }

    @OnClick({R.id.backLayout, R.id.tvApplyShop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvApplyShop:
                IsAuth();
                break;
        }
    }

    private void IsAuth() {
        OkHttpUtils.post()
                .url(Api.IS_AUTH)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            switch (resp.getCode()) {
                                case "200":
                                    ShopAuthenticationActivity.start();
                                    finish();
                                    break;
                                case "201":
                                    showToast("实名认证审核中");
                                    break;
                                case "202":
                                    showToast("实名认证审核失败");
                                    break;
                                case "203":
                                    CertificationDialog.show(mContext, new CertificationDialog.OnDialogClickListener() {
                                        @Override
                                        public void sure() {
                                            IdcardCertActivity.start();
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
    }


    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_SHOP_CENTER)
                .addParams("uid", SharedPreferencesUtils.getUid(ShopCenterActivity.this))
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
                        ShopCenterResponse resp = JsonUtils.parseByGson(response, ShopCenterResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<ShopCenterResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取商户中心失败");
                        }
                    }
                });
    }
}
