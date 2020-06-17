package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtils {

    private static final HashSet<Material> UNSAFE_BLOCKS = new HashSet<>();
    private static int xz;

    static {
        UNSAFE_BLOCKS.add(Material.LAVA);
        UNSAFE_BLOCKS.add(Material.WATER);
    }

    public TeleportUtils(ResourceWorld plugin) {
        xz = plugin.getConfig().getInt("teleport.settings.max_teleport_range");
    }

    /**
     * Checks for a safe location within that world
     *
     * @param world The world to find a random location from
     * @return A random location
     */
    public static Location generateLocation(World world) {
        Random random = new Random();
        World.Environment environment = world.getEnvironment();
        Location randomLocation;

        int x = random.nextInt(xz);
        int y = 100;
        int z = random.nextInt(xz);

        if (environment == World.Environment.THE_END) {
            randomLocation = new Location(world, x, y, z);
            y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        } else {
            randomLocation = new Location(world, x, y, z);
        }
        randomLocation.setY(y + 1);

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(world);
        }
        return randomLocation;
    }

    private static boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Material below = location.getWorld().getBlockAt(x, y - 2, z).getType();
        Material above = location.getWorld().getBlockAt(x, y + 1, z).getType();
        Material blockN = location.getWorld().getBlockAt(x - 1, y, z - 1).getType();
        Material blockP = location.getWorld().getBlockAt(x + 1, y + 1, z + 1).getType();
        return !(UNSAFE_BLOCKS.contains(below) || (UNSAFE_BLOCKS.contains(blockP)) || below.isAir() || above.isSolid() || blockN.isSolid() || blockP.isSolid());
    }
}
