package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.custom.piehead.PieLayout;
import com.ysxsoft.lock.ui.dialog.CertificationDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.UserInfoResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/UserInfoActivity")
public class UserInfoActivity extends BaseActivity {
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

    @BindView(R.id.pie)
    PieLayout pie;

    @BindView(R.id.civ)
    CircleImageView civ;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;
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


    private List<String> approveList;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getUserInfoActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void doWork() {
        super.doWork();
        CertificationDialog.show(mContext, new CertificationDialog.OnDialogClickListener() {
            @Override
            public void sure() {
                ApplyKeyActivity.start();
            }
        });
        initTitle();
        initData();
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

    @OnClick({R.id.backLayout, R.id.tv10, R.id.tv9, R.id.tv8, R.id.tv7, R.id.tv6, R.id.tv5, R.id.tv1, R.id.tv2, R.id.iv1, R.id.cL1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv5:
                KeyManagerActivity.start();
                break;
            case R.id.tv6:
                IdcardCertActivity.start();
                break;
            case R.id.tv7:
                PropertyCertActivity.start();
                break;
            case R.id.tv8:
                ShopCenterActivity.start();
                break;
            case R.id.tv9:
//                ShopInfoActivity.start();
                ShopManagerActivity.start();
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

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_USER_INFO)
                .addParams("uid", SharedPreferencesUtils.getUid(UserInfoActivity.this))
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
