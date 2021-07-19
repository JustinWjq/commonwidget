package com.common.widget.recyclerviewadapterhelper.base.listener;

import android.view.View;

import com.common.widget.recyclerviewadapterhelper.base.TxBaseQuickAdapter;


/**
 * Created by AllenCoder on 2016/8/03.
 * A convenience class to extend when you only want to OnItemChildLongClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link com.common.widget.recyclerviewadapterhelper.base.listener.SimpleClickListener}
 **/
public abstract class OnItemChildLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(TxBaseQuickAdapter adapter, View view, int position) {
        onSimpleItemChildLongClick(adapter, view, position);
    }

    public abstract void onSimpleItemChildLongClick(TxBaseQuickAdapter adapter, View view, int position);
}
