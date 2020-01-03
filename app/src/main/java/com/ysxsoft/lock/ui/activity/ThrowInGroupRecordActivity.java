package com.ysxsoft.lock.ui.activity;

import android.view.View;
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
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ThrowInListResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * Create By 胡
 * on 2019/12/30 0030
 */
@Route(path = "/main/ThrowInGroupRecordActivity")
public class ThrowInGroupRecordActivity extends BaseActivity implements IListAdapter<ThrowInListResponse.DataBean> {
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
    private ListManager<ThrowInListResponse.DataBean> manager;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getThrowInGroupRecordActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_throw_in_record;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initList();
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
        title.setText("投放记录");
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_throw_in_money_record;
    }

    @Override
    public void request(int page) {
        if (false) {
            debug(manager);
        } else {
            showLoadingDialog("请求中");
            OkHttpUtils.post()
                    .url(Api.TOU_HISTORY)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                    .addParams("pageSize", "10")
                    .addParams("type", "2")
                    .addParams("pageNum", String.valueOf(page))
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            hideLoadingDialog();
                            manager.releaseRefresh();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            hideLoadingDialog();
                            manager.releaseRefresh();
                            ThrowInListResponse resp = JsonUtils.parseByGson(response, ThrowInListResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                    //请求成功
                                    List<ThrowInListResponse.DataBean> data = resp.getData();
                                    manager.setData(data);
                                } else {
                                    //请求失败
                                    showToast(resp.getMsg());
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, ThrowInListResponse.DataBean o) {
        helper.setText(R.id.tvName, o.getTitle());
//        helper.setText(R.id.tvPhone, "手机号码：");
//        helper.setText(R.id.tvNikeName, "会员昵称：");
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.setText(R.id.tvTime, "投放日期：");
        } else {
            helper.setText(R.id.tvTime, "核销日期：");
        }
//        helper.setText(R.id.tvCheck, "已核销");
//        helper.setText(R.id.tvMoney, "¥");
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, ThrowInListResponse.DataBean o) {

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

    @OnClick(R.id.backLayout)
    public void onViewClicked() {
        backToActivity();
    }
}
