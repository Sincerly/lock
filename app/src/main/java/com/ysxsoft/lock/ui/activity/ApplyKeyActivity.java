package com.ysxsoft.lock.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ui.fragment.TabApplyKey1Fragment;
import com.ysxsoft.lock.ui.fragment.TabApplyKey2Fragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请钥匙
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ApplyKeyActivity")
public class ApplyKeyActivity extends BaseActivity {
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
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.et1)
    EditText et1;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getApplyKeyActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_key;
    }

    @OnClick(R.id.backLayout)
    public void onViewClicked() {
        backToActivity();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("申请钥匙");
    }

    @OnClick({R.id.backLayout, R.id.tv1, R.id.tv2, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv1:
                showToast("几栋楼");
                break;
            case R.id.tv2:
                showToast("几单元");
                break;
            case R.id.tvOk:
                finish();
                break;
        }
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
//        tabLayout.removeAllTabs();
//        List<Fragment> fragmentList = new ArrayList<>();
//        List<String> titles = new ArrayList<>();
//        titles.add("手机钥匙");
//        titles.add("人脸识别");
//        fragmentList.add(new TabApplyKey1Fragment());
//        fragmentList.add(new TabApplyKey2Fragment());
//        initViewPage(fragmentList, titles);
//        initTabLayout(titles);
    }

    private void initViewPage(List<Fragment> fragmentList, List<String> titles) {
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titles));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
    }

    private void initTabLayout(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.view_tab);
            TextView textView = tab.getCustomView().findViewById(R.id.tab);
            textView.setText(titles.get(i));
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorTabSelectedIndictor));
                textView.setTextSize(17);
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
                textView.setTextSize(15);
            }
        }
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(17);
            tv.setTextColor(getResources().getColor(R.color.colorTabSelectedIndictor));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };
}