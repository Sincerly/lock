package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.view.MyViewPager;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.ui.fragment.TabShopDetailFragment1;
import com.ysxsoft.lock.ui.fragment.TabShopDetailFragment2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ShopDetailResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 商铺详情
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ShopDetailActivity")
public class ShopDetailActivity extends BaseActivity {
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

    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rbar)
    RatingBar rbar;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvSales)
    TextView tvSales;
    @BindView(R.id.tvWorkTime)
    TextView tvWorkTime;

    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tvMore)
    TextView tvMore;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopDetailActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        tabLayout.removeAllTabs();
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("现金券");
        titles.add("招牌菜");
        fragmentList.add(new TabShopDetailFragment1());
        fragmentList.add(new TabShopDetailFragment2());
        initViewPage(fragmentList, titles);
        initTabLayout(titles);
        initList();
    }

    private void initList() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add(String.valueOf(i));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager);

        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item1_shop_detail, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        recyclerView1.setAdapter(adapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager1);

        RBaseAdapter<String> adapter2 = new RBaseAdapter<String>(mContext, R.layout.item2_shop_detail, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        recyclerView2.setAdapter(adapter2);


    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("xxx店铺");

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

    @OnClick({R.id.backLayout,R.id.tvMore,})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.backLayout:
                backToActivity();
                break;
                case R.id.tvMore:
                ShopListActivity.start();
                break;
        }

    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_SHOP_DETAIL)
                .addParams("uid", SharedPreferencesUtils.getUid(ShopDetailActivity.this))
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
                        ShopDetailResponse resp = JsonUtils.parseByGson(response, ShopDetailResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<ShopDetailResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取商铺详情失败");
                        }
                    }
                });
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
}
