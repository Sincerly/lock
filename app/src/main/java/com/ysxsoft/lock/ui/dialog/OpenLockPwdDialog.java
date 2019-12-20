package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
public class OpenLockPwdDialog extends Dialog {
    private String pwd;
    private Context mContext;
    private OnDialogClickListener listener;

    public OpenLockPwdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    //    dialog_open_lock_pwd
    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_open_lock_pwd, null);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 6));
        if (!TextUtils.isEmpty(pwd)) {
            char[] chars = pwd.toCharArray();
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < chars.length; i++) {
                Log.e("tag", ">>>>>>==" + chars[i]);
                strings.add(String.valueOf(chars[i]));
            }
            RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.dialog_item_open_lock_pwd, strings) {
                @Override
                protected void fillItem(RViewHolder holder, String item, int position) {
                    holder.setText(R.id.tv,item);
                }

                @Override
                protected int getViewType(String item, int position) {
                    return 0;
                }
            };

            recyclerView.setAdapter(adapter);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    /**
     * 设置数据
     *
     * @param pwd
     */
    public void setData(String pwd) {
        this.pwd = pwd;
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

    public static OpenLockPwdDialog show(Context context, String pwd, OnDialogClickListener listener) {
        OpenLockPwdDialog dialog = new OpenLockPwdDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        dialog.setData(pwd);
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}
