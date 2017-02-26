package com.github.daweizhou89.reqlist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class AbstractContentView extends FrameLayout {

    public AbstractContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void requestData();

    public abstract void refreshData();

    public abstract boolean needRequesting();
}
