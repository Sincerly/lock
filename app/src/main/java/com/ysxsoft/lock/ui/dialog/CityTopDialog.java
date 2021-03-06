package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ysxsoft.lock.ui.activity.AddPlaceActivity;
import com.ysxsoft.lock.ui.activity.KeyManagerActivity;
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

    private RBaseAdapter<DefaultPlaceResponse.DataBean.ListkeyBean> keyAdapter;

    public CityTopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_city_top, null);
        tvName = view.findViewById(R.id.tvName);
        LinearLayout addressLayout = view.findViewById(R.id.addressLayout);
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddPlaceActivity.start();
                KeyManagerActivity.start();
            }
        });
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
    private void initData2(){
        fangShiViewMenu.setAdapter(null);
        fangShiViewMenu.setNestedScrollingEnabled(false);
        fangShiViewMenu.setLayoutManager(new GridLayoutManager(mContext,4));

        ArrayList<String> list = new ArrayList<>();
        list.add("蓝牙开门");
        list.add("密码开门");
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
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, View view, int position) {
                if(keyAdapter!=null){
                    click1=position;
                    adapter.notifyDataSetChanged();

                    DefaultPlaceResponse.DataBean.ListkeyBean item=keyAdapter.getItemData(click);
                    if(listener!=null){
                        listener.sure(item.getRequ_id(),position,item.getEqu_id(),item.getEqu_pass());
                    }
                }

            }
        });
        fangShiViewMenu.setAdapter(adapter);
    }

    private void request() {
        Log.e("tag","token:"+SharedPreferencesUtils.getToken(mContext));
        OkHttpUtils.get()
//                .url(Api.GET_BIND_PLACE_LIST)
                .url(Api.GET_DEFAULT_PLACE_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag",e.getMessage());
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
                                keyAdapter = new RBaseAdapter<DefaultPlaceResponse.DataBean.ListkeyBean>(mContext, R.layout.item_city_top, listkey) {
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
                                keyAdapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        click=position;
                                        keyAdapter.notifyDataSetChanged();

                                        if(listener!=null){
                                            DefaultPlaceResponse.DataBean.ListkeyBean item=keyAdapter.getItemData(click);
                                            listener.sure(item.getRequ_id(),click1,item.getEqu_id(),item.getEqu_pass());
                                        }
                                    }
                                });
                                recyclerView.setAdapter(keyAdapter);
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
        void sure(String requ_id, int type,String deviceId,String password);// requ_id 小区id  type0蓝牙 1密码开门 2远程开门
    }
}
