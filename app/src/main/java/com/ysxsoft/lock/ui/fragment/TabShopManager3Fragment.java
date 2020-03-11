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
import com.ysxsoft.common_base.view.dialog.CenterTipsDialog;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.ActionResponse;
import com.ysxsoft.lock.models.response.CardListResponse;
import com.ysxsoft.lock.models.response.IsAuthResponse;
import com.ysxsoft.lock.ui.activity.AddPacketExperienceActivity;
import com.ysxsoft.lock.ui.activity.AddPacketGroupActivity;
import com.ysxsoft.lock.ui.activity.AddPacketMoneyActivity;
import com.ysxsoft.lock.ui.activity.ShopInfoActivity;
import com.ysxsoft.lock.ui.activity.StartAdServingActivity;
import com.ysxsoft.lock.ui.activity.ThrowInListActivity;
import com.ysxsoft.lock.ui.activity.TouListActivity;
import com.ysxsoft.lock.ui.dialog.ApplyShopDialog;
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
public class TabShopManager3Fragment extends BaseFragment {
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
        return R.layout.fragment_tabshopmanager3;
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
        //showLoadingDialog("请求中...");
        OkHttpUtils.get()
                .url(Api.CARD_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("business_id", business_id)
                .addParams("type", "3")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CardListResponse resp = JsonUtils.parseByGson(response, CardListResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //hideLoadingDialog();
                                groups = resp.getData();

                                RBaseAdapter<CardListResponse.DataBean> adapter = new RBaseAdapter<CardListResponse.DataBean>(getActivity(), R.layout.item_tabshopmanager3_fragment_list, groups) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, CardListResponse.DataBean item, int position) {
                                        RoundImageView riv = holder.getView(R.id.riv);
                                        Glide.with(getActivity()).load(AppConfig.BASE_URL + item.getImg()).into(riv);
                                        holder.setText(R.id.tvShopName, item.getTitle());
                                        holder.setText(R.id.tvMoney, item.getPrice());
                                        TextView tvGrayMoney = holder.getView(R.id.tvGrayMoney);
                                        tvGrayMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                                        tvGrayMoney.setText(item.getOprice());
                                        holder.setText(R.id.tvNum, item.getCostnum());//核销次数
                                        holder.setText(R.id.tvSum, item.getTotalnum());//总投放
                                        holder.setText(R.id.tvTime, item.getCreate_time2());
                                        TextView submit = holder.getView(R.id.submit);
                                        TextView delete = holder.getView(R.id.delete);
                                        TextView edit = holder.getView(R.id.edit);
                                        TextView list = holder.getView(R.id.list);
                                        submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //开始投放
                                                StartAdServingActivity.start(item.getId());
                                            }
                                        });
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //删除卡券信息
                                                CenterTipsDialog tipsDialog = new CenterTipsDialog(getActivity(), R.style.CenterDialogStyle);
                                                tipsDialog.initContent("是否删除优惠券?");
                                                tipsDialog.setListener(new CenterTipsDialog.OnDialogClickListener() {
                                                    @Override
                                                    public void sure() {
                                                        delete(item.getId());
                                                    }
                                                });
                                                tipsDialog.showDialog();
                                            }
                                        });
                                        edit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //编辑卡券信息
                                                int totalNum=Integer.parseInt(item.getTotalnum());
                                                boolean canEdit=false;
                                                if(totalNum==0){
                                                    canEdit=true;
                                                }
                                                AddPacketMoneyActivity.start(2,canEdit,item.getId(),item.getPrice(),item.getOprice(), AppConfig .BASE_URL+item.getImg(),item.getTitle(),item.getPrice());
                                            }
                                        });
                                        list.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //跳转投放列表
                                                TouListActivity.start(item.getId());
                                            }
                                        });
                                    }

                                    @Override
                                    protected int getViewType(CardListResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        TouListActivity.start(adapter.getItemData(position).getId());
                                    }
                                });
                                recyclerView.setAdapter(adapter);

                            }
                        }
                    }
                });
    }

    private void initList(View view) {

        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
//            @Override
//            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
//
//                SwipeMenuItem setNormalItem = new SwipeMenuItem(getActivity());
//                setNormalItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
//                setNormalItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//                setNormalItem.setBackgroundColor(Color.parseColor("#F9596C"));
//                setNormalItem.setTextSize(12);
//                setNormalItem.setText("删除\n" + "套餐");
//                setNormalItem.setTextColor(Color.parseColor("#FFFFFF"));
//                rightMenu.addMenuItem(setNormalItem);
//            }
//        });
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
                ThrowInListActivity.start(2, business_id);
                break;
            case R.id.FL2:
//                AddPacketExperienceActivity.start();
//                AddPacketMoneyActivity.start(2);
                getStatus();
                break;
        }
    }

    private void getStatus(){
        OkHttpUtils.post()
                .url(Api.IS_BUSINESS)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        IsAuthResponse resp = JsonUtils.parseByGson(response, IsAuthResponse.class);
                        if (resp != null) {
                            switch (resp.getCode()) {
                                case "200"://已完善
                                    AddPacketMoneyActivity.start(2);
                                    break;
                                case "201"://未完善
                                    ApplyShopDialog.show(getActivity(), new ApplyShopDialog.OnDialogClickListener() {
                                        @Override
                                        public void sure() {
                                            ShopInfoActivity.start();
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
    }

    private void delete(String cardId) {
        OkHttpUtils.post()
                .url(Api.DELETE_CARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("id", cardId)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ActionResponse resp = JsonUtils.parseByGson(response, ActionResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                requestData();
                            } else {
                                showToast(resp.getMsg());
                            }
                        }
                    }
                });
    }
}
