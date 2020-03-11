package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
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
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.BannerResponse;
import com.ysxsoft.lock.models.response.NearPlaceResponse;
import com.ysxsoft.lock.models.response.ShopListTypeResponse;
import com.ysxsoft.lock.ui.dialog.BusinessAreaDialog;
import com.ysxsoft.lock.utils.BaiduLocationUtils;
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
public class ShopListActivity extends BaseActivity implements IListAdapter<ShopListResponse.RowsBean> {
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
    @BindView(R.id.banner)
    BGABanner banner;
    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.editText)
    EditText editText;
    private double lat, lng;

    private String requid = "";
    private String bsort = "";
    private String orderby = "";
    private String asd = "DESC";
    List<NearPlaceResponse.RowsBean> rowsBeans = new ArrayList<>();
    List<ShopListTypeResponse.RowsBean> rowsBean2 = new ArrayList<>();

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
        getBanner();
    }

    private void getBanner() {
        Log.e("tag", "header:" + SharedPreferencesUtils.getToken(mContext) + "lng:" + lng + "lat:" + lat);
        OkHttpUtils.get()
                .url(Api.BANNER)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", " " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", response + " ");
                        BannerResponse resp = JsonUtils.parseByGson(response, BannerResponse.class);
                        if (resp != null) {
                            if (200 == resp.getCode()) {
                                //请求成功
                                List<BannerResponse.DataBean> dataBeans = resp.getData();
                                initBanner(dataBeans);
                            } else {
                                //请求失败
                                showToast(resp.getMsg() + "");
                            }
                        } else {
                            showToast("获取附近小区列表失败");
                        }
                    }
                });
    }

    private void initBanner(List<BannerResponse.DataBean> banners) {
        if (false) {
            //debug模式
            List<String> bannerBeans = new ArrayList<>();
            bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
            bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
            bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
            banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String item, int position) {
                    Glide.with(ShopListActivity.this)
                            .load(item).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(itemView);
                }
            });
            banner.setData(bannerBeans, new ArrayList<String>());
        } else {
            if (banners == null) {
                banners = new ArrayList<>();
            }
            banner.setAdapter(new BGABanner.Adapter<ImageView, BannerResponse.DataBean>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, BannerResponse.DataBean item, int position) {
                    Glide.with(ShopListActivity.this)
                            .load(AppConfig.BASE_URL + item.getAttach()).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(itemView);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            });
            banner.setData(banners, new ArrayList<>());
        }
    }


    private void getNearPlace() {
        Log.e("tag", "header:" + SharedPreferencesUtils.getToken(mContext) + "lng:" + lng + "lat:" + lat);

        OkHttpUtils.get()
                .url(Api.LIST_ADDRESS_NEAR)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("lng", lng + "")
                .addParams("lat", lat + "")
                .addParams("near", 10 + "")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", " " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", response + " ");
                        NearPlaceResponse resp = JsonUtils.parseByGson(response, NearPlaceResponse.class);
                        if (resp != null) {
                            if (200 == resp.getCode()) {
                                //请求成功
                                rowsBeans.clear();
                                rowsBeans.addAll(resp.getRows());
                            } else {
                                //请求失败
                                showToast(resp.getMsg() + "");
                            }
                        } else {
                            showToast("获取附近小区列表失败");
                        }
                    }
                });
    }

    private void getShopType() {
        OkHttpUtils.get()
                .url(Api.LIST_BSORT)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", " " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("tag", response + " ");
                        ShopListTypeResponse resp = JsonUtils.parseByGson(response, ShopListTypeResponse.class);
                        if (resp != null) {
                            if (200 == resp.getCode()) {
                                //请求成功
                                rowsBean2.clear();
                                rowsBean2.addAll(resp.getRows());
                            } else {
                                //请求失败
                                showToast(resp.getMsg() + "");
                            }
                        } else {
                            showToast("获取商户类型列表失败");
                        }
                    }
                });
    }


    private void initList() {
        manager = new ListManager(this);
        manager.init(getWindow().findViewById(android.R.id.content));
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //setResult(RESULT_OK, intent);
                //finish();
                ShopListResponse.RowsBean o = (ShopListResponse.RowsBean) adapter.getData().get(position);
                ShopDetailActivity.start(o.getId());
            }
        });
        manager.getAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(manager.nextPage());
            }
        }, recyclerView);

        BaiduLocationUtils.initBdMapLocatonOnce(ShopListActivity.this, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取纬度信息
                if (lat == 0) {
                    lat = bdLocation.getLatitude();
                    //获取经度信息
                    lng = bdLocation.getLongitude();
                    request(1);
                    getNearPlace();//获取附近小区列表
                    getShopType();//获取商户类型
                }
            }
        }, new BaiduLocationUtils.BDMapListener() {
            @Override
            public void end(LocationClient locationClient) {
                if (lat != 0.0) {
                    locationClient.stop();
                }
            }
        });
    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("社区商圈");
        title.setVisibility(View.GONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    manager.resetPage();
                    request(1);
                    return false;
                }
                return false;
            }
        });
    }


    public boolean isClick1 = false;
    public boolean isClick2 = false;
    public boolean isClick3 = false;
    public boolean isClick4 = false;

    private BusinessAreaDialog businessAreaDialog;

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

                    List<String> data = new ArrayList<>();
                    for (int i = 0; i < rowsBeans.size(); i++) {
                        //附近小区列表
                        data.add(rowsBeans.get(i).getQuarters_name());
                    }
                    businessAreaDialog = new BusinessAreaDialog();
                    businessAreaDialog.init(ShopListActivity.this);
                    businessAreaDialog.showPopDown(LL1, 0, 0, requid, data, new BusinessAreaDialog.OnPopupWindowListener() {
                        @Override
                        public void onSelected(String name, int position) {
                            //选中的小区id
                            if (rowsBeans.size() > position) {
                                requid = rowsBeans.get(position).getId();
                            }
                            request(1);
                        }
                    });
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

                    List<String> data = new ArrayList<>();
                    for (int i = 0; i < rowsBean2.size(); i++) {
                        //附近小区列表
                        data.add(rowsBean2.get(i).getBsort());
                    }
                    businessAreaDialog = new BusinessAreaDialog();
                    businessAreaDialog.init(ShopListActivity.this);
                    businessAreaDialog.showPopDown(LL1, 0, 0, bsort, data, new BusinessAreaDialog.OnPopupWindowListener() {
                        @Override
                        public void onSelected(String name, int position) {
                            //选中的小区id
                            if (rowsBean2.size() > position) {
                                bsort = "" + rowsBean2.get(position).getId();
                            }
                            request(1);
                        }
                    });

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

                    List<String> data = new ArrayList<>();
                    data.add("距离最近");
                    data.add("销量最高");
                    data.add("发布时间");
                    businessAreaDialog = new BusinessAreaDialog();
                    businessAreaDialog.init(ShopListActivity.this);
                    businessAreaDialog.showPopDown(LL1, 0, 0, orderby, data, new BusinessAreaDialog.OnPopupWindowListener() {
                        @Override
                        public void onSelected(String name, int position) {
                            //选中的小区id
                            if (position == 0) {
                                orderby = "distance";
                            } else if (position == 1) {
                                orderby = "xl";
                            } else {
                                orderby = "create_time";
                            }
                            request(1);
                        }
                    });
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

                if ("ASC".equals(asd)) {
                    //升序
                    //tvSelect.setCompoundDrawables(null, null, down, null);
                    tvSelect.setTextColor(getResources().getColor(R.color.color_282828));
                } else {
                    //降序
                    //tvSelect.setCompoundDrawables(null, null, up, null);
                    tvSelect.setTextColor(getResources().getColor(R.color.color_3BB0D2));
                }
                if ("ASC".equals(asd)) {
                    asd = "DESC";
                } else {
                    asd = "ASC";
                }
                request(1);
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
                    .addParams("lng", lng + "")
                    .addParams("lat", lat + "")
                    .addParams("near", 100000 + "")
                    .addParams("requid", requid)//小区id
