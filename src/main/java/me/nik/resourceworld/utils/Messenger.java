package me.nik.resourceworld.utils;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Messenger {

    private static Lang lang;

    public static void initialize(Lang lang) {
        Messenger.lang = lang;
    }

    /**
     * @param msg The message to format
     * @return The formatted message
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Returns a message from the given type
     *
     * @param type The message type
     * @return The type's Message
     */
    public static String message(MsgType type) {
        switch (type) {
            case PREFIX:
                return format(lang.get().getString("prefix"));
            case UPDATE_FOUND:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("update_found"));
            case DISABLED_COMMAND:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("disabled_command"));
            case DELETING:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("deleting"));
            case RESET_COOLDOWN:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("reset_cooldown"));
            case RESETTING_THE_WORLD:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("resetting_the_world"));
            case RESETTING_THE_NETHER:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("resetting_the_nether"));
            case RESETTING_THE_END:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("resetting_the_end"));
            case CONSOLE_MESSAGE:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("console_message"));
            case NO_PERMISSION:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("no_perm"));
            case COOLDOWN_MESSAGE:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("cooldown_message"));
            case BLOCK_PLACE:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("block_place"));
            case RELOADED:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("reloaded"));
            case RELOADING:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("reloading"));
            case TELEPORT_DELAY:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("teleport_delay"));
            case GUI_NAME:
                return format(lang.get().getString("gui_name"));
            case WORLDS_GUI_NAME:
                return format(lang.get().getString("worlds_gui_name"));
            case TELEPORTED_MESSAGE:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("teleported_message"));
            case NOT_EXIST:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("not_exist"));
            case TELEPORTED_PLAYERS:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("teleported_players"));
            case TELEPORTING_PLAYER:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("teleporting_player"));
            case MAIN_WORLD_ERROR:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("main_world_error"));
            case UPDATE_NOT_FOUND:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("update_not_found"));
            case UPDATE_DISABLED:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("update_disabled"));
            case FIXED_MISTAKES:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("fixed_mistakes"));
            case WORLD_HAS_BEEN_RESET:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("world_has_been_reset"));
            case NETHER_HAS_BEEN_RESET:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("nether_has_been_reset"));
            case END_HAS_BEEN_RESET:
                return format(lang.get().getString("prefix")) + format(lang.get().getString("end_has_been_reset"));
            default:
                return null;
        }
    }
    /**
     * @param message The message to send to the console
     */
    public static void consoleMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

}