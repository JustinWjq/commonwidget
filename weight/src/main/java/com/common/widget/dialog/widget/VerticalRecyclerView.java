package com.common.widget.dialog.widget;

import android.content.Context;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.common.widget.R;
import com.common.widget.dialog.util.XPopupUtils;


/**
 * Description:
 * Create by dance, at 2018/12/12
 */
public class VerticalRecyclerView extends RecyclerView {
    public VerticalRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setupDivider(Boolean isDark){
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(getResources().getColor(isDark ? R.color.tx_xpopup_list_dark_divider : R.color.tx_xpopup_list_divider));
        drawable.setSize(10, XPopupUtils.dp2px(getContext(), .4f));
        decoration.setDrawable(drawable);
        addItemDecoration(decoration);
    }

}
