package com.common.widget.glide.load.model.stream;

import android.content.Context;

import com.common.widget.glide.load.model.GlideUrl;
import com.common.widget.glide.load.model.ModelLoader;
import com.common.widget.glide.load.model.GenericLoaderFactory;
import com.common.widget.glide.load.model.ModelLoaderFactory;
import com.common.widget.glide.load.model.UrlLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * A wrapper class that translates {@link URL} objects into {@link GlideUrl}
 * objects and then uses the wrapped {@link ModelLoader} for
 * {@link GlideUrl}s to load the {@link InputStream} data.
 */
public class StreamUrlLoader extends UrlLoader<InputStream> {

    /**
     * The default factory for {@link StreamUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<URL, InputStream> {
        @Override
        public ModelLoader<URL, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.common.widget.glide.load.model.stream.StreamUrlLoader(factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamUrlLoader(ModelLoader<GlideUrl, InputStream> glideUrlLoader) {
        super(glideUrlLoader);
    }
}
