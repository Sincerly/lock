package com.ysxsoft.lock.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ysxsoft.lock.R;

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

    public void showPopDown(View parentView, int offsetX, int offsetY) {
        if (popupWindow == null) {
            View view = View.inflate(activity, R.layout.dialog_business_area_layout, null);

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
        } else {
            popupWindow.showAsDropDown(parentView, 0, 0, Gravity.BOTTOM);
        }
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
        void select(String  type,int allDay);
    }

}
