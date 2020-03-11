package com.ysxsoft.lock.ui.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.PacketCardResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.UseCouponActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * Create By 胡
 * on 2019/12/17 0017
 */
public class TabShopDetailFragment1 extends BaseFragment implements IListAdapter<PacketCardResponse.DataBean> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;
    public boolean isClick = false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_shop_detail_fragment;
    }

    @Override
    protected void doWork(View view) {
        initList(view);
    }

    private void initList(View view) {
        manager = new ListManager(this);
        manager.init(view);
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        request(1);

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_tabpacket1_fragment_list;
    }

    @Override
    public void request(int page) {
        if (false) {
            debug(manager);
        } else {
            OkHttpUtils.post()
                    .url(Api.MEM_BERCARD)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                    .addParams("type", "1")
                    .addParams("status", "1")
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            manager.releaseRefresh();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            manager.releaseRefresh();
                            PacketCardResponse resp = JsonUtils.parseByGson(response, PacketCardResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                    //请求成功
                                    List<PacketCardResponse.DataBean> data = resp.getData();
                                    manager.resetPage();
                                    manager.setData(data);
                                } else {
                                    //请求失败
                                    showToast(resp.getMsg());
                                }
                            } else {
                                showToast("获取失败");
                            }
                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, PacketCardResponse.DataBean o) {
        TextView tv5 = helper.getView(R.id.tv5);
        TextView tv6 = helper.getView(R.id.tv6);
        ImageView iv1 = helper.getView(R.id.iv1);
        CardView cv1 = helper.getView(R.id.cv1);
//        helper.setText(R.id.tv7, "");
        tv6.setVisibility(View.VISIBLE);
        iv1.setVisibility(View.GONE);



        helper.setText(R.id.tv2, o.getPrice());
        helper.setText(R.id.tvmj, "满" + o.getOprice() + "可用");
        helper.setText(R.id.tv3, o.getTitle());
        helper.setText(R.id.tv4, o.getStart_time_str() + "-" + o.getEnd_time_str());
        helper.setText(R.id.tv7, o.getRemark());


        Drawable down = getResources().getDrawable(R.mipmap.icon_down_arrow);
        Drawable up = getResources().getDrawable(R.mipmap.icon_up_arrow);
        down.setBounds(0, 0, down.getIntrinsicWidth(), down.getIntrinsicHeight());
        up.setBounds(0, 0, up.getIntrinsicWidth(), up.getIntrinsicHeight());
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o.isClick()) {
                    o.setClick(false);
                    cv1.setVisibility(View.GONE);
                    tv5.setCompoundDrawables(null, null, down, null);
                } else {
                    o.setClick(true);
                    cv1.setVisibility(View.VISIBLE);
                    tv5.setCompoundDrawables(null, null, up, null);
                }
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UseCouponActivity.start(o.getId());
            }
        });
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, PacketCardResponse.DataBean o) {

    }

    @Override
    public void attachActivity(AppCompatActivity activity) {

    }

    @Override
    public void dettachActivity() {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
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
}
