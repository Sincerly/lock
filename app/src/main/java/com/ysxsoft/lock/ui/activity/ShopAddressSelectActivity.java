package com.ysxsoft.lock.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.ui.dialog.BaiduCityDialog;
import com.ysxsoft.lock.ui.dialog.CitySelectDialog;
import com.ysxsoft.lock.utils.BaiduLocationUtils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By 胡
 * on 2020/1/3 0003
 */
@Route(path = "/main/ShopAddressSelectActivity")
public class ShopAddressSelectActivity extends BaseActivity {

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

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;


    private BaiduMap baiduMap;
    private SuggestionSearch suggestionSearch;
    private PoiSearch mPoiSearch;
    private BaiduCityDialog cityDialog;
    private boolean isClick = false;
    private double latitude;
    private double longitude;
    private String detailAddress;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopAddressSelectActivity()).navigation();
    }

    public static void start(Activity activity, int requestCode) {
        ARouter.getInstance().build(ARouterPath.getShopAddressSelectActivity()).navigation(activity, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_address_select;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initBaiduMap();
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etAddress.getText().toString().trim())) {

                    if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
                        showToast("所在地区不能为空");
                        return;
                    }

                    /**
                     *  PoiCiySearchOption 设置检索属性
                     *  city 检索城市
                     *  keyword 检索内容关键字
                     *  pageNum 分页页码
                     */
                    mPoiSearch.searchInCity(new PoiCitySearchOption()
                            .city(tv1.getText().toString().trim()) //必填
                            .keyword(etAddress.getText().toString().trim()) //必填
                            .pageNum(10));
                } else {
                    LL1.setVisibility(View.VISIBLE);
                    if (cityDialog != null) {
                        cityDialog.dismiss();
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

//        BaiduLocationUtils.initBdMapLocaton(mContext, new BDAbstractLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                double latitude = bdLocation.getLatitude();
//                double longitude = bdLocation.getLongitude();
//                provice = bdLocation.getProvince();
//                city = bdLocation.getCity();
//                String district = bdLocation.getDistrict();    //获取区县
//                String street = bdLocation.getStreet();
//                String addrStr = bdLocation.getAddrStr();
////                tv1.setText(provice + city + district);
////                etAddress.setText(addrStr);
//                LatLng point = new LatLng(latitude, longitude);
//                //构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.mipmap.icon_baidu);
//                //构建MarkerOption，用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions()
//                        .position(point)
//                        .icon(bitmap);
//                //在地图上添加Marker，并显示
//                baiduMap.addOverlay(option);
//                MapStatus mMapStatus = new MapStatus.Builder()
//                        .target(point)
//                        .zoom(15)
//                        .build();
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                //改变地图状态
//                baiduMap.setMapStatus(mMapStatusUpdate);
//            }
//        });
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(listener);

    }

    OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> CityList = poiResult.getAllPoi();
            if (CityList != null) {
                if (CityList.size() > 0) {

                    cityDialog = BaiduCityDialog.getInstance();
                    cityDialog.init(ShopAddressSelectActivity.this);
                    cityDialog.setData(CityList);
                    cityDialog.setOnPopupWindowListener(new BaiduCityDialog.OnPopupWindowListener() {
                        @Override
                        public void select(PoiInfo poiInfo) {
                            isClick = true;
                            provice = poiInfo.getProvince();
                            city = poiInfo.getCity();
                            area = poiInfo.area;
                            tv1.setText(poiInfo.getProvince() + poiInfo.getCity() + poiInfo.area);
                            etAddress.setText(poiInfo.address);
                            LatLng location = poiInfo.location;
                            latitude = location.latitude;
                            longitude = location.longitude;
                            String address = poiInfo.address;
                            String province = poiInfo.getProvince();
                            String city = poiInfo.getCity();
                            String area = poiInfo.getArea();
                            detailAddress = province + city + area + address;

                            LatLng point = new LatLng(latitude, longitude);
                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.icon_baidu);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap)
                                    .zIndex(1) // 设置marker所在层级
                                    .draggable(true); // 设置手势拖拽;
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
                    });
                    cityDialog.showPopDown(LL2, 0, 0);
                } else {
                    showToast("暂无数据");
                }
            } else {
                showToast("暂无数据");
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }

        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("店铺地址");
    }

    private String provice;
    private String city;
    private String area;

    @OnClick({R.id.backLayout, R.id.tv1, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv1:
                CitySelectDialog.show(mContext, new CitySelectDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String proviceName, String proviceCode, String cityName, String cityCode, String areaName, String areaCode) {
                        area = areaName;
                        city = cityName;
                        provice = proviceName;
                        tv1.setText(proviceName + cityName + areaName);
                    }
                });
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(tv1.getText().toString().trim())){
                    showToast("所在地区不能为空");
                    return;
                }
                 if (TextUtils.isEmpty(detailAddress)){
                    showToast("详细地址不能为空");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("longitude",String.valueOf(longitude));
                intent.putExtra("detailAddress",detailAddress);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        mPoiSearch.destroy();
        super.onDestroy();
    }

}
