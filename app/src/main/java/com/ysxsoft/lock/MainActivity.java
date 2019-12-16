package com.ysxsoft.lock;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ui.fragment.MainChild1Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getMainActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_tab_notitle;
    }

    @Override
    public void doWork() {
        super.doWork();
        initBottomNavigationView();
        initViewPager();
    }

    private void initBottomNavigationView() {
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.menu1:
                        viewPager.setCurrentItem(0);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainChild1Fragment());
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, new ArrayList<String>()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                resetMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        viewPager.setOffscreenPageLimit(1);
        resetMenu(0);
    }

    /**
     * 切换菜单
     *
     * @param position
     */
    private void resetMenu(int position) {
        if (bottomNavigationView != null) {
            bottomNavigationView.getMenu().getItem(0).setIcon(position == 0 ? R.mipmap.icon_tab1_selected : R.mipmap.icon_tab1_normal);
        }
        switch (position) {
            case 0:
                //菜单1
                break;
            default:
                break;
        }
    }
}
