package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class DisabledCmds implements Listener {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);

    @EventHandler
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getWorld().getName().equals(plugin.getConfig().getString("world_name"))) {
            for (String cmd : plugin.getConfig().getStringList("disabled_commands")) {
                if (p.hasPermission("rw.admin")) return;
                if (cmd.contains(e.getMessage())) {
                    e.setCancelled(true);
                    p.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("disabled_command")));
                }
            }
        }
    }
}