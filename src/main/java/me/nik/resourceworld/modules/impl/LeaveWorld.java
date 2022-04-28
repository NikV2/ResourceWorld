package me.nik.resourceworld.modules.impl;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.custom.CustomWorld;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveWorld extends ListenerModule {

    public LeaveWorld(ResourceWorld plugin) {
        super(Config.Setting.SETTINGS_TELEPORT_TO_SPAWN.getBoolean(), plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        final Player p = e.getPlayer();

        final String world = p.getWorld().getName();

        for (CustomWorld rw : this.plugin.getResourceWorlds().values()) {

            if (!rw.getName().equals(world)) continue;

            Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();

            if (loc != null) p.teleport(loc);
        }
    }
}