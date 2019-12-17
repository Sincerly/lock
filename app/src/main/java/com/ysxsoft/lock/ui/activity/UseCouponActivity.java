package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.IntentUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.zxing.util.ZxingUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.UseCouponResponse;
import com.ysxsoft.lock.net.Api;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 使用券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/UseCouponActivity")
public class UseCouponActivity extends BaseActivity {
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

    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    @BindView(R.id.cl2)
    ConstraintLayout cl2;
    @BindView(R.id.riv)
    RoundImageView riv;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.ivBarCode)
    ImageView ivBarCode;
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getUseCouponActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_use_coupon;
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
        title.setText("优惠券使用");
//        ivBarCode.setImageBitmap(ZxingUtils.createBarcode(this,"546456464", DisplayUtils.dp2px(this,160),DisplayUtils.dp2px(this,54),false));
//        ivQrCode.setImageBitmap(ZxingUtils.createQRImage("daf",DisplayUtils.dp2px(mContext,160),DisplayUtils.dp2px(mContext,160),null,"url"));
    }

    @OnClick({R.id.backLayout, R.id.cl1, R.id.cl2, R.id.tv8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.cl1:
                TuanDetailActivity.start();
                break;
            case R.id.cl2:
                ShopDetailActivity.start();
                break;
            case R.id.tv8:
                IntentUtils.call(mContext, tv7.getText().toString().trim());
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_USE_COUPON)
                .addParams("uid", SharedPreferencesUtils.getUid(UseCouponActivity.this))
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
                        UseCouponResponse resp = JsonUtils.parseByGson(response, UseCouponResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<UseCouponResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取使用券失败");
                        }
                    }
                });
    }
}
