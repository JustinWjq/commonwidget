package com.common.widget.glide.load.engine;

import com.common.widget.glide.load.Encoder;
import com.common.widget.glide.load.Key;
import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.load.ResourceEncoder;
import com.common.widget.glide.load.Transformation;
import com.common.widget.glide.load.resource.transcode.ResourceTranscoder;

class EngineKeyFactory {

    @SuppressWarnings("rawtypes")
    public EngineKey buildKey(String id, Key signature, int width, int height, ResourceDecoder cacheDecoder,
                              ResourceDecoder sourceDecoder, Transformation transformation, ResourceEncoder encoder,
                              ResourceTranscoder transcoder, Encoder sourceEncoder) {
        return new EngineKey(id, signature, width, height, cacheDecoder, sourceDecoder, transformation, encoder,
                transcoder, sourceEncoder);
    }

}
