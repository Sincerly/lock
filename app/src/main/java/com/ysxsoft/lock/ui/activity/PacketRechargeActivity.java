package com.ysxsoft.lock.ui.activity;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.pay.alipay.AlipayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.models.response.CardBanlanceResponse;
import com.ysxsoft.lock.models.response.PacketRechargeListResponse;
import com.ysxsoft.lock.ui.dialog.SelectPayMethodDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.PacketRechargeResponse;
import com.ysxsoft.lock.net.Api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

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

    private int Click = -1;

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
        requestData();
        recyclerViewBanlance();
    }

    private void recyclerViewBanlance() {
        OkHttpUtils.get()
                .url(Api.CARD_BANLANCE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CardBanlanceResponse resp = JsonUtils.parseByGson(response, CardBanlanceResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                tv2.setText(resp.getData());
                            }
                        }
                    }
                });
    }
    RBaseAdapter<PacketRechargeListResponse.DataBean> adapter;
    private void requestData() {
        OkHttpUtils.get()
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
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                ArrayList<PacketRechargeListResponse.DataBean> datas = resp.getData();
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                                adapter = new RBaseAdapter<PacketRechargeListResponse.DataBean>(mContext, R.layout.item_packet_recharge, datas) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, PacketRechargeListResponse.DataBean item, int position) {
                                        holder.setText(R.id.tv, item.getNum() + "点券" + item.getPrice() + "元");
                                        ImageView iv = holder.getView(R.id.iv);
                                        if (Click == position) {
                                            iv.setBackgroundResource(R.mipmap.icon_card_select);
                                        } else {
                                            iv.setBackgroundResource(R.mipmap.icon_card_normal);
                                        }
                                    }

                                    @Override
                                    protected int getViewType(PacketRechargeListResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        PacketRechargeListResponse.DataBean itemData = adapter.getItemData(position);
                                        tvMoney.setText("¥" + itemData.getPrice());
                                        tvGrayMoney.setText("¥" + itemData.getYprice());
                                        Click = position;
                                        adapter.notifyDataSetChanged();
                                        clickId=itemData.getId();
                                        clickPrice=itemData.getPrice();
                                    }
                                });
                                recyclerView.setAdapter(adapter);

                            }
                        }
                    }
                });
    }
    private String clickId="";
    private String clickPrice="";

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("点券充值");
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clickPrice="";
                clickId="";
                if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
//                    int i = Integer.parseInt(etInput.getText().toString().trim()) / 100;
//                    DecimalFormat format = new DecimalFormat("0.00");
                    BigDecimal content = new BigDecimal(etInput.getText().toString().trim());
                    BigDecimal a1 = new BigDecimal("100");
                    BigDecimal divide = content.divide(a1);
                    clickPrice=divide+"";
//                    String format1 = format.format(divide);
                    tvMoney.setText("￥"+String.valueOf(divide));
                } else {
                    tvMoney.setText("￥"+"0");
                }
                if(adapter!=null){
                    Click=-1;
                    adapter.notifyDataSetChanged();
                }
                tvGrayMoney.setText("￥0");
            }
        });
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
                if (Click == -1 && TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    showToast("请选择充值点数或输入点券数");
                    return;
                }

                if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    if (Integer.valueOf(etInput.getText().toString().trim()) < 100) {
                        showToast("输入点券数不能小于100");
                        return;
                    }
                }

                SelectPayMethodDialog.show(mContext, tvMoney.getText().toString(), new SelectPayMethodDialog.OnDialogClickListener() {
                    @Override
                    public void sure(int type) {// type 1 微信  2 支付宝
                        switch (type) {
                            case 1:
                                showToast("微信");
                                break;
                            case 2:
//                                showToast("支付宝");
                                request();
                                break;
                        }
                    }
                });
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        Log.e("tag","clickId:"+clickId);
        clickId="8";
        OkHttpUtils.post()
                .url(Api.RECHARGE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("body","邻里邻外点券充值")//对一笔交易的具体描述信息
                .addParams("subject","点券充值")//商品标题
                .addParams("amount",clickPrice)//订单总金额
                .addParams("passback_params",clickId)//充值中心id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag","response:"+response);
                        hideLoadingDialog();
                        //TODO:支付宝支付
//                        AlipayResponse alipayResponse = JsonUtils.parseByGson(response, AlipayResponse.class);
//                        if (HttpResponse.SUCCESS.equals(alipayResponse.getCode())) {
//                            String data = alipayResponse.getData();
                            AlipayUtils.startAlipay(PacketRechargeActivity.this, handler, 0x10, response);//支付宝支付
//                        } else {
//                            showToast(alipayResponse.getMsg());
//                        }
                    }
                });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x10:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    //9000支付成功  8000 正在处理中  4000 订单支付失败  5000重复请求  6001中途取消  6002网络连接出错 6004 支付结果未知  其他其他支付错误
                    if ("9000".equals(map.get("resultStatus"))) {//订单支付成功
                        showToast("支付成功！");
                        recyclerViewBanlance();
                    } else if ("4000".equals(map.get("resultStatus"))) {//订单支付失败
                        Toast.makeText(PacketRechargeActivity.this, "支付宝支付失败！", Toast.LENGTH_SHORT).show();
                    } else if ("6001".equals(map.get("resultStatus"))) {//订单支付中途取消
                        Toast.makeText(PacketRechargeActivity.this, "支付宝支付取消！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
