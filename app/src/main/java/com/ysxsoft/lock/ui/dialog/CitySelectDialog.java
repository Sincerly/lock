package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;

import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.CityUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.view.custom.picker.JsonBean;
import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.models.response.CityResponse;
import com.ysxsoft.lock.models.response.DistrictListResponse;
import com.ysxsoft.lock.models.response.ProvinceListResponse;
import com.ysxsoft.lock.net.Api;
import com.ysxsoft.lock.view.SideLetterBar;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择地区弹窗
 * create by Sincerly on 9999/9/9 0009
 **/
public class CitySelectDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3, tvTip;
    private View viewLine1;
    private View viewLine2;
    private View viewLine3;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private List<JsonBean.CityBean> cityList;

    public CitySelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_city_select, null);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ll1 = view.findViewById(R.id.LL1);
        ll2 = view.findViewById(R.id.LL2);
        ll3 = view.findViewById(R.id.LL3);
        tvTip = view.findViewById(R.id.tvTip);
        tvTip.setText("选择省");
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        viewLine1 = view.findViewById(R.id.viewLine1);
        viewLine2 = view.findViewById(R.id.viewLine2);
        viewLine3 = view.findViewById(R.id.viewLine3);
        SideLetterBar mLetterBar = view.findViewById(R.id.sideletterbar);
        mLetterBar.setVisibility(View.INVISIBLE);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView3.setLayoutManager(new LinearLayoutManager(mContext));
