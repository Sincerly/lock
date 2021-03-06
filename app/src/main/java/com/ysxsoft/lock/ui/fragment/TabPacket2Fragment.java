package com.ysxsoft.lock.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.response.PacketCardResponse;
import com.ysxsoft.lock.ui.activity.UseCouponActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.net.Api;
import butterknife.BindView;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * 
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabPacket2Fragment extends BaseFragment implements IListAdapter<PacketCardResponse.DataBean> {
    @BindView(R.id.tabRecyclerView)
    RecyclerView tabRecyclerView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;
    private int isSelect = 0;
    private String shopId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            shopId=getArguments().getString("shopId");
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpacket2;
    }

    @Override
    protected void doWork(View view) {
        initList(view);
    }

    private void initList(View view){

        //        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        tabRecyclerView.setLayoutManager(layoutManager);
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("待使用");
        list.add("已使用");
        list.add("已过期");
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(getActivity(), R.layout.item_tab_tabpacket1, list) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                TextView tv1 = holder.getView(R.id.tv1);
                tv1.setText(item);
                if (isSelect == position) {
                    tv1.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv1.setBackgroundResource(R.drawable.shape_btn_bg);
                }else {
                    tv1.setTextColor(getResources().getColor(R.color.color_999999));
                    tv1.setBackgroundResource(R.drawable.bg_shape_bolder_cccccc_radius_20);
                }
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, View view, int position) {
                isSelect = position;
                adapter.notifyDataSetChanged();
                manager.resetPage();
                request(1);
            }
        });
        tabRecyclerView.setAdapter(adapter);

        manager = new ListManager(this);
        manager.init(view);
        /*manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });*/
        request(1);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_tabpacket2_fragment_list;
    }

    @Override
    public void request(int page) {
        PostFormBuilder builder=OkHttpUtils.post()
                .url(Api.MEM_BERCARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("type", "2")
                .addParams("status", isSelect + "");
        if(shopId!=null){
            builder.addParams("business_id", shopId+"");
        }
        builder.tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        manager.releaseRefresh();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        manager.releaseRefresh();
                        PacketCardResponse resp = JsonUtils.parseByGson(response, PacketCardResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                //请求成功
                                List<PacketCardResponse.DataBean> data = resp.getData();
                                manager.resetPage();
                                manager.setData(data);
                            } else {
                                //请求失败
                                showToast(resp.getMsg());
                            }
                        } else {
                            showToast("获取失败");
                        }
                    }
                });
    }

    @Override
    public void fillView(BaseViewHolder helper, PacketCardResponse.DataBean item) {
        RoundImageView riv = helper.getView(R.id.riv);
        Glide.with(getActivity()).load(AppConfig.BASE_URL + item.getImg()).into(riv);
        helper.setText(R.id.tv1,item.getTitle());
        helper.setText(R.id.tv2,item.getStart_time_str() + "-" + item.getEnd_time_str());
        helper.setText(R.id.tv3,"总价：¥"+item.getPrice());
        TextView tv4 = helper.getView(R.id.tv4);
        ImageView iv1 = helper.getView(R.id.iv1);
        tv4.setVisibility(item.getStatus().equals("1")?View.VISIBLE:View.GONE);
        iv1.setVisibility(!item.getStatus().equals("1")?View.VISIBLE:View.GONE);
        iv1.setImageResource(item.getStatus().equals("3") ? R.mipmap.icon_expire : R.mipmap.icon_used);

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UseCouponActivity.start(item.getId());
            }
        });
    }

    @Override
    public void fillMuteView(BaseViewHolder helper, PacketCardResponse.DataBean o) {

    }

    @Override
    public void attachActivity(AppCompatActivity activity) {

    }

    @Override
    public void dettachActivity() {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
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
