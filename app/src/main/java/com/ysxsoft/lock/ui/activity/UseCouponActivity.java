package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.IntentUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.zxing.util.ZxingUtils;
import com.ysxsoft.lock.config.AppConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.UseCouponResponse;
import com.ysxsoft.lock.net.Api;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 使用券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/UseCouponActivity")
public class UseCouponActivity extends BaseActivity {
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

    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    @BindView(R.id.cl2)
    ConstraintLayout cl2;
    @BindView(R.id.riv)
    RoundImageView riv;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.ivBarCode)
    ImageView ivBarCode;
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv)
    CircleImageView iv;

    @Autowired
    String card_id;
    private BaiduMap baiduMap;
    private UseCouponResponse.DataBean dataBean;

    public static void start(String card_id) {
        ARouter.getInstance().build(ARouterPath.getUseCouponActivity()).withString("card_id", card_id).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_use_coupon;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
        initBaiduMap();
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

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("优惠券使用");
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    @OnClick({R.id.backLayout, R.id.cl1, R.id.cl2, R.id.tv8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.cl1:
                TuanDetailActivity.start();
                break;
            case R.id.cl2:
                ShopDetailActivity.start(dataBean.getBusiness_id());
                break;
            case R.id.tv8:
                IntentUtils.call(mContext, tv7.getText().toString().trim());
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.CARD_DETAIL_DATA)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("id", card_id)
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
                        UseCouponResponse resp = JsonUtils.parseByGson(response, UseCouponResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                dataBean = resp.getData();
                                Glide.with(mContext).load(AppConfig.BASE_URL + resp.getData().getImg()).into(riv);
                                tv1.setText(resp.getData().getTitle());
                                tv2.setText(resp.getData().getStart_time_str() + " -- " + resp.getData().getEnd_time_str());
                                tv3.setText("总价：￥" + resp.getData().getPrice());
                                ivBarCode.setImageBitmap(ZxingUtils.createBarcode(mContext, resp.getData().getId(), DisplayUtils.dp2px(mContext, 160), DisplayUtils.dp2px(mContext, 54), false));
                                ivQrCode.setImageBitmap(ZxingUtils.createQRImage(resp.getData().getId(), DisplayUtils.dp2px(mContext, 160), DisplayUtils.dp2px(mContext, 160), null, "url"));
                                tvCode.setText(resp.getData().getId());
                                tv4.setText(resp.getData().getName());
                                tv5.setText("距5你116m，步行约2分钟");
                                tv7.setText(resp.getData().getTel());
                                Glide.with(mContext).load(AppConfig.BASE_URL + resp.getData().getLogo()).into(iv);

                                if (TextUtils.isEmpty(resp.getData().getLat()) || TextUtils.isEmpty(resp.getData().getLng())) {
                                    return;
                                }

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

                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取使用券失败");
                        }
                    }
                });
    }
}
