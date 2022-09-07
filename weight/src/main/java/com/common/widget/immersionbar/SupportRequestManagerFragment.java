package com.common.widget.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/11 6:43 PM
 */
public final class SupportRequestManagerFragment extends Fragment {

    private TxImmersionDelegate mDelegate;

    public TxImmersionBar get(Object o) {
        if (mDelegate == null) {
            mDelegate = new TxImmersionDelegate(o);
        }
        return mDelegate.get();
    }

    public TxImmersionBar get(Activity activity, Dialog dialog) {
        if (mDelegate == null) {
            mDelegate = new TxImmersionDelegate(activity, dialog);
        }
        return mDelegate.get();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mDelegate != null) {
            mDelegate.onActivityCreated(getResources().getConfiguration());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDelegate != null) {
            mDelegate.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDelegate != null) {
            mDelegate.onDestroy();
            mDelegate = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDelegate != null) {
            mDelegate.onConfigurationChanged(newConfig);
        }
    }
}
