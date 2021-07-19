package com.common.widget.glide.load.resource.file;

import com.common.widget.glide.load.engine.Resource;
import com.common.widget.glide.load.resource.SimpleResource;

import java.io.File;

/**
 * A simple {@link Resource} that wraps a {@link File}.
 */
public class FileResource extends SimpleResource<File> {
    public FileResource(File file) {
        super(file);
    }
}
