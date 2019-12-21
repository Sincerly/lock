package com.ysxsoft.lock.ui.activity;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ui.dialog.SelectPayMethodDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.PacketRechargeResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 卡劵投放充值
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/PacketRechargeActivity")
public class PacketRechargeActivity extends BaseActivity {
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

    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tvTip)
    TextView tvTip;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.tvGrayMoney)
    TextView tvGrayMoney;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvOk)
    TextView tvOk;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getPacketRechargeActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_packet_recharge;
    }

    @Override
    public void doWork() {
        super.doWork();
        Drawable no = getResources().getDrawable(R.mipmap.icon_card_normal);
        no.setBounds(0, 0, no.getMinimumWidth(), no.getMinimumHeight());// 设置边界
        Drawable ok = getResources().getDrawable(R.mipmap.icon_card_select);
        ok.setBounds(0, 0, ok.getMinimumWidth(), ok.getMinimumHeight());// 设置边界
        tv4.setCompoundDrawables(null, null, ok, null);
        tv5.setCompoundDrawables(null, null, no, null);
        tv6.setCompoundDrawables(null, null, no, null);
        tvGrayMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        initTitle();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("卡劵投放");
    }

    @OnClick({R.id.backLayout, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tvOk})
    public void onViewClicked(View view) {

        Drawable no = getResources().getDrawable(R.mipmap.icon_card_normal);
        no.setBounds(0, 0, no.getMinimumWidth(), no.getMinimumHeight());// 设置边界
        Drawable ok = getResources().getDrawable(R.mipmap.icon_card_select);
        ok.setBounds(0, 0, ok.getMinimumWidth(), ok.getMinimumHeight());// 设置边界

        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv3:
                PacketServingActivity.start();
                break;
            case R.id.tv4:
                tv4.setCompoundDrawables(null, null, ok, null);
                tv5.setCompoundDrawables(null, null, no, null);
                tv6.setCompoundDrawables(null, null, no, null);
                break;
            case R.id.tv5:
                tv4.setCompoundDrawables(null, null, no, null);
                tv5.setCompoundDrawables(null, null, ok, null);
                tv6.setCompoundDrawables(null, null, no, null);
                break;
            case R.id.tv6:
                tv4.setCompoundDrawables(null, null, no, null);
                tv5.setCompoundDrawables(null, null, no, null);
                tv6.setCompoundDrawables(null, null, ok, null);
                break;
            case R.id.tvOk:
                SelectPayMethodDialog.show(mContext, new SelectPayMethodDialog.OnDialogClickListener() {
                    @Override
                    public void sure(int type) {// type 1 微信  2 支付宝
                        switch (type) {
                            case 1:
                                showToast("微信");
                                break;
                            case 2:
                                showToast("支付宝");
                                break;
                        }
                    }
                });
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_PACKET_RECHARGE)
                .addParams("uid", SharedPreferencesUtils.getUid(PacketRechargeActivity.this))
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
                        PacketRechargeResponse resp = JsonUtils.parseByGson(response, PacketRechargeResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<PacketRechargeResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取卡劵投放充值失败");
                        }
                    }
                });
    }
}
