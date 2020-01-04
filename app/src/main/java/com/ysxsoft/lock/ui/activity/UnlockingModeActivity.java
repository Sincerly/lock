package com.ysxsoft.lock.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.ui.dialog.OpenLockPwdDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
@Route(path = "/main/UnlockingModeActivity")
public class UnlockingModeActivity extends BaseActivity {
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

    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv10)
    TextView tv10;

    @Autowired
    String pass;

    public static void start(String pass) {
        ARouter.getInstance().build(ARouterPath.getUnlockingModeActivity()).withString("pass",pass).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlocking_mode;
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
        title.setText("开锁方式");
    }

    @OnClick({R.id.backLayout, R.id.tv2, R.id.tv4, R.id.tv6, R.id.tv8, R.id.tv10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv2:
                showToast("蓝牙开门");
                break;
              case R.id.tv4:
                  OpenLockPwdDialog dialog = new OpenLockPwdDialog(mContext, R.style.CenterDialogStyle);
                  dialog.setData(pass);
                  dialog.showDialog();
                break;
              case R.id.tv6:

                  showToast("远程开门");
                break;
              case R.id.tv8:
                finish();
                break;
              case R.id.tv10:
                break;
        }
    }


}
