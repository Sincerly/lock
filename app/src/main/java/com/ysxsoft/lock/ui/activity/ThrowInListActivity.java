package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.ui.fragment.Tab1ThrowInListFragment;
import com.ysxsoft.lock.ui.fragment.Tab2ThrowInListFragment;
import com.ysxsoft.lock.ui.fragment.Tab3ThrowInListFragment;
import com.ysxsoft.lock.ui.fragment.Tab4ThrowInListFragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager1Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager2Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager3Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager4Fragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By 胡
 * on 2019/12/30 0030
 */
@Route(path = "/main/ThrowInListActivity")
public class ThrowInListActivity extends BaseActivity {
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.backWithText)
    TextView backWithText;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customCenterTabView)
    LinearLayout customCenterTabView;
    @BindView(R.id.rightWithIcon)
    TextView rightWithIcon;
    @BindView(R.id.bg)
    LinearLayout bg;
    @BindView(R.id.bottomLineView)
    View bottomLineView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @Autowired
    int isSelect = 0;
    @Autowired
    String business_id;

    public static void start(int isSelect, String business_id) {
        ARouter.getInstance().build(ARouterPath.getThrowInListActivity()).withInt("isSelect", isSelect).withString("business_id", business_id).navigation();
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
        initViewPager();
        initList();

    }

    private void initList() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> list = new ArrayList<>();
        list.add("现金券");
        list.add("团购套餐");
        list.add("免费体验");
        list.add("会员卡");
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_tab_tabpacket1, list) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                TextView tv1 = holder.getView(R.id.tv1);
                tv1.setText(item);
                if (isSelect == position) {
                    tv1.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv1.setBackgroundResource(R.drawable.shape_btn_bg);
                } else {
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
                viewPager.setCurrentItem(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("投放列表");
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();

        Tab1ThrowInListFragment tabShopManager1Fragment = new Tab1ThrowInListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("business_id", business_id);
        tabShopManager1Fragment.setArguments(bundle);
        fragmentList.add(tabShopManager1Fragment);

        Tab2ThrowInListFragment tabShopManager2Fragment = new Tab2ThrowInListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("business_id", business_id);
        tabShopManager2Fragment.setArguments(bundle2);
        fragmentList.add(tabShopManager2Fragment);


        Tab3ThrowInListFragment tabShopManager3Fragment = new Tab3ThrowInListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("business_id", business_id);
        tabShopManager3Fragment.setArguments(bundle3);
        fragmentList.add(tabShopManager3Fragment);

        Tab4ThrowInListFragment tabShopManager4Fragment = new Tab4ThrowInListFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("business_id", business_id);
        tabShopManager4Fragment.setArguments(bundle4);
        fragmentList.add(tabShopManager4Fragment);

        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, new ArrayList<String>()));

        viewPager.setCurrentItem(isSelect);
        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_throw_in_list_layout;
    }

    @OnClick({R.id.backLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
        }
    }
}
