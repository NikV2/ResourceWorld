package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Random;

public class TeleportUtils {

    private final int xz = Config.get().getInt("teleport.settings.max_teleport_range");

    private final ArrayList<Material> unsafeBlocks = new ArrayList<>();

    {
        unsafeBlocks.add(Material.LAVA);
        unsafeBlocks.add(Material.WATER);
    }

    public Location generateLocation(World world) {
        Random random = new Random();
        World.Environment environment = world.getEnvironment();
        Location randomLocation = null;

        int x = random.nextInt(xz);
        int y = 100;
        int z = random.nextInt(xz);

        if (environment == World.Environment.THE_END) {
            randomLocation = new Location(world, x, y, z);
            y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        } else {
            boolean isSafe = false;
            while (!isSafe) {
                randomLocation = new Location(world, x, y, z);
                if (!randomLocation.getBlock().isEmpty()) {
                    isSafe = true;
                } else y--;
            }
        }
        randomLocation.setY(y + 1);

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(world);
        }

        if (Config.get().getBoolean("teleport.settings.load_chunk_before_teleporting") && !randomLocation.getChunk().isLoaded()) {
            randomLocation.getChunk().load(true);
        }
        return randomLocation;
    }

    private boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Block below = location.getWorld().getBlockAt(x, y - 2, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
        Block blockN = location.getWorld().getBlockAt(x - 1, y, z - 1);
        Block blockP = location.getWorld().getBlockAt(x + 1, y + 1, z + 1);
        return !(unsafeBlocks.contains(below.getType()) || (unsafeBlocks.contains(blockP.getType())) || below.isEmpty() || above.getType().isSolid() || blockN.getType().isSolid() || blockP.getType().isSolid());
    }
}
