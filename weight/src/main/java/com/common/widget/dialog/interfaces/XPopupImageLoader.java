package com.common.widget.dialog.interfaces;

import android.content.Context;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.common.widget.dialog.core.ImageViewerPopupView;
import com.common.widget.dialog.photoview.PhotoView;

import java.io.File;

public interface XPopupImageLoader{

    void loadSnapshot(@NonNull Object uri, @NonNull PhotoView snapshot);

    View loadImage(int position, @NonNull Object uri, @NonNull ImageViewerPopupView popupView, @NonNull PhotoView snapshot, @NonNull ProgressBar progressBar);

    /**
     * 获取图片对应的文件
     * @param context
     * @param uri
     * @return
     */
    File getImageFile(@NonNull Context context, @NonNull Object uri);
    void  destroy( int position, @NonNull Object object);
}
