package me.nik.resourceworld.utils;
import org.bukkit.ChatColor;

public class ColourUtils {
    public static String format(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
