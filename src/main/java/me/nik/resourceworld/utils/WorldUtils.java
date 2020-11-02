package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

import java.io.File;

public final class WorldUtils {

    private WorldUtils() {
    }

    public static void runWorldCommands() {
        if (!Config.Setting.WORLD_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.WORLD_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public static void runNetherCommands() {
        if (!Config.Setting.NETHER_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.NETHER_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public static void runEndCommands() {
        if (!Config.Setting.END_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.END_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
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