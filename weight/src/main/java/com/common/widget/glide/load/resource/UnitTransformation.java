package com.common.widget.glide.load.resource;

import com.common.widget.glide.load.Transformation;
import com.common.widget.glide.load.engine.Resource;

/**
 * A noop Transformation that simply returns the given resource.
 *
 * @param <T> The type of the resource that will always be returned unmodified.
 */
public class UnitTransformation<T> implements Transformation<T> {
    private static final Transformation<?> TRANSFORMATION = new com.common.widget.glide.load.resource.UnitTransformation<Object>();

    /**
     * Returns a UnitTransformation for the given type.
     *
     * @param <T> The type of the resource to be transformed.
     */
    @SuppressWarnings("unchecked")
    public static <T> com.common.widget.glide.load.resource.UnitTransformation<T> get() {
        return (com.common.widget.glide.load.resource.UnitTransformation<T>) TRANSFORMATION;
    }

    @Override
    public Resource<T> transform(Resource<T> resource, int outWidth, int outHeight) {
        return resource;
    }

    @Override
    public String getId() {
        return "";
    }
}
