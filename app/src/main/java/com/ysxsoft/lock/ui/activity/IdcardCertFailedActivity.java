package com.ysxsoft.lock.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ShopAuditFailedResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 商户认证审核失败
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/IdcardCertFailedActivity")
public class IdcardCertFailedActivity extends BaseActivity {
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
    @BindView(R.id.tvTip)
    TextView tvTip;
    @BindView(R.id.tvAgain)
    TextView tvAgain;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getIdcardCertFailedActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_idcard_cert_failed;
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
        title.setText("个人认证");
    }

    @OnClick({R.id.backLayout, R.id.tvAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvAgain:
                IdcardCertActivity.start();
                finish();
                break;
        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_SHOP_AUDIT_FAILED)
                .addParams("uid", SharedPreferencesUtils.getUid(IdcardCertFailedActivity.this))
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
                        ShopAuditFailedResponse resp = JsonUtils.parseByGson(response, ShopAuditFailedResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<ShopAuditFailedResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取商户认证审核失败失败");
                        }
                    }
                });
    }
}
