package com.drplump.droid.academy.cache;

import java.io.File;


public class CacheDir extends File {

    private static CacheDir instance;

    private CacheDir(String pathname) {
        super(pathname);
    }

    public static void init(String pathname) {
        if (instance == null) {
            instance = new CacheDir(pathname);
        }
    }

    public static CacheDir newInstance() {
        return instance;
    }
}
