package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.lock.ui.fragment.TabKeyManager1Fragment;
import com.ysxsoft.lock.ui.fragment.TabKeyManager2Fragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.KeyManagerResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 钥匙管理
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/KeyManagerActivity")
public class KeyManagerActivity extends BaseActivity {
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
    @BindView(R.id.parent)
    LinearLayout parent;



    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getKeyManagerActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_key_manager;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        tabLayout.removeAllTabs();
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("手机钥匙");
        titles.add("人脸识别");
        fragmentList.add(new TabKeyManager1Fragment());
        fragmentList.add(new TabKeyManager2Fragment());
        initViewPage(fragmentList, titles);
        initTabLayout(titles);
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
            textView.setWidth(DisplayUtils.getDisplayWidth(mContext) * 1 / 3);
            textView.setText(titles.get(i));
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorTabSelectedIndictor));
                textView.setTextSize(17);
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
                textView.setTextSize(13);
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
            tv.setTextSize(13);
            tv.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("钥匙管理");
    }

    @OnClick({R.id.backLayout/*, R.id.tv1*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
//            case R.id.tv1:
//                ApplyKeyActivity.start();
//                break;
        }
    }

    public void request(int page) {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_KEY_MANAGER)
                .addParams("uid", SharedPreferencesUtils.getUid(KeyManagerActivity.this))
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
                        KeyManagerResponse resp = JsonUtils.parseByGson(response, KeyManagerResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<KeyManagerResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取钥匙管理失败");
                        }
                    }
                });
    }

//    public int getItemLayoutId() {
//        return R.layout.item_key_manager;
//    }

}
