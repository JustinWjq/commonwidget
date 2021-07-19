package com.common.widget.aciton;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：Context 意图处理（扩展非 Context 类的方法，请不要用 Context 子类实现此接口）
 */
public interface ResourcesAction {

    Context getContext();

    default Resources getResources() {
        return getContext().getResources();
    }

    default String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    default String getString(@StringRes int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    default Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    @ColorInt
    default int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }
//androidx
//    default <S> S getSystemService(@NonNull Class<S> serviceClass) {
//        return ContextCompat.getSystemService(getContext(), serviceClass);
//    }
}