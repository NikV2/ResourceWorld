package me.nik.resourceworld;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.DisabledCmds;
import me.nik.resourceworld.listeners.Drowning;
import me.nik.resourceworld.listeners.GuiListener;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.Portals;
import me.nik.resourceworld.listeners.blockregen.BlockRegen;
import me.nik.resourceworld.listeners.blockregen.BlockRegenNether;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawning;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawningEnd;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawningNether;
import me.nik.resourceworld.listeners.explosion.Explosion;
import me.nik.resourceworld.listeners.explosion.ExplosionEnd;
import me.nik.resourceworld.listeners.explosion.ExplosionNether;
import me.nik.resourceworld.listeners.suffocation.Suffocation;
import me.nik.resourceworld.listeners.suffocation.SuffocationEnd;
import me.nik.resourceworld.listeners.suffocation.SuffocationNether;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.tasks.AlwaysDay;
import me.nik.resourceworld.tasks.ResetEndWorld;
import me.nik.resourceworld.tasks.ResetNetherWorld;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.ConfigManager;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ResourceWorld extends JavaPlugin {

    private Config config;
    private Data data;
    private Lang lang;

    @Override
    public void onEnable() {
        this.config = new Config();
        this.data = new Data();
        this.lang = new Lang();

        //Load Files
        loadFiles();

        //Check for mistakes
        new ConfigManager(this).checkForMistakes();

        //Startup Message
        Messenger.consoleMessage("");
        Messenger.consoleMessage("            " + ChatColor.GREEN + "Resource World v" + this.getDescription().getVersion());
        Messenger.consoleMessage("");
        Messenger.consoleMessage("                   " + ChatColor.WHITE + "Author: Nik");
        Messenger.consoleMessage("");

        getCommand("resource").setExecutor(new CommandManager(this));

        manageMillis();

        initializeClasses();

        initializeListeners();

        generateWorlds();

        startIntervals();

        initializeTasks();

        new MetricsLite(this, 6981);

        PaperLib.suggestPaper(this);
    }

    @Override
    public void onDisable() {

        //Store Time Left
        storeTimeLeft();

        //Reload Files
        config.reload();
        config.save();
        lang.reload();
        lang.save();
        data.reload();
    }

    @Override
    public FileConfiguration getConfig() {
        return config.get();
    }

    @Override
    public void saveConfig() {
        config.save();
    }

    @Override
    public void reloadConfig() {
        config.reload();
    }

    public FileConfiguration getData() {
        return data.get();
    }

    public void saveData() {
        data.save();
    }

    public void reloadData() {
        data.reload();
    }

    /**
     * Initialize all Existing Tasks.
     */
    private void initializeTasks() {
        if (isEnabled("settings.check_for_updates")) {
            BukkitTask updateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            Messenger.consoleMessage(Messenger.message(MsgType.UPDATE_DISABLED));
        }

        if (isEnabled("world.settings.always_day")) {
            BukkitTask alwaysDay = new AlwaysDay(this).runTaskTimer(this, 1200, 1200);
        }
    }

    private void manageMillis() {
        if (isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.store_time_on_shutdown") && data.get().getLong("world.millis") == 0) {
            data.get().set("world.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
        if (isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown") && data.get().getLong("nether.millis") == 0) {
            data.get().set("nether.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
        if (isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.store_time_on_shutdown") && data.get().getLong("end.millis") == 0) {
            data.get().set("end.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
    }

    private void storeTimeLeft() {
        if (isEnabled("world.settings.automated_resets.enabled") && isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.store_time_on_shutdown")) {
            data.get().set("world.timer", config.get().getInt("world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - data.get().getLong("world.millis")) / 1000D * 20D);
        }
        if (isEnabled("nether_world.settings.automated_resets.enabled") && isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            data.get().set("nether.timer", config.get().getInt("nether_world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - data.get().getLong("nether.millis")) / 1000D * 20D);
        }
        if (isEnabled("end_world.settings.automated_resets.enabled") && isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.store_time_on_shutdown")) {
            data.get().set("end.timer", config.get().getInt("end_world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - data.get().getLong("end.millis")) / 1000D * 20D);
        }
        data.save();
    }

    /**
     * @param input Path to the boolean
     * @return Whether or not that boolean is Enabled.
     */
    private boolean isEnabled(String input) {
        return config.get().getBoolean(input);
    }

    private long worldTimer() {
        if (!isEnabled("world.settings.automated_resets.store_time_on_shutdown")) {
            return config.get().getInt("world.settings.automated_resets.interval") * 72000;
        } else if (data.get().getLong("world.timer") <= 0) {
            return config.get().getInt("world.settings.automated_resets.interval") * 72000;
        } else {
            return data.get().getLong("world.timer");
        }
    }

    private long netherTimer() {
        if (!isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            return config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
        } else if (data.get().getLong("nether.timer") <= 0) {
            return config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
        } else {
            return data.get().getLong("nether.timer");
        }
    }

    private long endTimer() {
        if (!isEnabled("end_world.settings.automated_resets.store_time_on_shutdown")) {
            return config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
        } else if (data.get().getLong("end.timer") <= 0) {
            return config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
        } else {
            return data.get().getLong("end.timer");
        }
    }

    /**
     * Load all the built-in Files.
     */
    private void loadFiles() {
        config.setup(this);
        config.addDefaults();
        config.get().options().copyDefaults(true);
        config.save();
        lang.setup(this);
        lang.addDefaults();
        lang.get().options().copyDefaults(true);
        lang.save();
        data.setup(this);
        data.addDefaults();
        data.get().options().copyDefaults(true);
        data.save();
    }

    /**
     * Initializes all the classes that depend on a function in this class
     */
    public void initializeClasses() {
        Messenger.initialize(lang);
        WorldUtils.initialize(config);
    }


    /**
     * Initialize all the Existing Listeners
     * If a specific Listener is Disabled in the config.yml, Ignore it.
     */
    private void initializeListeners() {
        if (isEnabled("world.settings.block_regeneration.enabled")) {
            registerEvent(new BlockRegen(this));
        }
        if (isEnabled("nether_world.settings.block_regeneration.enabled")) {
            registerEvent(new BlockRegenNether(this));
        }
        if (isEnabled("world.settings.disable_suffocation_damage")) {
            registerEvent(new Suffocation(this));
        }
        if (isEnabled("nether_world.settings.disable_suffocation_damage")) {
            registerEvent(new SuffocationNether(this));
        }
        if (isEnabled("end_world.settings.disable_suffocation_damage")) {
            registerEvent(new SuffocationEnd(this));
        }
        if (isEnabled("world.settings.disable_drowning_damage")) {
            registerEvent(new Drowning(this));
        }
        if (isEnabled("world.settings.disable_entity_spawning")) {
            registerEvent(new EntitySpawning(this));
        }
        if (isEnabled("nether_world.settings.disable_entity_spawning")) {
            registerEvent(new EntitySpawningNether(this));
        }
        if (isEnabled("end_world.settings.disable_entity_spawning")) {
            registerEvent(new EntitySpawningEnd(this));
        }
        if (isEnabled("disabled_commands.enabled")) {
            registerEvent(new DisabledCmds(this));
        }
        if (isEnabled("nether_world.settings.portals.override") || isEnabled("end_world.settings.portals.override")) {
            registerEvent(new Portals(this));
        }
        if (isEnabled("settings.teleport_to_spawn_on_quit")) {
            registerEvent(new LeaveInWorld(this));
        }
        if (isEnabled("world.settings.disable_explosion_damage")) {
            registerEvent(new Explosion(this));
        }
        if (isEnabled("nether_world.settings.disable_explosion_damage")) {
            registerEvent(new ExplosionNether(this));
        }
        if (isEnabled("end_world.settings.disable_explosion_damage")) {
            registerEvent(new ExplosionEnd(this));
        }
        //Don't be an idiot Nik, Always register this Listener
        registerEvent(new GuiListener());
    }

    /**
     * Registers the specified Listener
     *
     * @param listener The listener to register
     */
    public void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Checks if there's any Time saved and Starts the Intervals afterwards.
     */
    private void startIntervals() {
        if (isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.enabled")) {
            int interval = config.get().getInt("world.settings.automated_resets.interval") * 72000;
            BukkitTask resetWorld = new ResetWorld(this).runTaskTimer(this, worldTimer(), interval);
        }
        if (isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.enabled")) {
            int interval = config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetNether = new ResetNetherWorld(this).runTaskTimer(this, netherTimer(), interval);
        }
        if (isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.enabled")) {
            int interval = config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetEnd = new ResetEndWorld(this).runTaskTimer(this, endTimer(), interval);
        }
    }

    /**
     * Generates or Loads all the Enabled Worlds
     */
    private void generateWorlds() {
        if (isEnabled("world.settings.enabled")) {
            new WorldGenerator(this).createWorld();
        }
        if (isEnabled("nether_world.settings.enabled")) {
            new WorldGeneratorNether(this).createWorld();
        }
        if (isEnabled("end_world.settings.enabled")) {
            new WorldGeneratorEnd(this).createWorld();
        }
    }
}