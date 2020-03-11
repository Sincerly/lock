package com.ysxsoft.lock.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.utils.WebViewUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.ysxsoft.common_base.view.dialog.BaseInputCenterDialog;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.CardBanlanceResponse;
import com.ysxsoft.lock.models.response.CardDetailResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CheckPutInDialog;
import com.ysxsoft.lock.ui.dialog.InputNumDialog;
import com.ysxsoft.lock.ui.dialog.PacketNotEnoughDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.StartAdServingResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * 开始投放
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/StartAdServingActivity")
public class StartAdServingActivity extends BaseActivity {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvUseMoney)
    TextView tvUseMoney;
    @BindView(R.id.tvname)
    EditText tvname;
    @BindView(R.id.tvOk)
    TextView tvOk;

    @Autowired
    String cardId;
    private int snum = 0;

    private RBaseAdapter<ItemBean> adapter;

    public static void start(String cardId) {
        ARouter.getInstance().build(ARouterPath.getStartAdServingActivity()).withString("cardId", cardId).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_ad_serving;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        getPrice();
        initTitle();
        initList();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("开始投放");
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    public void getPrice() {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.CARD_INFO + "?id=" + cardId)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        Log.e("tag", "onError" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        Log.e("tag", "" + response);
                        CardDetailResponse resp = JsonUtils.parseByGson(response, CardDetailResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                CardDetailResponse.DataBean data = resp.getData();
                                snum = Integer.parseInt(data.getSnum());//投放1个数量消耗的卡券点数
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取关于我们失败");
                        }
                    }
                });
    }

    private void requestData() {
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
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) ;
                            {
                                tvUseMoney.setText("可用点数" + resp.getData());
                            }
                        }
                    }
                });
    }

    private void initList() {
//        ArrayList<String> strings = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            strings.add(String.valueOf(i));
//        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new RBaseAdapter<ItemBean>(mContext, R.layout.item_start_ad_serving_activity_list, datas) {
            @Override
            protected void fillItem(RViewHolder holder, ItemBean item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
                ImageView ivClose = holder.getView(R.id.ivClose);
                ImageView ivMius = holder.getView(R.id.ivMius);
                ImageView ivadd = holder.getView(R.id.ivadd);
//                Glide.with(mContext).load("").into(riv);
                holder.setText(R.id.tvName, item.getName());
                holder.setText(R.id.tvAddress, item.getAddress());
                TextView tvNum = holder.getView(R.id.tvNum);
                tvNum.setText(item.getNum() + "");
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datas.remove(position);
                        adapter.notifyDataSetChanged();
                        refreshPrice();
                    }
                });

                ivMius.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = datas.get(position).getNum();
                        if (num == 1) {
                            showToast("数量不能小于0");
                            return;
                        } else {
                            num = num - 1;
                        }
                        datas.get(position).setNum(num);
                        adapter.notifyDataSetChanged();
                        refreshPrice();
                    }
                });
                ivadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = datas.get(position).getNum();
                        num++;
                        datas.get(position).setNum(num);
                        adapter.notifyDataSetChanged();
                        refreshPrice();
                    }
                });

                tvNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputNumDialog dialog = new InputNumDialog(mContext, R.style.CenterDialogStyle);
                        dialog.initTitle("数量");
                        dialog.initTips("请输入数量");
                        dialog.initContent(tvNum.getText().toString());
                        dialog.setListener(new InputNumDialog.OnDialogClickListener() {
                            @Override
                            public void sure(String num) {
                                //点击了确定
                                Pattern pattern = Pattern.compile("^\\d+$");
                                if (pattern.matcher(num).matches()) {
                                    int n = Integer.parseInt(num);
                                    if (n == 0) {
                                        showToast("数量不能小于0");
                                        return;
                                    }
                                    tvNum.setText(num);
                                    datas.get(position).setNum(Integer.parseInt(num));
                                    adapter.notifyDataSetChanged();
                                    refreshPrice();
                                } else {
                                    ToastUtils.shortToast(StartAdServingActivity.this, "请输入正确的数量！");
                                }
                            }
                        });
                        dialog.showDialog();
                    }
                });
            }

            @Override
            protected int getViewType(ItemBean item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);

    }


    public void request(int page) {
        OkHttpUtils.post()
                .url(Api.GET_START_AD_SERVING_LIST)
                .addParams("uid", SharedPreferencesUtils.getUid(StartAdServingActivity.this))
                .addParams("page", String.valueOf(page))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        StartAdServingResponse resp = JsonUtils.parseByGson(response, StartAdServingResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<StartAdServingResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取失败");
                        }
                    }
                });

    }

    @OnClick({R.id.backLayout, R.id.tvAdd, R.id.tvUseMoney, R.id.tvTime, R.id.tvStartTime, R.id.tvEndTime, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvUseMoney:
                PacketNotEnoughDialog.show(mContext, new PacketNotEnoughDialog.OnDialogClickListener() {
                    @Override
                    public void sure() {
                        PacketRechargeActivity.start();
                    }
                });
                break;
            case R.id.tvAdd:
                AddPlaceActivity.start(this, 2020, "1");
                break;
            case R.id.tvTime:
                //时间选择器
                DateYMDPicker date = new DateYMDPicker();
                date.init(mContext);
                date.show(new DateYMDPicker.OnSelectedListener() {
                    @Override
                    public void onSelected(Date date) {
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat3.format(date);
                        tvTime.setText(format);
                    }
                });
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
                CheckPutInDialog.show(mContext, getTotalPrice(), new CheckPutInDialog.OnDialogClickListener() {
                    @Override
                    public void sure() {
                        submit();
                    }
                });
                break;
        }
    }

    private void refreshPrice() {
        int totalNum = 0;
        for (int i = 0; i < datas.size(); i++) {
            totalNum += datas.get(i).getNum();
        }
        int totalPrice = snum * totalNum;
        tvMoney.setText(totalPrice + "点");
    }

    private int getTotalPrice() {
        int totalNum = 0;
        for (int i = 0; i < datas.size(); i++) {
            totalNum += datas.get(i).getNum();
        }
        int totalPrice = snum * totalNum;
        return totalPrice;
    }

    private int getTotalNum() {
        int totalNum = 0;
        for (int i = 0; i < datas.size(); i++) {
            totalNum += datas.get(i).getNum();
        }
        return totalNum;
    }

    private boolean isRequest=false;

    private void submit() {
        if(isRequest){
            return;
        }
        isRequest=true;
        if ("".equals(tvTime.getText().toString())) {
            showToast("请填写有效期结束时间！");
            isRequest=false;
            return;
        }
        if ("".equals(tvStartTime.getText().toString())) {
            showToast("请填写投放开始时间！");
            isRequest=false;
            return;
        }
        if ("".equals(tvEndTime.getText().toString())) {
            showToast("请填写投放结束时间！");
            isRequest=false;
            return;
        }
        showLoadingDialog("请求中...");
        for (int i = 0; i < datas.size(); i++) {
            OkHttpUtils.post()
                    .url(Api.PUBLISH_CARD)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                    .addParams("card_id", cardId)
                    .addParams("requ_id", datas.get(i).getRequid())
                    .addParams("put_name", tvname.getText().toString())
                    .addParams("total_num", getTotalNum() + "")
                    .addParams("start_time", tvStartTime.getText().toString().trim())
                    .addParams("end_time", tvEndTime.getText().toString().trim())
                    .addParams("card_time", tvTime.getText().toString().trim())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            hideLoadingDialog();
                            isRequest=false;
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            isRequest=false;
                            hideLoadingDialog();
                            CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                            if (resp != null) {
                                showToast(resp.getMsg());
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {

                                }
                            }

                        }
                    });
        }
        PacketServingActivity.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2020) {
            String requid = data.getStringExtra("requid");
            String name = data.getStringExtra("name");
            String address = data.getStringExtra("address");
            String url = data.getStringExtra("url");
            if (requid != null) {
                if (map.containsKey(requid)) {
                    ToastUtils.shortToast(this, "请勿重复选取小区");
                } else {
                    map.put(requid, requid);
                    ItemBean item = new ItemBean();
                    item.setName(name == null ? "" : name);
                    item.setNum(1);
                    item.setRequid(requid == null ? "" : requid);
                    item.setUrl(url == null ? "" : url);
                    item.setAddress(address == null ? "" : address);
                    datas.add(item);
                    adapter.notifyDataSetChanged();
                    refreshPrice();
                }
            }
        }
    }

    private List<ItemBean> datas = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    public class ItemBean {
        private int num;//选择的数量
        private String requid;//小区id
        private String name;//小区名字
        private String url;//小区图片
        private String address;//小区地址

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getRequid() {
            return requid;
        }

        public void setRequid(String requid) {
            this.requid = requid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
