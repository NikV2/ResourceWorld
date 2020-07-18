package me.nik.resourceworld.files;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.commentedfiles.CommentedFileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static final String[] HEADER = new String[]{
            "+----------------------------------------------------------------------------------------------+",
            "|                                                                                              |",
            "|                                          Resource World                                      |",
            "|                                                                                              |",
            "|                               Discord: https://discord.gg/m7j2Y9H                            |",
            "|                                                                                              |",
            "|                                           Author: Nik                                        |",
            "|                                                                                              |",
            "+----------------------------------------------------------------------------------------------+"
    };

    /**
     * Credits to Nicole for this amazing Commented File Configuration
     * https://github.com/Esophose/PlayerParticles
     */

    private final ResourceWorld plugin;
    private CommentedFileConfiguration configuration;

    public Config(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        boolean setHeaderFooter = !configFile.exists();
        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) {
            this.configuration.addComments(HEADER);
        }

        for (Setting setting : Setting.values()) {
            setting.reset();
            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) {
            this.configuration.save();
        }
    }

    public void reset() {
        for (Setting setting : Setting.values())
            setting.reset();
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    public enum Setting {
        SETTINGS("settings", "", "General plugin settings"),
        SETTINGS_CHECK_FOR_UPDATES("settings.check_for_updates", true, "Should we check for updates on startup?"),
        SETTINGS_SPAWN_WORLD("settings.main_spawn_world", "world", "The world that the player will Teleport to once a Reset Happens"),
        SETTINGS_TELEPORT_TO_SPAWN("settings.teleport_to_spawn_on_quit", true, "If a player quits inside a resource world, Should we teleport him to spawn?"),

        WORLD("world", "", "Overworld Settings"),
        WORLD_ENABLED("world.enabled", true, "Should we enable this?"),
        WORLD_NAME("world.world_name", "resource_world", "What should the Resource World be named?"),
        WORLD_GENERATE_STRUCTURES("world.generate_structures", true, "Should this world have structures? (Villages e.t.c)"),
        WORLD_TYPE("world.world_type", "NORMAL", "Available World Types: NORMAL , FLAT , LARGE_BIOMES , AMPLIFIED"),
        WORLD_ENVIRONMENT("world.environment", "NORMAL", "Available Environments: NORMAL , NETHER , THE_END"),
        WORLD_CUSTOM_SEED("world.custom_seed", "", "Seed Properties"),
        WORLD_SEED_ENABLED("world.custom_seed.enabled", false, "Should we use a custom seed?"),
        WORLD_SEED("world.custom_seed.seed", -686298914, "The seed to use"),
        WORLD_BORDER("world.world_border", "", "World Border Properties"),
        WORLD_BORDER_ENABLED("world.world_border.enabled", true, "Should we enable this?"),
        WORLD_BORDER_SIZE("world.world_border.size", 4500, "The border size (Note that if you set the World Border for example to 2000, It will be 1000 in one side and 1000 in the opposite one)"),
        WORLD_PVP("world.allow_pvp", true, "Should PvP be enabled?"),
        WORLD_ALWAYS_DAY("world.always_day", false, "Should it always be day in the Resource World?"),
        WORLD_DISABLE_SUFFOCATION("world.disable_suffocation_damage", true, "Should we disable suffocation damage?"),
        WORLD_DISABLE_DROWNING("world.disable_drowning_damage", false, "Should we disable drowning damage?"),
        WORLD_DISABLE_EXPLOSIONS("world.disable_explosion_damage", false, "Should we disable explosion damage?"),
        WORLD_DIFFICULTY("world.difficulty", "NORMAL", "Options: EASY , NORMAL , HARD , PEACEFUL"),
        WORLD_KEEP_INVENTORY("world.keep_inventory_on_death", false, "Should players keep their Inventory items if they die inside the Resource World?"),
        WORLD_KEEP_SPAWN_LOADED("world.keep_spawn_loaded", false, "Should the Resource World auto Generate chunks around the main Spawn Area? (Not Recommended)"),
        WORLD_WEATHER_STORMS("world.weather_storms", false, "Would you like to allow Rain , Snow , Thunderstorms inside the Resource World?"),
        WORLD_RESETS("world.automated_resets", "", "World Reset Properties"),
        WORLD_RESETS_ENABLED("world.automated_resets.enabled", false, "Would you like the Resource World to Automatically Reset?"),
        WORLD_RESETS_INTERVAL("world.automated_resets.interval", 8, "The Interval between Resource World Resets. (In Hours)"),
        WORLD_STORE_TIME("world.automated_resets.store_time_on_shutdown", true, "Do you wan't the plugin to store the remaining reset time once the server shuts down? and use the remaining time once it starts up again?"),
        WORLD_DISABLE_ENTITY_SPAWNING("world.disable_entity_spawning", false, "Should we disable Entity Spawning?"),
        WORLD_ENTITIES("world.entities", "", "Entity Spawning Properties, Higher values = More mobs."),
        WORLD_MAX_ANIMALS("world.entities.max_animals", 45),
        WORLD_MAX_MONSTERS("world.entities.max_monsters", 35),
        WORLD_MAX_AMBIENT("world.entities.max_ambient_entities", 5),
        WORLD_COMMANDS("world.commands_after_reset", "", "Execute specific commands after the world generates"),
        WORLD_COMMANDS_ENABLED("world.commands_after_reset.enabled", false, "Should we enable this?"),
        WORLD_COMMANDS_COMMANDS("world.commands_after_reset.commands", Arrays.asList("title @p title {\\\"text\\\":\\\"The Resource World\\\",\\\"color\\\":\\\"green\\\"}", "title @p subtitle {\\\"text\\\":\\\"Has been Reset!\\\",\\\"color\\\":\\\"green\\\"}"), "Commands listed below will be executed upon reset"),

        NETHER("nether", "", "Nether Settings"),
        NETHER_ENABLED("nether.enabled", false, "Should we enable this?"),
        NETHER_NAME("nether.world_name", "resource_nether", "What should the Resource World be named?"),
        NETHER_PORTALS("nether.portals", "", "Portal Properties"),
        NETHER_PORTALS_ENABLED("nether.portals.override", false, "Would you like to sync your portals with the Resource Worlds? For example if a player goes into a Nether Portal he'll get to the Nether Resource World instead of the default one"),
        NETHER_PORTALS_VANILLA_RATIO("nether.portals.vanilla_portal_ratio", true, "Makes Portal Location Behavior work simularly to the Vanilla one"),
        NETHER_PORTALS_PORTALWORLD("nether.portals.portal_world", "world", "If you're running 1.8 - 1.13 You should put the world where you wan't your players to be teleported to After they enter a Nether Portal inside the Nether. (Ignore if you're running 1.14 +)"),
        NETHER_TYPE("nether.world_type", "NORMAL", "Available World Types: NORMAL , FLAT , LARGE_BIOMES , AMPLIFIED"),
        NETHER_ENVIRONMENT("nether.environment", "NETHER", "Available Environments: NORMAL , NETHER , THE_END"),
        NETHER_BORDER("nether.world_border", "", "World Border Properties"),
        NETHER_BORDER_ENABLED("nether.world_border.enabled", true, "Should we enable this?"),
        NETHER_BORDER_SIZE("nether.world_border.size", 4500, "The border size (Note that if you set the World Border for example to 2000, It will be 1000 in one side and 1000 in the opposite one)"),
        NETHER_PVP("nether.allow_pvp", true, "Should PvP be enabled?"),
        NETHER_DISABLE_SUFFOCATION("nether.disable_suffocation_damage", true, "Should we disable suffocation damage?"),
        NETHER_DISABLE_EXPLOSIONS("nether.disable_explosion_damage", false, "Should we disable explosion damage?"),
        NETHER_DIFFICULTY("nether.difficulty", "NORMAL", "Options: EASY , NORMAL , HARD , PEACEFUL"),
        NETHER_KEEP_INVENTORY("nether.keep_inventory_on_death", false, "Should players keep their Inventory items if they die inside the Resource World?"),
        NETHER_KEEP_SPAWN_LOADED("nether.keep_spawn_loaded", false, "Should the Resource World auto Generate chunks around the main Spawn Area? (Not Recommended)"),
        NETHER_RESETS("nether.automated_resets", "", "World Reset Properties"),
        NETHER_RESETS_ENABLED("nether.automated_resets.enabled", false, "Would you like the Resource World to Automatically Reset?"),
        NETHER_RESETS_INTERVAL("nether.automated_resets.interval", 4, "The Interval between Resource World Resets. (In Hours)"),
        NETHER_STORE_TIME("nether.automated_resets.store_time_on_shutdown", true, "Do you wan't the plugin to store the remaining reset time once the server shuts down? and use the remaining time once it starts up again?"),
        NETHER_DISABLE_ENTITY_SPAWNING("nether.disable_entity_spawning", false, "Should we disable Entity Spawning?"),
        NETHER_ENTITIES("nether.entities", "", "Entity Spawning Properties, Higher values = More mobs."),
        NETHER_MAX_MONSTERS("nether.entities.max_monsters", 35),
        NETHER_COMMANDS("nether.commands_after_reset", "", "Execute specific commands after the world generates"),
        NETHER_COMMANDS_ENABLED("nether.commands_after_reset.enabled", false, "Should we enable this?"),
        NETHER_COMMANDS_COMMANDS("nether.commands_after_reset.commands", Arrays.asList("title @p title {\\\"text\\\":\\\"The Resource Nether\\\",\\\"color\\\":\\\"green\\\"}", "title @p subtitle {\\\"text\\\":\\\"Has been Reset!\\\",\\\"color\\\":\\\"green\\\"}"), "Commands listed below will be executed upon reset"),

        END("end", "", "End Settings"),
        END_ENABLED("end.enabled", false, "Should we enable this?"),
        END_NAME("end.world_name", "resource_end", "What should the Resource World be named?"),
        END_PORTALS("end.portals", "", "Portal Properties"),
        END_PORTALS_ENABLED("end.portals.override", false, "Would you like to sync your portals with the Resource Worlds? For example if a player goes into a Nether Portal he'll get to the Nether Resource World instead of the default one"),
        END_PORTALS_PORTALWORLD("end.portals.portal_world", "world", "If you're running 1.8 - 1.13 You should put the world where you wan't your players to be teleported to After they enter a Nether Portal inside the Nether. (Ignore if you're running 1.14 +)"),
        END_TYPE("end.world_type", "NORMAL", "Available World Types: NORMAL , FLAT , LARGE_BIOMES , AMPLIFIED"),
        END_ENVIRONMENT("end.environment", "THE_END", "Available Environments: NORMAL , NETHER , THE_END"),
        END_BORDER("end.world_border", "", "World Border Properties"),
        END_BORDER_ENABLED("end.world_border.enabled", true, "Should we enable this?"),
        END_BORDER_SIZE("end.world_border.size", 4500, "The border size (Note that if you set the World Border for example to 2000, It will be 1000 in one side and 1000 in the opposite one)"),
        END_PVP("end.allow_pvp", true, "Should PvP be enabled?"),
        END_DISABLE_SUFFOCATION("end.disable_suffocation_damage", true, "Should we disable suffocation damage?"),
        END_DISABLE_EXPLOSIONS("end.disable_explosion_damage", false, "Should we disable explosion damage?"),
        END_DIFFICULTY("end.difficulty", "NORMAL", "Options: EASY , NORMAL , HARD , PEACEFUL"),
        END_KEEP_INVENTORY("end.keep_inventory_on_death", false, "Should players keep their Inventory items if they die inside the Resource World?"),
        END_KEEP_SPAWN_LOADED("end.keep_spawn_loaded", false, "Should the Resource World auto Generate chunks around the main Spawn Area? (Not Recommended)"),
        END_RESETS("end.automated_resets", "", "World Reset Properties"),
        END_RESETS_ENABLED("end.automated_resets.enabled", false, "Would you like the Resource World to Automatically Reset?"),
        END_RESETS_INTERVAL("end.automated_resets.interval", 6, "The Interval between Resource World Resets. (In Hours)"),
        END_STORE_TIME("end.automated_resets.store_time_on_shutdown", true, "Do you wan't the plugin to store the remaining reset time once the server shuts down? and use the remaining time once it starts up again?"),
        END_DISABLE_ENTITY_SPAWNING("end.disable_entity_spawning", false, "Should we disable Entity Spawning?"),
        END_ENTITIES("end.entities", "", "Entity Spawning Properties, Higher values = More mobs."),
        END_MAX_MONSTERS("end.entities.max_monsters", 35),
        END_COMMANDS("end.commands_after_reset", "", "Execute specific commands after the world generates"),
        END_COMMANDS_ENABLED("end.commands_after_reset.enabled", false, "Should we enable this?"),
        END_COMMANDS_COMMANDS("end.commands_after_reset.commands", Arrays.asList("title @p title {\\\"text\\\":\\\"The Resource End\\\",\\\"color\\\":\\\"green\\\"}", "title @p subtitle {\\\"text\\\":\\\"Has been Reset!\\\",\\\"color\\\":\\\"green\\\"}"), "Commands listed below will be executed upon reset"),

        TELEPORT("teleport_settings", "", "Teleport Settings"),
        TELEPORT_ASYNC("teleport_settings.async", true, "If you're running Paper, Leave this enabled for Asynchronous Teleportation"),
        TELEPORT_COOLDOWN("teleport_settings.cooldown", 60, "Interval is in Seconds, So by default: 1 Minute"),
        TELEPORT_DELAY("teleport_settings.delay", 3, "The delay between Teleportations"),
        TELEPORT_MAX_RANGE("teleport_settings.max_teleport_range", 800, "The maximum X, Z The player will be teleported, DO NOT Set this value higher than your World Border"),
        TELEPORT_EFFECTS("teleport_settings.effects", "", "Effect Properties"),
        TELEPORT_EFFECT("teleport_settings.effects.effect", "ABSORPTION", "Available Effects: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html"),
        TELEPORT_EFFECT_DURATION("teleport_settings.effects.duration", 7, "The duration of the Effect"),
        TELEPORT_EFFECT_AMPLIFIER("teleport_settings.effects.amplifier", 2, "The amplifier of the Effect"),
        TELEPORT_SOUNDS("teleport_settings.sounds", "", "Sound Properties"),
        TELEPORT_SOUND_ENABLED("teleport_settings.sounds.enabled", false, "Would you like it to play a sound after a player Teleports to the Resource Worlds?"),
        TELEPORT_SOUND("teleport_settings.sounds.sound", "ENTITY_ENDERMAN_TELEPORT", "You should pick a sound that exists on your server's version API", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html"),

        DISABLED_COMMANDS("disabled_commands", "", "Disabled Commands Settings"),
        DISABLED_COMMANDS_ENABLED("disabled_commands.enabled", false, "Should we enable this?"),
        DISABLED_COMMANDS_LIST("disabled_commands.commands", Arrays.asList("/sethome", "/claim", "/setwarp", "/tpahere"), "Commands listed below will be Disabled if they're executed inside the Resource World");

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        private void loadValue() {
            if (this.value != null) return;
            this.value = ResourceWorld.getInstance().getConfiguration().get(this.key);
        }
    }
}