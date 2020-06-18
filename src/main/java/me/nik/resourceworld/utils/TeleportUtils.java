package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Random;

public class TeleportUtils {
    private final int xz;

    public TeleportUtils(ResourceWorld plugin) {
        this.xz = plugin.getConfig().getInt("teleport.settings.max_teleport_range");
    }

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

        int x = random.nextInt(xz);
        int y = 100;
        int z = random.nextInt(xz);

        if (environment == World.Environment.THE_END) {
            randomLocation = new Location(world, x, y, z);
            y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1;
            randomLocation.setY(y);
            Material below = randomLocation.getWorld().getBlockAt(x, y - 3, z).getType();
            if (!below.isSolid()) {
                randomLocation = generateLocation(world);
            }
            return randomLocation;
        } else {
            boolean safe = false;
            while (!safe) {
                randomLocation = new Location(world, x, y, z);
                if (!randomLocation.getBlock().isEmpty()) {
                    randomLocation.setY(y + 1);
                    safe = true;
                } else y--;
            }
        }

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(world);
        }
        return randomLocation;
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
