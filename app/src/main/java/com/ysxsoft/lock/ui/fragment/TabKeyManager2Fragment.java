package com.ysxsoft.lock.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.ImageUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.WebViewUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.AboutMeResponse;
import com.ysxsoft.lock.models.response.ActionResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.models.response.resp.FaceResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.PropertyCertActivity;
import com.ysxsoft.lock.ui.activity.StatusActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create By 胡
 * on 2019/12/16 0016
 */
public class TabKeyManager2Fragment extends BaseFragment {
    @BindView(R.id.riv)
    RoundImageView riv;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.LL1)
    LinearLayout LL1;//未认证
    @BindView(R.id.LL2)
    LinearLayout LL2;//认证成功
    @BindView(R.id.LL3)
    LinearLayout LL3; //审核中
    @BindView(R.id.LL4)
    LinearLayout LL4;//失败

    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    public static final int REQUEST_CODE_CROP = 0x02;
    private String path;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabkeymanager2;
    }

    @Override
    protected void doWork(View view) {
        initPhotoHelper();
        request();
    }

    @Override
    public void onResume() {
        super.onResume();
//        getIsface();
    }

    private void getIsface() {
        OkHttpUtils.post()
                .url(Api.IS_FACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        FaceResponse resp = JsonUtils.parseByGson(response, FaceResponse.class);
                        if (resp != null) {
                            switch (resp.getCode()) {
                                case 200:// 返回人脸认证信息
                                    LL1.setVisibility(View.GONE);
                                    LL2.setVisibility(View.VISIBLE);
                                    LL3.setVisibility(View.GONE);
                                    break;
                                case 201://  审核中
                                    LL1.setVisibility(View.GONE);
                                    LL2.setVisibility(View.GONE);
                                    LL3.setVisibility(View.VISIBLE);
                                    break;
                                case 202://审核失败
                                case 203://未申请认证
                                    LL1.setVisibility(View.VISIBLE);
                                    LL2.setVisibility(View.GONE);
                                    LL3.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
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

    @OnClick({R.id.tv2, R.id.tv3, R.id.tv1,})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                break;
            case R.id.tv2:
                choicePhotoWrapper();
                break;
            case R.id.tv3:
                submitData();
                break;

        }
    }

    private void submitData() {
        if (TextUtils.isEmpty(path)) {
            showToast("照片不能为空");
            return;
        }
        File file = new File(path);
        OkHttpUtils.post()
                .url(Api.UPLOAD_IMG)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addFile("faceimg", file.getName(), file)
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
//                                getIsface();
                                LL1.setVisibility(View.GONE);
                                request();
                            }
                        }
                    }
                });

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
                    Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(getActivity())
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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
                case REQUEST_CODE_CROP:
                    String cropPath = mPhotoHelper.getCropFilePath();
                    path = ImageUtils.compress(getActivity(), System.currentTimeMillis() + "", new File(cropPath), AppConfig.PHOTO_PATH);
                    //裁剪后的
                    riv.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(new File(path)).into(riv);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 用户人脸认证状态
     */
    public void request() {
        showLoadingDialog("请求中");
        OkHttpClient httpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type", "1");//1=人脸识别 2=物业认证
        Request request = new Request.Builder()
                .header("Authorization", SharedPreferencesUtils.getToken(getActivity()))//添加请求头的身份认证Token
                .url(Api.MEMBER_STATUS)
                .post(builder.build())
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             hideLoadingDialog();
                             Log.e("TAG-失败：", e.toString() + "");
                         }

                         @Override
                         public void onResponse(Call call, Response response) throws IOException {
                             new Handler(Looper.getMainLooper()).post(new Runnable() {
                                 @Override
                                 public void run() {
                                     hideLoadingDialog();
                                 }
                             });
                             ActionResponse resp = null;
                             try {
                                 resp = JsonUtils.parseByGson(response.body().string(), ActionResponse.class);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             if (resp != null) {
                                 if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                     ActionResponse finalResp = resp;
                                     getActivity().runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             switch (finalResp.getData()) {//0=未提交认证 1=通过 2=审核中 3=审核失败
                                                 case "0"://未提交审核
                                                     LL1.setVisibility(View.VISIBLE);
                                                     LL2.setVisibility(View.GONE);
                                                     LL3.setVisibility(View.GONE);
                                                     LL4.setVisibility(View.GONE);
                                                     break;
                                                 case "1"://通过
                                                     LL2.setVisibility(View.VISIBLE);
                                                     LL1.setVisibility(View.GONE);
                                                     LL3.setVisibility(View.GONE);
                                                     LL4.setVisibility(View.GONE);
                                                     break;
                                                 case "2"://审核中
                                                     LL3.setVisibility(View.VISIBLE);
                                                     LL1.setVisibility(View.GONE);
                                                     LL4.setVisibility(View.GONE);
                                                     LL2.setVisibility(View.GONE);
                                                     break;
                                                 case "3"://审核失败
                                                     LL4.setVisibility(View.VISIBLE);
                                                     LL1.setVisibility(View.GONE);
                                                     LL3.setVisibility(View.GONE);
                                                     LL2.setVisibility(View.GONE);
                                                     break;
                                             }
                                         }
                                     });
                                 } else {
                                     //请求失败
                                     showToast(resp.getMsg());
                                 }
                             } else {
                                 showToast("获取用户状态失败");
                             }
                         }
                     }
        );
    }
}
