package com.common.widget.dialog.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.transition.Visibility;

import com.google.android.material.transition.MaterialFade;
import com.common.widget.dialog.R;
import com.common.widget.dialog.XPopup;
import com.common.widget.dialog.core.CenterPopupView;
import com.common.widget.dialog.util.XPopupUtils;
import com.common.widget.dialog.widget.LoadingView;

/**
 * Description: 加载对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {
    private TextView tv_title;

    /**
     * @param context
     * @param bindLayoutId layoutId 如果要显示标题，则要求必须有id为tv_title的TextView，否则无任何要求
     */
    public LoadingPopupView(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout._xpopup_center_impl_loading;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tv_title = findViewById(R.id.tv_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getPopupImplView().setElevation(10f);
        }
        if (bindLayoutId == 0) {
            getPopupImplView().setBackground(XPopupUtils.createDrawable(Color.parseColor("#CF000000"), popupInfo.borderRadius));
        }
        setup();
    }

    protected void setup() {
        if (tv_title == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(centerPopupContainer, new TransitionSet()
                        .setDuration(getAnimationDuration())
                        .addTransition(new Fade())
                        .addTransition(new ChangeBounds()));
                if (title == null || title.length() == 0) {
                    tv_title.setVisibility(GONE);
                } else {
                    tv_title.setVisibility(VISIBLE);
                    tv_title.setText(title);
                }
            }
        });
    }

    private CharSequence title;

    public LoadingPopupView setTitle(CharSequence title) {
        this.title = title;
        setup();
        return this;
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        if (tv_title == null) return;
        tv_title.setText("");
        tv_title.setVisibility(GONE);
    }
}
