package me.nik.resourceworld.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private static File file;
    private static FileConfiguration config;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ResourceWorld").getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException ignored) {
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void addDefaults() {
        //config.yml
        List<String> blocks = Config.get().getStringList("world.settings.block_regeneration.blocks");
        blocks.add("DIAMOND_ORE");
        blocks.add("GOLD_ORE");
        blocks.add("EMERALD_ORE");
        blocks.add("REDSTONE_ORE");
        blocks.add("COAL_ORE");
        blocks.add("IRON_ORE");
        blocks.add("LAPIS_ORE");
        List<String> netherBlocks = Config.get().getStringList("nether_world.settings.block_regeneration.blocks");
        netherBlocks.add("NETHER_QUARTZ_ORE");
        List<String> list = Config.get().getStringList("disabled_commands.commands");
        list.add("/sethome");
        list.add("/claim");
        list.add("/setwarp");
        list.add("/tpahere");
        List<String> worldCmds = Config.get().getStringList("world.settings.automated_resets.commands");
        worldCmds.add("title @p title {\"text\":\"The Resource World\",\"color\":\"green\"}");
        worldCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> netherCmds = Config.get().getStringList("nether_world.settings.automated_resets.commands");
        netherCmds.add("title @p title {\"text\":\"The Resource Nether\",\"color\":\"green\"}");
        netherCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> endCmds = Config.get().getStringList("end_world.settings.automated_resets.commands");
        endCmds.add("title @p title {\"text\":\"The Resource End\",\"color\":\"green\"}");
        endCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> unsafeBlocks = Config.get().getStringList("teleport.settings.unsafe_blocks");
        unsafeBlocks.add("lava");
        unsafeBlocks.add("water");
        Config.get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        Config.get().addDefault("settings.check_for_updates", true);
        Config.get().addDefault("settings.main_spawn_world", "world");
        Config.get().addDefault("settings.teleport_to_spawn_on_quit", true);
        Config.get().addDefault("world.settings.enabled", true);
        Config.get().addDefault("world.settings.world_name", "resource_world");
        Config.get().addDefault("world.settings.generate_structures", true);
        Config.get().addDefault("world.settings.world_type", "NORMAL");
        Config.get().addDefault("world.settings.environment", "NORMAL");
        Config.get().addDefault("world.settings.custom_seed.enabled", false);
        Config.get().addDefault("world.settings.custom_seed.seed", -686298914);
        Config.get().addDefault("world.settings.world_border.enabled", true);
        Config.get().addDefault("world.settings.world_border.size", 4500);
        Config.get().addDefault("world.settings.allow_pvp", true);
        Config.get().addDefault("world.settings.always_day", true);
        Config.get().addDefault("world.settings.disable_suffocation_damage", true);
        Config.get().addDefault("world.settings.disable_drowning_damage", true);
        Config.get().addDefault("world.settings.difficulty", "NORMAL");
        Config.get().addDefault("world.settings.keep_inventory_on_death", false);
        Config.get().addDefault("world.settings.keep_spawn_loaded", false);
        Config.get().addDefault("world.settings.weather_storms", true);
        Config.get().addDefault("world.settings.automated_resets.enabled", false);
        Config.get().addDefault("world.settings.automated_resets.interval", 8);
        Config.get().addDefault("world.settings.automated_resets.store_time_on_shutdown", true);
        Config.get().addDefault("world.settings.disable_entity_spawning", false);
        Config.get().addDefault("world.settings.entities.max_animals", 45);
        Config.get().addDefault("world.settings.entities.max_monsters", 35);
        Config.get().addDefault("world.settings.entities.max_ambient_entities", 5);
        Config.get().addDefault("world.settings.block_regeneration.enabled", true);
        Config.get().addDefault("world.settings.block_regeneration.regeneration_delay", 30);
        Config.get().addDefault("world.settings.block_regeneration.blocks", blocks);
        Config.get().addDefault("world.settings.commands_after_reset.enabled", false);
        Config.get().addDefault("world.settings.commands_after_reset.commands", worldCmds);
        //Nether World
        Config.get().addDefault("nether_world.settings.enabled", false);
        Config.get().addDefault("nether_world.settings.portals.override", false);
        Config.get().addDefault("nether_world.settings.portals.vanilla_portal_ratio", true);
        Config.get().addDefault("nether_world.settings.portals.portal_world", "world");
        Config.get().addDefault("nether_world.settings.world_name", "resource_nether");
        Config.get().addDefault("nether_world.settings.world_type", "NORMAL");
        Config.get().addDefault("nether_world.settings.environment", "NETHER");
        Config.get().addDefault("nether_world.settings.world_border.enabled", true);
        Config.get().addDefault("nether_world.settings.world_border.size", 4500);
        Config.get().addDefault("nether_world.settings.allow_pvp", true);
        Config.get().addDefault("nether_world.settings.disable_suffocation_damage", true);
        Config.get().addDefault("nether_world.settings.difficulty", "NORMAL");
        Config.get().addDefault("nether_world.settings.keep_inventory_on_death", false);
        Config.get().addDefault("nether_world.settings.keep_spawn_loaded", false);
        Config.get().addDefault("nether_world.settings.automated_resets.enabled", false);
        Config.get().addDefault("nether_world.settings.automated_resets.interval", 4);
        Config.get().addDefault("nether_world.settings.automated_resets.store_time_on_shutdown", true);
        Config.get().addDefault("nether_world.settings.disable_entity_spawning", false);
        Config.get().addDefault("nether_world.settings.entities.max_monsters", 35);
        Config.get().addDefault("nether_world.settings.block_regeneration.enabled", false);
        Config.get().addDefault("nether_world.settings.block_regeneration.regeneration_delay", 30);
        Config.get().addDefault("nether_world.settings.block_regeneration.blocks", netherBlocks);
        Config.get().addDefault("nether_world.settings.commands_after_reset.enabled", false);
        Config.get().addDefault("nether_world.settings.commands_after_reset.commands", netherCmds);
        //End World
        Config.get().addDefault("end_world.settings.enabled", false);
        Config.get().addDefault("end_world.settings.portals.override", false);
        Config.get().addDefault("end_world.settings.portals.portal_world", "world");
        Config.get().addDefault("end_world.settings.world_name", "resource_end");
        Config.get().addDefault("end_world.settings.world_type", "NORMAL");
        Config.get().addDefault("end_world.settings.environment", "THE_END");
        Config.get().addDefault("end_world.settings.world_border.enabled", true);
        Config.get().addDefault("end_world.settings.world_border.size", 4500);
        Config.get().addDefault("end_world.settings.allow_pvp", true);
        Config.get().addDefault("end_world.settings.disable_suffocation_damage", true);
        Config.get().addDefault("end_world.settings.difficulty", "NORMAL");
        Config.get().addDefault("end_world.settings.keep_inventory_on_death", false);
        Config.get().addDefault("end_world.settings.keep_spawn_loaded", false);
        Config.get().addDefault("end_world.settings.automated_resets.enabled", false);
        Config.get().addDefault("end_world.settings.automated_resets.interval", 6);
        Config.get().addDefault("end_world.settings.automated_resets.store_time_on_shutdown", true);
        Config.get().addDefault("end_world.settings.disable_entity_spawning", false);
        Config.get().addDefault("end_world.settings.entities.max_monsters", 35);
        Config.get().addDefault("end_world.settings.commands_after_reset.enabled", false);
        Config.get().addDefault("end_world.settings.commands_after_reset.commands", endCmds);
        //Other Settings
        Config.get().addDefault("teleport.settings.cooldown", 60);
        Config.get().addDefault("teleport.settings.delay", 3);
        Config.get().addDefault("teleport.settings.max_teleport_range", 800);
        Config.get().addDefault("teleport.settings.effects.effect", "ABSORPTION");
        Config.get().addDefault("teleport.settings.effects.duration", 7);
        Config.get().addDefault("teleport.settings.effects.amplifier", 2);
        Config.get().addDefault("teleport.settings.sounds.enabled", false);
        Config.get().addDefault("teleport.settings.sounds.sound", "ENTITY_ENDERMAN_TELEPORT");
        Config.get().addDefault("teleport.settings.sounds.volume", 1);
        Config.get().addDefault("teleport.settings.sounds.pitch", 1);
        Config.get().addDefault("teleport.settings.unsafe_blocks", unsafeBlocks);
        Config.get().addDefault("disabled_commands.enabled", false);
        Config.get().addDefault("disabled_commands.commands", list);
    }
}
