package com.github.daweizhou89.listview.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import java.util.List;

/**
 * Created by daweizhou89 on 2016/11/2.
 */
public abstract class AnimationBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static int DEFAULT_DURATION_TIME = 500;

    private int mDuration = DEFAULT_DURATION_TIME;
    private Interpolator mInterpolator = new OvershootInterpolator(.5f);
    private int mLastPosition = -1;

    private boolean mFirstOnly = true;

    protected final Context mContext;

    protected final LayoutInflater mLayoutInflater;

    public AnimationBaseAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public final Context getContext() {
        return mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindViewHolderII(holder, position);
        tryStartAnim(holder);
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        onBindViewHolderII(holder, position, payloads);
        tryStartAnim(holder);
    }

    private void tryStartAnim(VH holder)  {
        int adapterPosition = holder.getAdapterPosition();
        if (!mFirstOnly || adapterPosition > mLastPosition) {
            for (Animator anim : getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = adapterPosition;
        } else {
            clear(holder.itemView);
        }
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        mLastPosition = start;
    }

    public void setFirstOnly(boolean firstOnly) {
        mFirstOnly = firstOnly;
    }

    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getMeasuredHeight(), 0)
        };
    }

    protected abstract void onBindViewHolderII(VH holder, int position);

    protected void onBindViewHolderII(VH holder, int position, List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    public static void clear(View v) {
        ViewCompat.setAlpha(v, 1);
        ViewCompat.setScaleY(v, 1);
        ViewCompat.setScaleX(v, 1);
        ViewCompat.setTranslationY(v, 0);
        ViewCompat.setTranslationX(v, 0);
        ViewCompat.setRotation(v, 0);
        ViewCompat.setRotationY(v, 0);
        ViewCompat.setRotationX(v, 0);
        ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
        ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
        ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
    }
}
