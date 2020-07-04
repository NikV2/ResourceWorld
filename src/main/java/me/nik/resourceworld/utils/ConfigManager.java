package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final ResourceWorld plugin;

    public ConfigManager(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    public void checkForMistakes() {

        boolean hasMistakes = false;

        List<String> difficulties = new ArrayList<>();
        difficulties.add("PEACEFUL");
        difficulties.add("EASY");
        difficulties.add("NORMAL");
        difficulties.add("HARD");

        List<String> types = new ArrayList<>();
        types.add("AMPLIFIED");
        types.add("BUFFET");
        types.add("CUSTOMIZED");
        types.add("FLAT");
        types.add("LARGE_BIOMES");
        types.add("NORMAL");
        types.add("VERSION_1_1");

        List<String> environments = new ArrayList<>();
        environments.add("NETHER");
        environments.add("NORMAL");
        environments.add("THE_END");

        List<String> mistakes = new ArrayList<>();

        int worldBorder = plugin.getConfig().getInt("world.settings.world_border.size") / 2;
        int netherBorder = plugin.getConfig().getInt("nether_world.settings.world_border.size") / 2;
        int endBorder = plugin.getConfig().getInt("end_world.settings.world_border.size") / 2;
        int teleportRange = plugin.getConfig().getInt("teleport.settings.max_teleport_range");
        int worldRegenerationDelay = plugin.getConfig().getInt("world.settings.block_regeneration.regeneration_delay");
        int netherRegenerationDelay = plugin.getConfig().getInt("nether_world.settings.block_regeneration.regeneration_delay");
        int worldReset = plugin.getConfig().getInt("world.settings.automated_resets.interval");
        int netherReset = plugin.getConfig().getInt("nether_world.settings.automated_resets.interval");
        int endReset = plugin.getConfig().getInt("end_world.settings.automated_resets.interval");

        String worldDifficulty = plugin.getConfig().getString("world.settings.difficulty");
        String netherDifficulty = plugin.getConfig().getString("nether_world.settings.difficulty");
        String endDifficulty = plugin.getConfig().getString("end_world.settings.difficulty");
        String worldType = plugin.getConfig().getString("world.settings.world_type");
        String netherType = plugin.getConfig().getString("nether_world.settings.world_type");
        String endType = plugin.getConfig().getString("end_world.settings.world_type");
        String worldEnvironment = plugin.getConfig().getString("world.settings.environment");
        String netherEnvironment = plugin.getConfig().getString("nether_world.settings.environment");
        String endEnvironment = plugin.getConfig().getString("end_world.settings.environment");

        boolean isWorldEnabled = plugin.getConfig().getBoolean("world.settings.enabled");
        boolean isNetherEnabled = plugin.getConfig().getBoolean("nether_world.settings.enabled");
        boolean isEndEnabled = plugin.getConfig().getBoolean("end_world.settings.enabled");

        if (teleportRange > worldBorder && isWorldEnabled) {
            plugin.getConfig().set("teleport.settings.max_teleport_range", 800);
            plugin.getConfig().set("world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the World Border");
        }

        if (teleportRange > netherBorder && isNetherEnabled) {
            plugin.getConfig().set("teleport.settings.max_teleport_range", 800);
            plugin.getConfig().set("nether_world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the Nether Border");
        }

        if (teleportRange > endBorder && isEndEnabled) {
            plugin.getConfig().set("teleport.settings.max_teleport_range", 800);
            plugin.getConfig().set("end_world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the End Border");
        }

        if (!difficulties.contains(worldDifficulty)) {
            plugin.getConfig().set("world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Difficulty was invalid");
        }

        if (!difficulties.contains(netherDifficulty)) {
            plugin.getConfig().set("nether_world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("Nether World Difficulty was invalid");
        }

        if (!difficulties.contains(endDifficulty)) {
            plugin.getConfig().set("end_world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("End World Difficulty was invalid");
        }

        if (!types.contains(worldType)) {
            plugin.getConfig().set("world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Type was invalid");
        }

        if (!types.contains(netherType)) {
            plugin.getConfig().set("nether_world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("Nether World Type was invalid");
        }

        if (!types.contains(endType)) {
            plugin.getConfig().set("end_world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("End World Type was invalid");
        }

        if (!environments.contains(worldEnvironment)) {
            plugin.getConfig().set("world.settings.environment", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Environment was invalid");
        }

        if (!environments.contains(netherEnvironment)) {
            plugin.getConfig().set("nether_world.settings.environment", "NETHER");
            hasMistakes = true;
            mistakes.add("Nether World Environment was invalid");
        }

        if (!environments.contains(endEnvironment)) {
            plugin.getConfig().set("end_world.settings.environment", "THE_END");
            hasMistakes = true;
            mistakes.add("End World Environment was invalid");
        }

        if (worldRegenerationDelay <= 0) {
            plugin.getConfig().set("world.settings.block_regeneration.regeneration_delay", 1);
            hasMistakes = true;
            mistakes.add("World Regeneration delay cant be set to zero");
        }

        if (netherRegenerationDelay <= 0) {
            plugin.getConfig().set("nether_world.settings.block_regeneration.regeneration_delay", 1);
            hasMistakes = true;
            mistakes.add("Nether World Regeneration delay cant be set to zero");
        }

        if (worldReset <= 0) {
            plugin.getConfig().set("world.settings.automated_resets.interval", 8);
            hasMistakes = true;
            mistakes.add("World Reset Interval cant be set to zero");
        }

        if (netherReset <= 0) {
            plugin.getConfig().set("nether_world.settings.automated_resets.interval", 4);
            hasMistakes = true;
            mistakes.add("Nether World Reset Interval cant be set to zero");
        }

        if (endReset <= 0) {
            plugin.getConfig().set("end_world.settings.automated_resets.interval", 6);
            hasMistakes = true;
            mistakes.add("End World Reset Interval cant be set to zero");
        }

        if (teleportRange <= 5) {
            plugin.getConfig().set("teleport.settings.max_teleport_range", 20);
            hasMistakes = true;
            mistakes.add("Teleport Range cant be set to less or equal or five");
        }

        if (hasMistakes) {
            plugin.saveConfig();
            plugin.reloadConfig();
            Messenger.consoleMessage(MsgType.FIXED_MISTAKES.getMessage().replaceAll("%mistakes%", mistakes.toString()));
        }

        difficulties.clear();
        types.clear();
        environments.clear();
        mistakes.clear();
    }
}