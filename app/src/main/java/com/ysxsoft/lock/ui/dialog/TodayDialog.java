package com.ysxsoft.lock.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;

/**
 * 核销记录今日弹窗
 * create by Sincerly on 9999/9/9 0009
 **/
public class TodayDialog {
    private PopupWindow popupWindow;
    private OnPopupWindowListener listener;
    private Activity activity;

    public void init(Activity activity) {
        this.activity = activity;
    }

    public void showPopDown(View parentView, int offsetX, int offsetY) {
        View view = View.inflate(activity, R.layout.dialog_today, null);
        if (popupWindow == null) {
            TextView tv2 = view.findViewById(R.id.tv2);
            TextView tv3 = view.findViewById(R.id.tv3);
            TextView tv4 = view.findViewById(R.id.tv4);
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.select("现金券",1);
                    }
                    dismiss();
                }
            });
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.select("团购套餐",2);
                    }
                    dismiss();
                }
            });
            tv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.select("免费体验",3);
                    }
                    dismiss();
                }
            });
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
            popupWindow.showAsDropDown(parentView, 0, 0, Gravity.BOTTOM);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Intent intent = new Intent("UPDATE_TEXT");
                    intent.putExtra("flag","1");
                    activity.sendBroadcast(intent);
                }
            });
            popupWindow.setFocusable(true);
        }else {
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
        void select(String  type,int today);
    }
}