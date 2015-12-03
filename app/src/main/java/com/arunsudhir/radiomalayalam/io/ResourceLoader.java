package com.arunsudhir.radiomalayalam.io;

import com.arunsudhir.radiomalayalam.exception.ResourceLoadingException;
import com.google.common.io.CharStreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Created by ullatil on 12/2/2015.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceLoader {
    private final Class<?> clazz;
    private final File folder;

    public static <T> ResourceLoader forClass(Class<T> clazz) {
        String resourceFolder = String.format("%s_Resource", clazz.getSimpleName());
        return new ResourceLoader(clazz, new File(resourceFolder));
    }

    public ResourceLoader fromFolder(String folderName) {
        return new ResourceLoader(clazz, new File(folderName));
    }

    public String loadResource(String filename) {
        File filePath = new File(folder, filename);
        try {
            return CharStreams.toString(new InputStreamReader(clazz.getResourceAsStream(filePath.toString())));
        } catch (IOException e) {
            String errorMessage = String.format("Failed to load resource %s", filePath);
            throw new ResourceLoadingException(errorMessage, e);
        }
    }
}
