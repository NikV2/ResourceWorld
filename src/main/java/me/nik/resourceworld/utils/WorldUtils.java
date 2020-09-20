package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

import java.io.File;

public class WorldUtils {

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

    /**
     * @return True if the Resource World exists
     */
    public static boolean worldExists() {
        return Bukkit.getWorld(Config.Setting.WORLD_NAME.getString()) != null;
    }

    /**
     * @return True if the Nether World exists
     */
    public static boolean netherExists() {
        return Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()) != null;
    }

    /**
     * @return True if the End World exists
     */
    public static boolean endExists() {
        return Bukkit.getWorld(Config.Setting.END_NAME.getString()) != null;
    }
}