package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

import java.io.File;

public class WorldUtils {

    private static Config config;

    public static void initialize(Config config) {
        WorldUtils.config = config;
    }

    /**
     * @param directory The directory to delete
     * @return Whether or not it has been deleted - exists (Usually never used)
     */
    public static boolean deleteDirectory(File directory) {
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
        return directory.delete();
    }

    /**
     * @return True if the Resource World exists
     */
    public static boolean worldExists() {
        return Bukkit.getWorld(config.get().getString("world.settings.world_name")) != null;
    }

    /**
     * @return True if the Nether World exists
     */
    public static boolean netherExists() {
        return Bukkit.getWorld(config.get().getString("nether_world.settings.world_name")) != null;
    }

    /**
     * @return True if the End World exists
     */
    public static boolean endExists() {
        return Bukkit.getWorld(config.get().getString("end_world.settings.world_name")) != null;
    }
}
