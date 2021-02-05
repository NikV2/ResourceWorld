package me.nik.resourceworld.utils;

import me.nik.resourceworld.managers.custom.ResourceWorldException;

import java.io.File;

public final class WorldUtils {

    private WorldUtils() {
        throw new ResourceWorldException("This is a static class dummy!");
    }

    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null)
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
        }
    }
}