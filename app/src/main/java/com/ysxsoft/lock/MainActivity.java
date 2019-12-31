package com.ysxsoft.lock;

import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.squareup.haha.perflib.Main;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.activity.UserInfoActivity;
import com.ysxsoft.lock.ui.dialog.CouponDialog;

import butterknife.BindView;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY = 0;

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
        maxOffsetY = DisplayUtils.getDisplayHeight(this) / 3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                //Log.e("tag", "offsetX:" + offsetX + "offsetY:" + offsetY);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                float offsetX = x - downX;
                float offsetY = y - downY;
                if(Math.abs(offsetX)>Math.abs(offsetY)){
                    //x轴移动距离大于y轴 (左右)
                    if(offsetX>0){
                        //右 获取优惠券
                        CouponDialog.show(MainActivity.this, new CouponDialog.OnDialogClickListener() {
                            @Override
                            public void sure() {
                                PacketActivity.start();
                            }
                        });
                        Log.e("tag","向右");
                    }else{
                        //左 个人中心
                        UserInfoActivity.start();
                        Log.e("tag","向左");
                    }
                }else{
                    //y轴移动距离大于x轴 (上下)
                    if(offsetY>0){
                        //下 开锁 下滑一半选择小区
                        Log.e("tag","向下");
                    }else{
                        //上  广告
                        Log.e("tag","切换广告");
                    }
                }
                x = 0;
                y = 0;
                downX = 0;
                downY = 0;
                if (showHalf) {
                    showHalf = false;
                }


                break;
        }
        return true;
    }
}
