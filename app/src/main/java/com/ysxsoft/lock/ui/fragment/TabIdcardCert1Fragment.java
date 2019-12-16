package com.ysxsoft.lock.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.common_base.base.BaseFragment;
import com.ysxsoft.common_base.utils.JsonUtils;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.ysxsoft.lock.R;
import com.ysxsoft.lock.net.Api;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * create by Sincerly on 9999/9/9 0009
 **/
public class TabIdcardCert1Fragment extends BaseFragment {


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.IdCardNum)
    EditText IdCardNum;
    @BindView(R.id.inputPhoneNum)
    EditText inputPhoneNum;
    @BindView(R.id.tvOk)
    TextView tvOk;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabidcardcert1;
    }

    @Override
    protected void doWork(View view) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @OnClick(R.id.tvOk)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvOk:
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    showToast("姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(IdCardNum.getText().toString().trim())) {
                    showToast("身份证号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(inputPhoneNum.getText().toString().trim())) {
                    showToast("手机号不能为空");
                    return;
                }
                break;
        }

    }

    public void request() {
        showLoadingDialog("请求中");
        OkHttpUtils.post()
//                .url(Api.GET_CODE)
                .addParams("uid", SharedPreferencesUtils.getUid(getActivity()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
//                        ListResponse resp = JsonUtils.parseByGson(response, ListResponse.class);
//                        if (resp != null) {
//                                if (HttpResponse.SUCCESS.equals(resp.getCode())) {
//                                    //请求成功
//                                    List<ListResponse.DataBean> data = resp.getData();
//                                    manager.setData(data);
//                                } else {
//                                    //请求失败
//                                    showToast(resp.getMsg());
//                                }
//                        } else {
//                            showToast("获取失败");
//                        }
                    }
                });
    }
}
