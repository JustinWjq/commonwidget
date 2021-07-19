package com.common.widget.glide.signature;

import com.common.widget.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * An empty key that is always equal to all other empty keys.
 */
public final class EmptySignature implements Key {
    private static final com.common.widget.glide.signature.EmptySignature EMPTY_KEY = new com.common.widget.glide.signature.EmptySignature();

    public static com.common.widget.glide.signature.EmptySignature obtain() {
        return EMPTY_KEY;
    }

    private EmptySignature() {
        // Empty.
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        // Do nothing.
    }
}
