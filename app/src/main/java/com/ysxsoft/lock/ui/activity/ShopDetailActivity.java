package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.IntentUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.TimeUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.ShopListResponse;
import com.ysxsoft.lock.models.response.TypeResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.fragment.TabPacket1Fragment;
import com.ysxsoft.lock.ui.fragment.TabPacket2Fragment;
import com.ysxsoft.lock.ui.fragment.TabPacket3Fragment;
import com.ysxsoft.lock.ui.fragment.TabPacket4Fragment;
import com.ysxsoft.lock.utils.BaiduLocationUtils;
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

import java.math.BigDecimal;
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
    @BindView(R.id.pic)
    ImageView pic;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;
    @BindView(R.id.mapView)
    TextureMapView mapView;
    private BaiduMap baiduMap;
    @Autowired
    String business_id;
    private boolean isFirstRequested = false;

    public static void start(String business_id) {
        ARouter.getInstance().build(ARouterPath.getShopDetailActivity()).withString("business_id", business_id).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
        initList();
        initBaiduMap();
        BaiduLocationUtils.initBdMapLocatonOnce(ShopDetailActivity.this, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取纬度信息
                if (!isFirstRequested) {
                    isFirstRequested = true;
                    request(bdLocation.getLatitude(), bdLocation.getLongitude());
                }
            }
        }, new BaiduLocationUtils.BDMapListener() {
            @Override
            public void end(LocationClient locationClient) {
                if (isFirstRequested) {
                    locationClient.stop();
                }
            }
        });
        getType();
    }

    private void getType() {
        OkHttpUtils.post()
                .url(Api.GET_CARD_TYPE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("business_id", business_id)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TypeResponse resp = JsonUtils.parseByGson(response, TypeResponse.class);
                        if (resp != null) {
                            if (200 == resp.getCode()) {
                                List<TypeResponse.DataBean> d = resp.getData();
                                tabLayout.removeAllTabs();
                                List<Fragment> fragmentList = new ArrayList<>();
                                List<String> titles = new ArrayList<>();
                                for (int i = 0; i < d.size(); i++) {
                                    TypeResponse.DataBean item = d.get(i);
                                    String typeName = item.getTypename();
                                    titles.add(typeName);
                                    if (typeName.equals("现金券")) {
                                        TabPacket1Fragment tabPacket1Fragment=new TabPacket1Fragment();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("shopId",business_id);
                                        tabPacket1Fragment.setArguments(bundle);
                                        fragmentList.add(tabPacket1Fragment);
                                    } else if (typeName.equals("团购套餐")) {
                                        TabPacket2Fragment tabPacket2Fragment=new TabPacket2Fragment();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("shopId",business_id);
                                        tabPacket2Fragment.setArguments(bundle);
                                        fragmentList.add(tabPacket2Fragment);
                                    } else if (typeName.equals("免费体验")) {
                                        TabPacket3Fragment tabPacket3Fragment=new TabPacket3Fragment();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("shopId",business_id);
                                        tabPacket3Fragment.setArguments(bundle);
                                        fragmentList.add(tabPacket3Fragment);
                                    }else{
                                        //会员卡
                                        TabPacket4Fragment tabPacket4Fragment=new TabPacket4Fragment();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("shopId",business_id);
                                        tabPacket4Fragment.setArguments(bundle);
                                        fragmentList.add(tabPacket4Fragment);
                                    }
                                }
                                initViewPage(fragmentList, titles);
                                initTabLayout(titles);
                            } else {
                                showToast(resp.getMsg());
                            }
                        }
                    }
                });
    }

    private void initBaiduMap() {
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mapView.showScaleControl(false);//是否显示比例尺
        mapView.showZoomControls(false);//缩放按钮
        baiduMap.setMyLocationEnabled(true);
        UiSettings uiSettings = baiduMap.getUiSettings();

        uiSettings.setScrollGesturesEnabled(true);
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


    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);


        //1,获取图片的高度
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ratingbar_ok);
        int starsImgHeight = bmp.getHeight();

        //2,将获取的图片高度设置给RatingBar
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rbar.getLayoutParams();
        lp.width = (LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height = starsImgHeight;
        rbar.setLayoutParams(lp);
        rbar.setRating(4.0f);
    }

    @OnClick({R.id.backLayout, R.id.tvMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvMore:
                ShopListActivity.start();
                break;
        }
    }

    public void request(double lat, double lng) {
        if (TextUtils.isEmpty(business_id)) {
            return;
        }
        getShopList(lat, lng);
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.ID_SHOP_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("id", business_id)
                .addParams("lat", String.valueOf(lat))
                .addParams("lng", String.valueOf(lng))
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
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                title.setText(resp.getData().getName());
                                tvName.setText(resp.getData().getName());
                                List<ShopDetailResponse.DataBean.ItemBean> itemBeans = resp.getData().getAttachList();
                                if (itemBeans != null) {
                                    if (itemBeans.size() > 0) {
                                        ShopDetailResponse.DataBean.ItemBean img = itemBeans.get(0);
                                        Glide.with(ShopDetailActivity.this).load(AppConfig.BASE_URL + img.getAttach()).into(pic);
                                    }
                                }
                                switch (resp.getData().getStatus()) {
                                    case "1":
                                        tvWorkTime.setText("营业中|" + resp.getData().getWeek1() + " -- " + resp.getData().getWeek2() + "  " + resp.getData().getTime1() + " -- " + resp.getData().getTime2());
                                        break;
                                    case "2":
                                        tvWorkTime.setText("停止营业|" + resp.getData().getWeek1() + " -- " + resp.getData().getWeek2() + "  " + resp.getData().getTime1() + " -- " + resp.getData().getTime2());
                                        break;
                                }
                                tvSales.setText("月销" + resp.getData().getXl() + "单");
                                tv4.setText(resp.getData().getAddress());
                                tv7.setText(resp.getData().getTel());
                                tv8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        IntentUtils.callEdit(ShopDetailActivity.this, resp.getData().getTel());
                                    }
                                });
                                String distance = resp.getData().getDistance();
                                if (distance != null) {
                                    Double d = Double.valueOf(distance);
                                    BigDecimal b = new BigDecimal(d);
                                    double value = b.doubleValue();
                                    //距离  公里
                                    int minute = (int) (value * 1000 / 50);//有多少分钟
                                    String time = TimeUtils.formattedTime(minute * 60 * 100);
                                    tv5.setText("距您" + distance + "km,步行约" + time);
                                } else {
                                    tv5.setText("");
                                }
                                if (resp.getData().getLat() != null && resp.getData().getLng() != null) {
                                    LatLng point = new LatLng(Double.parseDouble(resp.getData().getLat()), Double.parseDouble(resp.getData().getLng()));
                                    //构建Marker图标
                                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                                            .fromResource(R.mipmap.icon_baidu);
                                    //构建MarkerOption，用于在地图上添加Marker
                                    OverlayOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmap);
                                    //在地图上添加Marker，并显示
                                    baiduMap.addOverlay(option);
                                    MapStatus mMapStatus = new MapStatus.Builder()
                                            .target(point)
                                            .zoom(15)
                                            .build();
                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                    //改变地图状态
                                    baiduMap.setMapStatus(mMapStatusUpdate);
                                }
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取商铺详情失败");
                        }
                    }
                });
    }

    private void getShopList(double lat, double lng) {
        OkHttpUtils.get()
                .url(Api.LIST_NEAR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("lng", lng + "")
                .addParams("lat", lat + "")
                .addParams("near", 100000 + "")
                .addParams("requid", "")//小区id
                .addParams("bsort", "")//商户类型名称
                .addParams("orderby", "")//排序字段
                .addParams("asd", "DESC")//排序方式
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", " " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", response + " ");
                        ShopListResponse resp = JsonUtils.parseByGson(response, ShopListResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
                                List<ShopListResponse.RowsBean> rowsBeans = resp.getRows();

                                LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
                                layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                                recyclerView2.setLayoutManager(layoutManager1);
                                RBaseAdapter<ShopListResponse.RowsBean> adapter2 = new RBaseAdapter<ShopListResponse.RowsBean>(mContext, R.layout.item2_shop_detail, rowsBeans) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, ShopListResponse.RowsBean item, int position) {
                                        RoundImageView riv = holder.getView(R.id.riv);
                                        Glide.with(ShopDetailActivity.this).load(AppConfig.BASE_URL + item.getLogo()).into(riv);
                                        holder.setText(R.id.address, item.getAddress());
                                        holder.setText(R.id.title, item.getName());
                                    }

                                    @Override
                                    protected int getViewType(ShopListResponse.RowsBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter2.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        ShopListResponse.RowsBean o = (ShopListResponse.RowsBean) rowsBeans.get(position);
                                        ShopDetailActivity.start(o.getId());
                                    }
                                });
                                recyclerView2.setAdapter(adapter2);

                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取商圈失败");
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
