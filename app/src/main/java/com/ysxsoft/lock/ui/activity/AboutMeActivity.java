package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.WebViewUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.AboutMeResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 关于我们
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/AboutMeActivity")
public class AboutMeActivity extends BaseActivity {
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

    @BindView(R.id.webview)
    WebView webview;

    @Autowired
    String webUrl;
    @Autowired
    String webContent;
    public static void start() {
        ARouter.getInstance().build(ARouterPath.getAboutMeActivity()).navigation();
    }


    public static void start(String url) {
        ARouter.getInstance().build(ARouterPath.getAboutMeActivity()).withString("webUrl", url).navigation();
    }

    public static void startUrl(String title, String url) {
        ARouter.getInstance().build(ARouterPath.getAboutMeActivity()).withString("webTitle", title).withString("webUrl", url).navigation();
    }

    public static void startContent(String title, String content) {
        ARouter.getInstance().build(ARouterPath.getAboutMeActivity()).withString("webTitle", title).withString("webContent", content).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_me;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initWebView();
    }

    private void initWebView() {
        WebViewUtils.init(webview);
        if (webUrl != null) {
            webview.loadUrl(webUrl);
        } else {
            if (webContent != null) {
                WebViewUtils.setH5Data(webview, webContent);
            } else {
                //自定义处理
                onCustom(webview);
            }
        }
    }

    private void onCustom(WebView webview) {


    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("关于我们");
    }

    @OnClick(R.id.backLayout)
    public void onViewClicked() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        backToActivity();
    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
                .url(Api.GET_ABOUT_ME)
                .addParams("uid", SharedPreferencesUtils.getUid(AboutMeActivity.this))
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
                        AboutMeResponse resp = JsonUtils.parseByGson(response, AboutMeResponse.class);
                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<AboutMeResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
                        } else {
                            showToast("获取关于我们失败");
                        }
                    }
                });
    }
}
