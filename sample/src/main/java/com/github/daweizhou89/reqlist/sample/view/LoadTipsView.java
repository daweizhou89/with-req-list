package com.github.daweizhou89.reqlist.sample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.daweizhou89.reqlist.view.ILoadTips;

/**
 * Created by zhoudawei on 2017/2/26.
 */

public class LoadTipsView extends FrameLayout implements ILoadTips, View.OnClickListener {

    private OnReloadListener mOnReloadListener;

    private TextView mTextView;

    private String mEmptyText;

    public LoadTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xFF2e2e2e);
        textView.setTextSize(16);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(textView, layoutParams);
        textView.setOnClickListener(this);
        mTextView = textView;
    }

    @Override
    public void setTips(int tips) {
        switch (tips) {
            case TIPS_LOADED_EMPTY:
                mTextView.setText(mEmptyText == null ? "No Data!" : mEmptyText);
                setVisibility(View.VISIBLE);
                break;
            case TIPS_LOADED_FAIL:
                mTextView.setText("Loading Failed!");
                setVisibility(View.VISIBLE);
                break;
            case TIPS_LOADED_NO_NETWORK:
                mTextView.setText("No Network!");
                setVisibility(View.VISIBLE);
                break;
            case TIPS_LOADING:
                mTextView.setText("Loading!");
                setVisibility(View.VISIBLE);
                break;
            case TIPS_LOADED_OK:
                setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setEmptyText(String emptyText) {
        mEmptyText = emptyText;
    }

    @Override
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        mOnReloadListener = onReloadListener;
    }

    @Override
    public void onClick(View view) {
        if (mOnReloadListener != null) {
            mOnReloadListener.onReload();
        }
    }
}
