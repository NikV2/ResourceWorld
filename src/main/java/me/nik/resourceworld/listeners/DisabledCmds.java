package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisabledCmds implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("rw.admin")) return;
        if (isInWorld(p)) {
            if (e.getMessage().equals("/")) return;
            for (String cmd : Config.Setting.DISABLED_COMMANDS_LIST.getStringList()) {
                if (e.getMessage().contains(cmd)) {
                    e.setCancelled(true);
                    p.sendMessage(MsgType.DISABLED_COMMAND.getMessage());
                }
            }
        }
    }

    private boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(Config.Setting.WORLD_NAME.getString())) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(Config.Setting.NETHER_NAME.getString())) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(Config.Setting.END_NAME.getString());
    }
}