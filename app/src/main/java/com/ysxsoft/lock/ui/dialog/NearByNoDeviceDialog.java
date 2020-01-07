package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.R;

import androidx.annotation.NonNull;

/**
 * Create By 胡
 * on 2019/12/28 0028
 */
public class NearByNoDeviceDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    public NearByNoDeviceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        init();
    }
    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_near_by_no_device, null);
        TextView sure = view.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
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

    public static NearByNoDeviceDialog show(Context context/*, OnDialogClickListener listener*/) {
        NearByNoDeviceDialog dialog = new NearByNoDeviceDialog(context, R.style.CenterDialogStyle);
//        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
    }
}
