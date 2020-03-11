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

        if(m1!=null){
            tvMoney.setText(m1);
        }
        if(m2!=null){
            tvmj.setText("满"+m2+"可用");
        }
        if(t!=null){
            tv3.setText(t);
        }
        if(s!=null){
            tvTime.setText(s+"--"+e);
        }
        if(isOpenLock){
            tvYHQ.setText("本次开门获得"+(m1==null?"0":m1)+"元优惠券");
        }else{
            tvYHQ.setVisibility(View.GONE);
        }

        if(isOpenLock){
            tvTips.setText("门已开启！欢迎回家");
        }else{
            tvTips.setText("获得一张优惠券");
        }
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

    public static CouponDialog show(Context context,boolean isFromLeft,String tips, OnDialogClickListener listener) {
        int themeId=R.style.CenterDialogStyle;
        if(isFromLeft){
            themeId=R.style.LeftDialogOutStyle;
        }
        CouponDialog dialog = new CouponDialog(context, themeId);
        dialog.setListener(listener);
        dialog.showDialog();
        tvTips.setText(tips);
        return dialog;
    }

    public static CouponDialog show(Context context,boolean isFromLeft,boolean isOpen,String money,String money2,String title,String startTime,String endTime, OnDialogClickListener listener) {
        int themeId=R.style.CenterDialogStyle;
        if(isFromLeft){
            themeId=R.style.LeftDialogOutStyle;
        }
        CouponDialog dialog = new CouponDialog(context, themeId);
        dialog.setListener(listener);
        dialog.setData(isOpen,money,money2,title,startTime,endTime);
        dialog.showDialog();
        return dialog;
    }

    public void setData(boolean isOpen,String money,String money2,String title,String startTime,String endTime){
        isOpenLock=isOpen;
        m1=money;
        m2=money2;
        t=title;
        s=startTime;
        e=endTime;
    }

    private String m1,m2,t,s,e;
    private boolean isOpenLock=true;//是否是开锁

    public interface OnDialogClickListener {
        void sure();
    }

    private void setData(){
        //本次开门获得500元优惠券

        //减tvMoney
        //满
    }
}