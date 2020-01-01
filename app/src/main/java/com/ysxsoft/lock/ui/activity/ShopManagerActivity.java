package com.ysxsoft.lock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.common_base.zxing.ScanActivity;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.ShopInfoResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.fragment.TabShopManager1Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager2Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager3Fragment;
import com.ysxsoft.lock.ui.fragment.TabShopManager4Fragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 店铺管理
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ShopManagerActivity")
public class ShopManagerActivity extends BaseActivity {
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
    ViewPager viewPager;

    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    @BindView(R.id.civ)
    CircleImageView civ;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;

    private String business_id;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopManagerActivity()).navigation();
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
        tabLayout.removeAllTabs();
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("现金券");
        titles.add("团购套餐");
        titles.add("免费体验");
        titles.add("会员卡");
        TabShopManager1Fragment tabShopManager1Fragment = new TabShopManager1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("business_id", business_id);
        tabShopManager1Fragment.setArguments(bundle);
        fragmentList.add(tabShopManager1Fragment);

        TabShopManager2Fragment tabShopManager2Fragment = new TabShopManager2Fragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("business_id", business_id);
        tabShopManager2Fragment.setArguments(bundle2);
        fragmentList.add(tabShopManager2Fragment);


        TabShopManager3Fragment tabShopManager3Fragment = new TabShopManager3Fragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("business_id", business_id);
        tabShopManager3Fragment.setArguments(bundle3);
        fragmentList.add(tabShopManager3Fragment);

        TabShopManager4Fragment tabShopManager4Fragment = new TabShopManager4Fragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("business_id", business_id);
        tabShopManager4Fragment.setArguments(bundle4);
        fragmentList.add(tabShopManager4Fragment);
        initViewPage(fragmentList, titles);
        initTabLayout(titles);
    }

    private void requestData() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.SHOP_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
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
                        ShopInfoResponse resp = JsonUtils.parseByGson(response, ShopInfoResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                if (resp.getData()!=null) {
                                    business_id = resp.getData().getId();
                                    //请求成功
                                    Glide.with(mContext).load(AppConfig.BASE_URL + resp.getData().getLogo()).into(civ);
                                    tvName.setText(resp.getData().getName());
                                    tvType.setText("主营类目：" + resp.getData().getMainbusiness());
                                }
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取商户信息失败");
                        }
                    }
                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_manager;
    }

    @OnClick({R.id.backLayout, R.id.cl1, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.cl1:
                ShopInfoActivity.start();
                break;
            case R.id.tv1:
                Intent intent = new Intent(mContext, ScanActivity.class);
                startActivityForResult(intent,0x01);
                break;
            case R.id.tv2:
                CheckRecordActivity.start();
                break;
            case R.id.tv3:
                StartAdServingActivity.start();
//                PacketRechargeActivity.start();
                break;
            case R.id.tv4:
                HelpActivity.start();
                break;

        }
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("店铺管理");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x01:
                    String result = data.getStringExtra("result");
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        if("".equals(jsonObject.optString("id"))){
                            ToastUtils.shortToast(mContext,"获取失败");
                            return;
                        }
                        String Id=jsonObject.getString("id");
                        requestCheckData(Id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void requestCheckData(String Id) {
            OkHttpUtils.post()
                    .url(Api.HX)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                    .addParams("id",Id)
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                            if (resp!=null){
                                if (HttpResponse.SUCCESS.equals(resp.getCode())){
                                    CheckSucessActivity.start();
                                }
                            }
                        }
                    });


    }


}