package com.common.widget.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.common.widget.glide.load.ResourceDecoder;
import com.common.widget.glide.load.Transformation;
import com.common.widget.glide.load.engine.bitmaprecycle.BitmapPool;
import com.common.widget.glide.load.engine.cache.DiskCache;
import com.common.widget.glide.load.engine.prefill.PreFillType;
import com.common.widget.glide.load.model.ModelLoaderFactory;
import com.common.widget.glide.load.model.file_descriptor.FileDescriptorModelLoader;
import com.common.widget.glide.load.model.stream.StreamModelLoader;
import com.common.widget.glide.load.DecodeFormat;
import com.common.widget.glide.load.engine.Engine;
import com.common.widget.glide.load.engine.cache.DiskLruCacheFactory;
import com.common.widget.glide.load.engine.cache.MemoryCache;
import com.common.widget.glide.load.engine.prefill.BitmapPreFiller;
import com.common.widget.glide.load.model.GenericLoaderFactory;
import com.common.widget.glide.load.model.GlideUrl;
import com.common.widget.glide.load.model.ImageVideoWrapper;
import com.common.widget.glide.load.model.ModelLoader;
import com.common.widget.glide.load.model.file_descriptor.FileDescriptorFileLoader;
import com.common.widget.glide.load.model.file_descriptor.FileDescriptorResourceLoader;
import com.common.widget.glide.load.model.file_descriptor.FileDescriptorStringLoader;
import com.common.widget.glide.load.model.file_descriptor.FileDescriptorUriLoader;
import com.common.widget.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.common.widget.glide.load.model.stream.StreamByteArrayLoader;
import com.common.widget.glide.load.model.stream.StreamFileLoader;
import com.common.widget.glide.load.model.stream.StreamResourceLoader;
import com.common.widget.glide.load.model.stream.StreamStringLoader;
import com.common.widget.glide.load.model.stream.StreamUriLoader;
import com.common.widget.glide.load.model.stream.StreamUrlLoader;
import com.common.widget.glide.load.resource.bitmap.CenterCrop;
import com.common.widget.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;
import com.common.widget.glide.load.resource.bitmap.FitCenter;
import com.common.widget.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.common.widget.glide.load.resource.bitmap.ImageVideoDataLoadProvider;
import com.common.widget.glide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.common.widget.glide.load.resource.drawable.GlideDrawable;
import com.common.widget.glide.load.resource.file.StreamFileDataLoadProvider;
import com.common.widget.glide.load.resource.gif.GifDrawable;
import com.common.widget.glide.load.resource.gif.GifDrawableLoadProvider;
import com.common.widget.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.common.widget.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.common.widget.glide.load.resource.gifbitmap.ImageVideoGifDrawableLoadProvider;
import com.common.widget.glide.load.resource.transcode.GifBitmapWrapperDrawableTranscoder;
import com.common.widget.glide.load.resource.transcode.GlideBitmapDrawableTranscoder;
import com.common.widget.glide.load.resource.transcode.ResourceTranscoder;
import com.common.widget.glide.load.resource.transcode.TranscoderRegistry;
import com.common.widget.glide.manager.RequestManagerRetriever;
import com.common.widget.glide.module.ManifestParser;
import com.common.widget.glide.module.TXGlideModule;
import com.common.widget.glide.provider.DataLoadProvider;
import com.common.widget.glide.provider.DataLoadProviderRegistry;
import com.common.widget.glide.request.FutureTarget;
import com.common.widget.glide.request.Request;
import com.common.widget.glide.request.animation.GlideAnimation;
import com.common.widget.glide.request.target.ImageViewTargetFactory;
import com.common.widget.glide.request.target.Target;
import com.common.widget.glide.request.target.ViewTarget;
import com.common.widget.glide.util.Util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * A singleton to present a simple static interface for building requests with {@link BitmapRequestBuilder} and
 * maintaining an {@link Engine}, {@link BitmapPool}, {@link DiskCache} and
 * {@link MemoryCache}.
 */
public class TxGlide {

    private static final String TAG = "Glide";
    private static volatile TxGlide glide;
    private static boolean modulesEnabled = true;

