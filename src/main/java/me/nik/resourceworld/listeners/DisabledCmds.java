package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisabledCmds implements Listener {

    @EventHandler
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getWorld().getName().equals(Config.get().getString("world.settings.world_name"))) {
            for (String cmd : Config.get().getStringList("disabled.commands")) {
                if (p.hasPermission("rw.admin")) return;
                if (cmd.contains(e.getMessage())) {
                    e.setCancelled(true);
                    p.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("disabled_command")));
                }
            }
        }
    }
}