//        ArrayList<JsonBean> province = CityUtils.getProvince(mContext);
//        ll1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(tv1.getText().toString().trim())) {
//                    return;
//                }
//                ll2.setVisibility(View.INVISIBLE);
//                ll3.setVisibility(View.INVISIBLE);
//                recyclerView1.setVisibility(View.VISIBLE);
//                recyclerView2.setVisibility(View.GONE);
//                recyclerView3.setVisibility(View.GONE);
//
//                ArrayList<JsonBean> province = CityUtils.getProvince(mContext);
//                RBaseAdapter<JsonBean> adapter = new RBaseAdapter<JsonBean>(mContext, R.layout.item_city_layout, province) {
//                    @Override
//                    protected void fillItem(RViewHolder holder, JsonBean item, int position) {
//                        holder.setText(R.id.tvName, item.getName());
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                tv1.setText(item.getName());
//                                recyclerView1.setVisibility(View.GONE);
//                                viewLine1.setVisibility(View.GONE);
//                                ll2.setVisibility(View.VISIBLE);
//                                cityList = item.getCityList();
//                                setCityData(recyclerView2, item.getCityList());
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected int getViewType(JsonBean item, int position) {
//                        return 0;
//                    }
//                };
//                recyclerView1.setAdapter(adapter);
//            }
//        });
//
//        ll2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(tv2.getText().toString().trim())) {
//                    return;
//                }
//                ll3.setVisibility(View.INVISIBLE);
//                recyclerView2.setVisibility(View.VISIBLE);
//                recyclerView3.setVisibility(View.GONE);
//                RBaseAdapter<JsonBean.CityBean> rBaseAdapter = new RBaseAdapter<JsonBean.CityBean>(mContext, R.layout.item_city_layout, cityList) {
//                    @Override
//                    protected void fillItem(RViewHolder holder, JsonBean.CityBean item, int position) {
//                        holder.setText(R.id.tvName, item.getName());
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                tv2.setText(item.getName());
//                                recyclerView2.setVisibility(View.GONE);
//                                viewLine2.setVisibility(View.GONE);
//                                ll3.setVisibility(View.VISIBLE);
//                                setAreaData(recyclerView3, item.getArea());
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected int getViewType(JsonBean.CityBean item, int position) {
//                        return 0;
//                    }
//                };
//                recyclerView2.setAdapter(rBaseAdapter);
//            }
//        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        requestData();
        return view;
    }

    private void requestData() {
        OkHttpUtils.get()
                .url(Api.PROVINCE_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ProvinceListResponse resp = JsonUtils.parseByGson(response, ProvinceListResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                List<ProvinceListResponse.DataBean> province = resp.getData();
                                RBaseAdapter<ProvinceListResponse.DataBean> adapter = new RBaseAdapter<ProvinceListResponse.DataBean>(mContext, R.layout.item_city_layout, province) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, ProvinceListResponse.DataBean item, int position) {
                                        holder.setText(R.id.tvName, item.getName());
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tv1.setText(item.getName());
                                                recyclerView1.setVisibility(View.GONE);
                                                viewLine1.setVisibility(View.GONE);
                                                ll2.setVisibility(View.VISIBLE);
                                                tvTip.setText("选择市");
                                                setCityData(recyclerView2, item.getCode(),item.getName(),item.getCode());
                                            }
                                        });
                                    }

                                    @Override
                                    protected int getViewType(ProvinceListResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView1.setAdapter(adapter);
                            }
                        }
                    }
                });

    }

    /**
     * 设置城市
     *
     * @param recyclerView2
     * @param provinceCode
     * @param provinceName
     */
    private void setCityData(RecyclerView recyclerView2, String code, String provinceName, String provinceCode) {
        OkHttpUtils.get()
                .url(Api.CITY_LIST)
                .addParams("provincecode", code)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CityResponse city = JsonUtils.parseByGson(response, CityResponse.class);
                        if (city != null) {
                            if (HttpResponse.SUCCESS.equals(city.getCode())) {
                                List<CityResponse.DataBean> citys = city.getData();
                                RBaseAdapter<CityResponse.DataBean> rBaseAdapter = new RBaseAdapter<CityResponse.DataBean>(mContext, R.layout.item_city_layout, citys) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, CityResponse.DataBean item, int position) {
                                        holder.setText(R.id.tvName, item.getName());
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tv2.setText(item.getName());
                                                recyclerView2.setVisibility(View.GONE);
                                                viewLine2.setVisibility(View.GONE);
                                                ll3.setVisibility(View.VISIBLE);
                                                tvTip.setText("选择区县");
                                                setAreaData(recyclerView3, item.getCode(),provinceName,provinceCode,item.getName());
                                            }
                                        });
                                    }

                                    @Override
                                    protected int getViewType(CityResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView2.setAdapter(rBaseAdapter);
                            }
                        }
                    }
                });


    }

    private void setAreaData(RecyclerView recyclerView3, String code, String provinceName, String provinceCode ,String cityName) {
        OkHttpUtils.get()
                .url(Api.DISTRICT_LIST)
                .addParams("citycode", code)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DistrictListResponse resp = JsonUtils.parseByGson(response, DistrictListResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                List<DistrictListResponse.DataBean> areas = resp.getData();
                                RBaseAdapter<DistrictListResponse.DataBean> rBaseAdapter = new RBaseAdapter<DistrictListResponse.DataBean>(mContext, R.layout.item_city_layout, areas) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, DistrictListResponse.DataBean item, int position) {
                                        holder.setText(R.id.tvName, item.getName());
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tv3.setText(item.getName());
                                                viewLine3.setVisibility(View.GONE);
                                                if (listener != null) {
                                                    dismiss();
                                                    listener.sure(provinceName,provinceCode,cityName,code, item.getName(),item.getCode());
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    protected int getViewType(DistrictListResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView3.setAdapter(rBaseAdapter);
                            }
                        }
                    }
                });
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

    public void showDialog() {
        if (!isShowing()) {
            show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
//          lp.width = DisplayUtils.getDisplayWidth(mContext) * 4 / 5;
            lp.width = DisplayUtils.getDisplayWidth(mContext);
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = DisplayUtils.getDisplayHeight(mContext) * 2 / 3;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    public static CitySelectDialog show(Context context, OnDialogClickListener listener) {
        CitySelectDialog dialog = new CitySelectDialog(context, R.style.BottomDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure(String proviceName,String proviceCode, String cityName, String cityCode, String areaName, String areaCode);
    }
}