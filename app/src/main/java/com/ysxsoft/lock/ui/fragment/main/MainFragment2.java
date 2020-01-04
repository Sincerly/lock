package com.ysxsoft.lock.ui.fragment.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.dh.bluelock.util.Constants;
import com.google.gson.Gson;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.StatusBarUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
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

/**
 * 首页操作页面
 */
public class MainFragment2 extends BaseFragment implements View.OnTouchListener {
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY = 0;
    private int minOffset = 40;
    @BindView(R.id.touchView)
    ImageView touchView;

    private static final String TAG = "MainFragment2";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_2;
    }

    @Override
    public void doWork(View view) {
        maxOffsetY = DisplayUtils.getDisplayHeight(getActivity()) / 3;
        initData();
        touchView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
                            //UserInfoActivity.start();
                        } else {
                            //左 广告
                            Log.e(TAG, "向左 切换广告");
                            //showNext();
                        }
                    } else {
                        //y轴移动距离大于x轴 (上下)
                        if (offsetY > 0) {
                            //下 开锁 下滑一半选择小区
                            if (offsetY > DisplayUtils.getDisplayHeight(getActivity()) / 3 && downY < DisplayUtils.getDisplayHeight(getActivity()) / 3) {
                                Log.e(TAG, "滑动距离超过1/3");
                                CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
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
                                ToastUtils.show(getActivity(), "附近设备" + set.size());

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
                            CouponDialog.show(getActivity(), new CouponDialog.OnDialogClickListener() {
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
        blueLockPub = BlueLockPub.bleLockInit(getActivity());
        int result = blueLockPub.bleInit(getActivity());
        blueLockCallBack = new BlueLockPubCallBack();
        blueLockPub.setLockMode(Constants.LOCK_MODE_MANUL, null, false);

        if (result == 0) {
            //初始化成功
            //开始扫描设备
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 500);
        } else if (result == -4) {
            //不支持蓝牙
            ToastUtils.show(getActivity(),"暂不支持蓝牙");
        } else if (result == -5) {
            //请开启蓝牙
            ToastUtils.show(getActivity(),"请开启蓝牙");

            OpenBluthDialog.show(getActivity(), new OpenBluthDialog.OnDialogClickListener() {
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
            hasScannedDaHaoLock=true;
            Log.e(TAG, "scanDeviceCallBack "+rssi);
            getActivity().runOnUiThread(new Runnable() {
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
            //showToast("scanDeviceEndCallBack "+result);
            Log.e(TAG,"scanDeviceEndCallBack "+result);
            mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 500);
        }

        @Override
        public void connectDeviceCallBack(int result, int status) {
//            showToast("onConnectDevice "+result+" "+status);
            Log.e(TAG,"connectDeviceCallBack "+result);
        }

        @Override
        public void disconnectDeviceCallBack(int result, int status) {
//            showToast("onDisconnectDevice"+result+" "+status);
            Log.e(TAG,"disconnectDeviceCallBack "+result);
        }

        @Override
        public void connectingDeviceCallBack(int result) {
            Log.e(TAG,"onConnectingDevice"+result);
        }

        @Override
        public void openCloseDeviceCallBack(int result, int battery, String... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, " =============openCloseDeviceCallBack result: "
                            + result);

                    if (null != params && params.length > 0) {

                        Log.e(TAG, " ============device Id: " + params[0]);
                    }
                    if (0 == result) {
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
    public void onDestroy() {
        blueLockPub.stopScanDevice();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        blueLockPub.removeResultCallBack(blueLockCallBack);
        mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
        super.onPause();
    }

    @Override
    public void onResume() {
        blueLockPub.setResultCallBack(blueLockCallBack);
        super.onResume();
    }

//    private void showNext() {
//        View view=View.inflate(getActivity(),R.layout.view_img,null);
//        adLayout.addView(view);
//
//        int height=DisplayUtils.getDisplayHeight(getActivity())+ StatusBarUtils.getStatusBarHeight(getActivity());
//
//        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0f,1f);
//        valueAnimator.setDuration(300);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float f= (float) animation.getAnimatedValue();
//                float offset=f*height;
//                View v=adLayout.getChildAt(0);
//                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int) (height-offset)));
//                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)offset));
//            }
//        });
//        valueAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                adLayout.removeViewAt(0);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//        valueAnimator.setRepeatCount(0);
//        valueAnimator.start();
//    }
}
