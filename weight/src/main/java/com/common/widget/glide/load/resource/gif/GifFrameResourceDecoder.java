package com.common.widget.glide.load.resource.gif;

import android.graphics.Bitmap;

import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.load.engine.Resource;
import com.common.widget.glide.load.engine.bitmaprecycle.BitmapPool;
import com.common.widget.glide.load.resource.bitmap.BitmapResource;

class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap> {
    private final BitmapPool bitmapPool;

    public GifFrameResourceDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Resource<Bitmap> decode(GifDecoder source, int width, int height) {
        Bitmap bitmap = source.getNextFrame();
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {
        return "GifFrameResourceDecoder.com.txt.video.widget.glide.load.resource.gif";
    }
}
