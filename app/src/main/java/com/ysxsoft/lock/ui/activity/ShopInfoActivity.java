package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.ImageUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.KeyBoardUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.TimeUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.custom.picker.CitySelectPicker;
import com.ysxsoft.common_base.view.custom.picker.TwoPicker;
import com.ysxsoft.common_base.view.dialog.BaseInputCenterDialog;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CheckAddressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ShopInfoResponse;
import com.ysxsoft.lock.net.Api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * 商户信息
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ShopInfoActivity")
public class ShopInfoActivity extends BaseActivity {
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

    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.logo)
    CircleImageView logo;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvSaleType)
    TextView tvSaleType;
    @BindView(R.id.tvWorkTime)
    TextView tvWorkTime;
    @BindView(R.id.tvShopAddress)
    TextView tvShopAddress;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.tvday)
    TextView tvday;
    @BindView(R.id.pic)
    LinearLayout pic;
    @BindView(R.id.iv)
    RoundImageView iv;

    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    private static final int RC_CHOOSE_PHOTO2 = 0x04;
    public static final int REQUEST_CODE_CROP = 0x02;
    public static final int REQUEST_CODE_CROP2 = 0x05;

    private String day1;
    private String day2;
    private String time1;
    private String time2;

    private String p;
    private String c;
    private String d;
    private String latitude;
    private String longitude;
    private String detailAddress;

    private String address;
    private String detailaddress;
    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopInfoActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_info;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initPhotoHelper();
        request();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        title.setText("商户信息");
    }

    @OnClick({R.id.backLayout, R.id.LL1, R.id.tvShopName, R.id.tvSaleType, R.id.tvWorkTime, R.id.tvShopAddress, R.id.tvday, R.id.tvOk, R.id.pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                choicePhotoWrapper();
                break;
            case R.id.tvShopName:
                BaseInputCenterDialog dialog = new BaseInputCenterDialog(this, R.style.CenterDialogStyle);
                dialog.initTitle("店铺名称");
                dialog.initTips("请输入店铺名称");
                dialog.initContent(tvShopName.getText().toString());
                dialog.setListener(new BaseInputCenterDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String nickname) {
                        //点击了确定
                        tvShopName.setText(nickname);
                    }
                });
                dialog.showDialog();
                break;
            case R.id.tvSaleType:
                BaseInputCenterDialog dialog1 = new BaseInputCenterDialog(this, R.style.CenterDialogStyle);
                dialog1.initTitle("主营类目");
                dialog1.initTips("请输入主营类目");
                dialog1.initContent(tvSaleType.getText().toString());
                dialog1.setListener(new BaseInputCenterDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String nickname) {
                        //点击了确定
                        tvSaleType.setText(nickname);
                    }
                });
                dialog1.showDialog();
                break;
            case R.id.tvday:
                ArrayList<String> days = new ArrayList<>();
                days.add("周一");
                days.add("周二");
                days.add("周三");
                days.add("周四");
                days.add("周五");
                days.add("周六");
                days.add("周日");
                TwoPicker twoPicker = new TwoPicker(mContext, R.style.BottomDialogStyle);
                twoPicker.setData(days, days, 0, 0);
                twoPicker.setListener(new TwoPicker.OnDialogSelectListener() {
                    @Override
                    public void OnSelect(String data1, int position1, String data2, int position2) {
                        day1 = data1;
                        day2 = data2;
                        tvday.setText(data1 + "——" + data2);
                    }
                });
                twoPicker.setTitle("请选择星期");
                twoPicker.showDialog();
                break;
            case R.id.tvWorkTime:
                ArrayList<String> times = new ArrayList<>();
                times.add("1:00");
                times.add("1:30");
                times.add("2:00");
                times.add("2:30");
                times.add("3:00");
                times.add("3:30");
                times.add("4:00");
                times.add("4:30");
                times.add("5:00");
                times.add("5:30");
                times.add("6:00");
                times.add("6:30");
                times.add("7:00");
                times.add("7:30");
                times.add("8:00");
                times.add("8:30");
                times.add("9:00");
                times.add("9:30");
                times.add("10:00");
                times.add("10:30");
                times.add("11:00");
                times.add("11:30");
                times.add("12:00");
                times.add("12:30");
                times.add("13:00");
                times.add("13:30");
                times.add("14:00");
                times.add("14:30");
                times.add("15:00");
                times.add("15:30");
                times.add("16:00");
                times.add("16:30");
                times.add("17:00");
                times.add("17:30");
                times.add("18:00");
                times.add("18:30");
                times.add("19:00");
                times.add("19:30");
                times.add("20:00");
                times.add("20:30");
                times.add("21:00");
                times.add("21:30");
                times.add("22:00");
                times.add("22:30");
                times.add("23:00");
                times.add("23:30");
                times.add("00:00");

                TwoPicker timesPicker = new TwoPicker(mContext, R.style.BottomDialogStyle);
                timesPicker.setData(times, times, 0, 0);
                timesPicker.setListener(new TwoPicker.OnDialogSelectListener() {
                    @Override
                    public void OnSelect(String data1, int position1, String data2, int position2) {
                        time1 = data1;
                        time2 = data2;
                        tvWorkTime.setText(data1 + "——" + data2);
                    }
                });
                timesPicker.setTitle("请选择时间");
                timesPicker.showDialog();

                break;
            case R.id.tvShopAddress:
