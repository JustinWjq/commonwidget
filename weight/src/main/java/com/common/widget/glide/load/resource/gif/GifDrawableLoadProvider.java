package com.common.widget.glide.load.resource.gif;

import android.content.Context;

import com.common.widget.glide.provider.DataLoadProvider;
import  com.common.widget.glide.load.Encoder;
import  com.common.widget.glide.load.ResourceDecoder;
import  com.common.widget.glide.load.ResourceEncoder;
import  com.common.widget.glide.load.engine.bitmaprecycle.BitmapPool;
import com.common.widget.glide.load.model.StreamEncoder;
import com.common.widget.glide.load.resource.file.FileToStreamDecoder;

import java.io.File;
import java.io.InputStream;

/**
 * An {@link DataLoadProvider} that loads an {@link InputStream} into
 * {@link GifDrawable} that can be used to display an animated GIF.
 */
public class GifDrawableLoadProvider implements DataLoadProvider<InputStream, GifDrawable> {
    private final GifResourceDecoder decoder;
    private final GifResourceEncoder encoder;
    private final StreamEncoder sourceEncoder;
    private final FileToStreamDecoder<GifDrawable> cacheDecoder;

    public GifDrawableLoadProvider(Context context, BitmapPool bitmapPool) {
        decoder = new GifResourceDecoder(context, bitmapPool);
        cacheDecoder = new FileToStreamDecoder<GifDrawable>(decoder);
        encoder = new GifResourceEncoder(bitmapPool);
        sourceEncoder = new StreamEncoder();
    }

    @Override
    public ResourceDecoder<File, GifDrawable> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<InputStream, GifDrawable> getSourceDecoder() {
        return decoder;
    }

    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<GifDrawable> getEncoder() {
        return encoder;
    }
}
