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
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
public class TabShopDetailFragment1 extends BaseFragment implements IListAdapter {

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
                //HomeArticleResponse.DataBean item = (HomeArticleResponse.DataBean) adapter.getItem(position);
                //if ("1".equals(item.getStyle())) {
                //    //有视频的
                //   ARouter.getInstance().build(ARouterPath.getPlayActivity()).withString("nid", "" + item.getNid()).withBoolean("isFriendCircle", true).navigation();
                //} else {
                //   //无视频的
                //   ARouter.getInstance().build(ARouterPath.getArticleDetailActivity()).withString("nid", "" + item.getNid()).withString("tname", "帖子").withBoolean("isFriendCircle", true).navigation();
                //}
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
        if (IS_DEBUG_ENABLED) {
            debug(manager);
        } else {
            OkHttpUtils.post()
//                    .url(Api.GET_CODE)
                    .addParams("uid", SharedPreferencesUtils.getUid(getActivity()))
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
//                        HomeArticleResponse resp = JsonUtils.parseByGson(response, HomeArticleResponse.class);
//                        if (resp != null) {
//                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                //请求成功
//                                List<HomeArticleResponse.DataBean> data = resp.getData();
//                                manager.setData(data);
//                            } else {
//                                //请求失败
//                                showToast(resp.getMsg());
//                            }
//                        } else {
//                            showToast("获取失败");
//                        }
                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, Object o) {
        TextView tv5 = helper.getView(R.id.tv5);
        TextView tv6 = helper.getView(R.id.tv6);
        ImageView iv1 = helper.getView(R.id.iv1);
        CardView cv1 = helper.getView(R.id.cv1);
//        helper.setText(R.id.tv7, "");
        tv6.setVisibility(View.VISIBLE);
        iv1.setVisibility(View.GONE);

        Drawable down = getResources().getDrawable(R.mipmap.icon_down_arrow);
        Drawable up = getResources().getDrawable(R.mipmap.icon_up_arrow);
        down.setBounds(0, 0, down.getIntrinsicWidth(), down.getIntrinsicHeight());
        up.setBounds(0, 0, up.getIntrinsicWidth(), up.getIntrinsicHeight());
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = !isClick;
                if (isClick) {
                    cv1.setVisibility(View.GONE);
                    tv5.setCompoundDrawables(null, null, down, null);
                } else {
                    cv1.setVisibility(View.VISIBLE);
                    tv5.setCompoundDrawables(null, null, up, null);
                }
            }
        });
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
