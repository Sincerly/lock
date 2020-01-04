package com.ysxsoft.lock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TouchGroup extends ViewGroup {
    public TouchGroup(Context context) {
        super(context);
    }

    public TouchGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View left=getChildAt(0);

    }
}
