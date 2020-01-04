package com.ysxsoft.lock;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.imp.OneKeyInterface;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.dh.bluelock.util.Constants;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.StatusBarUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.activity.UserInfoActivity;
import com.ysxsoft.lock.ui.dialog.CheckAddressDialog;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.ysxsoft.lock.ui.dialog.OpenBluthDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {
    @BindView(R.id.adLayout)
    LinearLayout adLayout;
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY = 0;
    private int minOffset = 40;
    private static final String TAG = "MainActivity";
    private final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getMainActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void doWork() {
        super.doWork();
        maxOffsetY = DisplayUtils.getDisplayHeight(this) / 3;
        initData();
        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions re = new RxPermissions(this);
        re.requestEach(BASIC_PERMISSIONS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.e(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.e(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                float offsetX = x - downX;
                float offsetY = y - downY;

                if (Math.abs(offsetX) > minOffset || Math.abs(offsetY) > minOffset) {
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        //x轴移动距离大于y轴 (左右)
                        if (offsetX > 0) {
                            //右 个人中心
                            Log.e(TAG, "向右 个人中心");
                            UserInfoActivity.start();
                        } else {
                            //左 广告
                            Log.e(TAG, "向左 切换广告");
                            showNext();
                        }
                    } else {
                        //y轴移动距离大于x轴 (上下)
                        if (offsetY > 0) {
                            //下 开锁 下滑一半选择小区
                            if (offsetY > DisplayUtils.getDisplayHeight(MainActivity.this) / 3 && downY < DisplayUtils.getDisplayHeight(MainActivity.this) / 3) {
                                Log.e(TAG, "滑动距离超过1/3");
                                CheckAddressDialog.show(MainActivity.this, new CheckAddressDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure(String requid) {

                                    }

                                    @Override
                                    public void cancle() {
                                        AddPlaceActivity.start();
                                    }
                                });
                            } else {
                                Log.e(TAG, "向下");
                                Set<String> set = map.keySet();
                                ToastUtils.show(MainActivity.this, "附近设备" + set.size());

                                LEDevice leDevice = null;
                                for (Map.Entry<String, LEDevice> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    leDevice = entry.getValue();
                                }
                                if (leDevice != null) {
                                    String devicePassword = "12345678";
//                                    String devicePasswrod=blueLockPub.generateVisitPassword(leDevice.getDeviceId(),devicePassword,30);
//                                    Log.e(TAG,"devicePasswrod "+devicePassword);
                                    Log.e(TAG, "device：" + new Gson().toJson(leDevice) + "  deviceId:" + leDevice.getDeviceId());
                                    blueLockPub.oneKeyOpenDevice(leDevice, leDevice.getDeviceId(), devicePassword);
                                }
                            }
                        } else {
                            //上  获取优惠券
                            Log.e(TAG, "获取优惠券");
                            CouponDialog.show(MainActivity.this, new CouponDialog.OnDialogClickListener() {
                                @Override
                                public void sure() {
                                    PacketActivity.start(0);
                                }
                            });
                        }
                    }
                    x = 0;
                    y = 0;
                    downX = 0;
                    downY = 0;
                    if (showHalf) {
                        showHalf = false;
                    }
                } else {
                    Log.e(TAG, "未超过移动的距离");
                }
                break;
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 智能锁蓝牙
    ///////////////////////////////////////////////////////////////////////////
    private BlueLockPubCallBack blueLockCallBack;
    private BlueLockPub blueLockPub;
    private Handler mHandler;
    private final int MST_WHAT_START_SCAN_DEVICE = 0x01;
    private boolean hasScannedDaHaoLock;

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MST_WHAT_START_SCAN_DEVICE:
                        blueLockPub.scanDevice(10000);
                        break;
                }
            }
        };
    }

    private void initData() {
        initHandler();
        blueLockPub = BlueLockPub.bleLockInit(this);
        int result = blueLockPub.bleInit(this);
        blueLockCallBack = new BlueLockPubCallBack();
        blueLockPub.setLockMode(Constants.LOCK_MODE_MANUL, null, false);

        if (result == 0) {
            //初始化成功
            //开始扫描设备
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 500);
        } else if (result == -4) {
            //不支持蓝牙
            ToastUtils.show(MainActivity.this, "暂不支持蓝牙");
        } else if (result == -5) {
            //请开启蓝牙
            ToastUtils.show(MainActivity.this, "请开启蓝牙");

            OpenBluthDialog.show(mContext, new OpenBluthDialog.OnDialogClickListener() {
                @Override
                public void sure() {
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }

                @Override
                public void setting() {
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }
            });
        }
    }

    private Map<String, LEDevice> map = new HashMap<>();

    public class BlueLockPubCallBack extends BlueLockPubCallBackBase {
        @Override
        public void scanDeviceCallBack(final LEDevice ledevice,
                                       final int result, final int rssi) {
            hasScannedDaHaoLock = true;
            Log.e(TAG, "scanDeviceCallBack " + rssi);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //adapter.addDevice(ledevice);
                        String deviceId = ledevice.getDeviceId();
                        if (!map.containsKey(deviceId)) {
                            map.put(deviceId, ledevice);
                            //blueLockPub.connectDevice(ledevice);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        }

        @Override
        public void scanDeviceEndCallBack(final int result) {
            showToast("scanDeviceEndCallBack " + result);
            mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 500);
        }

        @Override
        public void connectDeviceCallBack(int result, int status) {
//            showToast("onConnectDevice "+result+" "+status);
            Log.e(TAG, "connectDeviceCallBack " + result);
        }

        @Override
        public void disconnectDeviceCallBack(int result, int status) {
//            showToast("onDisconnectDevice"+result+" "+status);
            Log.e(TAG, "disconnectDeviceCallBack " + result);
        }

        @Override
        public void connectingDeviceCallBack(int result) {
            Log.e(TAG, "onConnectingDevice" + result);
        }

        @Override
        public void openCloseDeviceCallBack(int result, int battery, String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, " =============openCloseDeviceCallBack result: "
                            + result);

                    if (null != params && params.length > 0) {

                        Log.e(TAG, " ============device Id: " + params[0]);
                    }
                    if (0 == result) {
//                        showToast(" openCloseDevice ok battery:" + battery
//                                + " spent time: "
//                                + (System.currentTimeMillis() - startTime)
//                                / 1000);
                        showToast("已打开");
                    } else if (11 == result) {
                        showToast("未注");
                    } else {
                        //enabledBtn(true);
                        showToast(" openCloseDeviceCallBack err code: "
                                + result);
                        if (-9 == result) {
                            if (hasScannedDaHaoLock) {
                                showToast("您走错门了！");
                            } else {
                                showToast("没有扫描到门禁设备！");
                            }
                        } else if (-6 == result) {
                            showToast("开门超时请验证密码是否正确");
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        blueLockPub.stopScanDevice();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        blueLockPub.removeResultCallBack(blueLockCallBack);
        mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        blueLockPub.setResultCallBack(blueLockCallBack);
        super.onResume();
    }

    private void showNext() {
        View view = View.inflate(MainActivity.this, R.layout.view_img, null);
        adLayout.addView(view);

        int height = DisplayUtils.getDisplayHeight(MainActivity.this) + StatusBarUtils.getStatusBarHeight(MainActivity.this);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                float offset = f * height;
                View v = adLayout.getChildAt(0);
                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height - offset)));
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) offset));
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                adLayout.removeViewAt(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
    }
}