//                    .addParams("requid", "1697eeb3bfde4a658eda41432eb86edd")//小区id
                    .addParams("bsort", bsort)//商户类型名称
                    .addParams("orderby", orderby)//排序字段
                    .addParams("asd", asd)//排序方式
                    .addParams("name", editText.getText().toString())//名称
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            hideLoadingDialog();
                            manager.releaseRefresh();
                            Log.e("tag", " " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            manager.releaseRefresh();
                            Log.e("tag", response + " ");
                            hideLoadingDialog();
                            ShopListResponse resp = JsonUtils.parseByGson(response, ShopListResponse.class);
                            if (resp != null) {
                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
                                    List<ShopListResponse.RowsBean> rowsBeans = resp.getRows();

                                    manager.resetPage();
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
    public void fillView(BaseViewHolder helper, ShopListResponse.RowsBean o) {
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
        Glide.with(mContext).load(AppConfig.BASE_URL + o.getLogo()).into(riv);
        helper.setText(R.id.tvName, o.getName());
//        helper.setText(R.id.tvLevel,o.get);
        helper.setText(R.id.tvSales, "月销" + o.getXl() + "单");
        helper.setText(R.id.tv1, o.getWeek1() + "-" + o.getWeek2() + " " + o.getTime1() + "-" + o.getTime2());
//        helper.setText(R.id.tv2,"");
        helper.setText(R.id.tvAddress, o.getAddress());
        helper.setText(R.id.tvDistance, o.getDistance() + "km");
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, ShopListResponse.RowsBean o) {

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
