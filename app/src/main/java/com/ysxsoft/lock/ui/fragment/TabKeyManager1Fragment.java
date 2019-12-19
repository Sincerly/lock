package com.ysxsoft.lock.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.ApplyKeyActivity;
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
    List<Item> groups = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabkeymanager1;
    }

    @Override
    protected void doWork(View view) {
        initParent();
//        request();
    }

    @OnClick({R.id.tv1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                AddPlaceActivity.start();
//                ApplyKeyActivity.start();
                break;
        }
    }

    private void initParent() {
        for (int i = 0; i < 5; i++) {
            Item item = new Item();
            item.setGroupId(String.valueOf(i));
            item.setGroupName("分组" + i);
            item.setExpanded(false);
            List<Item.Child> children = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Item.Child child = new Item.Child();
                child.setChildId(String.valueOf(j));
                child.setChildUid(String.valueOf("uid" + j));
                child.setChildName("好友" + i);
                child.setChildIcon("http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJypovT9OQz6zzI2TGl4D5doFde5s4GubSZuOvS0hym35PvCuoXjCmhypAfy3VQzkYoNXPGgXM3NA/132");
                children.add(child);
            }
            item.setChildList(children);
            groups.add(item);
        }
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

                SwipeMenuItem setNormalItem = new SwipeMenuItem(getActivity());
                setNormalItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                setNormalItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//                setNormalItem.setHeight(DisplayUtils.dp2px(getActivity(), 52));
                setNormalItem.setBackgroundColor(Color.parseColor("#3BB0D2"));
                setNormalItem.setTextSize(12);
                setNormalItem.setText("设为\n" + "默认");
                setNormalItem.setTextColor(Color.parseColor("#FFFFFF"));
                rightMenu.addMenuItem(setNormalItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//                deleteItem.setHeight(DisplayUtils.dp2px(getActivity(), 52));
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
                switch (menuPosition) {
                    case 0://设为默认
                        showToast("设为默认");
                        break;
                    case 1://删除小区
                        showToast("删除小区");
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RBaseAdapter<Item> adapter = new RBaseAdapter<Item>(getActivity(), R.layout.item_tab_key_manager_list, groups) {
            @Override
            protected void fillItem(RViewHolder holder, Item item, int position) {
                TextView tvNormal = holder.getView(R.id.tvNormal);
                TextView tvAddress = holder.getView(R.id.tvAddress);
                TextView tvName = holder.getView(R.id.tvName);
                tvName.setText(item.getGroupName());
                RecyclerView itemRecyclerView = holder.getView(R.id.itemRecyclerView);
                if (item.isExpanded()) {
                    itemRecyclerView.setVisibility(View.VISIBLE);
                    tvName.setSelected(true);
                } else {
                    itemRecyclerView.setVisibility(View.GONE);
                    tvName.setSelected(false);
                }
                if (position == 0) {
                    tvNormal.setVisibility(View.VISIBLE);
                } else {
                    tvNormal.setVisibility(View.GONE);
                }
                initRecyclerView(itemRecyclerView, item.getChildList());
            }

            @Override
            protected int getViewType(Item item, int position) {
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

    private void initRecyclerView(RecyclerView itemRecyclerView, List<Item.Child> childList) {
        itemRecyclerView.setNestedScrollingEnabled(false);
        itemRecyclerView.setAdapter(null);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add(String.valueOf(i));
        }
        RBaseAdapter<String> rBaseAdapter = new RBaseAdapter<String>(getActivity(), R.layout.item_item_tab_key_manager_list, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
                TextView tvOpenMethod = holder.getView(R.id.tvOpenMethod);
                if (holder.getAdapterPosition() % 2 == 0) {
                    holder.setText(R.id.tvKey, "大门钥匙");
                    tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_gray__radius_16);
                    tvOpenMethod.setText("开锁方式");
                    tvOpenMethod.setTextColor(getResources().getColor(R.color.color_999999));
                    tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnlockingModeActivity.start();
                        }
                    });
                } else {
                    holder.setText(R.id.tvKey, "单元门钥匙");
                    tvOpenMethod.setBackgroundResource(R.drawable.bg_white_border_theme_radius_r16);
                    tvOpenMethod.setText("立即开通");
                    tvOpenMethod.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                    tvOpenMethod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CertificationDialog.show(getActivity(), new CertificationDialog.OnDialogClickListener() {
                                @Override
                                public void sure() {
                                    ApplyKeyActivity.start();
                                }
                            });
                        }
                    });
                }

            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        itemRecyclerView.setAdapter(rBaseAdapter);

    }

    private void request() {
        OkHttpUtils.post()
//                .url(Api.)
                .addParams("uid", SharedPreferencesUtils.getUid(getActivity()))
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
