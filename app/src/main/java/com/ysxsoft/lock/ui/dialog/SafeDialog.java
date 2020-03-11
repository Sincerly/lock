package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ysxsoft.common_base.R;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.ui.activity.WebViewActivity;

/**
 * create by Sincerly on 2019/1/28 0028
 **/
public class SafeDialog extends Dialog {
    private Context mContext;
    private String content = "";
    private OnDialogClickListener listener;
    private TextView message;

    public OnDialogClickListener getListener() {
        return listener;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public SafeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_safe, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView sure = view.findViewById(R.id.sure);
        message = view.findViewById(R.id.message);
        String str="请你务必审慎阅读、充分理解\"服务协议\"和\"隐私政策\"各条款，包括但不限于:为了向你提供即时通讯、内容分享等服务，我们需要收集你的设备信息、操作日志等个人信息。你可以在\"设置\"中查看、变更、删除个人信息并管理你的授权。你可阅读《服务协议》和《隐私政策》了解详细信息。如你同意，请点击\"同意\"开始接受我们的服务。";
        SpannableString ss=new SpannableString(str);

        int index=str.indexOf("《服务协议》");
        int index2=str.indexOf("《隐私政策》");

        ClickSpan clickSpan=new ClickSpan(new ClickSpan.OnSpanClickListener() {
            @Override
            public void click() {
                //服务协议
//                ToastUtils.shortToast(mContext,"服务协议");
                WebViewActivity.start("服务协议","http://www.baidu.com");
            }
        });
        ss.setSpan(clickSpan,index,index+6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ClickSpan clickSpan2=new ClickSpan(new ClickSpan.OnSpanClickListener() {
            @Override
            public void click() {
                //隐私政策
//                ToastUtils.shortToast(mContext,"隐私政策");
                WebViewActivity.start("隐私政策","http://www.baidu.com");
            }
        });
        ss.setSpan(clickSpan2,index2,index2+6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        message.setText(ss);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sure();
                }
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (content != null && !"".equals(content)) {
            message.setText(content);
        }
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(init());
    }

    public void showDialog() {
        if (!isShowing()) {
            show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = DisplayUtils.getDisplayWidth(mContext) * 4 / 5;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    public void initContent(String content) {
        this.content = content;
    }

    public interface OnDialogClickListener {
        void sure();
    }

    public static class ClickSpan extends ClickableSpan{
        private OnSpanClickListener listener;

        public ClickSpan(OnSpanClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#169CF9"));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(@NonNull View widget) {
            if(listener!=null){
                listener.click();
            }
        }

        public interface OnSpanClickListener{
            void click();
        }
    }
}