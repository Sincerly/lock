package com.ysxsoft.lock.ui.fragment.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.dh.bluelock.util.Constants;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.umeng.share.ShareUtil;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.IntentUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.StatusBarUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.activity.UserInfoActivity;
import com.ysxsoft.lock.ui.dialog.CheckAddressDialog;
import com.ysxsoft.lock.ui.dialog.CityTopDialog;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.ysxsoft.lock.ui.dialog.OpenBluthDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;

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
        initViewPager2();
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
                            //页面向右 获取优惠券
                            Log.e(TAG, "页面向右 获取优惠券");
                            CouponDialog.show(getActivity(), new CouponDialog.OnDialogClickListener() {
                                @Override
                                public void sure() {
                                    PacketActivity.start(0);
                                }
                            });
                        } else {
                            //页面向左 获取优惠券
                            Log.e(TAG, "页面向左 个人中心");
                        }
                    } else {
                        //y轴移动距离大于x轴 (上下)
                        if (offsetY > 0) {
                            //下 开锁 下滑一半选择小区
                            if (DisplayUtils.getDisplayHeight(getActivity()) / 3 > offsetY && downY < DisplayUtils.getDisplayHeight(getActivity()) / 3) {
                                Log.e(TAG, "滑动距离未超过1/3");
                                CityTopDialog.show(getActivity(), new CityTopDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure() {
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
                            //上  广告
                            Log.e(TAG, "切换广告");
                            int currentItem=viewPager2.getCurrentItem();
                            if(currentItem!=(viewPager2.getAdapter().getItemCount()-1)){
                                viewPager2.setCurrentItem((currentItem+1));
                            }else{
                                ToastUtils.shortToast(getActivity(),"暂无广告");
                            }
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

    BaseQuickAdapter<String, BaseViewHolder> adapter;
    private void initViewPager2(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager2.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        viewPager2.setLayoutParams(params);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        List<String> datas=new ArrayList<>();
        for (int i = 0; i <3; i++) {
            datas.add(""+i);
        }
        if (adapter == null) {
            adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_img, datas) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                }
            };
            viewPager2.setAdapter(adapter);
        } else {
            viewPager2.getAdapter().notifyDataSetChanged();
        }
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
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 1000);
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
           // Log.e(TAG, "scanDeviceCallBack "+rssi);
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
            //Log.e(TAG,"scanDeviceEndCallBack "+result);
            mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 1000);
        }

        @Override
        public void connectDeviceCallBack(int result, int status) {
//            showToast("onConnectDevice "+result+" "+status);
            //Log.e(TAG,"connectDeviceCallBack "+result);
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

    /*CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
        @Override
        public void sure(String requid) {
        }

        @Override
        public void cancle() {
            AddPlaceActivity.start();
        }
    });*/
}
