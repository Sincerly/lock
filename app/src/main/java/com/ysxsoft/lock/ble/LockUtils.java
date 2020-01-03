package com.ysxsoft.lock.ble;

import android.content.Context;

import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.ysxsoft.common_base.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class LockUtils {
    /**
     * 扫描设备
     * @param context
     */
    public static void scanDevice(Context context){
        BlueLockPub pub=BlueLockPub.bleLockInit(context);
        int result=pub.bleInit(context);
        // 0: ok -4: not support ble, -5: bluetooth not enabled.
        if(result==0){
            //初始化成功
        }else if(result==-4){
            //不支持蓝牙
        }else if(result==-5){
            //请开启蓝牙
        }
    }

    public static void scanDeviceLoop(){

    }

//    private List<LEDevice> leDeviceList=new ArrayList<>();
//    private void openBleLock(String deviceId) {
//        LEDevice matchedDevice = null;
//        for (int i = 0; i < leDeviceList.size(); i++) {
//            if (deviceId.equals(leDeviceList.get(i).getDeviceId())) {
//                matchedDevice = leDeviceList.get(i);
//            }
//        }
//        if (null != matchedDevice) {
//            String devicePassword = "12345675";
//            blueLockPub.oneKeyOpenDevice(matchedDevice,matchedDevice.getDeviceId(), devicePassword);
//        } else {
//            ((BlueLockPub) blueLockPub).scanDevice(10000);
//            showRecData("没有扫描到对应的锁，请重试");
//        }
//    }
}
