package com.ysxsoft.lock.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysxsoft.common_base.net.HttpResponse;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.ysxsoft.common_base.utils.ToastUtils;
import com.ysxsoft.lock.R;
import com.ysxsoft.lock.models.response.CheckAddressResponse;
import com.ysxsoft.lock.models.response.resp.CommentResponse;
import com.ysxsoft.lock.net.Api;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import androidx.annotation.NonNull;
import okhttp3.Call;

/**
 * Create By 胡
 * on 2019/12/18 0018
 */
public class CheckAddressDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private TextView tvAddress;
    private String requid;

    public CheckAddressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
        requestData();
    }

    private void requestData() {
        OkHttpUtils.get()
                .url(Api.GET_NEAR_FLOOR_INFO)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("lat", "")
                .addParams("lng", "")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CheckAddressResponse resp = JsonUtils.parseByGson(response, CheckAddressResponse.class);
                        if (resp != null) {
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                requid = resp.getData().getId();
                                tvAddress.setText(resp.getData().getAddress() + resp.getData().getQuarters_name());
                            }
                        }
                    }
                });
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_check_address, null);
        tvAddress = view.findViewById(R.id.tvAddress);
        TextView sure = view.findViewById(R.id.sure);
        TextView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancle();
                }
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sure(requid);
                }
                submintData();
            }
        });
        return view;
    }

    private void submintData() {
        if(TextUtils.isEmpty(requid)){
            return;
        }
        OkHttpUtils.post()
                .url(Api.BIND_PLACE)
                .addHeader("Authorization", SharedPreferencesUtils.getToken(mContext))
                .addParams("requid", requid)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            ToastUtils.show(mContext,resp.getMsg());
                            if (HttpResponse.SUCCESS.equals(resp.getCode())) {
                                dismiss();
                            }
                        }
                    }
                });

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

    public static CheckAddressDialog show(Context context, OnDialogClickListener listener) {
        CheckAddressDialog dialog = new CheckAddressDialog(context, R.style.CenterDialogStyle);
        dialog.setListener(listener);
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure(String requid);

        void cancle();
    }
}
