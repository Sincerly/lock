package com.ysxsoft.lock;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.dh.bluelock.util.Constants;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.models.event.UnlockEvent;
import com.ysxsoft.lock.models.event.UnlockSuccessEvent;
import com.ysxsoft.lock.models.response.CheckPermissionResponse;
import com.ysxsoft.lock.models.response.MessageEvent;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.ysxsoft.lock.ui.dialog.CouponNullStatusDialog;
import com.ysxsoft.lock.ui.dialog.NearByNoDeviceDialog;
import com.ysxsoft.lock.ui.dialog.OpenBluthDialog;
import com.ysxsoft.lock.ui.dialog.SafeDialog;
import com.ysxsoft.lock.ui.fragment.main.MainFragment1;
import com.ysxsoft.lock.ui.fragment.main.MainFragment2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY = 0;
    private int minOffset = 40;
    private static final String TAG = "MainActivity";
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private MainFragment2 fragment2;
    private boolean isFromPage = false;

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
        EventBus.getDefault().register(this);
        requestPermissions();
        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new MainFragment3());
        fragment2 = new MainFragment2();
        fragmentList.add(fragment2);
        fragmentList.add(new MainFragment1());
        FragmentPagerAdapter pagerAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, new ArrayList<>());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
//        map.put("1131", new LEDevice());
        SafeDialog dialog=new SafeDialog(this,R.style.CenterDialogStyle);
        dialog.setListener(new SafeDialog.OnDialogClickListener() {
            @Override
            public void sure() {
                ToastUtils.shortToast(MainActivity.this,"点击了事件！");
            }
        });
        dialog.showDialog();
    }

    @Subscriber
    public void MessageEvent(MessageEvent messageEvent) {
        String pass = messageEvent.getPass();//密码
        String equ_id = messageEvent.getEqu_id();//门禁设备id
        String requ_id = messageEvent.getRequ_id();//小区id

        if (equ_id.equals(getDeivceId())) {
            isFromPage = true;
            checkPermision(requ_id, equ_id, pass);
        } else {
            isFromPage = false;
            sendHideLockProgressEvent();
//            EventBus.getDefault().post(new UnlockSuccessEvent());
            ToastUtils.shortToast(MainActivity.this, "设备不匹配");
        }
    }

    private void sendHideLockProgressEvent(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new UnlockEvent());
            }
        });
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

    public void toTab(int pageIndex) {
        if (viewPager != null && viewPager.getChildCount() > pageIndex) {
            viewPager.setCurrentItem(pageIndex);
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
    private boolean isConnected = false;

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
        blueLockPub = BlueLockPub.bleLockInit(MainActivity.this);
        int result = blueLockPub.bleInit(MainActivity.this);
        blueLockCallBack = new BlueLockPubCallBack();
        blueLockPub.setLockMode(Constants.LOCK_MODE_MANUL, null, false);

        if (blueLockPub != null) {
            blueLockPub.setResultCallBack(blueLockCallBack);
        }
        Log.e("tag", result + "");
        if (result == 0) {
            //初始化成功
            //开始扫描设备
            isConnected = true;
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(MST_WHAT_START_SCAN_DEVICE, 500);
        } else if (result == -4) {
            //不支持蓝牙
            ToastUtils.show(MainActivity.this, "暂不支持蓝牙");
        } else if (result == -5) {
            //请开启蓝牙
            ToastUtils.show(MainActivity.this, "请开启蓝牙");
            OpenBluthDialog.show(MainActivity.this, new OpenBluthDialog.OnDialogClickListener() {
                @Override
                public void sure() {
                    isConnected = false;
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }

                @Override
                public void setting() {
                    isConnected = false;
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
            //showToast("scanDeviceEndCallBack "+result);
            Log.e(TAG, "scanDeviceEndCallBack " + result);
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
            isLocking = false;
            Log.e(TAG, "disconnectDeviceCallBack " + result);
        }

        @Override
        public void connectingDeviceCallBack(int result) {
            Log.e(TAG, "onConnectingDevice" + result);
        }

        @Override
        public void openCloseDeviceCallBack(int result, int battery, String... params) {
            isLocking = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, " =============openCloseDeviceCallBack result: "
                            + result);

                    if (null != params && params.length > 0) {

                        Log.e(TAG, " ============device Id: " + params[0]);
                    }
                    if (0 == result) {
                        //showToast("已打开");
                        if (!isFromPage) {
                            hideLoadingDialog();
                            if (hashCoupon) {
                                //有优惠券
                                if (couponData != null) {
//                                CouponDialog.show(MainActivity.this, false, "门已开启！欢迎回家", new CouponDialog.OnDialogClickListener() {
//                                    @Override
//                                    public void sure() {
//                                        PacketActivity.start(0);
//                                    }
//                                });
                                    CouponDialog.show(MainActivity.this, true, true,
                                            couponData.getData().getOprice()
                                            , couponData.getData().getPrice()
                                            , couponData.getData().getTitle()
                                            , couponData.getData().getStart_time()
                                            , couponData.getData().getEnd_time()
                                            , new CouponDialog.OnDialogClickListener() {
                                                @Override
                                                public void sure() {
                                                    //跳转到卡券页面
                                                    PacketActivity.start(0);
                                                }
                                            });
                                } else {
                                }
                            } else {
                                //无优惠券
                                CouponNullStatusDialog.show(MainActivity.this, new CouponNullStatusDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure() {
                                    }
                                });
                            }
                        } else {
                            isFromPage = false;
                            EventBus.getDefault().post(new UnlockSuccessEvent());
                        }
                    } else {
                        showToast(" 开锁失败");
                        if (!isFromPage) {
                            EventBus.getDefault().post(new UnlockEvent());
                        } else {
                            isFromPage = false;
                        }
                    }
                    //                    } else if (11 == result) {
//                        showToast("未注");
//                    } else {
//                        //enabledBtn(true);
//                        showToast(" 开锁失败 errcode: "
//                                + result);
//                        if (-9 == result) {
//                            if (hasScannedDaHaoLock) {
//                                showToast("您走错门了！");
//                            } else {
//                                showToast("没有扫描到门禁设备！");
//                            }
//                        } else if (-6 == result) {
//                            showToast("开门超时请验证密码是否正确");
//                        }
//                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        blueLockPub.stopScanDevice();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (blueLockPub != null) {
            blueLockPub.removeResultCallBack(blueLockCallBack);
        }
        mHandler.removeMessages(MST_WHAT_START_SCAN_DEVICE);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (blueLockPub != null) {
            blueLockPub.setResultCallBack(blueLockCallBack);
        }
        if (!isConnected) {
            //未连接上 进行链接
            initData();
        }
        super.onResume();
    }

    public Map<String, LEDevice> getMap() {
        return map;
    }

    public Map<String, LEDevice> getBlueLockPub() {
        return map;
    }

    private boolean isLocking = false;

    public void open(String devicePassword) {
        if (isLocking) {
            return;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            //提示开启蓝牙
            OpenBluthDialog.show(MainActivity.this, new OpenBluthDialog.OnDialogClickListener() {
                @Override
                public void sure() {
                    isConnected = false;
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }

                @Override
                public void setting() {
                    isConnected = false;
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }
            });
            isLocking = false;
            return;
        }
        isLocking = true;
        Set<String> set = map.keySet();
        if (set.size() == 0) {
            ToastUtils.longToast(MainActivity.this, "附近设备数量:" + map.size());
            NearByNoDeviceDialog.show(mContext);
            isLocking = false;
            return;
        }
        LEDevice leDevice = null;
        for (Map.Entry<String, LEDevice> entry : map.entrySet()) {
            String key = entry.getKey();
            leDevice = entry.getValue();
        }
        if (leDevice != null) {
            if ("".equals(devicePassword)) {
                devicePassword = "12345678";
            }
            blueLockPub.oneKeyOpenDevice(leDevice, leDevice.getDeviceId(), devicePassword);
        }
        showLoadingDialog("开门中");
    }

    private boolean hashCoupon = false;//是否拥有优惠券
    private CheckPermissionResponse.DataBeanX couponData;//优惠券

    /**
     * 开锁接口
     *
     * @param requid
     * @param equid
     */
    public void checkPermision(String requid, String equid, String password) {
        Log.e("tag", "设备id:" + equid);
        if ("".equals(equid)) {
            Set<String> set = map.keySet();
            if (set.size() == 0) {
                NearByNoDeviceDialog.show(mContext);
                isFromPage = false;
                return;
            }
        }
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.OPEN_JUR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(MainActivity.this))
                .addParams("requid", requid)//小区id
                .addParams("equid", equid)//设备id
//                .addParams("equid", "0A73A65C")//设备id
//                .addParams("equid", "DF447F1C")//设备id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        isFromPage = false;
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        Log.e("tag", "response:" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String data = jsonObject1.optString("data");
                            if (data != null && "no".equals(data)) {
                                //无优惠券
                                hashCoupon = false;
                                int jur = jsonObject1.optInt("jur");
                                if (jur == 1) {
                                    //有权限开门
                                    open(password);
                                } else {
                                    //无权限开门
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            isFromPage = false;
                                            ToastUtils.longToast(MainActivity.this, "无开门权限!" + "\n小区id" + (requid == null ? "" : requid) + "\n设备id:" + (equid == null ? "" : equid));
                                        }
                                    });
                                }
                            } else {
                                //有优惠券
                                CheckPermissionResponse resp = JsonUtils.parseByGson(response, CheckPermissionResponse.class);
                                hashCoupon = true;
                                if (resp != null) {
                                    couponData = resp.getData();
                                    if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                        int jur = resp.getData().getJur();
                                        if (jur == 1) {
                                            //有权限开门
                                            open(password);
                                        } else {
                                            //无权限开门
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    isFromPage = false;
                                                    ToastUtils.longToast(MainActivity.this, "无开门权限!" + "\n小区id" + (requid == null ? "" : requid) + "\n设备id:" + (equid == null ? "" : equid));
                                                }
                                            });
                                        }
                                    } else {
                                        isFromPage = false;
                                        showToast(resp.getMsg());
                                    }
                                } else {
                                    isFromPage = false;
                                    showToast("检测权限失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isFromPage = false;
                        }
                    }
                });
    }

    public void getCoupon(String requid, String equid) {
        Log.e("tag", "设备id:" + equid);
        if ("".equals(equid)) {
            Set<String> set = map.keySet();
            if (set.size() == 0) {
                NearByNoDeviceDialog.show(mContext);
                return;
            }
        }
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.OPEN_JUR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(MainActivity.this))
                .addParams("requid", requid)//小区id
                .addParams("equid", equid)//设备id
