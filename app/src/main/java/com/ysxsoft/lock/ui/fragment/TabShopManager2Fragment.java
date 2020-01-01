package com.ysxsoft.lock.ui.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.CardListResponse;
import com.ysxsoft.lock.ui.activity.AddPacketGroupActivity;
import com.ysxsoft.lock.ui.activity.AddPacketMoneyActivity;
import com.ysxsoft.lock.ui.activity.ThrowInListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabShopManager2Fragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;

    @BindView(R.id.FL1)
    FrameLayout FL1;
    @BindView(R.id.FL2)
    FrameLayout FL2;

    private String business_id;
    private ArrayList<CardListResponse.DataBean> groups;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabshopmanager2;
    }

    @Override
    protected void doWork(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            business_id = bundle.getString("business_id");
        }
        initList(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        OkHttpUtils.get()
                .url(Api.CARD_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("business_id", business_id)
                .addParams("type", "2")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CardListResponse resp = JsonUtils.parseByGson(response, CardListResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                groups = resp.getData();
                                RBaseAdapter<CardListResponse.DataBean> adapter = new RBaseAdapter<CardListResponse.DataBean>(getActivity(), R.layout.item_tabshopmanager2_fragment_list, groups) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, CardListResponse.DataBean item, int position) {
                                        RoundImageView riv = holder.getView(R.id.riv);
                                        Glide.with(getActivity()).load(AppConfig.BASE_URL + item.getImg()).into(riv);
                                        holder.setText(R.id.tvShopName, item.getTitle());
                                        holder.setText(R.id.tvMoney, item.getPrice());
                                        TextView tvGrayMoney = holder.getView(R.id.tvGrayMoney);
                                        tvGrayMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                                        tvGrayMoney.setText(item.getOprice());
                                        holder.setText(R.id.tvNum, item.getYsnum());
                                        holder.setText(R.id.tvSum, item.getTotalnum());
//                holder.setText(R.id.tvTime,"");
                                        TextView tvStatus = holder.getView(R.id.tvStatus);
                                        switch (holder.getAdapterPosition() % 4) {
                                            case 0:
                                                tvStatus.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                                                tvStatus.setText("待投中");
                                                break;
                                            case 1:
                                                tvStatus.setTextColor(getResources().getColor(R.color.color_999999));
                                                tvStatus.setText("投放结束");
                                                break;
                                            case 2:
                                                tvStatus.setTextColor(getResources().getColor(R.color.color_999999));
                                                tvStatus.setText("投放中");
                                                break;
                                            case 3:
                                                tvStatus.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                                                tvStatus.setText("继续投放");
                                                break;
                                        }
                                    }

                                    @Override
                                    protected int getViewType(CardListResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView.setAdapter(adapter);

                            }
                        }
                    }
                });
    }

    private void initList(View view) {

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

                SwipeMenuItem setNormalItem = new SwipeMenuItem(getActivity());
                setNormalItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                setNormalItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setNormalItem.setBackgroundColor(Color.parseColor("#F9596C"));
                setNormalItem.setTextSize(12);
                setNormalItem.setText("删除\n" + "套餐");
                setNormalItem.setTextColor(Color.parseColor("#FFFFFF"));
                rightMenu.addMenuItem(setNormalItem);
            }
        });
        recyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection();
                int menuPosition = menuBridge.getPosition();
                switch (menuPosition) {
                    case 0://删除套餐
                        showToast("删除套餐");
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @OnClick({R.id.FL1, R.id.FL2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.FL1:
                ThrowInListActivity.start(1,business_id);
                break;
            case R.id.FL2:
                AddPacketGroupActivity.start();
                break;
        }
    }
}
