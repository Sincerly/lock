package com.ysxsoft.lock.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.models.response.VillageResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.ui.dialog.CitySelectDialog;
import com.ysxsoft.lock.ui.dialog.SelectVillageDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AddPlaceResponse;
import com.ysxsoft.lock.net.Api;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 添加新小区
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AddPlaceActivity")
public class AddPlaceActivity extends BaseActivity {
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

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @Autowired
    String flag;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getAddPlaceActivity()).navigation();
    }


    public static void start(Activity activity, int requestCode, String flag) {
        ARouter.getInstance().build(ARouterPath.getAddPlaceActivity()).withString("flag", flag).navigation(activity, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_place;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("添加新小区");
    }

    private String cityCode1;
    private String areaCode1;

    @OnClick({R.id.backLayout, R.id.tvOk, R.id.tv1, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv1:
                CitySelectDialog.show(mContext, new CitySelectDialog.OnDialogClickListener() {
                    @Override
                    public void sure(String proviceName, String proviceCode, String cityName, String cityCode, String areaName, String areaCode) {
                        cityCode1 = cityCode;
                        areaCode1 = areaCode;
                        tv1.setText(proviceName + cityName + areaName);
                    }
                });
                break;
            case R.id.tv2:
                if (TextUtils.isEmpty(cityCode1) || TextUtils.isEmpty(areaCode1)) {
                    showToast("城市选择不能为空");
                    return;
                }
                requestData(cityCode1, areaCode1);
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
                    showToast("城市不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
                    showToast("小区不能为空");
                    return;
                }

                if ("1".equals(flag)) {
                    Intent intent = new Intent();
                    intent.putExtra("requid", requid);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
//                    ApplyKeyActivity.start(requid);
                    addPlaceData();
                }
                break;


        }
    }

    /**
     * 添加小区
     */
    private void addPlaceData() {
        OkHttpUtils.post()
                .url(Api.BIND_PLACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
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
                                finish();
                            }
                        }
                    }
                });
    }

    private String requid;

    private void requestData(String cityCode1, String areaCode1) {
        OkHttpUtils.get()
                .url(Api.GET_ADDRESS_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("citycode", cityCode1)
                .addParams("areacode", areaCode1)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        VillageResponse resp = JsonUtils.parseByGson(response, VillageResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {

                                List<VillageResponse.DataBean> data = resp.getData();
                                SelectVillageDialog dialog = new SelectVillageDialog(mContext, R.style.BottomDialogStyle);
                                dialog.setDatas(data);
                                dialog.setListener(new SelectVillageDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure(String villageName, String villageCode) {
                                        requid = villageCode;
                                        tv2.setText(villageName);
                                    }
                                });
                                dialog.showDialog();
                            }
                        }
                    }
                });


    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ADD_PLACE)
                .addParams("uid", SharedPreferencesUtils.getUid(AddPlaceActivity.this))
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
                        AddPlaceResponse resp = JsonUtils.parseByGson(response, AddPlaceResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AddPlaceResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取添加新小区失败");
                        }
                    }
                });
    }
}
