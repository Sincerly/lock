package com.ysxsoft.lock.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ysxsoft.common_base.adapter.BaseQuickAdapter;
import com.ysxsoft.common_base.adapter.BaseViewHolder;
import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.base.frame.list.IListAdapter;
import com.ysxsoft.common_base.base.frame.list.ListManager;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.view.widgets.MultipleStatusView;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.CodeBean;
import com.ysxsoft.lock.models.response.PacketCardResponse;
import com.ysxsoft.lock.ui.dialog.CodeDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import com.ysxsoft.lock.ARouterPath;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import io.reactivex.internal.operators.maybe.MaybeFromAction;
import okhttp3.Call;

import static com.ysxsoft.lock.config.AppConfig.IS_DEBUG_ENABLED;

/**
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabPacket4Fragment extends BaseFragment implements IListAdapter<PacketCardResponse.DataBean> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    ListManager manager;

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
        return R.layout.fragment_tabpacket4;
    }

    @Override
    protected void doWork(View view) {
        initList(view);
    }

    private void initList(View view) {
        manager = new ListManager(this);
        manager.init(view);
        manager.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PacketCardResponse.DataBean item = (PacketCardResponse.DataBean)manager.getAdapter().getItem(position);
                if (item.getStatus().equals("1")) {
                    CodeBean bean=new CodeBean();
                    bean.setId(item.getId());
                    bean.setType("1");
                    CodeDialog.show(getActivity(),new Gson().toJson(bean),item.getLogo());
                }
            }
        });
        manager.getSmartRefresh().setEnableLoadMore(false);
        request(1);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_tabpacket4_fragment_list;
    }

    @Override
    public void request(int page) {
        PostFormBuilder builder=OkHttpUtils.post()
                .url(Api.MEM_BERCARD)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(getActivity()))
                .addParams("type", "4")
                .addParams("status","");
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
        ImageView ivbg = helper.getView(R.id.ivbg);
        CircleImageView civ = helper.getView(R.id.civ);
        ImageView iv1 = helper.getView(R.id.iv1);
        Glide.with(getActivity()).load(AppConfig.BASE_URL + item.getLogo()).into(civ);
        helper.setText(R.id.tv1,item.getName());
        helper.setText(R.id.tv2,item.getName()+"会员卡");
        helper.setText(R.id.tv3,item.getTitle());
        switch (helper.getAdapterPosition() % 3) {
            case 0:
                ivbg.setBackgroundResource(R.mipmap.icon_vip1);
                break;
            case 1:
                ivbg.setBackgroundResource(R.mipmap.icon_vip2);
                break;
            case 2:
                ivbg.setBackgroundResource(R.mipmap.icon_vip3);
                break;
        }

        if (!item.getStatus().equals("1")){
            ivbg.setBackgroundResource(R.mipmap.icon_vip_gray);
        }

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
