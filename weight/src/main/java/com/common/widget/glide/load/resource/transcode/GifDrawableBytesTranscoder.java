package com.common.widget.glide.load.resource.transcode;

import com.common.widget.glide.load.resource.gif.GifDrawable;
import com.common.widget.glide.load.engine.Resource;
import com.common.widget.glide.load.resource.bytes.BytesResource;

/**
 * An {@link ResourceTranscoder} that converts
 * {@link GifDrawable} into bytes by obtaining the original bytes of the GIF from
 * the {@link GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
    @Override
    public Resource<byte[]> transcode(Resource<GifDrawable> toTranscode) {
        GifDrawable gifData = toTranscode.get();
        return new BytesResource(gifData.getData());
    }

    @Override
    public String getId() {
        return "GifDrawableBytesTranscoder.com.txt.video.widget.glide.load.resource.transcode";
    }
}
