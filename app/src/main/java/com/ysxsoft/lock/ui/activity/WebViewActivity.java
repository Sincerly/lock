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
import com.ysxsoft.common_base.utils.WebViewUtils;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/main/WebViewActivity")
public class WebViewActivity extends BaseActivity {
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
    @BindView(R.id.webView)
    WebView webView;
    @Autowired
    String str;
    @Autowired
    String url;

    public static void start(String str,String url) {
        ARouter.getInstance().build(ARouterPath.getWebViewActivity())
                .withString("str",str)
                .withString("url",url)
                .navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
        WebViewUtils.init(webView);
        webView.loadUrl(url);
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.transparent));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_white_back);
        title.setText(str==null?"":str);
        title.setTextColor(getResources().getColor(R.color.colorWhite));
    }
}
