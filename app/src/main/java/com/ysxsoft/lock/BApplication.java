package com.ysxsoft.lock;

import android.content.Context;

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
