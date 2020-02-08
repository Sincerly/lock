package com.ysxsoft.lock.ui.fragment.main;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.common_base.utils.ViewHelper;
import com.ysxsoft.lock.MainActivity;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ADResponse;
import com.ysxsoft.lock.models.response.DefaultPlaceResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.PacketActivity;
import com.ysxsoft.lock.ui.dialog.CheckAddressDialog;
import com.ysxsoft.lock.ui.dialog.CityTopDialog;
import com.ysxsoft.lock.ui.dialog.CouponDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 首页操作页面
 */
public class MainFragment2 extends BaseFragment implements View.OnTouchListener {
    private float x;
    private float y;
    private float downX;
    private float downY;
    private boolean showHalf;
    private int maxOffsetY = 0;
    private int minOffset = 40;
    @BindView(R.id.touchView)
    ImageView touchView;
    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.down)
    TextView down;

    private static final String TAG = "MainFragment2";
    private DefaultPlaceResponse.DataBean dataBean;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_2;
    }

    @Override
    public void doWork(View view) {
        maxOffsetY = DisplayUtils.getDisplayHeight(getActivity()) / 3;
        touchView.setOnTouchListener(this);
        initViewPager2();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        showLoading("请求中...");
        OkHttpUtils.get()
                .url(Api.GET_DEFAULT_PLACE_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", "default place" + response);
                        hideLoadingDialog();
                        DefaultPlaceResponse resp = JsonUtils.parseByGson(response, DefaultPlaceResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                dataBean = resp.getData();
                                getad();//获取广告
                                tvName.setText(resp.getData().getQuarters_name());
                            } else if (HttpResponse.NO_RESPONSE.equals(resp.getCode())) {
                                CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
                                    @Override
                                    public void sure(String requid) {

                                    }

                                    @Override
                                    public void cancle() {

                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean result = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                float offsetX = x - downX;
                float offsetY = y - downY;

                if (Math.abs(offsetX) > minOffset || Math.abs(offsetY) > minOffset) {
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        //x轴移动距离大于y轴 (左右)
                        if (offsetX > 0) {
                            //页面向右 获取优惠券
                            Log.e(TAG, "页面向右 获取优惠券");
                            CouponDialog.show(getActivity(), true, "恭喜！送你一张优惠券", new CouponDialog.OnDialogClickListener() {
                                @Override
                                public void sure() {
                                    PacketActivity.start(0);
                                }
                            });
                        } else {
                            //页面向左 获取优惠券
                            Log.e(TAG, "页面向左 个人中心");
                        }
                    } else {
                        //y轴移动距离大于x轴 (上下)
                        if (offsetY > 0) {
                            //下 开锁 下滑一半选择小区
                            if (DisplayUtils.getDisplayHeight(getActivity()) / 3 > offsetY && downY < DisplayUtils.getDisplayHeight(getActivity()) / 3) {

                                if (dataBean != null) {
                                    if (TextUtils.isEmpty(dataBean.getQuarters_name())) {
                                        CheckAddressDialog.show(getActivity(), new CheckAddressDialog.OnDialogClickListener() {
                                            @Override
                                            public void sure(String requid) {
                                            }

                                            @Override
                                            public void cancle() {

                                            }
                                        });
                                    } else {
                                        Log.e(TAG, "滑动距离未超过1/3");
                                        CityTopDialog.show(getActivity(), new CityTopDialog.OnDialogClickListener() {
                                            @Override
                                            public void sure() {
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.e(TAG, "向下");
                                MainActivity activity = (MainActivity) getActivity();
                                String deviceId=activity.getDeivceId();
                                if(dataBean!=null){
                                    //检查开锁权限
                                    activity.checkPermision(dataBean.getId(),deviceId,"");
                                }
                            }
                        } else {
                            //上  广告
                            Log.e(TAG, "切换广告");

                            int currentItem = viewPager2.getCurrentItem();
                            if (currentItem != (viewPager2.getAdapter().getItemCount() - 1)) {
                                viewPager2.setCurrentItem((currentItem + 1));
                            } else {
                                ToastUtils.shortToast(getActivity(), "暂无广告");
                            }
                        }
                    }
                    x = 0;
                    y = 0;
                    downX = 0;
                    downY = 0;
                    if (showHalf) {
                        showHalf = false;
                    }
                } else {
                    Log.e(TAG, "未超过移动的距离");
                }
                break;
        }
        return result;
    }

    BaseQuickAdapter<ADResponse.DataBean, BaseViewHolder> adapter;

    private void initViewPager2() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager2.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        viewPager2.setLayoutParams(params);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0){
                    down.setAlpha(1);
                    ViewCompat.animate(down).alpha(1f).alpha(0f).setDuration(600).start();
                    down.setVisibility(View.GONE);
                }else{
                    if(down.getAlpha()!=1){
                        down.setAlpha(0);
                        down.setVisibility(View.VISIBLE);
                        ViewCompat.animate(down).alphaBy(0f).alpha(1f).setDuration(600).start();
                    }
                }
            }
        });

        List<ADResponse.DataBean> datas = new ArrayList<>();
        datas.add(new ADResponse.DataBean());
        if (adapter == null) {
            adapter = new BaseQuickAdapter<ADResponse.DataBean, BaseViewHolder>(R.layout.view_img, datas) {
                @Override
                protected void convert(BaseViewHolder helper,ADResponse.DataBean item) {
                    ImageView pic = helper.getView(R.id.pic);
                    int resourceId = R.mipmap.a1;
                    switch (helper.getAdapterPosition()) {
                        case 0:
                            resourceId = R.mipmap.main;
                            break;
                        case 1:
                            resourceId = R.mipmap.a1;
                            break;
                        case 2:
                            resourceId = R.mipmap.a2;
                            break;
                        case 3:
                            resourceId = R.mipmap.a3;
                            break;
                        case 4:
                            resourceId = R.mipmap.a4;
                            break;
                    }
                    pic.setImageResource(resourceId);
                }
            };
            viewPager2.setAdapter(adapter);
        } else {
            viewPager2.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 获取广告
     */
    public void getad() {
        String requid = "";
        if (dataBean != null) {
            requid = dataBean.getId();//默认小区id
            if (requid == null) {
                return;
            }
        }
        OkHttpUtils.get()
                .url(Api.AD)
                //.addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("requid", "1")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ADResponse resp = JsonUtils.parseByGson(response, ADResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                Log.e("tag","获取广告");
                                List<ADResponse.DataBean> dataBeans=new ArrayList<>();
                                if(resp.getData()!=null){
                                    dataBeans.add(new ADResponse.DataBean());
                                    dataBeans.add(resp.getData());
                                }
                                adapter.setNewData(dataBeans);
                            } else {
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取广告失败");
                        }
                    }
                });
    }

    @OnClick({R.id.tvName, R.id.down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvName:
                //选择城市
                AddPlaceActivity.start();
                break;
            case R.id.down:
                viewPager2.setCurrentItem(0,false);
                break;
        }
    }
}
