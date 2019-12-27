package com.ysxsoft.lock.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.view.custom.image.RoundImageView;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.base.RBaseAdapter;
import com.ysxsoft.lock.base.RViewHolder;
import com.ysxsoft.lock.models.response.VillageResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;

/**
 * Create By 胡
 * on 2019/12/27 0027
 */
public class SelectVillageDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private RecyclerView recyclerView;
    private int isClick = -1;

    public SelectVillageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        Log.e("tag", "SelectVillageDialog===");
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_village_layout, null);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add(String.valueOf(i));
        }
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_dialog_village_layout, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                RoundImageView riv = holder.getView(R.id.riv);
//                Glide.with(mContext).load("").into(riv);
//                holder.setText(R.id.tv1, "");
//                holder.setText(R.id.tvAddress, "");
                ImageView iv = holder.getView(R.id.iv);
                if (isClick == position) {
                    iv.setVisibility(View.VISIBLE);
                } else {
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
                isClick = position;
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(init());
        Log.e("tag", "onCreate===");
    }

    public void showDialog() {
        if (!isShowing()) {
            show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = DisplayUtils.getDisplayWidth(mContext);
            lp.height = DisplayUtils.getDisplayHeight(mContext) * 2 / 3;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    public OnDialogClickListener getListener() {
        return listener;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public static SelectVillageDialog show(Context context, OnDialogClickListener listener) {
        SelectVillageDialog dialog = new SelectVillageDialog(context, R.style.BottomDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();

        return dialog;
    }


    public interface OnDialogClickListener {
        void sure(String villageName, String villageCode);
    }
}
