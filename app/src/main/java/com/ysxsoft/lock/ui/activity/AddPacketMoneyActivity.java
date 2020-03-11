package com.ysxsoft.lock.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.alibaba.android.arouter.facade.annotation.Autowired;
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
import com.ysxsoft.lock.models.response.CardDetailResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.PacketTypeSelectDialog;
import com.ysxsoft.lock.view.TextOrPicLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPacketMoneyResponse;
import com.ysxsoft.lock.net.Api;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textOrPicLayout)
    TextOrPicLayout textOrPicLayout;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    private int typeClick = 0;

    private BGAPhotoHelper mPhotoHelper;
    private RxPermissions r;
    private static final int RC_CHOOSE_PHOTO = 0x01;
    public static final int REQUEST_CODE_CROP = 0x02;
    private String path;

    @Autowired
    int defaultPage;//0现金券 1团购套餐 2免费体验 3会员卡

    @Autowired
    String cardId;
    @Autowired
    String money1;
    @Autowired
    String money2;
    @Autowired
    String pic;
    @Autowired
    String name;
    @Autowired
    String zhekou;
    @Autowired
    boolean canEdit;

    public static void start(int page) {
        ARouter.getInstance().build(ARouterPath.getAddPacketMoneyActivity()).withInt("defaultPage", page).navigation();
    }

    public static void start(int page, boolean canEdit, String cardId, String money1, String money2, String pic, String name, String zhekou) {
        ARouter.getInstance().build(ARouterPath.getAddPacketMoneyActivity())
                .withInt("defaultPage", page)
                .withString("cardId", cardId)
                .withString("money1", money1)
                .withString("money2", money2)
                .withString("pic", pic)
                .withString("name", name)
                .withString("zhekou", zhekou)
                .withBoolean("canEdit", canEdit)
                .navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_packet_money;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        typeClick = defaultPage;
        initTitle();
        initPhotoHelper();
        textOrPicLayout.attachActivity(this);
        textOrPicLayout.attachRecyclerView(recyclerView);

        refreshUI(defaultPage);
        if (cardId != null) {
            getDetail();
        }
    }

    public void getDetail() {
        showLoadingDialog("请求中");
        OkHttpUtils.get()
                .url(Api.CARD_INFO + "?id=" + cardId)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        Log.e("tag", "onError" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        Log.e("tag", "" + response);
                        CardDetailResponse resp = JsonUtils.parseByGson(response, CardDetailResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                CardDetailResponse.DataBean data = resp.getData();

                                Glide.with(AddPacketMoneyActivity.this).load(AppConfig.BASE_URL+data.getImg()).into(iv);
                                money1 = "" + data.getPrice();
                                money2 = "" + data.getOprice();
                                pic = AppConfig.BASE_URL + data.getImg();
                                name = data.getTitle();
                                zhekou = data.getPrice() + "";
                                //编辑
                                etYhq.setText(money1);
                                etUseTJ.setText(money2);
                                etName.setText(name);
                                if (!canEdit) {
                                    etYhq.setEnabled(false);
                                    etUseTJ.setEnabled(false);
                                    etName.setEnabled(false);
                                }
                                if (defaultPage == 3) {
                                    //会员卡
                                    etYhq.setText(name);
                                    etUseTJ.setText(zhekou);
                                }

                                String json = data.getRemark_txt();
                                try {
                                    JSONArray jsonArray=new JSONArray(json);
                                    List<TextOrPicLayout.ItemData> itemData = new ArrayList<>();

                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        TextOrPicLayout.ItemData bean=new TextOrPicLayout.ItemData();
                                        bean.setP(i);//下标
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        String type=jsonObject.getString("type");
                                        String da=jsonObject.getString("data");
                                        bean.setPicPath(da);
                                        if("1".equals(type)){
                                            //图片
                                            bean.setType(1);
                                            bean.setContent(da);//图片path
                                        }else{
                                            //文本
                                            bean.setContent(da);//图片path
                                            bean.setType(0);
                                        }
                                        itemData.add(bean);
                                    }
                                    textOrPicLayout.setData(itemData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取关于我们失败");
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
                if(canEdit) {
                    choicePhotoWrapper();
                }
                break;
            case R.id.tvMoney:
                if(cardId==null){
                    PacketTypeSelectDialog.show(mContext, typeClick, new PacketTypeSelectDialog.OnDialogClickListener() {
                        @Override
                        public void sure(String data1, int type) {
                            typeClick = type;
                            tvMoney.setText(data1);

                            refreshUI(type);
                        }
                    });
                }
                break;
            case R.id.tvOk:
                if (cardId != null) {
                    //编辑
                    edit();
                } else {
                    //新增
                    submintData();
                }
                break;
        }
    }

    private void refreshUI(int type) {
        switch (type) {
            case 0:
                tvMoney.setText("现金券");
                tv1Name.setText("券的面额");
                tv2Name.setText("使用条件");
                tv3Name.setText("现金券名称");
                tv4Name.setText("活动详情");

                etYhq.setHint("优惠券金额");
                etUseTJ.setHint("用券最低订单金额，0元则全场通用");
                etName.setHint("请输入名称");
                etInputRules.setHint("请写下使用规则…");
                etInputRules.setVisibility(View.GONE);
                LL1.setVisibility(View.VISIBLE);
                LL2.setVisibility(View.VISIBLE);
                tv4Name.setVisibility(View.VISIBLE);
                cl1.setVisibility(View.VISIBLE);
                textOrPicLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvMoney.setText("团购套餐");
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
                textOrPicLayout.setVisibility(View.VISIBLE);

                break;
            case 2:
                tvMoney.setText("免费体验");
                tv1Name.setText("活动价格");
                tv2Name.setText("活动原价");
                tv3Name.setText("活动名");
                tv4Name.setText("活动详情");

                etYhq.setHint("优惠金额");
                etUseTJ.setHint("活动金额");
                etName.setHint("请输入名称");
                etInputRules.setHint("请写下详情…");
                LL1.setVisibility(View.VISIBLE);
                LL2.setVisibility(View.VISIBLE);
                tv4Name.setVisibility(View.VISIBLE);
                cl1.setVisibility(View.VISIBLE);
                textOrPicLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvMoney.setText("会员卡");
                tv1Name.setText("会员卡名称");
                tv2Name.setText("享受折扣");

                etYhq.setHint("请输入名称");
                etUseTJ.setHint("请输入享受折扣，如1,2,3");
                LL1.setVisibility(View.GONE);
                LL2.setVisibility(View.GONE);
                tv4Name.setVisibility(View.GONE);
                cl1.setVisibility(View.GONE);
                textOrPicLayout.setVisibility(View.GONE);
                break;
        }
        progressBar.setVisibility(View.GONE);
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
        OkHttpClient httpClient = new OkHttpClient();
        //RequestBody requestBody = RequestBody.create(mediaType, file);//把文件与类型放入请求体
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type", String.valueOf(typeClick + 1));//1=现金券 2=团购套餐 3=体验套餐 4=会员卡
        builder.addFormDataPart("collar", "1");//限领数量
        builder.addFormDataPart("ramd", textOrPicLayout.getStr());//4位随机数

        switch (typeClick) {
            case 0://现金券
            case 1://团购套餐
            case 2://体验套餐
                if (TextUtils.isEmpty(path)) {
                    showToast("主图不能为空");
                    hideLoadingDialog();
                    return;
                }
                File file = new File(path);
                builder.addFormDataPart("price", etYhq.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                builder.addFormDataPart("oprice", etUseTJ.getText().toString().trim());//	现金券 使用条件、套餐原价、体验套餐原价 type=1,2,3必填
                builder.addFormDataPart("title", etName.getText().toString().trim());//卡券、套餐名称
//                builder.addFormDataPart("remark", etInputRules.getText().toString().trim());//详情（或规则 ）或 会员卡有效期
                builder.addFormDataPart("remark", textOrPicLayout.getData());//详情（或规则 ）或 会员卡有效期

                builder.addFormDataPart("cardimg", file.getName(), RequestBody.create(MultipartBody.FORM, file));//文件名,请求体里的文件

                break;
            case 3://会员卡
                builder.addFormDataPart("remark", "");
                builder.addFormDataPart("price", etUseTJ.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                builder.addFormDataPart("title", etYhq.getText().toString().trim());
                break;
        }

        Request request = new Request.Builder()
                .header("Authorization", SharedPreferencesUtils.getToken(mContext))//添加请求头的身份认证Token
                .url(Api.ADD_CARD)
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
                             hideLoadingDialog();
                             String string = response.body().string();
                             Log.e("TAG-成功：", string + "");
                             CommentResponse resp = JsonUtils.parseByGson(string, CommentResponse.class);
                             if (resp != null) {
                                 if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                     //请求成功
                                     finish();
                                 } else {
                                     //请求失败
                                     new Handler(Looper.getMainLooper()).post(new Runnable() {
                                         @Override
                                         public void run() {
                                             showToast(resp.getMsg());
                                         }
                                     });
                                 }
                             }
                         }
                     }
        );
    }

    private void edit() {
        showLoadingDialog("请求中");
        OkHttpClient httpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type", String.valueOf(typeClick + 1));//1=现金券 2=团购套餐 3=体验套餐 4=会员卡
        builder.addFormDataPart("collar", "1");//限领数量
        builder.addFormDataPart("ramd", textOrPicLayout.getStr());//4位随机数
        builder.addFormDataPart("id", cardId);

        if (typeClick == 3) {
        } else {
            if (TextUtils.isEmpty(path)&&cardId==null) {
                showToast("主图不能为空");
                hideLoadingDialog();
                return;
            }
        }
        switch (typeClick) {
            case 0://现金券
            case 1://团购套餐
            case 2://体验套餐

                builder.addFormDataPart("price", etYhq.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                builder.addFormDataPart("oprice", etUseTJ.getText().toString().trim());//	现金券 使用条件、套餐原价、体验套餐原价 type=1,2,3必填
                builder.addFormDataPart("title", etName.getText().toString().trim());//卡券、套餐名称
//                builder.addFormDataPart("remark", etInputRules.getText().toString().trim());//详情（或规则 ）或 会员卡有效期
                builder.addFormDataPart("remark", textOrPicLayout.getData());//详情（或规则 ）或 会员卡有效期

                if(path!=null){
                    File file = new File(path);
                    builder.addFormDataPart("cardimg", file.getName(), RequestBody.create(MultipartBody.FORM, file));//文件名,请求体里的文件
                }

                break;
            case 3://会员卡
                builder.addFormDataPart("remark", "");
                builder.addFormDataPart("price", etUseTJ.getText().toString().trim());//现金券 券面额 、套餐价格、体验套餐价格、会员卡折扣
                builder.addFormDataPart("title", etYhq.getText().toString().trim());
                break;
        }

        Request request = new Request.Builder()
                .header("Authorization", SharedPreferencesUtils.getToken(mContext))//添加请求头的身份认证Token
                .url(Api.EDIT_CARD)
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
                             hideLoadingDialog();
                             String string = response.body().string();
                             Log.e("TAG-成功：", string + "");
                             CommentResponse resp = JsonUtils.parseByGson(string, CommentResponse.class);
                             if (resp != null) {
                                 if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                     //请求成功
                                     finish();
                                 } else {
                                     //请求失败
                                     new Handler(Looper.getMainLooper()).post(new Runnable() {
                                         @Override
                                         public void run() {
                                             showToast(resp.getMsg());
                                         }
                                     });
                                 }
                             }
                         }
                     }
        );
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