    private final GenericLoaderFactory loaderFactory;
    private final Engine engine;
    private final BitmapPool bitmapPool;
    private final MemoryCache memoryCache;
    private final DecodeFormat decodeFormat;
    private final ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
    private final TranscoderRegistry transcoderRegistry = new TranscoderRegistry();
    private final DataLoadProviderRegistry dataLoadProviderRegistry;
    private final CenterCrop bitmapCenterCrop;
    private final GifBitmapWrapperTransformation drawableCenterCrop;
    private final FitCenter bitmapFitCenter;
    private final GifBitmapWrapperTransformation drawableFitCenter;
    private final Handler mainHandler;
    private final BitmapPreFiller bitmapPreFiller;

    /**
     * Returns a directory with a default name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @see #getPhotoCacheDir(Context, String)
     *
     * @param context A context.
     */
    public static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, DiskLruCacheFactory.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @see #getPhotoCacheDir(Context)
     *
     * @param context A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     */
    public static File getPhotoCacheDir(Context context, String cacheName) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /**
     * Enable or disable the parsing of AndroidManifest.xml looking for {@link TXGlideModule} implementations.
     * @throws IllegalArgumentException if the Glide singleton has already been created.
     */
    public static void setModulesEnabled(boolean enabled) {
        synchronized (TxGlide.class) {
            if (glide != null) {
                throw new IllegalArgumentException("Glide singleton already exists.");
            }

            modulesEnabled = enabled;
        }
    }

    /**
     * Get the singleton.
     *
     * @return the singleton
     */
    public static TxGlide get(Context context) {
        if (glide == null) {
            synchronized (TxGlide.class) {
                if (glide == null) {
                    Context applicationContext = context.getApplicationContext();
                    GlideBuilder builder = new GlideBuilder(applicationContext);
                    List<TXGlideModule> modules = parseGlideModules(applicationContext);
                    for (TXGlideModule module : modules) {
                        module.applyOptions(applicationContext, builder);
                    }
                    glide = builder.createGlide();
                    for (TXGlideModule module : modules) {
                        module.registerComponents(applicationContext, glide);
                    }
                }
            }
        }

        return glide;
    }

