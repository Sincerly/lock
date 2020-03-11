package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.WebViewUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.AboutMeResponse;
import com.ysxsoft.lock.models.response.CardDetailResponse;
import com.ysxsoft.lock.ui.fragment.TabTuanDetailFragment1;
import com.ysxsoft.lock.ui.fragment.TabTuanDetailFragment2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.TuanDetailResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 团购详情
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/TuanDetailActivity")
public class TuanDetailActivity extends BaseActivity {
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

    @BindView(R.id.riv)
    RoundImageView riv;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rbar)
    RatingBar rbar;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvSales)
    TextView tvSales;
    @BindView(R.id.webview)
    WebView webview;


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @Autowired
    String id;

    public static void start(String id) {
        ARouter.getInstance().build(ARouterPath.getTuanDetailActivity()).withString("id",id).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tuan_detail;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        WebViewUtils.init(webview);
        initTitle();
        tabLayout.removeAllTabs();
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("活动详情");
//        titles.add("使用规则");
//        fragmentList.add(new TabTuanDetailFragment1());
//        fragmentList.add(new TabTuanDetailFragment2());
//        initViewPage(fragmentList, titles);
//        initTabLayout(titles);
        getDetail();
    }

    public void getDetail() {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.CARD_INFO+"?id="+id)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        Log.e("tag","onError"+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        Log.e("tag",""+response);
                        CardDetailResponse resp = JsonUtils.parseByGson(response, CardDetailResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                CardDetailResponse.DataBean data = resp.getData();
                                String content=data.getSnum();
                                tvSales.setText("月销"+content+"单");
                                tvName.setText(data.getTitle());
                                Glide.with(TuanDetailActivity.this).load(AppConfig.BASE_URL+data.getImg()).into(riv);
                                WebViewUtils.setH5Data(webview,data.getRemark());
//                                WebViewUtils.setH5Data(webview,"2225<img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'><img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'><img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'><img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'><img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'><img src='http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/business/card/u1k3upjks3y9bdwd.jpeg'>");
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取关于我们失败");
                        }
                    }
                });
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("活动详情");

        //1,获取图片的高度
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ratingbar_ok);
        int starsImgHeight = bmp.getHeight();

        //2,将获取的图片高度设置给RatingBar
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)rbar.getLayoutParams();
        lp.width = (LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height =starsImgHeight;
        rbar.setLayoutParams(lp);
        rbar.setRating(4.0f);
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
                textView.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                textView.setTextSize(17);
            } else {
                textView.setTextColor(getResources().getColor(R.color.color_666666));
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
            tv.setTextColor(getResources().getColor(R.color.color_3BB0D2));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(13);
            tv.setTextColor(getResources().getColor(R.color.color_666666));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    @OnClick(R.id.backLayout)
    public void onViewClicked() {
        backToActivity();
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_TUAN_DETAIL)
                .addParams("uid", SharedPreferencesUtils.getUid(TuanDetailActivity.this))
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
                        TuanDetailResponse resp = JsonUtils.parseByGson(response, TuanDetailResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<TuanDetailResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取团购详情失败");
                        }
                    }
                });
    }
}
