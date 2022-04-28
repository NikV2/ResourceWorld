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

public class PortalEnd extends ListenerModule {

    public PortalEnd(ResourceWorld plugin) {
        super(Config.Setting.END_PORTALS_ENABLED.getBoolean(), plugin);
    }

    @EventHandler
    public void onPortalWorld(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL || e.getFrom().getWorld().getEnvironment() != World.Environment.NORMAL)
            return;

        if (Config.Setting.END_PORTALS_ONLY_RESOURCE.getBoolean() && !e.getFrom().getWorld().getName().equals(Config.Setting.WORLD_NAME.getString()))
            return;

        World world = Bukkit.getWorld(Config.Setting.END_NAME.getString());

        if (world == null) return;

        Location to = e.getTo();

        try {

            e.setTo(new Location(world, to.getX(), to.getY(), to.getZ()));

        } catch (NullPointerException ignored) {
            /*
            I don't know why i did this, this is from two years ago.
            I'm guessing there's a good reason for it?
             */

            Location loc = world.getHighestBlockAt(world.getSpawnLocation()).getLocation();

            e.setTo(new Location(world, loc.getX(), loc.getY(), loc.getZ()));
        }
    }

    @EventHandler
    public void onPortalEnd(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL
                || !e.getFrom().getWorld().getName().equals(Config.Setting.END_NAME.getString())) return;

        Location from = e.getFrom();

        e.setTo(new Location(Bukkit.getWorld(Config.Setting.END_PORTALS_PORTALWORLD.getString()), from.getX(), from.getY(), from.getZ()));
    }
}