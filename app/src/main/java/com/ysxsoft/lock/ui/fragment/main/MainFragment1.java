package com.ysxsoft.lock.ui.fragment.main;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.custom.piehead.PieLayout;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.ActionResponse;
import com.ysxsoft.lock.models.response.IsAuthResponse;
import com.ysxsoft.lock.models.response.PersonCenterResponse;
import com.ysxsoft.lock.models.response.ShopCertResponse;
import com.ysxsoft.lock.models.response.ShopInfoResponse;
import com.ysxsoft.lock.models.response.UserInfoResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.FeedBackActivity;
import com.ysxsoft.lock.ui.activity.IdcardCertActivity;
import com.ysxsoft.lock.ui.activity.IdcardCertFailedActivity;
import com.ysxsoft.lock.ui.activity.KeyManagerActivity;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.activity.PropertyCertActivity;
import com.ysxsoft.lock.ui.activity.SettingActivity;
import com.ysxsoft.lock.ui.activity.ShopAuditFailedActivity;
import com.ysxsoft.lock.ui.activity.ShopAuthenticationActivity;
import com.ysxsoft.lock.ui.activity.ShopCenterActivity;
import com.ysxsoft.lock.ui.activity.ShopEgisActivity;
import com.ysxsoft.lock.ui.activity.ShopInfoActivity;
import com.ysxsoft.lock.ui.activity.ShopListActivity;
import com.ysxsoft.lock.ui.activity.ShopManagerActivity;
import com.ysxsoft.lock.ui.activity.StatusActivity;
import com.ysxsoft.lock.ui.activity.UserInfoActivity;
import com.ysxsoft.lock.ui.dialog.CertificationDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 个人中心fragment
 */
