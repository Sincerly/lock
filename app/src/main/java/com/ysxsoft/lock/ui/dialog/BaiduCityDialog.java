package com.ysxsoft.lock.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 核销记录全部弹窗
 * create by Sincerly on 9999/9/9 0009
 **/
public class BaiduCityDialog {
    private PopupWindow popupWindow;
    private OnPopupWindowListener listener;
    private Activity activity;
    private List<PoiInfo> cityList;
    private static BaiduCityDialog instance;

    public static  BaiduCityDialog getInstance() {
        if (instance == null) {
            instance = new BaiduCityDialog();
        }
        return instance;
    }

    public void init(Activity activity) {
        this.activity = activity;
    }

    public void showPopDown(View parentView, int offsetX, int offsetY) {
            View view = View.inflate(activity, R.layout.dialog_baidu, null);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            RBaseAdapter<PoiInfo> adapter = new RBaseAdapter<PoiInfo>(activity, R.layout.item_shop_address_select, cityList) {
                @Override
                protected void fillItem(RViewHolder holder, PoiInfo item, int position) {
                    holder.setText(R.id.tvCity, item.name);
                    holder.setText(R.id.tvAddress, item.address);
                }

                @Override
                protected int getViewType(PoiInfo item, int position) {
                    return 0;
                }

            };
            adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RViewHolder holder, View view, int position) {
                    PoiInfo itemData = adapter.getItemData(position);
                    if (listener != null) {
                        listener.select(itemData);
                    }
                    dismiss();
                }
            });
            recyclerView.setAdapter(adapter);

            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
            popupWindow.showAsDropDown(parentView, 0, 0, Gravity.BOTTOM);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
            popupWindow.setFocusable(true);

    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public boolean isShowing() {
        return popupWindow != null && popupWindow.isShowing();
    }

    public void setOnPopupWindowListener(OnPopupWindowListener listener) {
        this.listener = listener;
    }

    public void setData(List<PoiInfo> cityList) {
        this.cityList = cityList;
    }

    public interface OnPopupWindowListener {
        void select(PoiInfo poiInfo);
    }
}