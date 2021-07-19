package com.common.widget.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;

import com.common.widget.glide.load.Transformation;
import com.common.widget.glide.load.resource.bitmap.BitmapTransformation;
import com.common.widget.glide.load.resource.bitmap.CenterCrop;
import com.common.widget.glide.load.resource.bitmap.FitCenter;
import com.common.widget.glide.load.Encoder;
import com.common.widget.glide.load.Key;
import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.load.ResourceEncoder;
import com.common.widget.glide.load.engine.DiskCacheStrategy;
import com.common.widget.glide.load.resource.gif.GifDrawable;
import com.common.widget.glide.load.resource.gif.GifDrawableTransformation;
import com.common.widget.glide.load.resource.transcode.ResourceTranscoder;
import com.common.widget.glide.provider.LoadProvider;
import com.common.widget.glide.request.RequestListener;
import com.common.widget.glide.request.animation.DrawableCrossFadeFactory;
import com.common.widget.glide.request.animation.GlideAnimationFactory;
import com.common.widget.glide.request.animation.ViewPropertyAnimation;

import java.io.File;
import java.io.InputStream;

/**
 * A class for creating a request to load an animated gif.
 *
 * <p>
 *     Warning - It is <em>not</em> safe to use this builder after calling <code>into()</code>, it may be pooled and
 *     reused.
 * </p>
 *
 * @param <ModelType> The type of model that will be loaded into the target.
 */
