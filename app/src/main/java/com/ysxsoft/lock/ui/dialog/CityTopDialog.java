package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.models.response.CityTopResponse;
import com.ysxsoft.lock.models.response.DefaultPlaceResponse;
import com.ysxsoft.lock.models.response.TabKeyManager1FragmentResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
public class CityTopDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    RecyclerView recyclerView;
    RecyclerView fangShiViewMenu;
    private int click=0;
    private int click1=0;
    private TextView tvName;

    public CityTopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_city_top, null);
        tvName = view.findViewById(R.id.tvName);
        recyclerView = view.findViewById(R.id.recyclerView);
        fangShiViewMenu = view.findViewById(R.id.fangShiViewMenu);
        request();
        initData2();
        return view;
    }

    public OnDialogClickListener getListener() {
        return listener;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(init());
    }

    private void initData(){
        recyclerView.setAdapter(null);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        List<CityTopResponse.DataBean.ListkeyBean> data=new ArrayList<>();
        for (int i = 0; i <5; i++) {
            data.add(new CityTopResponse.DataBean.ListkeyBean());
        }
        RBaseAdapter<CityTopResponse.DataBean.ListkeyBean> adapter = new RBaseAdapter<CityTopResponse.DataBean.ListkeyBean>(mContext, R.layout.item_city_top, data) {
            @Override
            protected void fillItem(RViewHolder holder, CityTopResponse.DataBean.ListkeyBean item, int position) {
                TextView name=holder.getView(R.id.name);
                if(position==0){
                    name.setSelected(true);
                    name.setTextColor(Color.parseColor("#3BB0D2"));
                }else {
                    name.setSelected(false);
                    name.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                }
            }

            @Override
            protected int getViewType(CityTopResponse.DataBean.ListkeyBean item, int position) {
                return 0;
            }
        };
        adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, View view, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData2(){
        fangShiViewMenu.setAdapter(null);
        fangShiViewMenu.setNestedScrollingEnabled(false);
        fangShiViewMenu.setLayoutManager(new GridLayoutManager(mContext,4));

        ArrayList<String> list = new ArrayList<>();
        list.add("密码开门");
        list.add("蓝牙开门");
        list.add("远程开门");

        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_city_top, list) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                TextView name=holder.getView(R.id.name);
                if(position==click1){
                    name.setSelected(true);
                    name.setTextColor(Color.parseColor("#3BB0D2"));
                }else {
                    name.setSelected(false);
                    name.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                name.setText(item);
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
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
                click1=position;
                adapter.notifyDataSetChanged();
            }
        });
        fangShiViewMenu.setAdapter(adapter);
    }

    private void request() {
        OkHttpUtils.get()
//                .url(Api.GET_BIND_PLACE_LIST)
                .url(Api.GET_DEFAULT_PLACE_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        CityTopResponse cityTopResponse = JsonUtils.parseByGson(response, CityTopResponse.class);
                        DefaultPlaceResponse resp = JsonUtils.parseByGson(response, DefaultPlaceResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                tvName.setText(resp.getData().getQuarters_name());
                                recyclerView.setAdapter(null);
                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
                                List<DefaultPlaceResponse.DataBean.ListkeyBean> listkey = resp.getData().getListkey();
//                                List<CityTopResponse.DataBean.ListkeyBean> data=new ArrayList<>();
//                                for (int i = 0; i <5; i++) {
//                                    data.add(new CityTopResponse.DataBean.ListkeyBean());
//                                }
                                RBaseAdapter<DefaultPlaceResponse.DataBean.ListkeyBean> adapter = new RBaseAdapter<DefaultPlaceResponse.DataBean.ListkeyBean>(mContext, R.layout.item_city_top, listkey) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, DefaultPlaceResponse.DataBean.ListkeyBean item, int position) {
                                        TextView name=holder.getView(R.id.name);
                                        if(position==click){
                                            name.setSelected(true);
                                            name.setTextColor(Color.parseColor("#3BB0D2"));
                                        }else {
                                            name.setSelected(false);
                                            name.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                                        }
                                        name.setText(item.getEqu_name());
                                    }

                                    @Override
                                    protected int getViewType(DefaultPlaceResponse.DataBean.ListkeyBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        click=position;
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    public void showDialog() {
        if (!isShowing()) {
            show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = DisplayUtils.getDisplayWidth(mContext);
//            lp.width = DisplayUtils.getDisplayWidth(mContext);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.TOP);
        }
    }

    public static CityTopDialog show(Context context, OnDialogClickListener listener) {
        CityTopDialog dialog = new CityTopDialog(context, R.style.TopDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}
