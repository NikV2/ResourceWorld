package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;

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

        int worldBorder = Config.get().getInt("world.settings.world_border.size") / 2;
        int netherBorder = Config.get().getInt("nether_world.settings.world_border.size") / 2;
        int endBorder = Config.get().getInt("end_world.settings.world_border.size") / 2;
        int teleportRange = Config.get().getInt("teleport.settings.max_teleport_range");
        int worldRegenerationDelay = Config.get().getInt("world.settings.block_regeneration.regeneration_delay");
        int netherRegenerationDelay = Config.get().getInt("nether_world.settings.block_regeneration.regeneration_delay");
        int worldReset = Config.get().getInt("world.settings.automated_resets.interval");
        int netherReset = Config.get().getInt("nether_world.settings.automated_resets.interval");
        int endReset = Config.get().getInt("end_world.settings.automated_resets.interval");

        String worldDifficulty = Config.get().getString("world.settings.difficulty");
        String netherDifficulty = Config.get().getString("nether_world.settings.difficulty");
        String endDifficulty = Config.get().getString("end_world.settings.difficulty");
        String worldType = Config.get().getString("world.settings.world_type");
        String netherType = Config.get().getString("nether_world.settings.world_type");
        String endType = Config.get().getString("end_world.settings.world_type");
        String worldEnvironment = Config.get().getString("world.settings.environment");
        String netherEnvironment = Config.get().getString("nether_world.settings.environment");
        String endEnvironment = Config.get().getString("end_world.settings.environment");

        boolean isWorldEnabled = Config.get().getBoolean("world.settings.enabled");
        boolean isNetherEnabled = Config.get().getBoolean("nether_world.settings.enabled");
        boolean isEndEnabled = Config.get().getBoolean("end_world.settings.enabled");

        if (teleportRange > worldBorder && isWorldEnabled) {
            Config.get().set("teleport.settings.max_teleport_range", 800);
            Config.get().set("world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the World Border");
        }

        if (teleportRange > netherBorder && isNetherEnabled) {
            Config.get().set("teleport.settings.max_teleport_range", 800);
            Config.get().set("nether_world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the Nether Border");
        }

        if (teleportRange > endBorder && isEndEnabled) {
            Config.get().set("teleport.settings.max_teleport_range", 800);
            Config.get().set("end_world.settings.world_border.size", 4500);
            hasMistakes = true;
            mistakes.add("The Teleport Range was higher than the End Border");
        }

        if (!difficulties.contains(worldDifficulty)) {
            Config.get().set("world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Difficulty was invalid");
        }

        if (!difficulties.contains(netherDifficulty)) {
            Config.get().set("nether_world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("Nether World Difficulty was invalid");
        }

        if (!difficulties.contains(endDifficulty)) {
            Config.get().set("end_world.settings.difficulty", "NORMAL");
            hasMistakes = true;
            mistakes.add("End World Difficulty was invalid");
        }

        if (!types.contains(worldType)) {
            Config.get().set("world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Type was invalid");
        }

        if (!types.contains(netherType)) {
            Config.get().set("nether_world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("Nether World Type was invalid");
        }

        if (!types.contains(endType)) {
            Config.get().set("end_world.settings.world_type", "NORMAL");
            hasMistakes = true;
            mistakes.add("End World Type was invalid");
        }

        if (!environments.contains(worldEnvironment)) {
            Config.get().set("world.settings.environment", "NORMAL");
            hasMistakes = true;
            mistakes.add("World Environment was invalid");
        }

        if (!environments.contains(netherEnvironment)) {
            Config.get().set("nether_world.settings.environment", "NETHER");
            hasMistakes = true;
            mistakes.add("Nether World Environment was invalid");
        }

        if (!environments.contains(endEnvironment)) {
            Config.get().set("end_world.settings.environment", "THE_END");
            hasMistakes = true;
            mistakes.add("End World Environment was invalid");
        }

        if (worldRegenerationDelay <= 0) {
            Config.get().set("world.settings.block_regeneration.regeneration_delay", 1);
            hasMistakes = true;
            mistakes.add("World Regeneration delay cant be set to zero");
        }

        if (netherRegenerationDelay <= 0) {
            Config.get().set("nether_world.settings.block_regeneration.regeneration_delay", 1);
            hasMistakes = true;
            mistakes.add("Nether World Regeneration delay cant be set to zero");
        }

        if (worldReset <= 0) {
            Config.get().set("world.settings.automated_resets.interval", 8);
            hasMistakes = true;
            mistakes.add("World Reset Interval cant be set to zero");
        }

        if (netherReset <= 0) {
            Config.get().set("nether_world.settings.automated_resets.interval", 4);
            hasMistakes = true;
            mistakes.add("Nether World Reset Interval cant be set to zero");
        }

        if (endReset <= 0) {
            Config.get().set("end_world.settings.automated_resets.interval", 6);
            hasMistakes = true;
            mistakes.add("End World Reset Interval cant be set to zero");
        }

        if (teleportRange <= 5) {
            Config.get().set("teleport.settings.max_teleport_range", 20);
            hasMistakes = true;
            mistakes.add("Teleport Range cant be set to less or equal or five");
        }

        if (hasMistakes) {
            Config.save();
            Config.reload();
            plugin.consoleMessage(Messenger.message("fixed_mistakes").replaceAll("%mistakes%", mistakes.toString()));
        }

        difficulties.clear();
        types.clear();
        environments.clear();
        mistakes.clear();
    }
}