public class GifRequestBuilder<ModelType>
        extends GenericRequestBuilder<ModelType, InputStream, GifDrawable, GifDrawable>
        implements com.common.widget.glide.BitmapOptions, DrawableOptions {

    GifRequestBuilder(LoadProvider<ModelType, InputStream, GifDrawable, GifDrawable> loadProvider,
                      Class<GifDrawable> transcodeClass, GenericRequestBuilder<ModelType, ?, ?, ?> other) {
        super(loadProvider, transcodeClass, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> thumbnail(GenericRequestBuilder<?, ?, ?, GifDrawable> thumbnailRequest) {
        super.thumbnail(thumbnailRequest);
        return this;
    }

    /**
     * Loads and displays the GIF retrieved by the given thumbnail request if it finishes before this
     * request. Best used for loading thumbnail GIFs that are smaller and will be loaded more quickly
     * than the fullsize GIF. There are no guarantees about the order in which the requests will actually
     * finish. However, if the thumb request completes after the full request, the thumb GIF will never
     * replace the full image.
     *
     * @see #thumbnail(float)
     *
     * <p>
     *     Note - Any options on the main request will not be passed on to the thumbnail request. For example, if
     *     you want an animation to occur when either the full GIF loads or the thumbnail loads,
     *     you need to call {@link #animate(int)} on both the thumb and the full request. For a simpler thumbnail
     *     option where these options are applied to the humbnail as well, see {@link #thumbnail(float)}.
     * </p>
     *
     * <p>
     *     Only the thumbnail call on the main request will be obeyed, recursive calls to this method are ignored.
     * </p>
     *
     * @param thumbnailRequest The request to use to load the thumbnail.
     * @return This builder object.
     */
    public com.common.widget.glide.GifRequestBuilder<ModelType> thumbnail(com.common.widget.glide.GifRequestBuilder<?> thumbnailRequest) {
        super.thumbnail(thumbnailRequest);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> thumbnail(float sizeMultiplier) {
        super.thumbnail(sizeMultiplier);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> sizeMultiplier(float sizeMultiplier) {
        super.sizeMultiplier(sizeMultiplier);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> decoder(
            ResourceDecoder<InputStream, GifDrawable> decoder) {
        super.decoder(decoder);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> cacheDecoder(
            ResourceDecoder<File, GifDrawable> cacheDecoder) {
        super.cacheDecoder(cacheDecoder);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> encoder(
            ResourceEncoder<GifDrawable> encoder) {
        super.encoder(encoder);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> priority(Priority priority) {
        super.priority(priority);
        return this;
    }

    /**
     * Transforms each frame of the GIF using {@link CenterCrop}.
     *
     * @see #fitCenter()
     * @see #transformFrame(BitmapTransformation...)
     * @see #transformFrame(Transformation[])
     * @see #transform(Transformation[])
     *
     * @return This request builder.
     */
    public com.common.widget.glide.GifRequestBuilder<ModelType> centerCrop() {
        return transformFrame(glide.getBitmapCenterCrop());
    }

    /**
     * Transforms each frame of the GIF using {@link FitCenter}.
     *
     * @see #centerCrop()
     * @see #transformFrame(BitmapTransformation...)
     * @see #transformFrame(Transformation[])
     * @see #transform(Transformation[])
     *
     * @return This request builder..
     */
    public com.common.widget.glide.GifRequestBuilder<ModelType> fitCenter() {
        return transformFrame(glide.getBitmapFitCenter());
    }

    /**
     * Transforms each frame of the GIF using the given transformations.
     *
     * @see #centerCrop()
     * @see #fitCenter()
     * @see #transformFrame(Transformation[])
     * @see #transform(Transformation[])
     *
     * @param bitmapTransformations The transformations to apply in order to each frame.
     * @return This request builder.
     */
    public com.common.widget.glide.GifRequestBuilder<ModelType> transformFrame(BitmapTransformation... bitmapTransformations) {
        return transform(toGifTransformations(bitmapTransformations));
    }

    /**
     * Transforms each frame of the GIF using the given transformations.
     *
     * @see #fitCenter()
     * @see #centerCrop()
     * @see #transformFrame(BitmapTransformation...)
     * @see #transform(Transformation[])
     *
     * @param bitmapTransformations The transformations to apply in order to each frame.
     * @return This request builder.
     */
    public com.common.widget.glide.GifRequestBuilder<ModelType> transformFrame(Transformation<Bitmap>... bitmapTransformations) {
        return transform(toGifTransformations(bitmapTransformations));
    }

    private GifDrawableTransformation[] toGifTransformations(Transformation<Bitmap>[] bitmapTransformations) {
        GifDrawableTransformation[] transformations = new GifDrawableTransformation[bitmapTransformations.length];
        for (int i = 0; i < bitmapTransformations.length; i++) {
            transformations[i] = new GifDrawableTransformation(bitmapTransformations[i], glide.getBitmapPool());
        }
        return transformations;
    }

    /**
     * {@inheritDoc}
     *
     * @see #fitCenter()
     * @see #centerCrop()
     * @see #transformFrame(BitmapTransformation...)
     * @see #transformFrame(Transformation[])
     *
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> transform(Transformation<GifDrawable>... transformations) {
        super.transform(transformations);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> transcoder(ResourceTranscoder<GifDrawable, GifDrawable> transcoder) {
        super.transcoder(transcoder);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> crossFade() {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> crossFade(int duration) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(duration));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> crossFade(Animation animation, int duration) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(animation, duration));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> crossFade(int animationId, int duration) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(context, animationId, duration));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> dontAnimate() {
        super.dontAnimate();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> animate(int animationId) {
        super.animate(animationId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> animate(Animation animation) {
        super.animate(animation);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> animate(ViewPropertyAnimation.Animator animator) {
        super.animate(animator);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> animate(GlideAnimationFactory<GifDrawable> animationFactory) {
        super.animate(animationFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> placeholder(int resourceId) {
        super.placeholder(resourceId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> placeholder(Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }

    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> fallback(Drawable drawable) {
        super.fallback(drawable);
        return this;
    }

    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> fallback(int resourceId) {
        super.fallback(resourceId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> error(int resourceId) {
        super.error(resourceId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> error(Drawable drawable) {
        super.error(drawable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> listener(
            RequestListener<? super ModelType, GifDrawable> requestListener) {
        super.listener(requestListener);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> skipMemoryCache(boolean skip) {
        super.skipMemoryCache(skip);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> diskCacheStrategy(DiskCacheStrategy strategy) {
        super.diskCacheStrategy(strategy);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> override(int width, int height) {
        super.override(width, height);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> sourceEncoder(Encoder<InputStream> sourceEncoder) {
        super.sourceEncoder(sourceEncoder);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> dontTransform() {
        super.dontTransform();
        return this;
    }

    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> signature(Key signature) {
        super.signature(signature);
        return this;
    }

    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> load(ModelType model) {
        super.load(model);
        return this;
    }

    @Override
    public com.common.widget.glide.GifRequestBuilder<ModelType> clone() {
        return (com.common.widget.glide.GifRequestBuilder<ModelType>) super.clone();
    }

    @Override
    void applyFitCenter() {
        fitCenter();
    }

    @Override
    void applyCenterCrop() {
        centerCrop();
    }
}
