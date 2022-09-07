package com.common.widget.aciton;

import androidx.annotation.IdRes;
import android.view.View;

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：点击事件意图
 */
public interface ClickAction extends View.OnClickListener {

    <V extends View> V findViewById(@IdRes int id);

    default void setOnClickListener(@IdRes int... ids) {
        setOnClickListener(this, ids);
    }

    default void setOnClickListener(View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    default void setOnClickListener(View... views) {
       setOnClickListener(this, views);
    }

    default void setOnClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    default void onClick(View view) {
        // 默认不实现，让子类实现
    }
}