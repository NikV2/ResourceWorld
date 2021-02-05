package me.nik.resourceworld.utils;

import me.nik.resourceworld.managers.custom.ResourceWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Messenger {

    private Messenger() {
        throw new ResourceWorldException("This is a static class dummy!");
    }

    /**
     * @param msg The message to format
     * @return The formatted message
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * @param message The message to send to the console
     */
    public static void consoleMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }
}