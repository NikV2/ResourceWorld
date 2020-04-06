package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtils {
    public static HashSet<Material> unsafeBlocks = new HashSet<>();
    static{
        unsafeBlocks.add(Material.LAVA);
        unsafeBlocks.add(Material.WATER);
        unsafeBlocks.add(Material.FIRE);
    }
    public Location generateLocation(World world) {
        Random random = new Random();

        int x = random.nextInt(Config.get().getInt("teleport.settings.max_teleport_range"));
        int y = 85;
        int z = random.nextInt(Config.get().getInt("teleport.settings.max_teleport_range"));

        Location randomLocation = new Location(Bukkit.getWorld(Config.get().getString("world.settings.world_name")), x, y, z);

        if (!(randomLocation.getWorld().getEnvironment() == World.Environment.NETHER)) {
            y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 2;
        }
        randomLocation.setY(y);

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(world);
        }
        if (Config.get().getBoolean("teleport.settings.load_chunk_before_teleporting")) {
            randomLocation.getChunk().load(true);
        }
        return randomLocation;
    }

    private boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Block block = location.getWorld().getBlockAt(x + 3, y, z + 3);
        Block below = location.getWorld().getBlockAt(x - 5, y - 10, z - 5);
        Block above = location.getWorld().getBlockAt(x + 5, y + 10, z + 5);
        return !(unsafeBlocks.contains(below.getType()) || (block.getType().isSolid()) || (above.getType().isSolid()) || (below.isEmpty()));
    }
}
