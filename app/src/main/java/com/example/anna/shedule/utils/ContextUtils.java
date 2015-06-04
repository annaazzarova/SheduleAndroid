package com.example.anna.shedule.utils;

import android.content.Context;

import java.io.File;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class ContextUtils {

    @Getter
    @Setter
    private static Context context;

    private static boolean isCacheDirExists = false;


    public static File getCacheDir() {
        File cacheDir = context.getCacheDir();
        if (!isCacheDirExists) {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            isCacheDirExists = true;
        }
        return cacheDir;
    }

    public static File getPrivateFile(String name) {
        return new File(getCacheDir(), name);
    }

    private ContextUtils(){
    }
}
