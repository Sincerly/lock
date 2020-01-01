package com.ysxsoft.lock.ui.fragment;

import android.os.Bundle;
import android.view.View;
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
import com.ysxsoft.lock.models.response.CardListResponse;
import com.ysxsoft.lock.models.response.ThrowInListResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.ThrowInMoneyRecordActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * Create By 胡
 * on 2019/12/30 0030
 */
public class Tab1ThrowInListFragment extends BaseFragment implements IListAdapter<CardListResponse.DataBean> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;
    private String business_id;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_throw_in_list_layout;
    }

    @Override
    protected void doWork(View view) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            business_id = bundle.getString("business_id");
        }

        initList(view);
    }

    private void initList(View view) {
        manager = new ListManager(this);
        manager.init(view);
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ThrowInMoneyRecordActivity.start();
            }
        });
        request(1);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_tab1_throw_in_list_layout;
    }

    @Override
    public void request(int page) {
        if (false) {
            debug(manager);
        } else {
            OkHttpUtils.get()
//                    .url(Api.TOU_HISTORY)
                    .url(Api.CARD_LIST)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                    .addParams("business_id", business_id)
                    .addParams("type", "1")
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
//                            ThrowInListResponse resp = JsonUtils.parseByGson(response, ThrowInListResponse.class);
                            CardListResponse resp = JsonUtils.parseByGson(response, CardListResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                    List<CardListResponse.DataBean> data = resp.getData();
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
    public void fillView(BaseViewHolder helper, CardListResponse.DataBean o) {
        TextView tv1 = helper.getView(R.id.tv1);
        TextView tvMoney = helper.getView(R.id.tvMoney);
        TextView tvMj = helper.getView(R.id.tvMj);
        TextView tvName = helper.getView(R.id.tvName);
        TextView tvCurrentNum = helper.getView(R.id.tvCurrentNum);
        TextView tvSumNum = helper.getView(R.id.tvSumNum);
        TextView tvStatus = helper.getView(R.id.tvStatus);

        tvName.setText(o.getTitle());
        tvCurrentNum.setText(o.getYsnum());
        tvSumNum.setText(o.getTotalnum());


        if (o.getStatus() .equals("1")) {
            tv1.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tvMoney.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tvName.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvCurrentNum.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvSumNum.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_3BB0D2));
            tvStatus.setText("未结束");
        } else {
            tv1.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvMoney.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvName.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvCurrentNum.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvSumNum.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tvStatus.setText("已结束");
        }
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, CardListResponse.DataBean o) {

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
