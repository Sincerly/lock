package com.ysxsoft.lock.ui.activity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.HelpResponse;
import com.ysxsoft.lock.net.Api;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * 帮助
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/HelpActivity")
public class HelpActivity extends BaseActivity implements IListAdapter<HelpResponse.DataBean> {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ListManager<HelpResponse.DataBean> manager;

    public static void start(){
        ARouter.getInstance().build(ARouterPath.getHelpActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void doWork() {
        super.doWork();
        //ARouter.getInstance().inject(this);
        initTitle();
        initList();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_help_activity_list;
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("帮助");
    }

    private void initList() {
        manager = new ListManager(this);
        manager.init(getWindow().findViewById(android.R.id.content));
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //setResult(RESULT_OK, intent);
                //finish();
            }
        });
        manager.getAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(manager.nextPage());
            }
        }, recyclerView);
        request(1);
    }

    @Override
    public void request(int page) {
        if(false){
            debug(manager);
        }else{
            OkHttpUtils.get()
                    .url(Api.HELP_LIST)
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            manager.releaseRefresh();
                            Log.e("tag",e.getMessage()+" ");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            manager.releaseRefresh();
                            Log.e("tag",response+" =");
                            HelpResponse resp = JsonUtils.parseByGson(response, HelpResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
                                    List<HelpResponse.DataBean> data = resp.getData();
                                    manager.setData(data);
                                } else {
                                    //请求失败
                                    showToast(resp.getMsg());
                                }
                            } else {
                                showToast("获取失败");
                            }
                        }
                    });
        }
    }
    private boolean isClick=false;
    @Override
    public void fillView(BaseViewHolder helper, HelpResponse.DataBean s) {
        helper.setText(R.id.textView,s.getTitle());
        helper.setText(R.id.tvDesc,s.getContent());
        ImageView iv = helper.getView(R.id.iv);
        WebView webview = helper.getView(R.id.webview);
        TextView textView = helper.getView(R.id.textView);
        TextView tvDesc = helper.getView(R.id.tvDesc);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick=!isClick;
                if (isClick){
                    webview.setVisibility(View.VISIBLE);
                    iv.setBackgroundResource(R.mipmap.icon_black_down_arrow);
                    tvDesc.setVisibility(View.VISIBLE);
                }else {
                    webview.setVisibility(View.GONE);
                    tvDesc.setVisibility(View.GONE);
                    iv.setBackgroundResource(R.mipmap.icon_right_arrow);
                }
                textView.setSelected(isClick);
            }
        });
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, HelpResponse.DataBean s) {

    }

    @Override
    public void attachActivity(AppCompatActivity activity) {

    }

    @Override
    public void dettachActivity() {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public boolean isMuteAdapter() {
        return false;
    }

    @Override
    public int[] getMuteTypes() {
        return new int[0];
    }

    @Override
    public int[] getMuteLayouts() {
        return new int[0];
    }

    @OnClick({R.id.backLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
        }
    }
}
