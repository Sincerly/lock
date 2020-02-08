package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.map.GDMapUtils;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.ui.dialog.BusinessAreaDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.ShopListResponse;
import com.ysxsoft.lock.net.Api;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * 商圈
 * create by Sincerly on 9999/9/9 0009
 **/
@Route(path = "/main/ShopListActivity")
public class ShopListActivity extends BaseActivity implements IListAdapter{
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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;

    @BindView(R.id.FL1)
    FrameLayout FL1;
    @BindView(R.id.FL2)
    FrameLayout FL2;
    @BindView(R.id.FL3)
    FrameLayout FL3;
    @BindView(R.id.FL4)
    FrameLayout FL4;
    @BindView(R.id.tvShopList)
    TextView tvShopList;
    @BindView(R.id.tvShopType)
    TextView tvShopType;
    @BindView(R.id.tvAiSort)
    TextView tvAiSort;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    private double lat,lng;


    public static void start() {
        ARouter.getInstance().build(ARouterPath.getShopListActivity()).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_list;
    }

    @Override
    public void doWork() {
        super.doWork();
        initTitle();
        initList();
    }

    private void initList() {
        manager = new ListManager(this);
        manager.init(getWindow().findViewById(android.R.id.content));
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //setResult(RESULT_OK, intent);
                //finish();
                Object o = adapter.getData().get(position);
                adapter.getItem(position);

//                ShopDetailActivity.start();
            }
        });
        manager.getAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(manager.nextPage());
            }
        }, recyclerView);

        GDMapUtils mapUtils=new GDMapUtils();
        mapUtils.startOnceLocation(ShopListActivity.this, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                lat=aMapLocation.getLatitude();
                lng=aMapLocation.getLongitude();
                request(1);
            }
        });
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("社区商圈");
        title.setVisibility(View.GONE);
    }

    public boolean isClick1 = false;
    public boolean isClick2 = false;
    public boolean isClick3 = false;
    public boolean isClick4 = false;

    @OnClick({R.id.backLayout, R.id.FL1, R.id.FL2, R.id.FL3, R.id.FL4})
    public void onViewClicked(View view) {
        Drawable down = getResources().getDrawable(R.mipmap.icon_black_down_arrow);
        Drawable up = getResources().getDrawable(R.mipmap.icon_theme_up_arrow);
        down.setBounds(0, 0, down.getIntrinsicWidth(), down.getIntrinsicHeight());
        up.setBounds(0, 0, up.getIntrinsicWidth(), up.getIntrinsicHeight());
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.FL1:
                if (isClick1) {
                    isClick1 = false;
                    tvShopList.setCompoundDrawables(null, null, down, null);
                    tvShopList.setTextColor(getResources().getColor(R.color.color_282828));
                } else {
                    isClick1 = true;
                    tvShopList.setCompoundDrawables(null, null, up, null);
                    tvShopList.setTextColor(getResources().getColor(R.color.color_3BB0D2));

                    BusinessAreaDialog businessAreaDialog = new BusinessAreaDialog();
                    businessAreaDialog.init(ShopListActivity.this);
                    businessAreaDialog.showPopDown(LL1,0,0);
                }
                tvShopType.setCompoundDrawables(null, null, down, null);
                tvShopType.setTextColor(getResources().getColor(R.color.color_282828));
                tvAiSort.setCompoundDrawables(null, null, down, null);
                tvAiSort.setTextColor(getResources().getColor(R.color.color_282828));
                tvSelect.setCompoundDrawables(null, null, down, null);
                tvSelect.setTextColor(getResources().getColor(R.color.color_282828));
                isClick2 = false;
                isClick3 = false;
                isClick4 = false;

                break;
            case R.id.FL2:

                if (isClick2) {
                    isClick2 = false;
                    tvShopType.setCompoundDrawables(null, null, down, null);
                    tvShopType.setTextColor(getResources().getColor(R.color.color_282828));
                } else {
                    isClick2 = true;
                    tvShopType.setCompoundDrawables(null, null, up, null);
                    tvShopType.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                }
                tvShopList.setCompoundDrawables(null, null, down, null);
                tvShopList.setTextColor(getResources().getColor(R.color.color_282828));
                tvAiSort.setCompoundDrawables(null, null, down, null);
                tvAiSort.setTextColor(getResources().getColor(R.color.color_282828));
                tvSelect.setCompoundDrawables(null, null, down, null);
                tvSelect.setTextColor(getResources().getColor(R.color.color_282828));
                isClick1 = false;
                isClick3 = false;
                isClick4 = false;
                break;
            case R.id.FL3:

                if (isClick3) {
                    isClick3 = false;
                    tvAiSort.setCompoundDrawables(null, null, down, null);
                    tvAiSort.setTextColor(getResources().getColor(R.color.color_282828));
                } else {
                    isClick3 = true;
                    tvAiSort.setCompoundDrawables(null, null, up, null);
                    tvAiSort.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                }
                tvShopList.setCompoundDrawables(null, null, down, null);
                tvShopList.setTextColor(getResources().getColor(R.color.color_282828));
                tvShopType.setCompoundDrawables(null, null, down, null);
                tvShopType.setTextColor(getResources().getColor(R.color.color_282828));
                tvSelect.setCompoundDrawables(null, null, down, null);
                tvSelect.setTextColor(getResources().getColor(R.color.color_282828));
                isClick1 = false;
                isClick2 = false;
                isClick4 = false;
                break;
            case R.id.FL4:

                if (isClick4) {
                    isClick4 = false;
                    //tvSelect.setCompoundDrawables(null, null, down, null);
                    tvSelect.setTextColor(getResources().getColor(R.color.color_282828));
                } else {
                    isClick4 = true;
                    //tvSelect.setCompoundDrawables(null, null, up, null);
                    tvSelect.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                }
                tvShopList.setCompoundDrawables(null, null, down, null);
                tvShopList.setTextColor(getResources().getColor(R.color.color_282828));
                tvShopType.setCompoundDrawables(null, null, down, null);
                tvShopType.setTextColor(getResources().getColor(R.color.color_282828));
                tvAiSort.setCompoundDrawables(null, null, down, null);
                tvAiSort.setTextColor(getResources().getColor(R.color.color_282828));
                isClick1 = false;
                isClick2 = false;
                isClick3 = false;
                break;
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_shop_list;
    }

    @Override
    public void request(int page) {
        if (false) {
            debug(manager);
        } else {
            showLoadingDialog("请求中");
            OkHttpUtils.get()
                    .url(Api.LIST_NEAR)
                    .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                    .addParams("lng",lng+"")
                    .addParams("lat", lat+"")
                    .addParams("near", 10+"")
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            hideLoadingDialog();
                            Log.e("tag"," "+e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("tag",response+" ");
                            hideLoadingDialog();
                            ShopListResponse resp = JsonUtils.parseByGson(response, ShopListResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
                                    List<ShopListResponse.RowsBean> rowsBeans=resp.getRows();
                                    manager.setData(rowsBeans);
                                } else {
                                    //请求失败
                                    showToast(resp.getMsg());
                                }
                            } else {
                                showToast("获取商圈失败");
                            }
                        }
                    });
        }
    }

    @Override
    public void fillView(BaseViewHolder helper, Object o) {
        RatingBar rbar = helper.getView(R.id.rbar);
        //1,获取图片的高度
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ratingbar_ok);
        int starsImgHeight = bmp.getHeight();

        //2,将获取的图片高度设置给RatingBar
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rbar.getLayoutParams();
        lp.width = (LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height = starsImgHeight;
        rbar.setLayoutParams(lp);
        rbar.setRating(4.0f);

        RoundImageView riv = helper.getView(R.id.riv);
//        Glide.with(mContext).load("").into(riv);
//        helper.setText(R.id.tvName,"");
//        helper.setText(R.id.tvLevel,"");
//        helper.setText(R.id.tvSales,"");
//        helper.setText(R.id.tv1,"");
//        helper.setText(R.id.tv2,"");
//        helper.setText(R.id.tvAddress,"");
//        helper.setText(R.id.tvDistance,"");

    }

    @Override
    public void fillMuteView(BaseViewHolder helper, Object o) {

    }

    @Override
    public void attachActivity(AppCompatActivity activity) {

    }

    @Override
    public void dettachActivity() {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
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
}
