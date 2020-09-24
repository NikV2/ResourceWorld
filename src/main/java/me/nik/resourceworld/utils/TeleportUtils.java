package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Random;

public class TeleportUtils {

    /**
     * Checks for a safe location within that world
     *
     * @param world The world to find a random location from
     * @return A random location
     */
    public Location generateLocation(World world) {
        Random random = new Random();
        World.Environment environment = world.getEnvironment();
        Location randomLocation = null;

        int x;
        int y = 100;
        int z;

        switch (environment) {
            case NETHER:
                x = random.nextInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                y = 80;
                z = random.nextInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
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
                x = random.nextInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                z = random.nextInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                randomLocation = new Location(world, x, y, z);
                y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1;
                randomLocation.setY(y);
                Material below = randomLocation.getWorld().getBlockAt(x, y - 3, z).getType();
                if (!below.isSolid()) {
                    randomLocation = generateLocation(world);
                }
                return randomLocation;
            default:
                x = random.nextInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                z = random.nextInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
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

        Block feet = location.getBlock();
        if (feet.getType().isSolid() && feet.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
            return false;
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (head.getType().isSolid()) {
            return false;
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false;
        }
        if (ground.isLiquid()) {
            return false;
        }
        return !feet.getLocation().add(0, -1, 0).getBlock().isLiquid();
    }
}