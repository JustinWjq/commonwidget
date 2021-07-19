package com.common.widget.recyclerviewadapterhelper.base.listener;

import android.view.View;

import com.common.widget.recyclerviewadapterhelper.base.TxBaseQuickAdapter;


/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {
        onSimpleItemLongClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(TxBaseQuickAdapter adapter, View view, int position) {
    }

    public abstract void onSimpleItemLongClick(TxBaseQuickAdapter adapter, View view, int position);
}
