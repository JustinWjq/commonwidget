package com.common.widget.glide.load.resource.transcode;

import com.common.widget.glide.load.engine.Resource;

/**
 * A simple {@link ResourceTranscoder} that simply returns the given resource.
 *
 * @param <Z> The type of the resource that will be transcoded from and to.
 */
public class UnitTranscoder<Z> implements ResourceTranscoder<Z, Z> {
    private static final com.common.widget.glide.load.resource.transcode.UnitTranscoder<?> UNIT_TRANSCODER = new com.common.widget.glide.load.resource.transcode.UnitTranscoder<Object>();

    @SuppressWarnings("unchecked")
    public static <Z> ResourceTranscoder<Z, Z> get() {
        return (ResourceTranscoder<Z, Z>) UNIT_TRANSCODER;
    }

    @Override
    public Resource<Z> transcode(Resource<Z> toTranscode) {
        return toTranscode;
    }

    @Override
    public String getId() {
        return "";
    }
}
