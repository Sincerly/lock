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

import com.bumptech.glide.Glide;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.view.custom.image.CircleImageView;
import com.ysxsoft.common_base.zxing.util.ZxingUtils;
import com.ysxsoft.lock.R;
import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.config.AppConfig;
import com.ysxsoft.lock.models.CodeBean;

/**
 * 券码弹窗
 * create by Sincerly on 9999/9/9 0009
 **/
public class CodeDialog extends Dialog {
    private  String card_id,logo;
    private Context mContext;
    private OnDialogClickListener listener;

    public CodeDialog(@NonNull Context context, int themeResId,String card_id,String logo) {
        super(context, themeResId);
        this.mContext = context;
        this.card_id = card_id;
        this.logo = logo;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.dialog_code, null);
        CircleImageView civ = view.findViewById(R.id.civ);
        ImageView iv2Code = view.findViewById(R.id.iv2Code);
        ImageView ivQRCode = view.findViewById(R.id.ivQRCode);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvCode = view.findViewById(R.id.tvCode);

        Glide.with(mContext).load(AppConfig.BASE_URL + logo).into(civ);

        CodeBean codeBean= JsonUtils.parseByGson(card_id,CodeBean.class);
        tvCode.setText(codeBean.getId());
        iv2Code.setImageBitmap(ZxingUtils.createBarcode(mContext, card_id, DisplayUtils.dp2px(mContext, 160), DisplayUtils.dp2px(mContext, 54), false));
        ivQRCode.setImageBitmap(ZxingUtils.createQRImage(card_id, DisplayUtils.dp2px(mContext, 135), DisplayUtils.dp2px(mContext, 135), null, "url"));


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

    public static CodeDialog show(Context context, String cardId,String logo/*, OnDialogClickListener listener*/) {
        CodeDialog dialog = new CodeDialog(context, R.style.CenterDialogStyle,cardId,logo);
        /*dialog.setListener(listener);*/
        dialog.showDialog();
        return dialog;
    }

    public interface OnDialogClickListener {
        void sure();
    }
}