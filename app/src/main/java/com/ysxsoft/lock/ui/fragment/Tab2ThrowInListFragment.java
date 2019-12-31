package com.ysxsoft.lock.ui.fragment;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.ui.activity.ThrowInGroupRecordActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
public class Tab2ThrowInListFragment extends BaseFragment implements IListAdapter {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_throw_in_list_layout;
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
                ThrowInGroupRecordActivity.start();
            }
        });
        request(1);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_tab2_throw_in_list_layout;
    }

    @Override
    public void request(int page) {
        if (IS_DEBUG_ENABLED) {
            debug(manager);
        } else {
            OkHttpUtils.post()
//                    .url(Api.)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, Object o) {
        TextView tv1 = helper.getView(R.id.tv1);
        TextView tvMoney = helper.getView(R.id.tvMoney);
        TextView tvGrayMoney = helper.getView(R.id.tvGrayMoney);
        TextView tvName = helper.getView(R.id.tvName);
        TextView tvCurrentNum = helper.getView(R.id.tvCurrentNum);
        TextView tvSumNum = helper.getView(R.id.tvSumNum);
        TextView tvStatus = helper.getView(R.id.tvStatus);
        tvGrayMoney.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        if (helper.getAdapterPosition()%2==0){
            tv1.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tvMoney.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tvName.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvCurrentNum.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvSumNum.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_3BB0D2));
            tvStatus.setText("未结束");
        }else {
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
    public void fillMuteView(BaseViewHolder helper, Object o) {

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
