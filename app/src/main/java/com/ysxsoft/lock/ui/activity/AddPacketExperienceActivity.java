package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.picker.DateYMDPicker;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPacketExperienceResponse;
import com.ysxsoft.lock.net.Api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 * 添加卡卷体验券
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AddPacketExperienceActivity")
public class AddPacketExperienceActivity extends BaseActivity {
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


    @BindView(R.id.tvPacketType)
    TextView tvPacketType;
    @BindView(R.id.etYh)
    EditText etYh;
    @BindView(R.id.etYj)
    EditText etYj;
    @BindView(R.id.etMoneyZL)
    EditText etMoneyZL;
    @BindView(R.id.etLimitNum)
    EditText etLimitNum;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etInputRules)
    EditText etInputRules;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.tvOk)
    TextView tvOk;
    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    public static final int REQUEST_CODE_CROP = 0x02;
    private String path;

    public static void start(){
        ARouter.getInstance().build(ARouterPath.getAddPacketExperienceActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_packet_experience;
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
    }


    @OnClick({R.id.backLayout, R.id.tvPacketType, R.id.tvStartTime, R.id.tvEndTime, R.id.LL1, R.id.ivAdd, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.LL1:
                choicePhotoWrapper();
                break;
            case R.id.ivAdd:
                break;
            case R.id.tvPacketType:
                break;
            case R.id.tvStartTime:
                //时间选择器
                DateYMDPicker dateYMDPicker = new DateYMDPicker();
                dateYMDPicker.init(mContext);
                dateYMDPicker.show(new DateYMDPicker.OnSelectedListener() {
                    @Override
                    public void onSelected(Date date) {
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat3.format(date);
                        tvStartTime.setText(format);
                    }
                });

                break;
            case R.id.tvEndTime:
                DateYMDPicker picker = new DateYMDPicker();
                picker.init(mContext);
                picker.show(new DateYMDPicker.OnSelectedListener() {
                    @Override
                    public void onSelected(Date date) {
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat3.format(date);
                        tvEndTime.setText(format);
                    }
                });
                break;
            case R.id.tvOk:
                submintData();
                break;
        }
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

    private void submintData() {
        if (TextUtils.isEmpty(path)){
            showToast("主图不能为空");
            return;
        }
        File file = new File(path);
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.ADD_CARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("type", "3")//1=现金券 2=团购套餐 3=体验套餐 4=会员卡
                .addParams("price", etYh.getText().toString().trim())
                .addParams("oprice", etYj.getText().toString().trim())
                .addParams("collar", etMoneyZL.getText().toString().trim())
                .addParams("title", etName.getText().toString().trim())
                .addParams("remark", etInputRules.getText().toString().trim())
                .addFile("cardimg",file.getName(),file)
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
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                finish();
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取意见反馈失败");
                        }
                    }
                });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ADD_PACKET_EXPERIENCE)
                .addParams("uid", SharedPreferencesUtils.getUid(AddPacketExperienceActivity.this))
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
                        AddPacketExperienceResponse resp = JsonUtils.parseByGson(response,AddPacketExperienceResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AddPacketExperienceResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取添加卡卷体验券失败");
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
