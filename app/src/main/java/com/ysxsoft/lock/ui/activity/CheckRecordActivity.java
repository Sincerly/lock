package com.ysxsoft.lock.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ui.dialog.AllDialog;
import com.ysxsoft.lock.ui.dialog.TodayDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.CheckRecordResponse;
import com.ysxsoft.lock.net.Api;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * 核销记录
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/CheckRecordActivity")
public class CheckRecordActivity extends BaseActivity implements IListAdapter<CheckRecordResponse.DataBean> {
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

    @BindView(R.id.FL1)
    FrameLayout FL1;
    @BindView(R.id.FL2)
    FrameLayout FL2;
    @BindView(R.id.tvToday)
    TextView tvToday;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.LL1)
    LinearLayout LL1;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ListManager<CheckRecordResponse.DataBean> manager;
    private TodayDialog todayDialog;
    private AllDialog allDialog;
    private MyBroadCast myBroadCast;

    private String todayType="true";

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getCheckRecordActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_record;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initList();
        myBroadCast = new MyBroadCast();
        IntentFilter filter = new IntentFilter("UPDATE_TEXT");
        registerReceiver(myBroadCast, filter);
    }

    public class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("UPDATE_TEXT".equals(intent.getAction())) {
                String flag = intent.getStringExtra("flag");
                Drawable down = getResources().getDrawable(R.mipmap.icon_black_down_arrow);
                down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());// 设置边界
                Drawable up = getResources().getDrawable(R.mipmap.icon_theme_up_arrow);
                up.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());// 设置边界
                switch (flag) {
                    case "1":
                        tvToday.setCompoundDrawables(null, null, down, null);
                        tvToday.setTextColor(getResources().getColor(R.color.color_282828));
                        break;
                    case "2":
                        tvAll.setCompoundDrawables(null, null, down, null);
                        tvAll.setTextColor(getResources().getColor(R.color.color_282828));
                        break;
                }
            }
        }
    }

    private void initList() {
        manager = new ListManager(this);
        manager.init(getWindow().findViewById(android.R.id.content));
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //setResult(RESULT_OK, intent);
                //finish();
            }
        });
        manager.getAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(manager.nextPage());
            }
        }, recyclerView);
        request(1);
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("核销记录");
    }

    private int intToday;

    @OnClick({R.id.backLayout, R.id.FL1, R.id.FL2})
    public void onViewClicked(View view) {

        Drawable down = getResources().getDrawable(R.mipmap.icon_black_down_arrow);
        down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());// 设置边界
        Drawable up = getResources().getDrawable(R.mipmap.icon_theme_up_arrow);
        up.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());// 设置边界

        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.FL1:
                if (allDialog != null) {
                    if (allDialog.isShowing())
                        allDialog.dismiss();
                }
                todayDialog = new TodayDialog();
                todayDialog.init(CheckRecordActivity.this);
                todayDialog.setOnPopupWindowListener(new TodayDialog.OnPopupWindowListener() {

                    @Override
                    public void select(String type, int today) {
                        intToday = today;
                        todayType = type;
                        request(1);
                    }
                });
                todayDialog.showPopDown(LL1, 0, 0);
                if (todayDialog.isShowing()) {
                    tvToday.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                    tvToday.setCompoundDrawables(null, null, up, null);
                } else {
                    tvToday.setTextColor(getResources().getColor(R.color.color_282828));
                    tvToday.setCompoundDrawables(null, null, down, null);
                }
                tvAll.setCompoundDrawables(null, null, down, null);
                tvAll.setTextColor(getResources().getColor(R.color.color_282828));
                break;
            case R.id.FL2:
                if (todayDialog != null) {
                    if (todayDialog.isShowing())
                        todayDialog.dismiss();
                }
                allDialog = new AllDialog();
                allDialog.init(CheckRecordActivity.this);
                allDialog.setOnPopupWindowListener(new AllDialog.OnPopupWindowListener() {
                    @Override
                    public void select(String type,int allDay) {
                        intToday = allDay;
                        todayType = type;
                        request(1);
                    }
                });
                allDialog.showPopDown(LL1, 0, 0);
                if (allDialog.isShowing()) {
                    tvAll.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                    tvAll.setCompoundDrawables(null, null, up, null);
                } else {
                    tvAll.setTextColor(getResources().getColor(R.color.color_282828));
                    tvAll.setCompoundDrawables(null, null, down, null);
                }
                tvToday.setCompoundDrawables(null, null, down, null);
                tvToday.setTextColor(getResources().getColor(R.color.color_282828));
                break;
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_check_record;
    }

    @Override
    public void request(int page) {
        if (false) {
            debug(manager);
        } else {
            showLoadingDialog("请求中");
            GetBuilder getBuilder = OkHttpUtils.get().url(Api.HX_HISTORY);
            getBuilder.addHeader("Authorization", SharedPreferencesUtils.getToken(mContext));
            getBuilder.addParams("today", todayType);
            getBuilder.addParams("type", String.valueOf(intToday));
            getBuilder.addParams("pageNum", String.valueOf(page))
                    .addParams("pageSize", "10")
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
                            CheckRecordResponse resp = JsonUtils.parseByGson(response, CheckRecordResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                    //请求成功
                                    List<CheckRecordResponse.DataBean> data = resp.getData();
                                    manager.setData(data);
                                } else {
                                    //请求失败
                                    showToast(resp.getMsg());
                                }
                            } else {
                                showToast("获取核销记录失败");
                            }
                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, CheckRecordResponse.DataBean o) {
        helper.setText(R.id.tv1, o.getRemark());
        helper.setText(R.id.tv2, "券号：" + o.getId());
        helper.setText(R.id.tvPhone, "手机号码：" );
        helper.setText(R.id.tvNikeName, "会员昵称：" );
        helper.setText(R.id.tv3, "核销日期：" + o.getCost_time());
        helper.setText(R.id.tvMoney, "¥" + o.getPrice());
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, CheckRecordResponse.DataBean o) {

    }

    @Override
    public void attachActivity(AppCompatActivity activity) {

    }

    @Override
    public void dettachActivity() {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public boolean isMuteAdapter() {
        return false;
    }

    @Override
    public int[] getMuteTypes() {
        return new int[0];
    }

    @Override
    public int[] getMuteLayouts() {
        return new int[0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCast);
    }
}
