package me.nik.resourceworld.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private File file;
    private FileConfiguration config;

    public void setup(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException ignored) {
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void addDefaults() {
        //config.yml
        List<String> blocks = get().getStringList("world.settings.block_regeneration.blocks");
        blocks.add("DIAMOND_ORE");
        blocks.add("GOLD_ORE");
        blocks.add("EMERALD_ORE");
        blocks.add("REDSTONE_ORE");
        blocks.add("COAL_ORE");
        blocks.add("IRON_ORE");
        blocks.add("LAPIS_ORE");
        List<String> netherBlocks = get().getStringList("nether_world.settings.block_regeneration.blocks");
        netherBlocks.add("NETHER_QUARTZ_ORE");
        List<String> list = get().getStringList("disabled_commands.commands");
        list.add("/sethome");
        list.add("/claim");
        list.add("/setwarp");
        list.add("/tpahere");
        List<String> worldCmds = get().getStringList("world.settings.automated_resets.commands");
        worldCmds.add("title @p title {\"text\":\"The Resource World\",\"color\":\"green\"}");
        worldCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> netherCmds = get().getStringList("nether_world.settings.automated_resets.commands");
        netherCmds.add("title @p title {\"text\":\"The Resource Nether\",\"color\":\"green\"}");
        netherCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> endCmds = get().getStringList("end_world.settings.automated_resets.commands");
        endCmds.add("title @p title {\"text\":\"The Resource End\",\"color\":\"green\"}");
        endCmds.add("title @p subtitle {\"text\":\"Has been Reset!\",\"color\":\"green\"}");
        List<String> unsafeBlocks = get().getStringList("teleport.settings.unsafe_blocks");
        unsafeBlocks.add("lava");
        unsafeBlocks.add("water");
        get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        get().addDefault("settings.check_for_updates", true);
        get().addDefault("settings.main_spawn_world", "world");
        get().addDefault("settings.teleport_to_spawn_on_quit", true);
        get().addDefault("world.settings.enabled", true);
        get().addDefault("world.settings.world_name", "resource_world");
        get().addDefault("world.settings.generate_structures", true);
        get().addDefault("world.settings.world_type", "NORMAL");
        get().addDefault("world.settings.environment", "NORMAL");
        get().addDefault("world.settings.custom_seed.enabled", false);
        get().addDefault("world.settings.custom_seed.seed", -686298914);
        get().addDefault("world.settings.world_border.enabled", true);
        get().addDefault("world.settings.world_border.size", 4500);
        get().addDefault("world.settings.allow_pvp", true);
        get().addDefault("world.settings.always_day", false);
        get().addDefault("world.settings.disable_suffocation_damage", true);
        get().addDefault("world.settings.disable_drowning_damage", true);
        get().addDefault("world.settings.disable_explosion_damage", false);
        get().addDefault("world.settings.difficulty", "NORMAL");
        get().addDefault("world.settings.keep_inventory_on_death", false);
        get().addDefault("world.settings.keep_spawn_loaded", false);
        get().addDefault("world.settings.weather_storms", true);
        get().addDefault("world.settings.automated_resets.enabled", false);
        get().addDefault("world.settings.automated_resets.interval", 8);
        get().addDefault("world.settings.automated_resets.store_time_on_shutdown", true);
        get().addDefault("world.settings.disable_entity_spawning", false);
        get().addDefault("world.settings.entities.max_animals", 45);
        get().addDefault("world.settings.entities.max_monsters", 35);
        get().addDefault("world.settings.entities.max_ambient_entities", 5);
        get().addDefault("world.settings.block_regeneration.enabled", true);
        get().addDefault("world.settings.block_regeneration.regeneration_delay", 30);
        get().addDefault("world.settings.block_regeneration.blocks", blocks);
        get().addDefault("world.settings.commands_after_reset.enabled", false);
        get().addDefault("world.settings.commands_after_reset.commands", worldCmds);
        //Nether World
        get().addDefault("nether_world.settings.enabled", false);
        get().addDefault("nether_world.settings.portals.override", false);
        get().addDefault("nether_world.settings.portals.vanilla_portal_ratio", true);
        get().addDefault("nether_world.settings.portals.portal_world", "world");
        get().addDefault("nether_world.settings.world_name", "resource_nether");
        get().addDefault("nether_world.settings.world_type", "NORMAL");
        get().addDefault("nether_world.settings.environment", "NETHER");
        get().addDefault("nether_world.settings.world_border.enabled", true);
        get().addDefault("nether_world.settings.world_border.size", 4500);
        get().addDefault("nether_world.settings.allow_pvp", true);
        get().addDefault("nether_world.settings.disable_suffocation_damage", true);
        get().addDefault("nether_world.settings.disable_explosion_damage", false);
        get().addDefault("nether_world.settings.difficulty", "NORMAL");
        get().addDefault("nether_world.settings.keep_inventory_on_death", false);
        get().addDefault("nether_world.settings.keep_spawn_loaded", false);
        get().addDefault("nether_world.settings.automated_resets.enabled", false);
        get().addDefault("nether_world.settings.automated_resets.interval", 4);
        get().addDefault("nether_world.settings.automated_resets.store_time_on_shutdown", true);
        get().addDefault("nether_world.settings.disable_entity_spawning", false);
        get().addDefault("nether_world.settings.entities.max_monsters", 35);
        get().addDefault("nether_world.settings.block_regeneration.enabled", false);
        get().addDefault("nether_world.settings.block_regeneration.regeneration_delay", 30);
        get().addDefault("nether_world.settings.block_regeneration.blocks", netherBlocks);
        get().addDefault("nether_world.settings.commands_after_reset.enabled", false);
        get().addDefault("nether_world.settings.commands_after_reset.commands", netherCmds);
        //End World
        get().addDefault("end_world.settings.enabled", false);
        get().addDefault("end_world.settings.portals.override", false);
        get().addDefault("end_world.settings.portals.portal_world", "world");
        get().addDefault("end_world.settings.world_name", "resource_end");
        get().addDefault("end_world.settings.world_type", "NORMAL");
        get().addDefault("end_world.settings.environment", "THE_END");
        get().addDefault("end_world.settings.world_border.enabled", true);
        get().addDefault("end_world.settings.world_border.size", 4500);
        get().addDefault("end_world.settings.allow_pvp", true);
        get().addDefault("end_world.settings.disable_suffocation_damage", true);
        get().addDefault("end_world.settings.disable_explosion_damage", false);
        get().addDefault("end_world.settings.difficulty", "NORMAL");
        get().addDefault("end_world.settings.keep_inventory_on_death", false);
        get().addDefault("end_world.settings.keep_spawn_loaded", false);
        get().addDefault("end_world.settings.automated_resets.enabled", false);
        get().addDefault("end_world.settings.automated_resets.interval", 6);
        get().addDefault("end_world.settings.automated_resets.store_time_on_shutdown", true);
        get().addDefault("end_world.settings.disable_entity_spawning", false);
        get().addDefault("end_world.settings.entities.max_monsters", 35);
        get().addDefault("end_world.settings.commands_after_reset.enabled", false);
        get().addDefault("end_world.settings.commands_after_reset.commands", endCmds);
        //Other Settings
        get().addDefault("teleport.settings.cooldown", 60);
        get().addDefault("teleport.settings.delay", 3);
        get().addDefault("teleport.settings.max_teleport_range", 800);
        get().addDefault("teleport.settings.effects.effect", "ABSORPTION");
        get().addDefault("teleport.settings.effects.duration", 7);
        get().addDefault("teleport.settings.effects.amplifier", 2);
        get().addDefault("teleport.settings.sounds.enabled", false);
        get().addDefault("teleport.settings.sounds.sound", "ENTITY_ENDERMAN_TELEPORT");
        get().addDefault("teleport.settings.sounds.volume", 1);
        get().addDefault("teleport.settings.sounds.pitch", 1);
        get().addDefault("teleport.settings.unsafe_blocks", unsafeBlocks);
        get().addDefault("disabled_commands.enabled", false);
        get().addDefault("disabled_commands.commands", list);
    }
}