    /**
     * If modules are enabled, parses the application manifest and returns the configured modules.
     * Otherwise, returns an empty list.
     */
    private static List<TXGlideModule> parseGlideModules(Context applicationContext) {
        if (modulesEnabled) {
            return new ManifestParser(applicationContext).parse();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns false if the {@link TxGlide} singleton has not yet been created and can therefore be setup using
     * {@link #setup(GlideBuilder)}.
     *
     * @see #setup(GlideBuilder)
     *
     * @deprecated Use {@link TXGlideModule} instead. Scheduled to be removed in Glide 4.0.
     */
    @Deprecated
    public static boolean isSetup() {
        return glide != null;
    }

    /**
     * Creates the {@link TxGlide} singleton using the given builder. Can be used to set options like cache sizes and
     * locations.
     *
     * @see #isSetup()
     *
     * @deprecated Use {@link TXGlideModule} instead. Scheduled to be removed in Glide 4.0.
     * @param builder The builder.
     * @throws IllegalArgumentException if the Glide singleton has already been created.
     */
    @Deprecated
    public static void setup(GlideBuilder builder) {
        if (isSetup()) {
            throw new IllegalArgumentException("Glide is already setup, check with isSetup() first");
        }

        glide = builder.createGlide();
    }

    // For testing.
    static void tearDown() {
        glide = null;
        modulesEnabled = true;
    }

    TxGlide(Engine engine, MemoryCache memoryCache, BitmapPool bitmapPool, Context context, DecodeFormat decodeFormat) {
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.memoryCache = memoryCache;
        this.decodeFormat = decodeFormat;
        loaderFactory = new GenericLoaderFactory(context);
        mainHandler = new Handler(Looper.getMainLooper());
        bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, decodeFormat);

        dataLoadProviderRegistry = new DataLoadProviderRegistry();

        StreamBitmapDataLoadProvider streamBitmapLoadProvider =
                new StreamBitmapDataLoadProvider(bitmapPool, decodeFormat);
        dataLoadProviderRegistry.register(InputStream.class, Bitmap.class, streamBitmapLoadProvider);

        FileDescriptorBitmapDataLoadProvider fileDescriptorLoadProvider =
                new FileDescriptorBitmapDataLoadProvider(bitmapPool, decodeFormat);
        dataLoadProviderRegistry.register(ParcelFileDescriptor.class, Bitmap.class, fileDescriptorLoadProvider);

        ImageVideoDataLoadProvider imageVideoDataLoadProvider =
                new ImageVideoDataLoadProvider(streamBitmapLoadProvider, fileDescriptorLoadProvider);
        dataLoadProviderRegistry.register(ImageVideoWrapper.class, Bitmap.class, imageVideoDataLoadProvider);

        GifDrawableLoadProvider gifDrawableLoadProvider =
                new GifDrawableLoadProvider(context, bitmapPool);
        dataLoadProviderRegistry.register(InputStream.class, GifDrawable.class, gifDrawableLoadProvider);

        dataLoadProviderRegistry.register(ImageVideoWrapper.class, GifBitmapWrapper.class,
                new ImageVideoGifDrawableLoadProvider(imageVideoDataLoadProvider, gifDrawableLoadProvider, bitmapPool));

        dataLoadProviderRegistry.register(InputStream.class, File.class, new StreamFileDataLoadProvider());

        register(File.class, ParcelFileDescriptor.class, new FileDescriptorFileLoader.Factory());
        register(File.class, InputStream.class, new StreamFileLoader.Factory());
        register(int.class, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
        register(int.class, InputStream.class, new StreamResourceLoader.Factory());
        register(Integer.class, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
        register(Integer.class, InputStream.class, new StreamResourceLoader.Factory());
        register(String.class, ParcelFileDescriptor.class, new FileDescriptorStringLoader.Factory());
        register(String.class, InputStream.class, new StreamStringLoader.Factory());
        register(Uri.class, ParcelFileDescriptor.class, new FileDescriptorUriLoader.Factory());
        register(Uri.class, InputStream.class, new StreamUriLoader.Factory());
        register(URL.class, InputStream.class, new StreamUrlLoader.Factory());
        register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());
        register(byte[].class, InputStream.class, new StreamByteArrayLoader.Factory());

        transcoderRegistry.register(Bitmap.class, GlideBitmapDrawable.class,
                new GlideBitmapDrawableTranscoder(context.getResources(), bitmapPool));
        transcoderRegistry.register(GifBitmapWrapper.class, GlideDrawable.class,
                new GifBitmapWrapperDrawableTranscoder(
                        new GlideBitmapDrawableTranscoder(context.getResources(), bitmapPool)));

        bitmapCenterCrop = new CenterCrop(bitmapPool);
        drawableCenterCrop = new GifBitmapWrapperTransformation(bitmapPool, bitmapCenterCrop);

        bitmapFitCenter = new FitCenter(bitmapPool);
        drawableFitCenter = new GifBitmapWrapperTransformation(bitmapPool, bitmapFitCenter);
    }

    /**
     * Returns the {@link BitmapPool} used to temporarily store
     * {@link Bitmap}s so they can be reused to avoid garbage collections.
     *
     * <p>
     *     Note - Using this pool directly can lead to undefined behavior and strange drawing errors. Any
     *     {@link Bitmap} added to the pool must not be currently in use in any other part of the
     *     application. Any {@link Bitmap} added to the pool must be removed from the pool before it
     *     is added a second time.
     * </p>
     *
     * <p>
     *     Note - To make effective use of the pool, any {@link Bitmap} removed from the pool must
     *     eventually be re-added. Otherwise the pool will eventually empty and will not serve any useful purpose.
     * </p>
     *
     * <p>
     *     The primary reason this object is exposed is for use in custom
     *     {@link ResourceDecoder}s and {@link Transformation}s. Use
     *     outside of these classes is not generally recommended.
     * </p>
     */
    public BitmapPool getBitmapPool() {
        return bitmapPool;
    }

    <Z, R> ResourceTranscoder<Z, R> buildTranscoder(Class<Z> decodedClass, Class<R> transcodedClass) {
        return transcoderRegistry.get(decodedClass, transcodedClass);
    }

    <T, Z> DataLoadProvider<T, Z> buildDataProvider(Class<T> dataClass, Class<Z> decodedClass) {
        return dataLoadProviderRegistry.get(dataClass, decodedClass);
    }

    <R> Target<R> buildImageViewTarget(ImageView imageView, Class<R> transcodedClass) {
        return imageViewTargetFactory.buildTarget(imageView, transcodedClass);
    }

    Engine getEngine() {
        return engine;
    }

    CenterCrop getBitmapCenterCrop() {
        return bitmapCenterCrop;
    }

    FitCenter getBitmapFitCenter() {
        return bitmapFitCenter;
    }

    GifBitmapWrapperTransformation getDrawableCenterCrop() {
        return drawableCenterCrop;
    }

    GifBitmapWrapperTransformation getDrawableFitCenter() {
        return drawableFitCenter;
    }

    Handler getMainHandler() {
        return mainHandler;
    }

    DecodeFormat getDecodeFormat() {
        return decodeFormat;
    }

    private GenericLoaderFactory getLoaderFactory() {
        return loaderFactory;
    }

    /**
     * Pre-fills the {@link BitmapPool} using the given sizes.
     *
     * <p>
     *   Enough Bitmaps are added to completely fill the pool, so most or all of the Bitmaps currently in the pool will
     *   be evicted. Bitmaps are allocated according to the weights of the given sizes, where each size gets
     *   (weight / prefillWeightSum) percent of the pool to fill.
     * </p>
     *
     * <p>
     *     Note - Pre-filling is done asynchronously using and {@link android.os.MessageQueue.IdleHandler}. Any
     *     currently running pre-fill will be cancelled and replaced by a call to this method.
     * </p>
     *
     * <p>
     *     This method should be used with caution, overly aggressive pre-filling is substantially worse than not
     *     pre-filling at all. Pre-filling should only be started in onCreate to avoid constantly clearing and
     *     re-filling the {@link BitmapPool}. Rotation should be carefully
     *     considered as well. It may be worth calling this method only when no saved instance state exists so that
     *     pre-filling only happens when the Activity is first created, rather than on every rotation.
     * </p>
     *
     * @param bitmapAttributeBuilders The list of
     *     {@link PreFillType.Builder Builders} representing
     *     individual sizes and configurations of {@link Bitmap}s to be pre-filled.
     */
    public void preFillBitmapPool(PreFillType.Builder... bitmapAttributeBuilders) {
        bitmapPreFiller.preFill(bitmapAttributeBuilders);
    }

    /**
     * Clears as much memory as possible.
     *
     * @see android.content.ComponentCallbacks#onLowMemory()
     * @see android.content.ComponentCallbacks2#onLowMemory()
     */
    public void clearMemory() {
        // Engine asserts this anyway when removing resources, fail faster and consistently
        Util.assertMainThread();
        // memory cache needs to be cleared before bitmap pool to clear re-pooled Bitmaps too. See #687.
        memoryCache.clearMemory();
        bitmapPool.clearMemory();
    }

    /**
     * Clears some memory with the exact amount depending on the given level.
     *
     * @see android.content.ComponentCallbacks2#onTrimMemory(int)
     */
    public void trimMemory(int level) {
        // Engine asserts this anyway when removing resources, fail faster and consistently
        Util.assertMainThread();
        // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
        memoryCache.trimMemory(level);
        bitmapPool.trimMemory(level);
    }

    /**
     * Clears disk cache.
     *
     * <p>
     *     This method should always be called on a background thread, since it is a blocking call.
     * </p>
     */
    public void clearDiskCache() {
        Util.assertBackgroundThread();
        getEngine().clearDiskCache();
    }

    /**
     * Adjusts Glide's current and maximum memory usage based on the given {@link MemoryCategory}.
     *
     * <p>
     *     The default {@link MemoryCategory} is {@link MemoryCategory#NORMAL}. {@link MemoryCategory#HIGH} increases
     *     Glide's maximum memory usage by up to 50% and {@link MemoryCategory#LOW} decreases Glide's maximum memory
     *     usage by 50%. This method should be used to temporarily increase or decrease memory useage for a single
     *     Activity or part of the app. Use {@link GlideBuilder#setMemoryCache(MemoryCache)} to set a permanent
     *     memory size if you want to change the default.
     * </p>
     */
    public void setMemoryCategory(MemoryCategory memoryCategory) {
        // Engine asserts this anyway when removing resources, fail faster and consistently
        Util.assertMainThread();
        // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
        memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
        bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
    }

    /**
     * Cancel any pending loads Glide may have for the target and free any resources (such as {@link Bitmap}s) that may
     * have been loaded for the target so they may be reused.
     *
     * @param target The Target to cancel loads for.
     */
    public static void clear(Target<?> target) {
        Util.assertMainThread();
        Request request = target.getRequest();
        if (request != null) {
            request.clear();
            target.setRequest(null);
        }
    }

    /**
     * Cancel any pending loads Glide may have for the target and free any resources that may have been loaded into
     * the target so they may be reused.
     *
     * @param target The target to cancel loads for.
     */
    public static void clear(FutureTarget<?> target) {
        target.clear();
    }

    /**
     * Cancel any pending loads Glide may have for the view and free any resources that may have been loaded for the
     * view.
     *
     * <p>
     *     Note that this will only work if {@link View#setTag(Object)} is not called on this view outside of Glide.
     * </p>
     *
     * @see #clear(Target).
     *
     * @param view The view to cancel loads and free resources for.
     * @throws IllegalArgumentException if an object other than Glide's metadata is set as the view's tag.
     */
    public static void clear(View view) {
        Target<?> viewTarget = new ClearTarget(view);
        clear(viewTarget);
    }

    /**
     * Use the given factory to build a {@link ModelLoader} for models of the given class. Generally the best use of
     * this method is to replace one of the default factories or add an implementation for other similar low level
     * models. Typically the {@link RequestManager#using(StreamModelLoader)} or
     * {@link RequestManager#using(FileDescriptorModelLoader)} syntax is
     * preferred because it directly links the model with the ModelLoader being used to load it. Any factory replaced
     * by the given factory will have its {@link ModelLoaderFactory#teardown()}} method called.
     *
     * <p>
     *     Note - If a factory already exists for the given class, it will be replaced. If that factory is not being
     *     used for any other model class, {@link ModelLoaderFactory#teardown()}
     *     will be called.
     * </p>
     *
     * <p>
     *     Note - The factory must not be an anonymous inner class of an Activity or another object that cannot be
     *     retained statically.
     * </p>
     *
     * @see RequestManager#using(FileDescriptorModelLoader)
     * @see RequestManager#using(StreamModelLoader)
     *
     * @param modelClass The model class.
     * @param resourceClass The resource class the model loader will translate the model type into.
     * @param factory The factory to use.
     * @param <T> The type of the model.
     * @param <Y> the type of the resource.
     */
    public <T, Y> void register(Class<T> modelClass, Class<Y> resourceClass, ModelLoaderFactory<T, Y> factory) {
        ModelLoaderFactory<T, Y> removed = loaderFactory.register(modelClass, resourceClass, factory);
        if (removed != null) {
            removed.teardown();
        }
    }

    /**
     * Removes any {@link ModelLoaderFactory} registered for the given model and resource classes if one exists. If a
     * {@link ModelLoaderFactory} is removed, its {@link ModelLoaderFactory#teardown()}} method will be called.
     *
     * @deprecated Use {@link #register(Class, Class, ModelLoaderFactory)} to replace
     * a registered loader rather than simply removing it.
     * @param modelClass The model class.
     * @param resourceClass The resource class.
     * @param <T> The type of the model.
     * @param <Y> The type of the resource.
     */
    @Deprecated
    public <T, Y> void unregister(Class<T> modelClass, Class<Y> resourceClass) {
        ModelLoaderFactory<T, Y> removed = loaderFactory.unregister(modelClass, resourceClass);
        if (removed != null) {
            removed.teardown();
        }
    }

    /**
     * Build a {@link ModelLoader} for the given model class using registered {@link ModelLoaderFactory}s.
     *
     * @see  #buildModelLoader(Object, Class, Context)
     * @see  #buildStreamModelLoader(Class, Context)
     * @see  #buildFileDescriptorModelLoader(Class, Context)
     *
     * @param modelClass The class to get a {@link ModelLoader} for.
     * @param resourceClass The resource class to get a {@link ModelLoader} for.
     * @param context Any context.
     * @param <T> The type of the model.
     * @param <Y> The type of the resource.
     * @return A new {@link ModelLoader} for the given model class.
     */
    public static <T, Y> ModelLoader<T, Y> buildModelLoader(Class<T> modelClass, Class<Y> resourceClass,
                                                            Context context) {
         if (modelClass == null) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Unable to load null model, setting placeholder only");
            }
            return null;
        }
        return TxGlide.get(context).getLoaderFactory().buildModelLoader(modelClass, resourceClass);
    }

    /**
     * A convenience method to build a {@link ModelLoader} for a given model object using registered
     * {@link ModelLoaderFactory}s.
     *
     * @see #buildModelLoader(Class, Class, Context)
     *
     * @param model A non null model object whose class we will get a {@link ModelLoader} for.
     * @param resourceClass The resource class to get a {@link ModelLoader} for.
     * @param context Any context.
     * @param <T> The type of the model.
     * @param <Y> The type of the resource.
     * @return A new {@link ModelLoader} for the given model and resource classes, or null if model is null.
     */
    @SuppressWarnings("unchecked")
    public static <T, Y> ModelLoader<T, Y> buildModelLoader(T model, Class<Y> resourceClass, Context context) {
        return buildModelLoader(model != null ? (Class<T>) model.getClass() : null, resourceClass, context);
    }

    /**
     * A method to build a {@link ModelLoader} for the given model that produces {@link InputStream}s using a registered
     * factory.
     *
     * @see #buildModelLoader(Class, Class, Context)
     */
    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(Class<T> modelClass, Context context) {
        return buildModelLoader(modelClass, InputStream.class, context);
    }

    /**
     * A method to build a {@link ModelLoader} for the given model that produces {@link InputStream}s using a registered
     * factory.
     *
     * @see #buildModelLoader(Object, Class, Context)
     */
    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(T model, Context context) {
        return buildModelLoader(model, InputStream.class, context);
    }

    /**
     * A method to build a {@link ModelLoader} for the given model class that produces
     * {@link ParcelFileDescriptor}s using a registered factory.
     *
     * @see #buildModelLoader(Class, Class, Context)
     */
    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(Class<T> modelClass,
                                                                                          Context context) {
        return buildModelLoader(modelClass, ParcelFileDescriptor.class, context);
    }

    /**
     * A method to build a {@link ModelLoader} for the given model class that produces
     * {@link ParcelFileDescriptor}s using a registered factory.
     *
     * @see #buildModelLoader(Object, Class, Context)
     */
    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(T model, Context context) {
        return buildModelLoader(model, ParcelFileDescriptor.class, context);
    }

    /**
     * Begin a load with Glide by passing in a context.
     *
     * <p>
     *     Any requests started using a context will only have the application level options applied and will not be
     *     started or stopped based on lifecycle events. In general, loads should be started at the level the result
     *     will be used in. If the resource will be used in a view in a child fragment,
     *     the load should be started with {@link #with(android.app.Fragment)}} using that child fragment. Similarly,
     *     if the resource will be used in a view in the parent fragment, the load should be started with
     *     {@link #with(android.app.Fragment)} using the parent fragment. In the same vein, if the resource will be used
     *     in a view in an activity, the load should be started with {@link #with(Activity)}}.
     * </p>
     *
     * <p>
     *     This method is appropriate for resources that will be used outside of the normal fragment or activity
     *     lifecycle (For example in services, or for notification thumbnails).
     * </p>
     *
     * @see #with(Activity)
     * @see #with(android.app.Fragment)
     * @see #with(Fragment)
     * @see #with(FragmentActivity)
     *
     * @param context Any context, will not be retained.
     * @return A RequestManager for the top level application that can be used to start a load.
     */
    public static RequestManager with(Context context) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(context);
    }

