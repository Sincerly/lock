package com.ysxsoft.lock.ui.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
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
import com.ysxsoft.lock.ui.activity.AddPacketExperienceActivity;
import com.ysxsoft.lock.ui.activity.AddPacketVipActivity;
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
 * 
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabShopManager4Fragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;

    @BindView(R.id.FL1)
    FrameLayout FL1;
    List<String> groups = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabshopmanager4;
    }

    @Override
    protected void doWork(View view) {
        initList(view);
    }

    private void initList(View view){
        for (int i = 0; i < 5; i++) {
            groups.add(String.valueOf(i));
        }
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

                SwipeMenuItem setNormalItem = new SwipeMenuItem(getActivity());
                setNormalItem.setWidth(DisplayUtils.dp2px(getActivity(), 50));
                setNormalItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setNormalItem.setBackgroundColor(Color.parseColor("#F9596C"));
                setNormalItem.setTextSize(12);
                setNormalItem.setText("删除\n" +"卡券" );
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
                        showToast("删除卡券");
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RBaseAdapter<String> adapter = new RBaseAdapter<String>(getActivity(), R.layout.item_tabshopmanager4_fragment_list, groups) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
//                holder.setText(R.id.tvShopName,"");
//                holder.setText(R.id.tvNum,"");
//                holder.setText(R.id.tvSum,"");
//                holder.setText(R.id.tvTime,"");
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.FL1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.FL1:
                AddPacketVipActivity.start();
                break;
        }
    }
}