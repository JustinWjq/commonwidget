package com.common.widget.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DELL on 2017/4/17.
 */

public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


}
