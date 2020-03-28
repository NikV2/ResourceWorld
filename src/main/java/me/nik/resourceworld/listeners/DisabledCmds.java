package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisabledCmds extends Manager {

    @EventHandler
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getWorld().getName().equals(configString("world.settings.world_name"))) {
            for (String cmd : configStringList("disabled.commands")) {
                if (p.hasPermission("rw.admin")) return;
                if (cmd.contains(e.getMessage())) {
                    e.setCancelled(true);
                    p.sendMessage(Messenger.message("disabled_command"));
                } else return;
            }
        }
    }
}