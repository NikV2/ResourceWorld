package me.nik.resourceworld.utils;
import me.nik.resourceworld.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Messenger {
    /**
     * @param msg The message to format
     * @return The formatted message
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * @param msg Path to the message from Lang.yml
     * @return The Prefix and a Message formatted
     */
    public static String prefix(String msg) {
        return format(Lang.get().getString("prefix")) + msg;
    }

    /**
     * @param msg Path to the message from Lang.yml
     * @return The Prefix and Message from Lang.yml formatted
     */
    public static String message(String msg) {
        return format(Lang.get().getString("prefix") + format(Lang.get().getString(msg)));
    }

    /**
     * @param message The message to send to the console
     */
    public static void consoleMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }
}