package me.nik.resourceworld.listeners.disabledcommands;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandsNether implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("rw.admin")) return;
        if (p.getWorld().getName().equals(Config.Setting.NETHER_NAME.getString())) {
            if (e.getMessage().equals("/")) return;
            for (String cmd : Config.Setting.NETHER_DISABLED_COMMANDS_LIST.getStringList()) {
                if (e.getMessage().contains(cmd)) {
                    e.setCancelled(true);
                    p.sendMessage(MsgType.DISABLED_COMMAND.getMessage());
                }
            }
        }
    }
}