package com.common.widget.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.TxGlide;
import com.common.widget.glide.load.DecodeFormat;
import com.common.widget.glide.load.engine.Resource;
import com.common.widget.glide.load.engine.bitmaprecycle.BitmapPool;

import java.io.InputStream;

/**
 * An {@link ResourceDecoder} that uses an
 * {@link Downsampler} to decode an {@link Bitmap} from an
 * {@link InputStream}.
 */
public class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap> {
    private static final String ID = "StreamBitmapDecoder.com.txt.video.widget.glide.load.resource.bitmap";
    private final Downsampler downsampler;
    private BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;
    private String id;

    public StreamBitmapDecoder(Context context) {
        this(TxGlide.get(context).getBitmapPool());
    }

    public StreamBitmapDecoder(BitmapPool bitmapPool) {
        this(bitmapPool, DecodeFormat.DEFAULT);
    }

    public StreamBitmapDecoder(Context context, DecodeFormat decodeFormat) {
        this(TxGlide.get(context).getBitmapPool(), decodeFormat);
    }

    public StreamBitmapDecoder(BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this(Downsampler.AT_LEAST, bitmapPool, decodeFormat);
    }

    public StreamBitmapDecoder(Downsampler downsampler, BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this.downsampler = downsampler;
        this.bitmapPool = bitmapPool;
        this.decodeFormat = decodeFormat;
    }

    @Override
    public Resource<Bitmap> decode(InputStream source, int width, int height) {
        Bitmap bitmap = downsampler.decode(source, bitmapPool, width, height, decodeFormat);
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {
        if (id == null) {
            id = new StringBuilder()
                .append(ID)
                .append(downsampler.getId())
                .append(decodeFormat.name())
                .toString();
        }
        return id;
    }
}
