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

import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;

/**
* 推荐优惠券弹窗
* create by Sincerly on 9999/9/9 0009
**/
public class CouponDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private static TextView tvTips;

    public CouponDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_coupon, null);
        TextView tvYHQ = view.findViewById(R.id.tvYHQ);
        tvTips = view.findViewById(R.id.tvTips);
        TextView tvMoney = view.findViewById(R.id.tvMoney);
        TextView tvmj = view.findViewById(R.id.tvmj);
        TextView tv3 = view.findViewById(R.id.tv3);
        TextView tvRule = view.findViewById(R.id.tvRule);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView sure = view.findViewById(R.id.sure);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.sure();
                }
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

    public static CouponDialog show(Context context,String tips, OnDialogClickListener listener) {
        CouponDialog dialog = new CouponDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        tvTips.setText(tips);
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}