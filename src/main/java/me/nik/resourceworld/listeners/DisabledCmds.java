package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisabledCmds extends Manager {

    public DisabledCmds(ResourceWorld plugin) {
        super(plugin);
    }

    @EventHandler
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("rw.admin")) return;
        if (isInWorld(p)) {
            for (String cmd : configStringList("disabled_commands.commands")) {
                if (cmd.contains(e.getMessage())) {
                    e.setCancelled(true);
                    p.sendMessage(Messenger.message("disabled_command"));
                }
            }
        }
    }
}
