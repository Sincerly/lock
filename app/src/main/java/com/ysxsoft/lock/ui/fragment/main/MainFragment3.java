package com.ysxsoft.lock.ui.fragment.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告fragment
 */
public class MainFragment3 extends BaseFragment {
    @BindView(R.id.pic)
    ImageView pic;
    @BindView(R.id.adLayout)
    LinearLayout adLayout;
    @BindView(R.id.down)
    TextView down;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_3;
    }

    @Override
    protected void doWork(View view) {

    }

    @OnClick(R.id.down)
    public void onViewClicked() {
        MainActivity activity= (MainActivity) getActivity();
        activity.toTab(1);
    }
}
