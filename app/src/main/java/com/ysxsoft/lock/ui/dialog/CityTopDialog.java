package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.R;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
public class CityTopDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;

    public CityTopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_city_top, null);
        TextView sure = view.findViewById(R.id.sure);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sure();
                }
                dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
