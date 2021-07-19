package com.common.widget.glide.load.resource;

import com.common.widget.glide.load.Encoder;

import java.io.OutputStream;

/**
 * A simple {@link Encoder} that never writes data.
 *
 * @param <T> type discarded by this Encoder
 */
public class NullEncoder<T> implements Encoder<T> {
    private static final com.common.widget.glide.load.resource.NullEncoder<?> NULL_ENCODER = new com.common.widget.glide.load.resource.NullEncoder<Object>();

    /**
     * Returns an Encoder for the given data type.
     *
     * @param <T> The type of data to be written (or not in this case).
     */
    @SuppressWarnings("unchecked")
    public static <T> Encoder<T> get() {
        return (Encoder<T>) NULL_ENCODER;

    }

    @Override
    public boolean encode(T data, OutputStream os) {
        return false;
    }

    @Override
    public String getId() {
        return "";
    }
}
