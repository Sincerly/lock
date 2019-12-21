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
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPacketGroupResponse;
import com.ysxsoft.lock.net.Api;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 添加卡卷团购券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AddPacketGroupActivity")
public class AddPacketGroupActivity extends BaseActivity {
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
    @BindView(R.id.etYh)
    EditText etYh;
    @BindView(R.id.etYj)
    EditText etYj;
    @BindView(R.id.etMoneyZL)
    EditText etMoneyZL;
    @BindView(R.id.etLimitNum)
    EditText etLimitNum;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etInputRules)
    EditText etInputRules;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.tvOk)
    TextView tvOk;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getAddPacketGroupActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_packet_group;
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

    @OnClick({R.id.backLayout, R.id.tvPacketType, R.id.tvStartTime, R.id.tvEndTime, R.id.LL1, R.id.ivAdd, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                showToast("图片选择");
                break;
            case R.id.ivAdd:
                showToast("图片选择");
                break;
            case R.id.tvPacketType:
                showToast("团购套餐");
                break;
            case R.id.tvStartTime:
                //时间选择器
                DateYMDPicker dateYMDPicker = new DateYMDPicker();
                dateYMDPicker.init(mContext);
                dateYMDPicker.show(new DateYMDPicker.OnSelectedListener() {
                    @Override
                    public void onSelected(Date date) {
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat3.format(date);
                        tvStartTime.setText(format);
                    }
                });

                break;
            case R.id.tvEndTime:
                DateYMDPicker picker = new DateYMDPicker();
                picker.init(mContext);
                picker.show(new DateYMDPicker.OnSelectedListener() {
                    @Override
                    public void onSelected(Date date) {
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat3.format(date);
                        tvEndTime.setText(format);
                    }
                });
                break;
            case R.id.tvOk:
                showToast("确认添加");
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ADD_PACKET_GROUP)
                .addParams("uid", SharedPreferencesUtils.getUid(AddPacketGroupActivity.this))
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
                        AddPacketGroupResponse resp = JsonUtils.parseByGson(response, AddPacketGroupResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AddPacketGroupResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取添加卡卷团购券失败");
                        }
                    }
                });
    }
}
