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
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create By 胡
 * on 2019/12/30 0030
 */
public class PacketTypeSelectDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private ArrayList<String> list;

    public int getOnClick() {
        return onClick;
    }

    public void setOnClick(int onClick) {
        this.onClick = onClick;
    }

    private int onClick=-1;// 0 现金券 1 团购套餐 2 免费体验 3 会员卡

    public PacketTypeSelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    private View init() {
        list = new ArrayList<>();
        list.add("现金券");
        list.add("团购套餐");
        list.add("免费体验");
        list.add("会员卡");
        View view = View.inflate(mContext, R.layout.dialog_packet_type_select, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_dialog_packet_type_select, list) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(item);
                ImageView iv = holder.getView(R.id.iv);
                if (onClick==position){
                    tv.setTextColor(mContext.getResources().getColor(R.color.color_3BB0D2));
                    iv.setVisibility(View.VISIBLE);
                }else {
                    tv.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                    iv.setVisibility(View.GONE);
                }
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        adapter.setOnItemClickListener(new RBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, View view, int position) {
                onClick=position;
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView sure = view.findViewById(R.id.tvOk);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sure(list.get(onClick),onClick);
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

    public static PacketTypeSelectDialog show(Context context,int onClick, OnDialogClickListener listener) {
        PacketTypeSelectDialog dialog = new PacketTypeSelectDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        dialog.setOnClick(onClick);
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure(String data1,int type);
    }
}