    /**
     * Begin a load with Glide that will be tied to the given {@link Activity}'s lifecycle and that uses the
     * given {@link Activity}'s default options.
     *
     * @param activity The activity to use.
     * @return A RequestManager for the given activity that can be used to start a load.
     */
    public static RequestManager with(Activity activity) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(activity);
    }

    /**
     * Begin a load with Glide that will tied to the give {@link FragmentActivity}'s lifecycle
     * and that uses the given {@link FragmentActivity}'s default options.
     *
     * @param activity The activity to use.
     * @return A RequestManager for the given FragmentActivity that can be used to start a load.
     */
    public static RequestManager with(FragmentActivity activity) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(activity);
    }

    /**
     * Begin a load with Glide that will be tied to the given {@link android.app.Fragment}'s lifecycle and that uses
     * the given {@link android.app.Fragment}'s default options.
     *
     * @param fragment The fragment to use.
     * @return A RequestManager for the given Fragment that can be used to start a load.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static RequestManager with(android.app.Fragment fragment) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(fragment);
    }

    /**
     * Begin a load with Glide that will be tied to the given {@link Fragment}'s lifecycle and
     * that uses the given {@link Fragment}'s default options.
     *
     * @param fragment The fragment to use.
     * @return A RequestManager for the given Fragment that can be used to start a load.
     */
    public static RequestManager with(Fragment fragment) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(fragment);
    }

    private static class ClearTarget extends ViewTarget<View, Object> {
        public ClearTarget(View view) {
            super(view);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            // Do nothing.
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            // Do nothing.
        }

        @Override
        public void onResourceReady(Object resource, GlideAnimation<? super Object> glideAnimation) {
            // Do nothing.
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
            // Do nothing.
        }
    }
}
