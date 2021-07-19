package com.common.widget.glide.load.engine.bitmaprecycle;

import android.graphics.Bitmap;

public interface LruPoolStrategy {
    void put(Bitmap bitmap);
    Bitmap get(int width, int height, Bitmap.Config config);
    Bitmap removeLast();
    String logBitmap(Bitmap bitmap);
    String logBitmap(int width, int height, Bitmap.Config config);
    int getSize(Bitmap bitmap);
}
