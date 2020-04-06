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
            } catch (IOException e) {
                //Does not exist
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
        } catch (IOException e) {
            //Cannot save file
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void addDefaults() {
        //config.yml
        List<String> list = Config.get().getStringList("disabled.commands");
        list.add("/sethome");
        list.add("/claim");
        list.add("/setwarp");
        list.add("/tpahere");
        Config.get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        Config.get().addDefault("settings.enabled", false);
        Config.get().addDefault("settings.check_for_updates", true);
        Config.get().addDefault("world.settings.world_name", "resource_world");
        Config.get().addDefault("world.settings.generate_structures", true);
        Config.get().addDefault("world.settings.world_type", "NORMAL");
        Config.get().addDefault("world.settings.environment", "NORMAL");
        Config.get().addDefault("world.settings.custom_seed.enabled", false);
        Config.get().addDefault("world.settings.custom_seed.seed", -686298914);
        Config.get().addDefault("world.settings.world_border.enabled", true);
        Config.get().addDefault("world.settings.world_border.size", 3500);
        Config.get().addDefault("world.settings.allow_pvp", true);
        Config.get().addDefault("world.settings.difficulty", "NORMAL");
        Config.get().addDefault("world.settings.keep_spawn_loaded", false);
        Config.get().addDefault("world.settings.weather_storms", true);
        Config.get().addDefault("world.settings.automated_resets.enabled", false);
        Config.get().addDefault("world.settings.automated_resets.interval", 6);
        Config.get().addDefault("world.settings.entities.max_animals", 45);
        Config.get().addDefault("world.settings.entities.max_monsters", 35);
        Config.get().addDefault("world.settings.entities.max_ambient_entities", 5);
        Config.get().addDefault("world.settings.main_spawn_world", "world");
        Config.get().addDefault("world.settings.gamerules.not_always_day", true);
        Config.get().addDefault("world.settings.gamerules.can_mobs_spawn", true);
        Config.get().addDefault("world.settings.gamerules.can_fire_spread", true);
        Config.get().addDefault("world.settings.gamerules.keep_inventory_on_death", false);
        Config.get().addDefault("world.settings.gamerules.mob_griefing", true);
        Config.get().addDefault("world.settings.gamerules.show_death_messages", true);
        Config.get().addDefault("teleport.settings.cooldown", 60);
        Config.get().addDefault("teleport.settings.delay", 3);
        Config.get().addDefault("teleport.settings.max_teleport_range", 800);
        Config.get().addDefault("teleport.settings.load_chunk_before_teleporting", false);
        Config.get().addDefault("teleport.settings.effects.effect", "ABSORPTION");
        Config.get().addDefault("teleport.settings.effects.duration", 7);
        Config.get().addDefault("teleport.settings.effects.amplifier", 2);
        Config.get().addDefault("teleport.settings.sounds.enabled", true);
        Config.get().addDefault("teleport.settings.sounds.sound", "ENTITY_ENDERMAN_TELEPORT");
        Config.get().addDefault("teleport.settings.sounds.volume", 1);
        Config.get().addDefault("teleport.settings.sounds.pitch", 1);
        Config.get().addDefault("disabled.commands", list);
    }
}
