package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtils {
    public static HashSet<Material> unsafeBlocks = new HashSet<>();
    static{
        unsafeBlocks.add(Material.LAVA);
        unsafeBlocks.add(Material.FIRE);
        unsafeBlocks.add(Material.WATER);
    }
    public static Location generateLocation(World world) {
        Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
        Random random = new Random();

        int x = random.nextInt(plugin.getConfig().getInt("Max Teleport Range"));
        int y = 150;
        int z = random.nextInt(plugin.getConfig().getInt("Max Teleport Range"));

        Location randomLocation = new Location(Bukkit.getWorld(plugin.getConfig().getString("World Name")), x, y, z);

        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y + 3);

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(world);
        }
        return randomLocation;
    }
    public static boolean isLocationSafe(Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 5, z);
        Block above = location.getWorld().getBlockAt(x, y + 5, z);

        return !(unsafeBlocks.contains(below.getType()) || (block.getType().isSolid()) || (above.getType().isSolid()));
    }
}
