package me.nik.resourceworld.modules.impl;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;

public class Portals extends ListenerModule {

    public Portals(ResourceWorld plugin) {
        super(Config.Setting.NETHER_PORTALS_ENABLED.getBoolean() || Config.Setting.END_PORTALS_ENABLED.getBoolean(), plugin);
    }

    private void handleEndPortal(PlayerPortalEvent e) {
        //Overworld -> End
        if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL) {

            if (!Config.Setting.END_PORTALS_ONLY_RESOURCE.getBoolean() && e.getFrom().getWorld().getName().equals(Config.Setting.WORLD_NAME.getString())) {

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
            //End -> Overworld
        } else if (e.getFrom().getWorld().getName().equals(Config.Setting.END_NAME.getString())) {

            Location from = e.getFrom();

            e.setTo(new Location(Bukkit.getWorld(Config.Setting.END_PORTALS_PORTALWORLD.getString()), from.getX(), from.getY(), from.getZ()));
        }
    }

    private void handleNetherPortal(PlayerPortalEvent e) {
        //Overworld -> Nether
        if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL) {

            if (Config.Setting.NETHER_PORTALS_ONLY_RESOURCE.getBoolean() && !e.getFrom().getWorld().getName().equals(Config.Setting.WORLD_NAME.getString()))
                return;

            World world = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());

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

            //End -> Overworld
        } else if (e.getFrom().getWorld().getName().equals(Config.Setting.NETHER_NAME.getString())) {

            Location from = e.getFrom();

            e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_PORTALS_PORTALWORLD.getString()), from.getX(), from.getY(), from.getZ()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPortalEnd(PlayerPortalEvent e) {
        switch (e.getCause()) {
            case NETHER_PORTAL:
                handleNetherPortal(e);
                break;
            case END_PORTAL:
                handleEndPortal(e);
                break;
        }
    }
}