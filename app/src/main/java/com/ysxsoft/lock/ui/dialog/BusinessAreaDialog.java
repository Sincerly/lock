package com.ysxsoft.lock.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;

import java.util.List;

/**
 * Create By 胡
 * on 2020/1/7 0007
 */
public class BusinessAreaDialog {

    private PopupWindow popupWindow;
    private OnPopupWindowListener listener;
    private Activity activity;

    public void init(Activity activity) {
        this.activity = activity;
    }

    public void showPopDown(View parentView, int offsetX, int offsetY,String defaultValue,List<String> data,OnPopupWindowListener listener) {
            View view = View.inflate(activity, R.layout.dialog_business_area_layout, null);
            RecyclerView recyclerView=view.findViewById(R.id.recyclerView);

            RBaseAdapter<String> adapter=new RBaseAdapter<String>(activity,R.layout.item_txt,data){

                @Override
                protected void fillItem(RViewHolder holder, String item, int position) {
                    holder.setText(R.id.text,item);
                }

                @Override
                protected int getViewType(String item, int position) {
                    return 0;
                }
            };
            adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RViewHolder holder, View view, int position) {
                    if(listener!=null){
                        listener.onSelected(data.get(position),position);
                    }
                    dismiss();
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
            if(data.size()>8){
                popupWindow.setHeight(DisplayUtils.dp2px(activity,44)*8);
            }
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

    public interface OnPopupWindowListener {
        void onSelected(String name,int position);
    }

}
