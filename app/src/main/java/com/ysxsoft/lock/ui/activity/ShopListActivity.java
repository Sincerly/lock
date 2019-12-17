package com.ysxsoft.lock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseActivity;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
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
public class ShopListActivity extends BaseActivity implements IListAdapter {
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

    @BindView(R.id.banner)
    BGABanner banner;
    @BindView(R.id.FL1)
    FrameLayout FL1;
    @BindView(R.id.FL2)
    FrameLayout FL2;
    @BindView(R.id.FL3)
    FrameLayout FL3;
    @BindView(R.id.FL4)
    FrameLayout FL4;


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
        initBanner();
        //http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=0&pn=3&spn=0&di=58520&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2229864841%2C4232235061&os=1657753798%2C2900886188&simid=4281798412%2C697443542&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Ffile02.16sucai.com%2Fd%2Ffile%2F2014%2F0704%2Fe53c868ee9e8e7b28c424b56afe2066d.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3F8mf7vwt_z%26e3Bv54AzdH3Fda89AzdH3Fa0AzdH3F99ndm_z%26e3Bip4s&gsm=&islist=&querylist=
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
            }
        });
        manager.getAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(manager.nextPage());
            }
        }, recyclerView);
        request(1);
    }

    private void initBanner() {
        List<String> bannerBeans = new ArrayList<>();
        bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
        bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
        bannerBeans.add("http://alaing.cn/uploads/images/20190612/691307f0342f03cd49f50dce6d1f7c99.png");
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String item, int position) {
                Glide.with(mContext)
                        .load(item).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView);
            }
        });
        banner.setData(bannerBeans, new ArrayList<String>());

    }

    private void initTitle() {
        bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        backLayout.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.icon_gray_back);
        title.setText("社区商圈");
    }

    @OnClick({R.id.backLayout, R.id.FL1, R.id.FL2, R.id.FL3, R.id.FL4,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backLayout:
                backToActivity();
                break;
            case R.id.FL1:

                break;
            case R.id.FL2:

                break;
            case R.id.FL3:

                break;
            case R.id.FL4:

                break;
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_shop_list;
    }

    @Override
    public void request(int page) {
        if (IS_DEBUG_ENABLED) {
            debug(manager);
        } else {
            showLoadingDialog("请求中");
            OkHttpUtils.post()
                    .url(Api.GET_SHOP_LIST)
                    .addParams("uid", SharedPreferencesUtils.getUid(ShopListActivity.this))
                    .tag(this)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            hideLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            hideLoadingDialog();
                            ShopListResponse resp = JsonUtils.parseByGson(response, ShopListResponse.class);
                            if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<ShopListResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
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
