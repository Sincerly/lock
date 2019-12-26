package com.ysxsoft.lock.ui.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.lock.R;

@Route(path = "/main/WelcomeActivity")
public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

}
