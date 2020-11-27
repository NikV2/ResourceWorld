package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Random;

public class LocationFinder {

    /**
     * Checks for a safe location within that world
     *
     * @param world The world to find a random location from
     * @return A random location
     */
    public Location generateLocation(World world) {
        final World.Environment environment = world.getEnvironment();
        Location randomLocation = null;

        int x;
        int y = 100;
        int z;

        switch (environment) {
            case NETHER:
                x = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                y = 60;
                z = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                boolean safe = false;
                while (!safe) {
                    randomLocation = new Location(world, x, y, z);
                    if (!randomLocation.getBlock().isEmpty()) {
                        randomLocation.setY(y + 1);
                        safe = true;
                    } else y--;
                }
                while (!isLocationSafe(randomLocation)) {
                    randomLocation = generateLocation(world);
                }
                return randomLocation;
            case THE_END:
                x = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                randomLocation = new Location(world, x, y, z);
                y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1;
                randomLocation.setY(y);
                Material below = randomLocation.getWorld().getBlockAt(x, y - 3, z).getType();
                if (!below.isSolid()) {
                    randomLocation = generateLocation(world);
                }
                return randomLocation;
            default:
                x = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                boolean safee = false;
                while (!safee) {
                    randomLocation = new Location(world, x, y, z);
                    if (!randomLocation.getBlock().isEmpty()) {
                        randomLocation.setY(y + 1);
                        safee = true;
                    } else y--;
                }
                while (!isLocationSafe(randomLocation)) {
                    randomLocation = generateLocation(world);
                }
                return randomLocation;
        }
    }

    private boolean isLocationSafe(Location location) {

        final Block feet = location.getBlock();
        if (feet.getType().isSolid() && feet.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
            return false;
        }

        if (feet.getRelative(BlockFace.UP).getType().isSolid()) {
            return false;
        }

        final Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false;
        }
        if (ground.isLiquid()) {
            return false;
        }

        if (location.getWorld().getEnvironment() == World.Environment.NETHER) {
            final double expand = 0.35;
            for (double x = -expand; x <= expand; x += expand) {
                for (double z = -expand; z <= expand; z += expand) {
                    Block block1 = location.clone().add(z, 0, x).getBlock();
                    Block block2 = location.clone().add(z, -0.001, x).getBlock();
                    //Gotta do contains due to the material name changes between 1.8-1.16, Not efficient.
                    //TODO: Use enums for material names and initialize them on startup instead of this lazy ass method
                    if (block1.getType().name().contains("LAVA") || block2.getType().name().contains("LAVA"))
                        return true;
                }
            }
        }

        return !feet.getLocation().add(0, -1, 0).getBlock().isLiquid();
    }

    private int randomInt(int random) {
        Random r = new Random();

        int min = -random;

        return r.nextInt((random - min) + 1) + min;
    }
}