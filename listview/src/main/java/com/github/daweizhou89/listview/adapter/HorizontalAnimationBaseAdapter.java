package com.github.daweizhou89.listview.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ****  *   *   ***  *  *
 * *     *   *  *     * *
 * ****  *   *  *     **
 * *     *   *  *     * *
 * *      ***    ***  *  *
 * <p>
 * Created by daweizhou89 on 2016/11/2.
 */

public abstract class HorizontalAnimationBaseAdapter<VH extends RecyclerView.ViewHolder> extends AnimationBaseAdapter<VH> {

    public HorizontalAnimationBaseAdapter(Context context) {
        super(context);
    }

    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.getMeasuredWidth(), 0)
        };
    }

}
