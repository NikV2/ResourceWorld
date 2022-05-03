package me.nik.resourceworld.utils;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.files.Config;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class LocationFinder {

    private static final Random random = new Random();

    public void teleportSafely(Player player, World world) {

        final World.Environment environment = world.getEnvironment();

        int x, z;
        final boolean nether;

        switch (environment) {

            case NETHER:

                x = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_NETHER_MAX_RANGE.getInt());
                nether = true;

                break;

            case THE_END:

                x = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_END_MAX_RANGE.getInt());
                nether = false;

                break;

            default:

                x = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                z = randomInt(Config.Setting.TELEPORT_WORLD_MAX_RANGE.getInt());
                nether = false;

                break;
        }

        Location location = new Location(world, x, (world.getHighestBlockYAt(x, z) + 1), z);

        //Load the chunk.
        CompletableFuture<Chunk> chunk = PaperLib.getChunkAtAsync(location);

        //Once it's grabbed, execute the following action.
        chunk.thenRunAsync(() -> {

            //Special things we need to account for the nether world.
            if (nether) {

                //Usually the best height.
                location.setY(80);

                //Keep subtracting until we reach the ground.
                while (world.getBlockAt(location).isEmpty()) {
                    location.subtract(0, 1, 0);
                }

                //Add one to make sure the player teleports on top of the block.
                location.add(0, 1, 0);
            }

            //If the location is safe, proceed otherwise just look for another location.
            if (isLocationSafe(location, world, environment)) {

                //Finally teleport and apply effects on the main thread.
                TaskUtils.task(() -> {

                    PaperLib.teleportAsync(player, location);

                    //Idiot proof
                    try {

                        if (Config.Setting.TELEPORT_EFFECTS_ENABLED.getBoolean()) {
                            player.addPotionEffect(
                                    new PotionEffect(
                                            PotionEffectType.getByName(Config.Setting.TELEPORT_EFFECT.getString()),
                                            Config.Setting.TELEPORT_EFFECT_DURATION.getInt() * 20,
                                            Config.Setting.TELEPORT_EFFECT_AMPLIFIER.getInt()));
                        }

                        if (Config.Setting.TELEPORT_SOUND_ENABLED.getBoolean()) {
                            player.playSound(player.getLocation(), Sound.valueOf(Config.Setting.TELEPORT_SOUND.getString()), 2, 2);
                        }

                    } catch (IllegalArgumentException ignored) {
                    }
                });

            } else teleportSafely(player, world); //Repeat
        });
    }

    private boolean isLocationSafe(Location location, World world, World.Environment environment) {

        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        final Block feet = world.getBlockAt(blockX, blockY, blockZ);

        if (!feet.getRelative(BlockFace.UP).isEmpty()) return false;

        switch (environment) {

            case NETHER:

                for (int x = (blockX - 1); x <= (blockX + 1); x++) {
                    for (int y = (blockY - 1); y <= (blockY + 1); y++) {
                        for (int z = (blockZ - 1); z <= (blockZ + 1); z++) {
                            //Make sure no liquid (Lava) is nearby.
                            if (world.getBlockAt(x, y, z).isLiquid()) return false;
                        }
                    }
                }

                return true;

            case THE_END:
                //Make sure the player is not floating
                return !feet.getRelative(BlockFace.DOWN, 2).isEmpty();
        }

        //Return false on liquid otherwise the player will only teleport in the sea.
        return !feet.getRelative(BlockFace.DOWN).isLiquid();
    }

    private int randomInt(int value) {

        int min = -value;

        return random.nextInt((value - min) + 1) + min;
    }
}