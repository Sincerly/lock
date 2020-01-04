package com.ysxsoft.lock.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.utils.ToastUtils;

import androidx.fragment.app.FragmentActivity;
import io.reactivex.functions.Consumer;

/**
 * Create By 胡
 * on 2020/1/3 0003
 */
public class BaiduLocationUtils {
    /**
     * 初始化百度地图定位
     *
     * @param context
     * @param listener
     */
    @SuppressLint("CheckResult")
    public static void initBdMapLocaton(Context context, BDAbstractLocationListener listener) {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
                //声明LocationClient类
                mLocationClient.registerLocationListener(listener);

                //注册监听函数
                LocationClientOption option = new LocationClientOption();

                option.setIsNeedAddress(true);

                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

                option.setCoorType("bd09ll");
                //可选，设置返回经纬度坐标类型，默认GCJ02
                //GCJ02：国测局坐标；
                //BD09ll：百度经纬度坐标；
                //BD09：百度墨卡托坐标；
                //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

                option.setScanSpan(3000);
                //可选，设置发起定位请求的间隔，int类型，单位ms
                //如果设置为0，则代表单次定位，即仅定位一次，默认为0
                //如果设置非0，需设置1000ms以上才有效

                option.setOpenGps(true);
                //可选，设置是否使用gps，默认false
                //使用高精度和仅用设备两种定位模式的，参数必须设置为true

                option.setLocationNotify(true);
                //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

                option.setIgnoreKillProcess(false);
                //可选，定位SDK内部是一个service，并放到了独立进程。
                //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

                option.SetIgnoreCacheException(false);
                //可选，设置是否收集Crash信息，默认收集，即参数为false

                option.setWifiCacheTimeOut(5 * 60 * 1000);
                //可选，V7.2版本新增能力
                //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

                option.setEnableSimulateGps(false);
                //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

                mLocationClient.setLocOption(option);
                //mLocationClient为第二步初始化过的LocationClient对象
                //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

                mLocationClient.start();
                //mLocationClient为第二步初始化过的LocationClient对象
                //调用LocationClient的start()方法，便可发起定位请求
            } else {
                rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
                            //声明LocationClient类
                            mLocationClient.registerLocationListener(listener);

                            //注册监听函数
                            LocationClientOption option = new LocationClientOption();

                            option.setIsNeedAddress(true);

                            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

                            option.setCoorType("bd09ll");
                            //可选，设置返回经纬度坐标类型，默认GCJ02
                            //GCJ02：国测局坐标；
                            //BD09ll：百度经纬度坐标；
                            //BD09：百度墨卡托坐标；
                            //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

                            option.setScanSpan(3000);
                            //可选，设置发起定位请求的间隔，int类型，单位ms
                            //如果设置为0，则代表单次定位，即仅定位一次，默认为0
                            //如果设置非0，需设置1000ms以上才有效

                            option.setOpenGps(true);
                            //可选，设置是否使用gps，默认false
                            //使用高精度和仅用设备两种定位模式的，参数必须设置为true

                            option.setLocationNotify(true);
                            //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

                            option.setIgnoreKillProcess(false);
                            //可选，定位SDK内部是一个service，并放到了独立进程。
                            //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

                            option.SetIgnoreCacheException(false);
                            //可选，设置是否收集Crash信息，默认收集，即参数为false

                            option.setWifiCacheTimeOut(5 * 60 * 1000);
                            //可选，V7.2版本新增能力
                            //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

                            option.setEnableSimulateGps(false);
                            //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

                            mLocationClient.setLocOption(option);
                            //mLocationClient为第二步初始化过的LocationClient对象
                            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

                            mLocationClient.start();
                            //mLocationClient为第二步初始化过的LocationClient对象
                            //调用LocationClient的start()方法，便可发起定位请求

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtils.shortToast(context, "请在设置中开启权限");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            ToastUtils.shortToast(context, "请在设置中开启权限");
                        }
                    }
                });
            }
        }else {
            LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
            //声明LocationClient类
            mLocationClient.registerLocationListener(listener);
            //注册监听函数
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setCoorType("bd09ll");
            //可选，设置返回经纬度坐标类型，默认GCJ02
            //GCJ02：国测局坐标；
            //BD09ll：百度经纬度坐标；
            //BD09：百度墨卡托坐标；
            //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
            option.setScanSpan(3000);
            //可选，设置发起定位请求的间隔，int类型，单位ms
            //如果设置为0，则代表单次定位，即仅定位一次，默认为0
            //如果设置非0，需设置1000ms以上才有效
            option.setOpenGps(true);
            //可选，设置是否使用gps，默认false
            //使用高精度和仅用设备两种定位模式的，参数必须设置为true
            option.setLocationNotify(true);
            //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
            option.setIgnoreKillProcess(false);
            //可选，定位SDK内部是一个service，并放到了独立进程。
            //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
            option.SetIgnoreCacheException(false);
            //可选，设置是否收集Crash信息，默认收集，即参数为false
            option.setWifiCacheTimeOut(5 * 60 * 1000);
            //可选，V7.2版本新增能力
            //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
            option.setEnableSimulateGps(false);
            //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
            mLocationClient.setLocOption(option);
            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
            mLocationClient.start();
            //mLocationClient为第二步初始化过的LocationClient对象
            //调用LocationClient的start()方法，便可发起定位请求
        }
    }



}
