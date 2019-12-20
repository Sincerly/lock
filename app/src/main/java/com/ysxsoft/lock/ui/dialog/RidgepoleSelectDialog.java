package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.view.custom.wheel.NumberPickerView;
import com.ysxsoft.lock.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
public class RidgepoleSelectDialog extends Dialog implements View.OnClickListener{
    NumberPickerView picker1;
    private Context mContext;
    private int p1;
    private String[] dataArray1;
    private OnDialogSelectListener listener;
    private String title;

    public RidgepoleSelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }
    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_ridgepole, null);
        TextView sure = view.findViewById(R.id.sure);
        TextView t = view.findViewById(R.id.t);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        picker1 = view.findViewById(R.id.picker1);
        ivClose.setOnClickListener(this);
        sure.setOnClickListener(this);
        if (dataArray1 != null && dataArray1.length != 0) {
            picker1.setDisplayedValuesAndPickedIndex(dataArray1, p1, true);
        }
        if(title!=null){
            t.setText(title);
        }
        return view;
    }

    /**
     * 设置数据源
     */
    public void setData(List<String> datas1, int initP1,OnDialogSelectListener listener) {
        for (int i = 0; i < datas1.size(); i++) {
            datas1.set(i, datas1.get(i));
        }
        this.dataArray1 = datas1.toArray(new String[datas1.size()]);
        this.p1 = initP1;
        if (datas1 != null && datas1.size() != 0) {
            picker1.setDisplayedValuesAndPickedIndex(dataArray1, initP1, true);
        }
        setListener(listener);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivClose) {
            dismiss();
        } else if (id == com.ysxsoft.common_base.R.id.sure) {//确认
            if (listener != null) {
                listener.OnSelect(picker1.getContentByCurrValue(), picker1.getValue());
            }
            dismiss();
        }
    }

    public OnDialogSelectListener getListener() {
        return listener;
    }

    public void setListener(OnDialogSelectListener listener) {
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
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    public interface OnDialogSelectListener {
        void OnSelect(String data1, int position1);
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
