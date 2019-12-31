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
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.models.response.PacketRechargeListResponse;
import com.ysxsoft.lock.ui.dialog.SelectPayMethodDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.PacketRechargeResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.tvGrayMoney)
    TextView tvGrayMoney;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int Click=-1;

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
        tvGrayMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        initTitle();
        initList();
        requestData();
    }

    private void requestData() {
        OkHttpUtils.post()
                .url(Api.CARD_RECHARGE_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PacketRechargeListResponse resp = JsonUtils.parseByGson(response, PacketRechargeListResponse.class);
                        if (resp!=null){
//                            if (HttpResponse.SUCCESS.equals(resp.getCode())){
//
//                            }
                        }
                    }
                });
    }

    private void initList() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            strings.add(String.valueOf(i));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_packet_recharge, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
//                holder.setText(R.id.tv,"");
                ImageView iv = holder.getView(R.id.iv);
                if (Click==position){
                    iv.setBackgroundResource(R.mipmap.icon_card_select);
                }else {
                    iv.setBackgroundResource(R.mipmap.icon_card_normal);
                }
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, View view, int position) {
                Click=position;
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("点券充值");
    }

    @OnClick({R.id.backLayout, R.id.tv3, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv3:
                PacketServingActivity.start();
                break;
            case R.id.tvOk:
                if (Click==-1){
                    showToast("请选择充值点数");
                    return;
                }
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
