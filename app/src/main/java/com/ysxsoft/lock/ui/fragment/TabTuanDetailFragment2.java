package com.ysxsoft.lock.ui.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.lock.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.BindView;

/**
 * Create By 胡
 * on 2019/12/17 0017
 */
public class TabTuanDetailFragment2 extends BaseFragment {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_tuandetail;
    }

    @Override
    protected void doWork(View view) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webview.setWebViewClient(new MyWebViewClient());
//        webview.loadDataWithBaseURL(null, getNewContent(url), "text/html", "utf-8", null)
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}
