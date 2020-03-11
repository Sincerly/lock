package com.ysxsoft.lock.ui.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.utils.ViewHelper;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.ADResponse;
import com.ysxsoft.lock.models.response.ActionResponse;
import com.ysxsoft.lock.models.response.CheckPermissionResponse;
import com.ysxsoft.lock.models.response.DefaultPlaceResponse;
import com.ysxsoft.lock.models.response.MobileResponse;
import com.ysxsoft.lock.models.response.PwdResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.GuideActivity;
import com.ysxsoft.lock.ui.activity.OtherLoginActivity;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.dialog.CheckAddressDialog;
import com.ysxsoft.lock.ui.dialog.CityTopDialog;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.ysxsoft.lock.ui.dialog.OpenLockPwdDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.ysxsoft.lock.net.Api.BASE_URL;

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
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.down)
    TextView down;

    private static final String TAG = "MainFragment2";
    private DefaultPlaceResponse.DataBean dataBean;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_2;
    }

    @Override
    public void doWork(View view) {
        maxOffsetY = DisplayUtils.getDisplayHeight(getActivity()) / 3;
        touchView.setOnTouchListener(this);
        initViewPager2();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        OkHttpUtils.get()
                .url(Api.GET_DEFAULT_PLACE_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "default place" + response);
                        DefaultPlaceResponse resp = JsonUtils.parseByGson(response, DefaultPlaceResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                dataBean = resp.getData();
                                getad();//获取广告
                                tvName.setText(resp.getData().getQuarters_name());
                            } else if (HttpResponse.NO_RESPONSE.equals(resp.getCode())) {
                                CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure(String requid) {
                                        //设置默认小区
                                        setDefault(requid);
                                    }

                                    @Override
                                    public void cancle() {
                                        AddPlaceActivity.start();
                                    }
                                });
                            }else if("401".equals(resp.getCode())){
                                ToastUtils.shortToast(getActivity(),"token已过期");
                                call();
                            }
                        }
                    }
                });
    }

    private void setDefault(String requid) {
        OkHttpUtils.post()
                .url(Api.SET_DEFAULT_PLACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("requid", requid)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                requestData();
                            }
                        }
                    }
                });

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean result = true;
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
//                            CouponDialog.show(getActivity(), true, "恭喜！送你一张优惠券", new CouponDialog.OnDialogClickListener() {
//                                @Override
//                                public void sure() {
//                                    PacketActivity.start(0);
//                                }
//                            });
                            if(dataBean!=null){
                                //检查开锁权限
                                MainActivity activity = (MainActivity) getActivity();
                                String deviceId=activity.getDeivceId();
                                if(dataBean!=null){
                                    //检查开锁权限
                                    activity.getCoupon(dataBean.getRequ_id(),deviceId);
                                }
                            }
                        } else {
                            //页面向左 获取优惠券
                            Log.e(TAG, "页面向左 个人中心");
                        }
                    } else {
                        //y轴移动距离大于x轴 (上下)
                        if (offsetY > 0) {
                            //下 开锁 下滑一半选择小区
                            if (DisplayUtils.getDisplayHeight(getActivity()) / 3 > offsetY && downY < DisplayUtils.getDisplayHeight(getActivity()) / 3) {

                                if (dataBean != null) {
                                    if (TextUtils.isEmpty(dataBean.getQuarters_name())) {
                                        CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
                                            @Override
                                            public void sure(String requid) {
                                                //设置默认小区
                                                setDefault(requid);
                                            }

                                            @Override
                                            public void cancle() {
                                                AddPlaceActivity.start();
                                            }
                                        });
                                    } else {
                                        Log.e(TAG, "滑动距离未超过1/3");
                                        CityTopDialog.show(getActivity(), new CityTopDialog.OnDialogClickListener() {
                                            @Override
                                            public void sure(String requid, int type,String deviceId,String password) {
                                                switch (type){//0蓝牙 1密码开门 2远程开门
                                                    case 0:
                                                        //蓝牙开门
                                                        MainActivity activity = (MainActivity) getActivity();
                                                        if(activity.getDeivceId().equals(deviceId)){
                                                            if(dataBean!=null){
                                                                //检查开锁权限
                                                                activity.resetStatus();
                                                                activity.checkPermision(requid,deviceId,"");
                                                            }
                                                        }else{
                                                            ToastUtils.shortToast(getActivity(),"设备不匹配！");
                                                        }
                                                        break;
                                                    case 1:
                                                        //密码开门
                                                        getPwd(deviceId);
                                                        break;
                                                    case 2:
                                                        //远程开门
                                                        openRemote(deviceId);
                                                        break;
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.e(TAG, "向下");
                                MainActivity activity = (MainActivity) getActivity();
                                String deviceId=activity.getDeivceId();
                                if(dataBean!=null){
                                    //检查开锁权限
                                    activity.resetStatus();
                                    activity.checkPermision(dataBean.getRequ_id(),deviceId,"");
                                }
                            }
                        } else {
                            //上  广告
                            Log.e(TAG, "切换广告");

                            int currentItem = viewPager2.getCurrentItem();
                            if (currentItem != (viewPager2.getAdapter().getItemCount() - 1)) {
                                viewPager2.setCurrentItem((currentItem + 1));
                            } else {
                                ToastUtils.shortToast(getActivity(), "暂无广告");
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
        return result;
    }

    private void getPwd(String equid){
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.GET_PASSWORD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("equid", equid)//设备锁id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "response:" + response);
                        hideLoadingDialog();
                        PwdResponse resp = JsonUtils.parseByGson(response, PwdResponse.class);
                        if (resp != null) {
                            if ("0".equals(resp.getResult())) {
                                //showToast(resp.getMsg());
                                OpenLockPwdDialog dialog = new OpenLockPwdDialog(getActivity(), R.style.CenterDialogStyle);
                                dialog.setData(resp.getData());
                                dialog.showDialog();
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("生成密码失败");
                        }
                    }
                });
    }

    /**
     * 远程开门
     * @param equid
     */
    private void openRemote(String equid) {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.REMOTDOOR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("equid", equid)//设备锁id
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "response:" + response);
                        hideLoadingDialog();
                        ActionResponse resp = JsonUtils.parseByGson(response, ActionResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                showToast(resp.getMsg());
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("开门失败");
                        }
                    }
                });
    }

    BaseQuickAdapter<ADResponse.DataBean, BaseViewHolder> adapter;

    private void initViewPager2() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager2.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        viewPager2.setLayoutParams(params);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0){
                    down.setAlpha(1);
                    ViewCompat.animate(down).alpha(1f).alpha(0f).setDuration(600).start();
                    down.setVisibility(View.GONE);
                }else{
                    if(down.getAlpha()!=1){
                        down.setAlpha(0);
                        down.setVisibility(View.VISIBLE);
                        ViewCompat.animate(down).alphaBy(0f).alpha(1f).setDuration(600).start();
                    }
                }
            }
        });

        List<ADResponse.DataBean> datas = new ArrayList<>();
        datas.add(new ADResponse.DataBean());
        if (adapter == null) {
            adapter = new BaseQuickAdapter<ADResponse.DataBean, BaseViewHolder>(R.layout.view_img, datas) {
                @Override
                protected void convert(BaseViewHolder helper,ADResponse.DataBean item) {
                    ImageView pic = helper.getView(R.id.pic);

                    String url= AppConfig.BASE_URL +item.getPicturePath();
                    boolean showDefault=true;
                    switch (helper.getAdapterPosition()) {
                        case 0:
                            showDefault=true;
                            break;
                         default:
                             showDefault=false;
                            break;
                    }
                    if(showDefault){
                        Glide.with(getActivity()).load(R.mipmap.main).into(pic);
                    }else{
                        Glide.with(getActivity()).load(url).into(pic);
                    }
                }
            };
            viewPager2.setAdapter(adapter);
        } else {
            viewPager2.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 获取广告
     */
    public void getad() {
        String requid = "";
        if (dataBean != null) {
            requid = dataBean.getId();//默认小区id
            if (requid == null) {
                return;
            }
        }
        OkHttpUtils.get()
                .url(Api.AD)
                //.addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("requid", requid)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ADResponse resp = JsonUtils.parseByGson(response, ADResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                Log.e("tag","获取广告");
                                List<ADResponse.DataBean> dataBeans=new ArrayList<>();
                                if(resp.getData()!=null){
                                    dataBeans.add(new ADResponse.DataBean());
                                    dataBeans.add(resp.getData());
                                }
                                adapter.setNewData(dataBeans);
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取广告失败");
                        }
                    }
                });
    }

    @OnClick({R.id.tvName, R.id.down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvName:
                //选择城市
                AddPlaceActivity.start();
                break;
            case R.id.down:
                viewPager2.setCurrentItem(0,false);
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 手机号登录
    ///////////////////////////////////////////////////////////////////////////
    PhoneNumberAuthHelper helper;
    private void call() {
        helper = PhoneNumberAuthHelper.getInstance(getActivity(), tokenResultListener);
        helper.setAuthSDKInfo("isgu8Z+e5PJrU4I19s1OUByrgrcXD2aZswJ66jWoD/VRTW7umKLhbR1AAGGcMP9epo/v5LniY45VHEkbVRupMffyzUfTjpWZQyuuMnO/7r66hu/TDpjBKSAB8MqFh+F9FxUpx6+eUn75ZH1RLvJ3VIBRl/5qmu1gIWGFs9dgNFQJtWt6jVw8jfK6ZyXLjakfI5HmV2d2ekoxwDOjacGAeQdR+NobYAwBbCFP2sXB/ouESJd/Pko2aBzODZc1H0+/UWWyPmkNo8M2WTuwa4rT3A0v1/zR7D/b");
        helper.setLoggerEnable(true);
        if (helper.checkEnvAvailable()) {
            //检查终端是否支持号码认证
            helper.setAuthUIConfig(new AuthUIConfig.Builder()
                    .setLogBtnText("手机号码一键登录")
                    .setNavHidden(true)
                    .setNavColor(getResources().getColor(R.color.colorPrimary))
                    .setLogoImgPath("ic_launcher")
                    .setLogoWidth(DisplayUtils.dp2px(getActivity(), 32))
                    .setLogoHeight(DisplayUtils.dp2px(getActivity(), 32))
                    .setSloganText(" ")
                    .setLogBtnBackgroundPath("shape_btn_bg")
                    .setLogBtnWidth(DisplayUtils.dp2px(getActivity(), 122))
                    .setLogBtnHeight(DisplayUtils.dp2px(getActivity(), 16))
                    .setLogBtnTextSize(16)
                    .setSwitchAccText("其他方式登录")
                    .setSwitchAccTextSize(12)
                    .setSwitchAccTextColor(Color.parseColor("#666666"))

                    .setPrivacyBefore("登录即同意我们的")
                    .setCheckboxHidden(true)
                    .setAppPrivacyOne("《服务协议》","http://info.linlilinwai.com/appinfo/xy")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());
            helper.getLoginToken(getActivity(), 30000);
        }
    }

    private TokenResultListener tokenResultListener = new TokenResultListener() {
        @Override
        public void onTokenSuccess(final String token) {
            Log.e("tag", "onTokenSuccess" + token);
            helper.setUIClickListener(new AuthUIControlClickListener() {
                @Override
                public void onClick(String s, Context context, JSONObject jsonObject) {
                    Log.e("tag",s+" "+jsonObject);
                    switch (s){
                        case "700000":
                            //点击返回 用户取消免密登录
                            break;
                        case "700001":
                            //点击切换 用户取消免密登录
                            OtherLoginActivity.start();
                            break;
                        case "700002":
                            //点击登录按钮事件
                            break;
                        case "700003":
                            //点击check box事件
                            break;
                        case "700004":
                            //点击协议富文本文字事件
                            break;
                    }
                }
            });

            TokenRet tokenRet = JSON.parseObject(token, TokenRet.class);
            if (tokenRet != null) {
                switch (tokenRet.getCode()){
                    case "600000":
                        //获取token成功
                        String tok = tokenRet.getToken();
                        getMobile(tok);
                        break;
                    case "600001":
                        //唤起授权页成功
                        //showToast(tokenRet.getMsg());
                        break;
                    case "600002":
                        //唤起授权页失败
                        showToast(tokenRet.getMsg());
                        break;
                    case "600004":
                        //获取运营商配置信息失败
                        showToast(tokenRet.getMsg());
                        break;
                    case "600005":
                        //手机终端不安全
                        showToast(tokenRet.getMsg());
                        break;
                }
            }
        }

        @Override
        public void onTokenFailed(String s) {
            Log.e("tag", "onTokenFailed" + s);
        }
    };

    public void getMobile(String accessToken) {
        OkHttpUtils.get()
                .url(Api.GET_MOBILE+"?accessToken="+accessToken)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag","返回值:"+response);
                        MobileResponse resp = JsonUtils.parseByGson(response, MobileResponse.class);
                        if (resp != null) {
                            SharedPreferencesUtils.saveToken(getActivity(),resp.getApitoken());
                            SharedPreferencesUtils.savePhone(getActivity(),resp.getPhone());
                            MainActivity.start();
                            helper.quitAuthActivity();
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
    }
}
