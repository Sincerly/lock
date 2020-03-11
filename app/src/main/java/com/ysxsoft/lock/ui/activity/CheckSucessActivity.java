package com.ysxsoft.lock.ui.activity;

import android.os.Bundle;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.CheckSucessResponse;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 核销成功
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/CheckSucessActivity")
public class CheckSucessActivity extends BaseActivity {
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
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.tvTip)
    TextView tvTip;

    @Autowired
    boolean isCard;

    public static void start(boolean isCard) {
        ARouter.getInstance().build(ARouterPath.getCheckSucessActivity())
                .withBoolean("isCard",isCard).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_sucess;
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
        if(isCard){
            title.setText("核实成功");
            tips.setText("核实成功");
            tvTip.setText("详细信息可在核实记录里查看");
        }else{
            title.setText("核销成功");
            tips.setText("核销成功");
            tvTip.setText("详细信息可在核销记录里查看");
        }
    }

    @OnClick({R.id.backLayout, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tvOk:
                finish();
                break;
        }
    }
}
