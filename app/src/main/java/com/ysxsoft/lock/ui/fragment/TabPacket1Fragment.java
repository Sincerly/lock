package com.ysxsoft.lock.ui.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
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
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.ui.activity.KeyManagerActivity;
import com.ysxsoft.lock.ui.activity.UseCouponActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabPacket1Fragment extends BaseFragment implements IListAdapter {
    @BindView(R.id.tabRecyclerView)
    RecyclerView tabRecyclerView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;

    public boolean isClick = false;
    private int isSelect = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpacket1;
    }

    @Override
    protected void doWork(View view) {
        initList(view);
    }

    private void initList(View view) {

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        tabRecyclerView.setLayoutManager(layoutManager);
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("待使用");
        list.add("已使用");
        list.add("已过期");
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(getActivity(), R.layout.item_tab_tabpacket1, list) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                TextView tv1 = holder.getView(R.id.tv1);
                tv1.setText(item);
                if (isSelect == position) {
                    tv1.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv1.setBackgroundResource(R.drawable.shape_btn_bg);
                }else {
                    tv1.setTextColor(getResources().getColor(R.color.color_999999));
                    tv1.setBackgroundResource(R.drawable.bg_shape_bolder_cccccc_radius_20);
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
                isSelect = position;
                adapter.notifyDataSetChanged();
            }
        });
        tabRecyclerView.setAdapter(adapter);

        manager = new ListManager(this);
        manager.init(view);
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //HomeArticleResponse.DataBean item = (HomeArticleResponse.DataBean) adapter.getItem(position);

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
//        helper.setText(R.id.tv2, "");
//        helper.setText(R.id.tvmj, "");
//        helper.setText(R.id.tv3, "");
//        helper.setText(R.id.tv4, "");
//        helper.setText(R.id.tv5, "");
//        helper.setText(R.id.tv6, "");
        TextView tv1 = helper.getView(R.id.tv1);
        TextView tv2 = helper.getView(R.id.tv2);
        TextView tv3 = helper.getView(R.id.tv3);
        TextView tv5 = helper.getView(R.id.tv5);
        TextView tv6 = helper.getView(R.id.tv6);
        ImageView iv1 = helper.getView(R.id.iv1);
        CardView cv1 = helper.getView(R.id.cv1);
//        helper.setText(R.id.tv7, "");
        if (helper.getAdapterPosition() % 2 == 0) {
            tv1.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tv2.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            tv3.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tv5.setTextColor(getActivity().getResources().getColor(R.color.color_282828));
            tv6.setVisibility(View.VISIBLE);
            iv1.setVisibility(View.GONE);

            tv6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UseCouponActivity.start();
                }
            });

        } else {

            tv1.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tv2.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tv3.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
            tv5.setTextColor(getActivity().getResources().getColor(R.color.color_999999));

            tv6.setVisibility(View.GONE);
            iv1.setVisibility(View.VISIBLE);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyManagerActivity.start();
                }
            });
        }
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
