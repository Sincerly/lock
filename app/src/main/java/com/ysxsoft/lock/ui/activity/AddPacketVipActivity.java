package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
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
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPacketVipResponse;
import com.ysxsoft.lock.net.Api;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 添加卡卷会员卡券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AddPacketVipActivity")
public class AddPacketVipActivity extends BaseActivity {
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

    @BindView(R.id.tvPacketType)
    TextView tvPacketType;
    @BindView(R.id.etMoneyZL)
    EditText etMoneyZL;
    @BindView(R.id.etLimitNum)
    EditText etLimitNum;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvOk)
    TextView tvOk;
    public static void start() {
        ARouter.getInstance().build(ARouterPath.getAddPacketVipActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_packet_vip;
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
        title.setText("添加卡卷");
    }

    @OnClick({R.id.backLayout, R.id.tvPacketType, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvPacketType:
                showToast("会员卡");
                break;
            case R.id.tvOk:
                submintData();
                break;
        }
    }

    private void submintData() {
        OkHttpUtils.post()
                .url(Api.ADD_CARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("type", "4")//1=现金券 2=团购套餐 3=体验套餐 4=会员卡
                .addParams("oprice", etMoneyZL.getText().toString().trim())
                .addParams("collar", etLimitNum.getText().toString().trim())
                .addParams("title", etName.getText().toString().trim())
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
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                finish();
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取意见反馈失败");
                        }
                    }
                });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ADD_PACKET_VIP)
                .addParams("uid", SharedPreferencesUtils.getUid(AddPacketVipActivity.this))
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
                        AddPacketVipResponse resp = JsonUtils.parseByGson(response, AddPacketVipResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AddPacketVipResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取添加卡卷会员卡券失败");
                        }
                    }
                });
    }
}
