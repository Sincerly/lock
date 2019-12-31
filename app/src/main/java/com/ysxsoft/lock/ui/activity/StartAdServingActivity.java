package com.ysxsoft.lock.ui.activity;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.ysxsoft.common_base.view.dialog.BaseInputCenterDialog;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.models.response.CardBanlanceResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CheckPutInDialog;
import com.ysxsoft.lock.ui.dialog.InputNumDialog;
import com.ysxsoft.lock.ui.dialog.PacketNotEnoughDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    @BindView(R.id.tvOk)
    TextView tvOk;

    private RBaseAdapter<String> adapter;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getStartAdServingActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_ad_serving;
    }

    @Override
    public void doWork() {
        super.doWork();
        //ARouter.getInstance().inject(this);
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
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(String.valueOf(i));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new RBaseAdapter<String>(mContext, R.layout.item_start_ad_serving_activity_list, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
                ImageView ivClose = holder.getView(R.id.ivClose);
                ImageView ivMius = holder.getView(R.id.ivMius);
                ImageView ivadd = holder.getView(R.id.ivadd);
//                Glide.with(mContext).load("").into(riv);
//                holder.setText(R.id.tvName,"");
//                holder.setText(R.id.tvAddress,"");
                TextView tvNum = holder.getView(R.id.tvNum);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strings.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                ivMius.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(tvNum.getText().toString().trim());
                        if (num <= 0) {
                            showToast("数量不能小于0");
                        } else {
                            num = num - 1;
                        }
                        tvNum.setText(String.valueOf(num));
                        adapter.notifyDataSetChanged();
                    }
                });
                ivadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(tvNum.getText().toString().trim());
                        num++;
                        tvNum.setText(String.valueOf(num));
                        adapter.notifyDataSetChanged();
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
                            public void sure(String nickname) {
                                //点击了确定
                                tvNum.setText(nickname);
                            }
                        });
                        dialog.showDialog();
                    }
                });

            }

            @Override
            protected int getViewType(String item, int position) {
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
                AddPlaceActivity.start();
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
                CheckPutInDialog.show(mContext, 2000, new CheckPutInDialog.OnDialogClickListener() {
                    @Override
                    public void sure() {
                        submintData();
                        PacketServingActivity.start();
                    }
                });
                break;
        }
    }

    private void submintData() {
        String substring = tvMoney.getText().toString().trim().substring(0, tvMoney.getText().toString().trim().length() - 1);
        showLoadingDialog("请求中...");
        OkHttpUtils.post()
                .url(Api.PUBLISH_CARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("card_id", "")
                .addParams("requ_id", "")
                .addParams("total_num", substring)
                .addParams("start_time", tvStartTime.getText().toString().trim())
                .addParams("end_time", tvEndTime.getText().toString().trim())
                .addParams("card_time", tvTime.getText().toString().trim())
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
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {

                            }
                        }

                    }
                });
    }
}