public class MainFragment1 extends BaseFragment {
    @BindView(R.id.statusBar2)
    View statusBar2;
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
    @BindView(R.id.civ)
    CircleImageView civ;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.tvL1)
    TextView tvL1;
    @BindView(R.id.tvL2)
    TextView tvL2;
    @BindView(R.id.tvL3)
    TextView tvL3;
    @BindView(R.id.tvL4)
    TextView tvL4;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.pie)
    PieLayout pie;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.cL1)
    ConstraintLayout cL1;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv9)
    TextView tv9;
    @BindView(R.id.tv10)
    TextView tv10;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;
    @BindView(R.id.LL3)
    LinearLayout LL3;
    @BindView(R.id.LL4)
    LinearLayout LL4;

    private List<String> approveList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_1;
    }

    @Override
    protected void doWork(View view) {
        bg.setBackgroundColor(getResources().getColor(R.color.transparent));
        backLayout.setVisibility(View.VISIBLE);
        bottomLineView.setVisibility(View.GONE);
        back.setImageResource(R.mipmap.icon_white_back);
        title.setText("个人中心");
        title.setTextColor(getResources().getColor(R.color.colorWhite));
        statusBar2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtils.getStatusBarHeight(getActivity())));
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPersonData();
    }

    private void requestPersonData() {
        OkHttpUtils.post()
                .url(Api.PERSON_DATA)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
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
                                tvL1.setText(resp.getData().getCard1());
                                tvL2.setText(resp.getData().getCard2());
                                tvL3.setText(resp.getData().getCard3());
                                tvL4.setText(resp.getData().getCard4());
                                if(resp.getData().getIcon()!=null){
                                    Glide.with(getActivity()).load(AppConfig.BASE_URL+resp.getData().getIcon()).into(civ);
                                }else{
                                    Glide.with(getActivity()).load(R.mipmap.icon_app_logo).into(civ);
                                }
                                tv1.setText(resp.getData().getNickname());
                                tv2.setText(resp.getData().getAutograph());
                            }
                        }
                    }
                });
    }

    private void IsAuth() {
        OkHttpUtils.post()
                .url(Api.IS_AUTH)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        IsAuthResponse resp = JsonUtils.parseByGson(response, IsAuthResponse.class);
                        if (resp != null) {
                            switch (resp.getCode()) {
                                case "200"://已实名认证
                                    StatusActivity.start("实名认证",2,0);
                                    break;
                                case "201"://审核中
                                    StatusActivity.start("实名认证",0,0);
                                    break;
                                case "202"://审核失败
//                                    IdcardCertFailedActivity.start();
                                    StatusActivity.start("实名认证",1,0);
                                    break;
                                case "203"://是否认证
                                    CertificationDialog.show(getActivity(), new CertificationDialog.OnDialogClickListener() {
                                        @Override
                                        public void sure() {
                                            IdcardCertActivity.start();
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
    }

    private void initData() {
        approveList = new ArrayList<>();
        approveList.add("http://img2.imgtn.bdimg.com/it/u=1939271907,257307689&fm=21&gp=0.jpg");
        approveList.add("http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg");
        approveList.add("http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg");
        pie.setFlag(true);
        pie.setUrls(approveList);
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.transparent));
        backLayout.setVisibility(View.VISIBLE);
        bottomLineView.setVisibility(View.GONE);
        back.setImageResource(R.mipmap.icon_white_back);
        title.setText("个人中心");
        title.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @OnClick({R.id.backLayout, R.id.tv10, R.id.tv9, R.id.tv8, R.id.tv7, R.id.tv6, R.id.tv5, R.id.tv1, R.id.tv2, R.id.iv1, R.id.LL1, R.id.LL2, R.id.LL3, R.id.LL4, R.id.cL1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                MainActivity activity= (MainActivity) getActivity();
                activity.toTab(0);
                break;
            case R.id.LL1:
                PacketActivity.start(0);
                break;
            case R.id.LL2:
                PacketActivity.start(1);
                break;
            case R.id.LL3:
                PacketActivity.start(2);
                break;
            case R.id.LL4:
                PacketActivity.start(3);
                break;
            case R.id.tv5:
                KeyManagerActivity.start();
                break;
            case R.id.tv6:
                IsAuth();
                break;
            case R.id.tv7:
                //物业中心
                getWuYeStatus();
                break;
            case R.id.tv8:
                //商户中心
                IsPersonAuth();//先判断有没有实名认证成功
//                IsShopCert();
                break;
            case R.id.tv9://广告中心
                //showToast("广告中心");
                //ShopInfoActivity.start();
                break;
            case R.id.tv10:
                FeedBackActivity.start();
                break;
            case R.id.iv1:
            case R.id.civ:
            case R.id.tv1:
            case R.id.tv2:
                SettingActivity.start();
                break;
            case R.id.cL1:
                ShopListActivity.start();
                break;
        }
    }

    public void getWuYeStatus() {
        showLoadingDialog("请求中");
        OkHttpClient httpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type","2");//1=人脸识别 2=物业认证
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
                             hideLoadingDialog();
                             ActionResponse resp = JsonUtils.parseByGson(response.body().string(), ActionResponse.class);
                             if (resp != null) {
                                 if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                     //请求成功
                                     switch (resp.getData()){//0=未提交认证 1=通过 2=审核中 3=审核失败
                                         case "0":
                                             PropertyCertActivity.start();
                                             break;
                                         case "1":
                                             StatusActivity.start("物业认证",2,1);
                                             break;
                                         case "2":
                                             StatusActivity.start("物业认证",0,1);
                                             break;
                                         case "3":
                                             StatusActivity.start("物业认证",resp.getMsg(),1,1);
                                             break;
                                     }
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

    private void IsPersonAuth() {
        OkHttpUtils.post()
                .url(Api.IS_AUTH)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        IsAuthResponse resp = JsonUtils.parseByGson(response, IsAuthResponse.class);
                        if (resp != null) {
                            switch (resp.getCode()) {
                                case "200":
                                    IsShopCert();
                                    break;
                                case "201":
                                    StatusActivity.start("实名认证",0,0);
                                    break;
                                case "202":
                                    StatusActivity.start("实名认证",1,0);
                                    break;
                                case "203":
                                    CertificationDialog.show(getActivity(), new CertificationDialog.OnDialogClickListener() {
                                        @Override
                                        public void sure() {
                                            IdcardCertActivity.start();
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
    }

    //是否通过商户认证
    private void IsShopCert() {
        OkHttpUtils.post()
                .url(Api.IS_SHOP_CERT)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ShopCertResponse resp = JsonUtils.parseByGson(response, ShopCertResponse.class);
                        if (resp != null) {
                            //showToast(resp.getMsg());
                            switch (resp.getCode()) {
                                case "200"://返回商户认证信息
                                    ShopManagerActivity.start();
                                    break;
                                case "201"://审核中
                                    StatusActivity.start("商户认证",0,2);
//                                    ShopEgisActivity.start("商户认证");
                                    break;
                                case "202"://审核失败
                                    StatusActivity.start("商户认证",resp.getMsg(),1,2);
//                                    ShopAuditFailedActivity.start("商户认证");
                                    break;
                                case "203"://未申请认证
                                    ShopCenterActivity.start();
                                    break;
                            }
                        } else {
                            showToast("获取商户认证失败");
                        }
                    }
                });
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_USER_INFO)
                .addParams("uid", SharedPreferencesUtils.getUid(getActivity()))
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
                        UserInfoResponse resp = JsonUtils.parseByGson(response, UserInfoResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<UserInfoResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取我的失败");
                        }
                    }
                });
    }
}

