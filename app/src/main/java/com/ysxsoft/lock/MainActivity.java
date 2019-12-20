package com.ysxsoft.lock;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ui.fragment.MainChild1Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY=0;

    public static void start() {
        ARouter.getInstance().build(ARouterPath.getMainActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void doWork() {
        super.doWork();
        maxOffsetY=DisplayUtils.getDisplayHeight(this)/3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX=event.getX();
                downY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x=event.getX();
                y=event.getY();
                float offsetX=x-downX;
                float offsetY=y-downY;
                if(Math.abs(offsetY)>= ViewConfiguration.getTouchSlop()){
                    //竖直滑动距离
                    if(offsetY>=0){
                        //向下
                        if(Math.abs(offsetY)<maxOffsetY){
                            Log.e("tag","向下");
                        }
                    }else{
                        //向上
                        if(Math.abs(offsetY)>=maxOffsetY){
                            Log.e("tag","向上");
                        }
                    }
                    if(offsetX>=0){
                        Log.e("tag","offsetX>0:"+offsetX);
                    }else{
                        Log.e("tag","offsetX<0:"+offsetX);
                    }
                }else{
                    //滑动
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                x=0;
                y=0;
                downX=0;
                downY=0;
                if(showHalf){
                    showHalf=false;
                }
                break;
        }
        return true;
    }
}
