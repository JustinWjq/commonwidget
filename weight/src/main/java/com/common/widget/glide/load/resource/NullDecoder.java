package com.common.widget.glide.load.resource;

import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.load.engine.Resource;

/**
 * A simple {@link ResourceDecoder} that always returns null.
 *
 * @param <T> The type of the data that will be ignored by this class.
 * @param <Z> The type of the decoded resource that will always be null.
 */
public class NullDecoder<T, Z> implements ResourceDecoder<T, Z> {
    private static final com.common.widget.glide.load.resource.NullDecoder<?, ?> NULL_DECODER = new com.common.widget.glide.load.resource.NullDecoder<Object, Object>();

    /**
     * Returns an instance of the NullDecoder for the given types.
     *
     * @param <T> The data type.
     * @param <Z> The resource type.
     */
    @SuppressWarnings("unchecked")
    public static <T, Z> com.common.widget.glide.load.resource.NullDecoder<T, Z> get() {
        return (com.common.widget.glide.load.resource.NullDecoder<T, Z>) NULL_DECODER;
    }

    @Override
    public Resource<Z> decode(T source, int width, int height) {
        return null;
    }

    @Override
    public String getId() {
        return "";
    }
}
