package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Location;
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
        int y = environment == World.Environment.NETHER ? 50 : 100;
        int z;

        switch (environment) {

            case NETHER:

                x = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());

                boolean safe = false;

                while (!safe) {

                    randomLocation = new Location(world, x, y, z);

                    if (!randomLocation.getBlock().isEmpty()) {

                        randomLocation.setY(y + 1);

                        safe = true;

                    } else y--;
                }

                if (!isLocationSafe(randomLocation)) {

                    randomLocation = generateLocation(world);
                }

                return randomLocation;

            case THE_END:

                x = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());

                randomLocation = new Location(world, x, y, z);

                y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1;

                randomLocation.setY(y);

                if (!randomLocation.getWorld().getBlockAt(x, y - 3, z).getType().isSolid()) {
                    randomLocation = generateLocation(world);
                }

                return randomLocation;

            default:

                x = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());

                randomLocation = new Location(world, x, y, z);

                randomLocation.setY(randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1);

                if (!isLocationSafe(randomLocation)) {
                    randomLocation = generateLocation(world);
                }

                return randomLocation;
        }
    }

    private boolean isLocationSafe(Location location) {

        final Block feet = location.getBlock();

        if (feet.getRelative(BlockFace.UP).getType().isSolid()) return false;

        if (location.getWorld().getEnvironment() == World.Environment.NETHER) {

            final double expand = .5D;

            Block under, center;

            for (double x = -expand; x <= expand; x += expand) {

                for (double z = -expand; z <= expand; z += expand) {

                    under = location.clone().add(z, 0D, x).getBlock();

                    center = location.clone().add(z, -.5D, x).getBlock();

                    if (under.isLiquid() || center.isLiquid()) return false;
                }
            }

            return true;
        }

        return !feet.getRelative(BlockFace.DOWN).isLiquid();
    }

    private int randomInt(int random) {
        Random r = new Random();

        int min = -random;

        return r.nextInt((random - min) + 1) + min;
    }
}