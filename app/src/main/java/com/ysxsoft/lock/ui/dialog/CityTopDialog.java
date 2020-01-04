package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.ysxsoft.lock.models.response.TabKeyManager1FragmentResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

    public CityTopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_city_top, null);
        recyclerView = view.findViewById(R.id.recyclerView);
        fangShiViewMenu = view.findViewById(R.id.fangShiViewMenu);
        request();
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

    private void request() {
        OkHttpUtils.get()
                .url(Api.GET_BIND_PLACE_LIST)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("reqid", "")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TabKeyManager1FragmentResponse gson = JsonUtils.parseByGson(response, TabKeyManager1FragmentResponse.class);
                        if (gson != null) {
                            if (HttpResponse.SUCCESS.equals(gson.getCode())) {

                                recyclerView.setAdapter(null);
                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                RBaseAdapter<TabKeyManager1FragmentResponse.DataBean> adapter = new RBaseAdapter<TabKeyManager1FragmentResponse.DataBean>(getActivity(), R.layout.item_tab_key_manager_list, groups) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, TabKeyManager1FragmentResponse.DataBean item, int position) {
                                        TextView tvNormal = holder.getView(R.id.tvNormal);
                                        TextView tvAddress = holder.getView(R.id.tvAddress);
                                        TextView tvName = holder.getView(R.id.tvName);
                                        tvName.setText(item.getQuarters_name());
                                        tvAddress.setText(item.getAddress());
                                        RecyclerView itemRecyclerView = holder.getView(R.id.itemRecyclerView);
                                        if (item.isExpanded()) {
                                            itemRecyclerView.setVisibility(View.VISIBLE);
                                            tvName.setSelected(true);
                                        } else {
                                            itemRecyclerView.setVisibility(View.GONE);
                                            tvName.setSelected(false);
                                        }
                                        if (item.getIsdefault() == 1) {
                                            tvNormal.setVisibility(View.VISIBLE);
                                        } else {
                                            tvNormal.setVisibility(View.GONE);
                                        }
                                        initRecyclerView(itemRecyclerView, item.getListkey(), position);
                                    }

                                    @Override
                                    protected int getViewType(TabKeyManager1FragmentResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(RViewHolder holder, View view, int position) {
                                        for (int i = 0; i < groups.size(); i++) {
                                            if (i == position) {
                                                if (groups.get(i).isExpanded()) {
                                                    groups.get(i).setExpanded(false);
                                                } else {
                                                    groups.get(i).setExpanded(true);
                                                }
                                            } else {
                                                groups.get(i).setExpanded(false);
                                            }
                                        }
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
            lp.width = DisplayUtils.getDisplayWidth(mContext) * 4 / 5;
//            lp.width = DisplayUtils.getDisplayWidth(mContext);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    public static CityTopDialog show(Context context, OnDialogClickListener listener) {
        CityTopDialog dialog = new CityTopDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}
