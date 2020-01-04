package com.ysxsoft.lock;

import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.dh.bluelock.pub.BlueLockPub;
import com.ysxsoft.common_base.base.BaseApplication;
import com.ysxsoft.common_base.jpush.JpushUtils;

import java.util.UUID;

public class BApplication extends BaseApplication {

    public static Context context;

    @Override
    protected void handle() {
        super.handle();
        JpushUtils.init(this);
        BlueLockPub.bleLockInit(this);
        context = super.getApplicationContext();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
