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

import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;

/**
 * 券码弹窗
 * create by Sincerly on 9999/9/9 0009
 **/
public class CodeDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;

    public CodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_code, null);
        CircleImageView civ = view.findViewById(R.id.civ);
        ImageView iv2Code = view.findViewById(R.id.iv2Code);
        ImageView ivQRCode = view.findViewById(R.id.ivQRCode);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvCode = view.findViewById(R.id.tvCode);
//        TextView sure = view.findViewById(R.id.sure);
//        TextView cancel = view.findViewById(R.id.cancel);
//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.sure();
//                }
//                dismiss();
//            }
//        });
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

    public static CodeDialog show(Context context, OnDialogClickListener listener) {
        CodeDialog dialog = new CodeDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}