package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.FeedBackResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 意见反馈
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/FeedBackActivity")
public class FeedBackActivity extends BaseActivity {
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

    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tvOk)
    TextView tvOk;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getFeedBackActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("意见反馈");
        etContent.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.backLayout, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    showToast("反馈内容不能为空");
                    return;
                }
                request();
                break;

        }
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.FEED_BACK)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("msg", etContent.getText().toString().trim())
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
//                        FeedBackResponse resp = JsonUtils.parseByGson(response, FeedBackResponse.class);
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
}