//                KeyBoardUtils.hideInputMethod(this);
//                CitySelectPicker cityPicker = new CitySelectPicker();
//                cityPicker.initData(this);
//                cityPicker.setListener(new CitySelectPicker.OnCityPickerClickListener() {
//                    @Override
//                    public void onSelect(String province, String city, String district) {
//                        p = province;
//                        c = city;
//                        d = district;
//                        tvShopAddress.setText(p + c + d);
//                    }
//                });
//                cityPicker.show();
                ShopAddressSelectActivity.start(ShopInfoActivity.this,latitude,longitude,address,detailaddress,0x03);
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(tvShopName.getText().toString().trim())) {
                    showToast("店铺名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tvSaleType.getText().toString().trim())) {
                    showToast("主营类目不能为空");
                    return;
                }
                if (TextUtils.isEmpty(day2)) {
                    showToast("营业时间星期不能为空");
                    return;
                }
                if (TextUtils.isEmpty(time2)) {
                    showToast("营业时间不能为空");
                    return;
                }
                if (TextUtils.isEmpty(latitude)) {
                    showToast("地址选择不能为空");
                    return;
                }
                submintData();
                break;
            case R.id.pic:
                //上传背景图片
                choicePhotoWrapper2();
                break;
        }
    }

    private void uploadImg(String path) {
        showLoadingDialog("上传中...");
        File file = new File(path);
        OkHttpUtils.post()
                .url(Api.UPLOAD_BANNER)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addFile("businessimg", file.getName(), file)
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
                                request();
                            }
                        }
                    }
                });

    }

    private void submintData() {
        //信息未完善
        if (day1 == null) {
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善营业时间");
            return;
        }
        if (day2 == null) {
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善营业时间");
            return;
        }
        if (time1 == null) {
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善营业时间");
            return;
        }
        if (time2 == null) {
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善营业时间");
            return;
        }
        if("".equals(tvShopAddress.getText().toString())){
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善店铺地址");
            return;
        }
        if("".equals(etPhone.getText().toString())){
            ToastUtils.shortToast(ShopInfoActivity.this, "请完善手机号");
            return;
        }

        showLoadingDialog("请求中...");
        OkHttpUtils.post()
                .url(Api.EDIT_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("name", tvShopName.getText().toString().trim())
                .addParams("mainbusiness", tvSaleType.getText().toString().trim())
                .addParams("week1", day1)
                .addParams("week2", day2)
                .addParams("time1", time1)
                .addParams("time2", time2)
                .addParams("address", tvShopAddress.getText().toString().trim())
                .addParams("tel", etPhone.getText().toString().trim())
                .addParams("lat", latitude)
                .addParams("lng", longitude)
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
                                finish();
                            }
                        }
                    }
                });

    }

    private void choicePhotoWrapper2() {
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
                    Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(mContext)
                            .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                            .maxChooseCount(1) // 图片选择张数的最大值
                            .selectedPhotos(null) // 当前已选中的图片路径集合
                            .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                            .build();
                    startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO2);
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
                    Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(mContext)
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
                .url(Api.SHOP_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
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
                        ShopInfoResponse resp = JsonUtils.parseByGson(response, ShopInfoResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                if (resp.getData() != null) {
                                    //请求成功
                                    Glide.with(mContext).load(AppConfig.BASE_URL + resp.getData().getLogo()).into(logo);
                                    tvShopName.setText(resp.getData().getName());
                                    tvSaleType.setText(resp.getData().getMainbusiness());
                                    day1 = resp.getData().getWeek1();
                                    day2 = resp.getData().getWeek2();
                                    tvday.setText(day1 + "--" + day2);
                                    time1 = resp.getData().getTime1();
                                    time2 = resp.getData().getTime2();

                                    tvWorkTime.setText(time1 + "--" + time2);
                                    tvShopAddress.setText(resp.getData().getAddress());
                                    etPhone.setText(resp.getData().getTel());
                                    latitude=resp.getData().getLat();
                                    longitude=resp.getData().getLng();
                                    address=resp.getData().getAddress();

                                    List<ShopInfoResponse.DataBean.ItemBean> itemBeans = resp.getData().getAttachList();
                                    if (itemBeans != null && itemBeans.size() > 0) {
                                        ShopInfoResponse.DataBean.ItemBean item = itemBeans.get(itemBeans.size()-1);
                                        Glide.with(ShopInfoActivity.this).load(AppConfig.BASE_URL + item.getAttach()).into(iv);
                                    }
                                }
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取商户信息失败");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                case RC_CHOOSE_PHOTO2:
                    //商铺图片返回
                    if (data != null) {
                        List<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
                        if (selectedPhotos != null && selectedPhotos.size() > 0) {
                            //拍照返回
                            try {
                                startActivityForResult(mPhotoHelper.getCropIntent(selectedPhotos.get(0), 400, 200), REQUEST_CODE_CROP2);
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
                    String path = ImageUtils.compress(mContext, System.currentTimeMillis() + "", new File(cropPath), AppConfig.PHOTO_PATH);
                    //裁剪后的
                    EditShopLogo(path);
                    break;
                case REQUEST_CODE_CROP2:
                    String p = ImageUtils.compress(mContext, System.currentTimeMillis() + "", new File(mPhotoHelper.getCropFilePath()), AppConfig.PHOTO_PATH);
                    //裁剪后的
                    uploadImg(p);
                    break;
                case 0x03:
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    detailAddress = data.getStringExtra("detailAddress");
                    tvShopAddress.setText(detailAddress);
                    break;
                default:
                    break;
            }
        }
    }

    private void EditShopLogo(String path) {
        if (TextUtils.isEmpty(path)) {
            showToast("店铺Logo不能为空");
            return;
        }
        File file = new File(path);
        OkHttpUtils.post()
                .url(Api.UPLOAD_SHOP_LOGO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addFile("img", file.getName(), file)
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
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                Glide.with(mContext).load(new File(path)).into(logo);
                            }
                        }
                    }
                });

    }
}
