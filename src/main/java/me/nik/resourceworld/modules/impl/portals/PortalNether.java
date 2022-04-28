package me.nik.resourceworld.modules.impl.portals;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalNether extends ListenerModule {

    public PortalNether(ResourceWorld plugin) {
        super(Config.Setting.NETHER_PORTALS_ENABLED.getBoolean(), plugin);
    }

    @EventHandler
    public void onPortalWorld(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || e.getFrom().getWorld().getEnvironment() != World.Environment.NORMAL)
            return;

        if (Config.Setting.NETHER_PORTALS_ONLY_RESOURCE.getBoolean() && !e.getFrom().getWorld().getName().equals(Config.Setting.WORLD_NAME.getString()))
            return;

        World world = Bukkit.getWorld(Config.Setting.END_NAME.getString());

        if (world == null) return;

        Location from = e.getFrom();

        double x, y, z;

        if (Config.Setting.NETHER_PORTALS_VANILLA_RATIO.getBoolean()) {

            x = from.getX() / 8;
            y = from.getY() / 8;
            z = from.getZ() / 8;

        } else {

            x = from.getX();
            y = from.getY();
            z = from.getZ();
        }

        e.setTo(new Location(world, x, y, z));
    }

    @EventHandler
    public void onPortalNether(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || !e.getFrom().getWorld().getName().equals(Config.Setting.NETHER_NAME.getString()))
            return;

        Location from = e.getFrom();

        e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_PORTALS_PORTALWORLD.getString()), from.getX(), from.getY(), from.getZ()));
    }
}