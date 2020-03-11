package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;

/**
* 选择付款方式弹窗
* create by Sincerly on 9999/9/9 0009
**/
public class SelectPayMethodDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private int type=2;//默认支付宝支付
    private static TextView tvMoney;

    public SelectPayMethodDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        Drawable no = mContext.getResources().getDrawable(R.mipmap.icon_card_normal);
        no.setBounds(0, 0, no.getMinimumWidth(), no.getMinimumHeight());// 设置边界
        Drawable ok = mContext.getResources().getDrawable(R.mipmap.icon_card_select);
        ok.setBounds(0, 0, ok.getMinimumWidth(), ok.getMinimumHeight());// 设置边界
        View view = View.inflate(mContext, R.layout.dialog_select_pay_method, null);
        LinearLayout LL1 = view.findViewById(R.id.LL1);
        LinearLayout LL2 = view.findViewById(R.id.LL2);
        TextView tvOk = view.findViewById(R.id.tvOk);
        tvMoney = view.findViewById(R.id.tvMoney);
        TextView tvWeChat = view.findViewById(R.id.tvWeChat);
        tvWeChat.setCompoundDrawables(null,null,ok,null);
        TextView tvAliPay = view.findViewById(R.id.tvAliPay);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        tvAliPay.setCompoundDrawables(null,null,ok,null);

        LL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                tvWeChat.setCompoundDrawables(null,null,ok,null);
                tvAliPay.setCompoundDrawables(null,null,no,null);
            }
        });
        LL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                tvWeChat.setCompoundDrawables(null,null,no,null);
                tvAliPay.setCompoundDrawables(null,null,ok,null);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sure(type);
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
//          lp.width = DisplayUtils.getDisplayWidth(mContext) * 4 / 5;
            lp.width = DisplayUtils.getDisplayWidth(mContext);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    public static SelectPayMethodDialog show(Context context,String str, OnDialogClickListener listener) {
//        SelectPayMethodDialog dialog = new SelectPayMethodDialog(context, R.style.CenterDialogStyle);
        SelectPayMethodDialog dialog = new SelectPayMethodDialog(context, R.style.BottomDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        tvMoney.setText(str);
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure(int type);
    }
}