//                .addParams("equid", "0A73A65C")//设备id
//                .addParams("equid", "DF447F1C")//设备id
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
                        Log.e("tag", "response:" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String data = jsonObject1.optString("data");
                            if (data != null && "no".equals(data)) {
                                //无优惠券
                                CouponNullStatusDialog.show(MainActivity.this, new CouponNullStatusDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure() {
                                    }
                                });
                            } else {
                                //有优惠券
                                CheckPermissionResponse resp = JsonUtils.parseByGson(response, CheckPermissionResponse.class);
                                if (resp != null) {
                                    if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                        CheckPermissionResponse.DataBeanX item = resp.getData();

                                        CouponDialog.show(MainActivity.this, true, false,
                                                item.getData().getOprice()
                                                , item.getData().getPrice()
                                                , item.getData().getTitle()
                                                , item.getData().getStart_time(), item.getData().getEnd_time(), new CouponDialog.OnDialogClickListener() {
                                                    @Override
                                                    public void sure() {
                                                        //跳转到卡券页面
                                                        PacketActivity.start(0);
                                                    }
                                                });
                                    } else {
                                        //showToast(resp.getMsg());
                                    }
                                } else {
                                    //showToast("检测权限失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String getDeivceId() {
        String result = "";
        for (Map.Entry<String, LEDevice> entry : map.entrySet()) {
            LEDevice device = entry.getValue();
            result = device.getDeviceId();
        }
        return result;
    }

    public void resetStatus(){
        isFromPage=false;
    }
}
