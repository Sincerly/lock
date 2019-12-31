package com.ysxsoft.lock.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.google.android.material.tabs.TabLayout;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.ViewPagerFragmentAdapter;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.widgets.NoScrollViewPager;
import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.models.response.ListUnitResponse;
import com.ysxsoft.lock.models.response.ListfloorDataResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.ui.dialog.RidgepoleSelectDialog;
import com.ysxsoft.lock.ui.fragment.TabApplyKey1Fragment;
import com.ysxsoft.lock.ui.fragment.TabApplyKey2Fragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 申请钥匙
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ApplyKeyActivity")
public class ApplyKeyActivity extends BaseActivity {
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
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.et1)
    EditText et1;

    @Autowired
    String requid;
    private List<ListfloorDataResponse.DataBean> listFloors;
    private String floor_id;
    private String unit_id;

    public static void start(String requid) {
        ARouter.getInstance().build(ARouterPath.getApplyKeyActivity()).withString("requid", requid).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_key;
    }

    @OnClick(R.id.backLayout)
    public void onViewClicked() {
        backToActivity();
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("申请钥匙");
    }

    @OnClick({R.id.backLayout, R.id.tv1, R.id.tv2, R.id.tvOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.tv1:
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < listFloors.size(); i++) {
                    strings.add(listFloors.get(i).getFloor_name());
                }
                RidgepoleSelectDialog ridgepoleSelectDialog = new RidgepoleSelectDialog(mContext, R.style.CenterDialogStyle);
                ridgepoleSelectDialog.setTitle("栋数选择");
                ridgepoleSelectDialog.setData(strings, 0, new RidgepoleSelectDialog.OnDialogSelectListener() {
                    @Override
                    public void OnSelect(String data1, int position1) {
                        floor_id = listFloors.get(position1).getId();
                        String requ_id = listFloors.get(position1).getRequ_id();
                        tv1.setText(data1);
                        tv2.setText("");
                        requestListUnitData(floor_id);
                    }
                });
                ridgepoleSelectDialog.showDialog();
                break;
            case R.id.tv2:
                if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
                    showToast("栋选择不能为空");
                    return;
                }
                ArrayList<String> strings1 = new ArrayList<>();
                RidgepoleSelectDialog ridgepoleSelectDialog1 = new RidgepoleSelectDialog(mContext, R.style.CenterDialogStyle);
                ridgepoleSelectDialog1.setTitle("单元选择");
                ridgepoleSelectDialog1.setData(strings1, 0, new RidgepoleSelectDialog.OnDialogSelectListener() {
                    @Override
                    public void OnSelect(String data1, int position1) {
                        tv2.setText(data1);
                    }
                });
                ridgepoleSelectDialog1.showDialog();
                break;
            case R.id.tvOk:
                if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
                    showToast("栋选择不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv2.getText().toString().trim())) {
                    showToast("单元选择不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et1.getText().toString().trim())) {
                    showToast("房号选择不能为空");
                    return;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        OkHttpUtils.post()
                .url(Api.SAVE_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("floor_id",floor_id)
                .addParams("unit_id",unit_id)
                .addParams("room", et1.getText().toString().trim())
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp!=null){
                            showToast(resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(resp.getCode())){
                                finish();
                            }
                        }
                    }
                });
    }

    @Override
    public void doWork() {
        super.doWork();
        ARouter.getInstance().inject(this);
        initTitle();
//        tabLayout.removeAllTabs();
//        List<Fragment> fragmentList = new ArrayList<>();
//        List<String> titles = new ArrayList<>();
//        titles.add("手机钥匙");
//        titles.add("人脸识别");
//        fragmentList.add(new TabApplyKey1Fragment());
//        fragmentList.add(new TabApplyKey2Fragment());
//        initViewPage(fragmentList, titles);
//        initTabLayout(titles);
        requestListfloorData();
    }

    /**
     * 获取小区楼栋单元信息
     */
    private void requestListUnitData(String floor_id) {
        OkHttpUtils.get()
                .url(Api.GET_FLOOR_CHILD_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("floorid", floor_id)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ListUnitResponse resp = JsonUtils.parseByGson(response, ListUnitResponse.class);
                        if (resp != null) {

                        }
                    }
                });
    }

    /**
     * 获取小区楼栋信息
     */
    private void requestListfloorData() {
        OkHttpUtils.get()
                .url(Api.GET_FLOOR_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("requid", requid)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ListfloorDataResponse resp = JsonUtils.parseByGson(response, ListfloorDataResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                listFloors = resp.getData();
                            }
                        }
                    }
                });
    }


    private void initViewPage(List<Fragment> fragmentList, List<String> titles) {
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titles));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
    }

    private void initTabLayout(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.view_tab);
            TextView textView = tab.getCustomView().findViewById(R.id.tab);
            textView.setText(titles.get(i));
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorTabSelectedIndictor));
                textView.setTextSize(17);
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
                textView.setTextSize(15);
            }
        }
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(17);
            tv.setTextColor(getResources().getColor(R.color.colorTabSelectedIndictor));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.colorTabNormalIndictor));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };
}