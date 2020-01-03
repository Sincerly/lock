package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.ImageUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.PacketTypeSelectDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPacketMoneyResponse;
import com.ysxsoft.lock.net.Api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * 添加卡卷现金券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AddPacketMoneyActivity")
public class AddPacketMoneyActivity extends BaseActivity {
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

    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.etYhq)
    EditText etYhq;
    @BindView(R.id.etUseTJ)
    EditText etUseTJ;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etInputRules)
    EditText etInputRules;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tvOk)
    TextView tvOk;

    @BindView(R.id.tv1Name)
    TextView tv1Name;
    @BindView(R.id.tv2Name)
    TextView tv2Name;
    @BindView(R.id.tv3Name)
    TextView tv3Name;
    @BindView(R.id.tv4Name)
    TextView tv4Name;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    private int typeClick = 0;

    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    public static final int REQUEST_CODE_CROP = 0x02;
    private String path;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getAddPacketMoneyActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_packet_money;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initPhotoHelper();
        request();
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
        title.setText("添加卡卷");

        etInputRules.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv1.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.backLayout, R.id.tvMoney, R.id.LL1, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                choicePhotoWrapper();
                break;

            case R.id.tvMoney:
                PacketTypeSelectDialog.show(mContext, typeClick, new PacketTypeSelectDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String data1, int type) {
                        typeClick = type;
                        tvMoney.setText(data1);
                        switch (type) {
                            case 0:
                                tv1Name.setText("券的面额");
                                tv2Name.setText("使用条件");
                                tv3Name.setText("现金券名称");
                                tv4Name.setText("现金券规则");

                                etYhq.setHint("优惠券金额");
                                etUseTJ.setHint("用券最低订单金额，0元则全场通用");
                                etName.setHint("请输入名称");
                                etInputRules.setHint("请写下使用规则…");
                                LL1.setVisibility(View.GONE);
                                LL2.setVisibility(View.VISIBLE);
                                tv4Name.setVisibility(View.VISIBLE);
                                cl1.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                tv1Name.setText("套餐价格");
                                tv2Name.setText("套餐原价");
                                tv3Name.setText("套餐名");
                                tv4Name.setText("套餐详情");

                                etYhq.setHint("优惠金额");
                                etUseTJ.setHint("套餐金额");
                                etName.setHint("请输入套餐名");
                                etInputRules.setHint("请写下详情…");
                                LL1.setVisibility(View.VISIBLE);
                                LL2.setVisibility(View.VISIBLE);
                                tv4Name.setVisibility(View.VISIBLE);
                                cl1.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                tv1Name.setText("套餐价格");
                                tv2Name.setText("套餐原价");
                                tv3Name.setText("套餐名");
                                tv4Name.setText("套餐详情");

                                etYhq.setHint("优惠金额");
                                etUseTJ.setHint("套餐金额");
                                etName.setHint("请输入套餐名");
                                etInputRules.setHint("请写下详情…");
                                LL1.setVisibility(View.VISIBLE);
                                LL2.setVisibility(View.VISIBLE);
                                tv4Name.setVisibility(View.VISIBLE);
                                cl1.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                tv1Name.setText("会员卡名称");
                                tv2Name.setText("享受折扣");

                                etYhq.setHint("请输入名称");
                                etUseTJ.setHint("请输入享受折扣，如1,2,3");
                                LL1.setVisibility(View.GONE);
                                LL2.setVisibility(View.GONE);
                                tv4Name.setVisibility(View.GONE);
                                cl1.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
                break;
            case R.id.tvOk:
                submintData();
                break;
        }
    }

    @SuppressLint("CheckResult")
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

    private void submintData() {
        showLoadingDialog("请求中");
        PostFormBuilder formBuilder = OkHttpUtils.post().url(Api.ADD_CARD);
        formBuilder.addHeader("Authorization", SharedPreferencesUtils.getToken(mContext));
        formBuilder.addParams("type", String.valueOf(typeClick + 1));//1=现金券 2=团购套餐 3=体验套餐 4=会员卡

        switch (typeClick) {
            case 0://现金券
                formBuilder.addParams("price", etYhq.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                formBuilder.addParams("oprice", etUseTJ.getText().toString().trim());//	现金券 使用条件、套餐原价、体验套餐原价 type=1,2,3必填
                formBuilder.addParams("title", etName.getText().toString().trim());//卡券、套餐名称
                formBuilder.addParams("remark", etInputRules.getText().toString().trim());//详情（或规则 ）或 会员卡有效期
                break;
            case 1://团购套餐
            case 2://体验套餐
                if (TextUtils.isEmpty(path)){
                    showToast("主图不能为空");
                    return;
                }
                File file = new File(path);
                formBuilder.addParams("price", etYhq.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                formBuilder.addParams("oprice", etUseTJ.getText().toString().trim());//	现金券 使用条件、套餐原价、体验套餐原价 type=1,2,3必填
                formBuilder.addParams("title", etName.getText().toString().trim());//卡券、套餐名称
                formBuilder.addParams("remark", etInputRules.getText().toString().trim());//详情（或规则 ）或 会员卡有效期
                formBuilder.addFile("cardimg",file.getName(),file);
                break;
            case 3://会员卡
                formBuilder.addParams("price", etUseTJ.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                formBuilder.addParams("title", etYhq.getText().toString().trim());
                break;
        }
        formBuilder.tag(this)
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
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                finish();
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        }
                    }
                });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ADD_PACKET_MONEY)
                .addParams("uid", SharedPreferencesUtils.getUid(AddPacketMoneyActivity.this))
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
                        AddPacketMoneyResponse resp = JsonUtils.parseByGson(response, AddPacketMoneyResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AddPacketMoneyResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取添加卡卷现金券失败");
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
                    path = ImageUtils.compress(this, System.currentTimeMillis() + "", new File(cropPath), AppConfig.PHOTO_PATH);
                    Glide.with(mContext).load(path).into(iv);
                    break;
                default:
                    break;
            }
        }
    }
}
