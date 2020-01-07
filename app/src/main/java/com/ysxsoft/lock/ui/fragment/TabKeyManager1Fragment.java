package com.ysxsoft.lock.ui.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.TabKeyManager1FragmentResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.ApplyKeyActivity;
import com.ysxsoft.lock.ui.activity.IdcardCertActivity;
import com.ysxsoft.lock.ui.activity.UnlockingModeActivity;
import com.ysxsoft.lock.ui.dialog.CertificationDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create By 胡
 * on 2019/12/16 0016
 */
public class TabKeyManager1Fragment extends BaseFragment {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    private List<TabKeyManager1FragmentResponse.DataBean> groups;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabkeymanager1;
    }

    @Override
    protected void doWork(View view) {
    }

    @Override
    public void onResume() {
        super.onResume();
        request();
    }


    private void request() {
        showLoadingDialog("请求中...");
        OkHttpUtils.get()
                .url(Api.GET_BIND_PLACE_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("onError", e.getMessage());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        TabKeyManager1FragmentResponse gson = JsonUtils.parseByGson(response, TabKeyManager1FragmentResponse.class);
                        if (gson != null) {
                            if (HttpResponse.SUCCESS.equals(gson.getCode())) {
                                groups = gson.getData();
                                recyclerView.setAdapter(null);
                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
                                    @Override
                                    public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                                        SwipeMenuItem setNormalItem = new SwipeMenuItem(getActivity());
                                        setNormalItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                                        setNormalItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                                        setNormalItem.setBackgroundColor(Color.parseColor("#3BB0D2"));
                                        setNormalItem.setTextSize(12);
                                        setNormalItem.setText("设为\n" + "默认");
                                        setNormalItem.setTextColor(Color.parseColor("#FFFFFF"));
                                        rightMenu.addMenuItem(setNormalItem);

                                        SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                                        deleteItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                                        deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                                        deleteItem.setBackgroundColor(Color.parseColor("#F9596C"));
                                        deleteItem.setTextSize(12);
                                        deleteItem.setText("删除\n" + "小区");
                                        deleteItem.setTextColor(Color.parseColor("#FFFFFF"));
                                        rightMenu.addMenuItem(deleteItem);
                                    }
                                });
                                recyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
                                    @Override
                                    public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                                        menuBridge.closeMenu();
                                        int direction = menuBridge.getDirection();
                                        int menuPosition = menuBridge.getPosition();
                                        String requ_id = groups.get(position).getRequ_id();
                                        switch (menuPosition) {
                                            case 0://设为默认
                                                settingNormal(requ_id);
                                                break;
                                            case 1://删除小区
                                                DeleteData(requ_id);
                                                groups.remove(position);
                                                break;
                                        }
                                    }
                                });
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                RBaseAdapter<TabKeyManager1FragmentResponse.DataBean> adapter = new RBaseAdapter<TabKeyManager1FragmentResponse.DataBean>(getActivity(), R.layout.item_tab_key_manager_list, groups) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, TabKeyManager1FragmentResponse.DataBean item, int position) {
                                        TextView tvNormal = holder.getView(R.id.tvNormal);
                                        TextView tvAddress = holder.getView(R.id.tvAddress);
                                        TextView tvName = holder.getView(R.id.tvName);
                                        tvName.setText(item.getQuarters_name());
                                        tvAddress.setText(item.getAddress());
                                        RecyclerView itemRecyclerView = holder.getView(R.id.itemRecyclerView);
                                        if (item.isExpanded()) {
                                            itemRecyclerView.setVisibility(View.VISIBLE);
                                            tvName.setSelected(true);
                                        } else {
                                            itemRecyclerView.setVisibility(View.GONE);
                                            tvName.setSelected(false);
                                        }
                                        if (item.getIsdefault() == 1) {
                                            tvNormal.setVisibility(View.VISIBLE);
                                        } else {
                                            tvNormal.setVisibility(View.GONE);
                                        }
                                        initRecyclerView(itemRecyclerView, item.getListkey(), position);
                                    }

                                    @Override
                                    protected int getViewType(TabKeyManager1FragmentResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        for (int i = 0; i < groups.size(); i++) {
                                            if (i == position) {
                                                if (groups.get(i).isExpanded()) {
                                                    groups.get(i).setExpanded(false);
                                                } else {
                                                    groups.get(i).setExpanded(true);
                                                }
                                            } else {
                                                groups.get(i).setExpanded(false);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.tv1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                AddPlaceActivity.start();
                break;
        }
    }

    /**
     * 设置默认
     */
    private void settingNormal(String requid) {
        OkHttpUtils.post()
                .url(Api.SET_DEFAULT_PLACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("requid", requid)
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
                                request();
                            }
                        }
                    }
                });

    }

    /**
     * 删除小区
     */
    private void DeleteData(String requid) {
        showLoadingDialog("正在删除...");
        OkHttpUtils.post()
                .url(Api.DELETE_PLACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("requid", requid)
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
                                request();
                            }
                        }
                    }
                });

    }

    private void initRecyclerView(RecyclerView itemRecyclerView, List<TabKeyManager1FragmentResponse.DataBean.ListkeyBean> listkey, int AreaPosition) {
        itemRecyclerView.setNestedScrollingEnabled(false);
        itemRecyclerView.setAdapter(null);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RBaseAdapter<TabKeyManager1FragmentResponse.DataBean.ListkeyBean> rBaseAdapter = new RBaseAdapter<TabKeyManager1FragmentResponse.DataBean.ListkeyBean>(getActivity(), R.layout.item_item_tab_key_manager_list, listkey) {
            @Override
            protected void fillItem(RViewHolder holder, TabKeyManager1FragmentResponse.DataBean.ListkeyBean item, int position) {

                TextView tvOpenMethod = holder.getView(R.id.tvOpenMethod);

                if (AreaPosition == 0) {
                    tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_theme_radius_r16);
                    tvOpenMethod.setText("开锁方式");
                    tvOpenMethod.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                    tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnlockingModeActivity.start(item.getEqu_pass(),item.getEqu_id(),groups.get(AreaPosition).getRequ_id());
                        }
                    });
                    switch (item.getEqu_type()) {
                        case 1:
                            break;
                        case 2:
                            tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_theme_radius_r16);
                            tvOpenMethod.setText("立即开通");
                            tvOpenMethod.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                            tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApplyKeyActivity.start(item.getRequ_id());
                                }
                            });
                            break;
                        case 3:
                            break;
                    }
                } else {
                    if (item.getEqu_status() == 0) {
                        tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_gray__radius_16);
                        tvOpenMethod.setText("申请中...");
                        tvOpenMethod.setTextColor(getResources().getColor(R.color.color_999999));
                        switch (item.getEqu_type()) {
                            case 1:
                                break;
                            case 2:
                                tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_theme_radius_r16);
                                tvOpenMethod.setText("立即开通");
                                tvOpenMethod.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                                tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ApplyKeyActivity.start(item.getRequ_id());
                                    }
                                });
                                break;
                            case 3:
                                break;

                        }
                    } else {
                        switch (item.getEqu_status()) {////状态 1：通过审核 2=待审批 10=禁止开门
                            case 1:
                                tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_theme_radius_r16);
                                tvOpenMethod.setText("开锁方式");
                                tvOpenMethod.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                                tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UnlockingModeActivity.start(item.getEqu_pass(),item.getEqu_id(),groups.get(AreaPosition).getRequ_id());
                                    }
                                });
                                break;
                            case 2:
                                tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_gray__radius_16);
                                tvOpenMethod.setText("申请中...");
                                tvOpenMethod.setTextColor(getResources().getColor(R.color.color_999999));
                                break;
                            case 10:
                                break;
                        }
                    }
                }
                holder.setText(R.id.tvKey, item.getEqu_name());
            }

            @Override
            protected int getViewType(TabKeyManager1FragmentResponse.DataBean.ListkeyBean item, int position) {
                return 0;
            }
        };
        itemRecyclerView.setAdapter(rBaseAdapter);
    }


    public static class Item {
        private String groupName;
        private String groupId;
        private boolean isExpanded;
        private List<Child> childList;

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupId() {
            return groupId == null ? "" : groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<Item.Child> getChildList() {
            if (childList == null) {
                return new ArrayList<>();
            }
            return childList;
        }

        public void setChildList(List<Item.Child> childList) {
            this.childList = childList;
        }

        public static class Child {
            private String childName;
            private String childId;
            private String childUid;
            private String childIcon;

            public String getChildUid() {
                return childUid == null ? "" : childUid;
            }

            public void setChildUid(String childUid) {
                this.childUid = childUid;
            }

            public String getChildIcon() {
                return childIcon == null ? "" : childIcon;
            }

            public void setChildIcon(String childIcon) {
                this.childIcon = childIcon;
            }

            public String getChildName() {
                return childName == null ? "" : childName;
            }

            public void setChildName(String childName) {
                this.childName = childName;
            }

            public String getChildId() {
                return childId == null ? "" : childId;
            }

            public void setChildId(String childId) {
                this.childId = childId;
            }
        }
    }

}
