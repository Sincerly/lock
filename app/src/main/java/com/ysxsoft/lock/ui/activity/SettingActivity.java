package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.ImageUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.dialog.BaseInputCenterDialog;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.MobileResponse;
import com.ysxsoft.lock.models.response.PersonCenterResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CheckLoginOutDialog;
import com.ysxsoft.lock.utils.ClearCacheManagerUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.SettingResponse;
import com.ysxsoft.lock.net.Api;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_TEST_LOGIN;

/**
 * 设置
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/SettingActivity")
public class SettingActivity extends BaseActivity {
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

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.autograph)
    TextView autograph;
    @BindView(R.id.tvMemery)
    TextView tvMemery;
    @BindView(R.id.LoginOut)
    TextView LoginOut;

    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;
    @BindView(R.id.LL3)
    LinearLayout LL3;
    @BindView(R.id.LL4)
    LinearLayout LL4;
    @BindView(R.id.LL5)
    LinearLayout LL5;
    @BindView(R.id.LL6)
    LinearLayout LL6;
    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    public static final int REQUEST_CODE_CROP = 0x02;
    private String cacheSize;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getSettingActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void doWork() {
        super.doWork();
        try {
            cacheSize = ClearCacheManagerUtils.getTotalCacheSize(mContext);
            tvMemery.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initTitle();
        initPhotoHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPersonData();
    }

    private void requestPersonData() {
        OkHttpUtils.post()
                .url(Api.PERSON_DATA)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PersonCenterResponse resp = JsonUtils.parseByGson(response, PersonCenterResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                Glide.with(mContext).load(AppConfig.BASE_URL+resp.getData().getIcon()).into(ivAvatar);
                                name.setText(resp.getData().getNickname());
                                autograph.setText(resp.getData().getAutograph());
                            }
                        }
                    }
                });
    }

    private void initPhotoHelper() {
        r = new RxPermissions(this);
        if (r.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && r.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File takePhotoDir = new File(AppConfig.PHOTO_CACHE_PATH);
            mPhotoHelper = new BGAPhotoHelper(takePhotoDir);
        } else {
            r.requestEach(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        File takePhotoDir = new File(AppConfig.PHOTO_CACHE_PATH);
                        mPhotoHelper = new BGAPhotoHelper(takePhotoDir);
                    }
                }
            });
        }
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("设置");
    }

    @OnClick({R.id.backLayout, R.id.LL1, R.id.LL2, R.id.LL3, R.id.LL4, R.id.LL5, R.id.LL6, R.id.LoginOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                choicePhotoWrapper();
                break;
            case R.id.LL2:
                EditInfoActivity.start();
                break;
            case R.id.LL3:
                BaseInputCenterDialog dialog = new BaseInputCenterDialog(this, R.style.CenterDialogStyle);
                dialog.initTitle("个性签名");
                dialog.initTips("请输入个性签名");
                dialog.initContent(autograph.getText().toString());
                dialog.setListener(new BaseInputCenterDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String nickname) {
                        //点击了确定
                        editSign(nickname);
                    }
                });
                dialog.showDialog();
                break;
            case R.id.LL4:
                ForgetPwdActivity.start("2");
                break;
            case R.id.LL5:
                ClearCacheManagerUtils.clearAllCache(mContext);
                tvMemery.setText("0MB");
                showToast("清理缓存成功");
                break;
            case R.id.LL6:
                AboutMeActivity.start();
                break;
            case R.id.LoginOut:
                CheckLoginOutDialog.show(mContext, new CheckLoginOutDialog.OnDialogClickListener() {
                    @Override
                    public void sure() {
                        LoginOutData();
                    }
                });
                break;
        }
    }

    private void editSign(String nickname) {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.EDIT_SIGN)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("grap", nickname)
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
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                autograph.setText(nickname);
                            }
                        }
                    }
                });


    }

    private void LoginOutData() {
        OkHttpUtils.post()
                .url(Api.LOGOUT)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()

                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse gson = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (gson != null) {
                            if (HttpResponse.SUCCESS.equals(gson.getCode())) {
//                                toLogin();
                                SharedPreferencesUtils.saveToken(SettingActivity.this,"");
                                if(IS_TEST_LOGIN){
                                    ARouter.getInstance().build("/main/LoginActivity").navigation();
                                    finish();
                                }
                                call();
                            }
                        }
                    }
                });
    }

    private void choicePhotoWrapper() {
        r.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                    File takePhotoDir = new File(AppConfig.PHOTO_PATH, "space");
                    File f = new File(AppConfig.PHOTO_PATH);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(SettingActivity.this)
                            .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                            .maxChooseCount(1) // 图片选择张数的最大值
                            .selectedPhotos(null) // 当前已选中的图片路径集合
                            .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                            .build();
                    startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
                }
            }
        });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_SETTING)
                .addParams("uid", SharedPreferencesUtils.getUid(SettingActivity.this))
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
                        SettingResponse resp = JsonUtils.parseByGson(response, SettingResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<SettingResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取设置失败");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RC_CHOOSE_PHOTO:
                    if (data != null) {
                        List<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
                        if (selectedPhotos != null && selectedPhotos.size() > 0) {
                            //拍照返回
                            try {
                                startActivityForResult(mPhotoHelper.getCropIntent(selectedPhotos.get(0), 200, 200), REQUEST_CODE_CROP);
                            } catch (Exception e) {
                                mPhotoHelper.deleteCameraFile();
                                mPhotoHelper.deleteCropFile();
                                BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_crop);
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case REQUEST_CODE_CROP:
                    String cropPath = mPhotoHelper.getCropFilePath();
                    String path = ImageUtils.compress(this, System.currentTimeMillis() + "", new File(cropPath), AppConfig.PHOTO_PATH);
                    //裁剪后的
                    EditHead(path);
                    break;
                default:
                    break;
            }
        }
    }

    private void EditHead(String path) {
        OkHttpUtils.post()
                .url(Api.EDIT_LOGO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addFile("headimg", new File(path).getName(), new File(path))
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
                                Glide.with(mContext).load(new File(path)).into(ivAvatar);
                            }
                        }
                    }
                });
    }
    PhoneNumberAuthHelper helper;
    private void call() {
        helper = PhoneNumberAuthHelper.getInstance(this, tokenResultListener);
        helper.setAuthSDKInfo("isgu8Z+e5PJrU4I19s1OUByrgrcXD2aZswJ66jWoD/VRTW7umKLhbR1AAGGcMP9epo/v5LniY45VHEkbVRupMffyzUfTjpWZQyuuMnO/7r66hu/TDpjBKSAB8MqFh+F9FxUpx6+eUn75ZH1RLvJ3VIBRl/5qmu1gIWGFs9dgNFQJtWt6jVw8jfK6ZyXLjakfI5HmV2d2ekoxwDOjacGAeQdR+NobYAwBbCFP2sXB/ouESJd/Pko2aBzODZc1H0+/UWWyPmkNo8M2WTuwa4rT3A0v1/zR7D/b");
        helper.setLoggerEnable(true);
        if (helper.checkEnvAvailable()) {
            //检查终端是否支持号码认证
            helper.setAuthUIConfig(new AuthUIConfig.Builder()
                    .setLogBtnText("手机号码一键登录")
                    .setNavHidden(true)
                    .setNavColor(getResources().getColor(R.color.colorPrimary))
                    .setLogoImgPath("ic_launcher")
                    .setLogoWidth(DisplayUtils.dp2px(SettingActivity.this, 24))
                    .setLogoHeight(DisplayUtils.dp2px(SettingActivity.this, 24))
                    .setSloganText(" ")
                    .setLogBtnBackgroundPath("shape_btn_bg")
                    .setLogBtnWidth(DisplayUtils.dp2px(SettingActivity.this, 122))
                    .setLogBtnHeight(DisplayUtils.dp2px(SettingActivity.this, 16))
                    .setLogBtnTextSize(16)
                    .setSwitchAccText("其他方式登录")
                    .setSwitchAccTextSize(12)
                    .setSwitchAccTextColor(Color.parseColor("#666666"))

                    .setPrivacyBefore("登录即同意我们的")
                    .setCheckboxHidden(true)
                    .setAppPrivacyOne("《服务协议》","http://info.linlilinwai.com/appinfo/xy")
                    .setAppPrivacyColor(Color.parseColor("#999999"),Color.parseColor("#3BB0D2"))
                    .create());
            helper.getLoginToken(SettingActivity.this, 30000);
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
                            SharedPreferencesUtils.saveToken(SettingActivity.this,resp.getApitoken());
                            SharedPreferencesUtils.savePhone(SettingActivity.this,resp.getPhone());
                            MainActivity.start();
                            helper.quitAuthActivity();
                        } else {
                            showToast("获取登录失败");
                        }
                    }
                });
    }
}
