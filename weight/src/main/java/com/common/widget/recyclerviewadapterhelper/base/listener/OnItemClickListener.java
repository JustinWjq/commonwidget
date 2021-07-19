package com.common.widget.recyclerviewadapterhelper.base.listener;

import android.view.View;

import com.common.widget.recyclerviewadapterhelper.base.TxBaseQuickAdapter;


/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * <p>
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 */
public abstract class OnItemClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {
        onSimpleItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemClick(TxBaseQuickAdapter adapter, View view, int position);
}
