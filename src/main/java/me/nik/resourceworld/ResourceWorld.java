package me.nik.resourceworld;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.BlockRegen;
import me.nik.resourceworld.listeners.BlockRegenNether;
import me.nik.resourceworld.listeners.DisabledCmds;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.MenuHandler;
import me.nik.resourceworld.listeners.Portals;
import me.nik.resourceworld.listeners.WorldSettings;
import me.nik.resourceworld.tasks.ResetEndWorld;
import me.nik.resourceworld.tasks.ResetNetherWorld;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.ConfigManager;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ResourceWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        //Load Files
        loadFiles();

        //Check for mistakes
        new ConfigManager(this).checkForMistakes();

        //Startup Message
        consoleMessage("");
        consoleMessage("            " + ChatColor.GREEN + "Resource World v" + this.getDescription().getVersion());
        consoleMessage("");
        consoleMessage("                   " + ChatColor.WHITE + "Author: Nik");
        consoleMessage("");
        consoleMessage("     " + ChatColor.GREEN + "Running on " + ChatColor.WHITE + this.getServer().getVersion());
        consoleMessage("");

        getCommand("resource").setExecutor(new CommandManager(this));

        manageMillis();

        initialize();

        generateWorlds();

        startIntervals();

        //Check for updates
        if (isEnabled("settings.check_for_updates")) {
            BukkitTask updateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            consoleMessage(Messenger.message("update_disabled"));
        }

        //Enable bStats
        int pluginId = 6981;
        MetricsLite metricsLite = new MetricsLite(this, pluginId);

        PaperLib.suggestPaper(this);
    }

    @Override
    public void onDisable() {

        //Store Time Left
        storeTimeLeft();

        //Reload Files
        Config.reload();
        Config.save();
        Lang.reload();
        Lang.save();
        Data.reload();
    }

    private void manageMillis() {
        if (isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.store_time_on_shutdown") && Data.get().getLong("world.millis") == 0) {
            Data.get().set("world.millis", System.currentTimeMillis());
            Data.save();
            Data.reload();
        }
        if (isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown") && Data.get().getLong("nether.millis") == 0) {
            Data.get().set("nether.millis", System.currentTimeMillis());
            Data.save();
            Data.reload();
        }
        if (isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.store_time_on_shutdown") && Data.get().getLong("end.millis") == 0) {
            Data.get().set("end.millis", System.currentTimeMillis());
            Data.save();
            Data.reload();
        }
    }

    private void storeTimeLeft() {
        if (isEnabled("world.settings.automated_resets.enabled") && isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("world.timer", Config.get().getInt("world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - Data.get().getLong("world.millis")) / 1000D * 20D);
        }
        if (isEnabled("nether_world.settings.automated_resets.enabled") && isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("nether.timer", Config.get().getInt("nether_world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - Data.get().getLong("nether.millis")) / 1000D * 20D);
        }
        if (isEnabled("end_world.settings.automated_resets.enabled") && isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("end.timer", Config.get().getInt("end_world.settings.automated_resets.interval") * 72000 - (System.currentTimeMillis() - Data.get().getLong("end.millis")) / 1000D * 20D);
        }
        Data.save();
    }

    private boolean isEnabled(String input) {
        return Config.get().getBoolean(input);
    }

    private long worldTimer() {
        if (!isEnabled("world.settings.automated_resets.store_time_on_shutdown")) {
            return Config.get().getInt("world.settings.automated_resets.interval") * 72000;
        } else if (Data.get().getLong("world.timer") <= 0) {
            return Config.get().getInt("world.settings.automated_resets.interval") * 72000;
        } else {
            return Data.get().getLong("world.timer");
        }
    }

    private long netherTimer() {
        if (!isEnabled("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            return Config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
        } else if (Data.get().getLong("nether.timer") <= 0) {
            return Config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
        } else {
            return Data.get().getLong("nether.timer");
        }
    }

    private long endTimer() {
        if (!isEnabled("end_world.settings.automated_resets.store_time_on_shutdown")) {
            return Config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
        } else if (Data.get().getLong("end.timer") <= 0) {
            return Config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
        } else {
            return Data.get().getLong("end.timer");
        }
    }

    private void loadFiles() {
        Config.setup();
        Config.addDefaults();
        Config.get().options().copyDefaults(true);
        Config.save();
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();
        Data.setup();
        Data.addDefaults();
        Data.get().options().copyDefaults(true);
        Data.save();
    }

    private void initialize() {
        registerEvent(new MenuHandler(this));
        if (isEnabled("world.settings.block_regeneration.enabled")) {
            registerEvent(new BlockRegen(this));
        }
        if (isEnabled("nether_world.settings.block_regeneration.enabled")) {
            registerEvent(new BlockRegenNether(this));
        }
        if (isEnabled("world.settings.disable_suffocation_damage") || isEnabled("world.settings.disable_drowning_damage") || isEnabled("world.settings.disable_entity_spawning")) {
            registerEvent(new WorldSettings(this));
        }
        if (isEnabled("disabled_commands.enabled")) {
            registerEvent(new DisabledCmds());
        }
        if (isEnabled("nether_world.settings.portals.override") || Config.get().getBoolean("end_world.settings.portals.override")) {
            registerEvent(new Portals());
        }
        if (isEnabled("settings.teleport_to_spawn_on_quit")) {
            registerEvent(new LeaveInWorld());
        }
    }

    public void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void startIntervals() {
        if (isEnabled("world.settings.enabled") && isEnabled("world.settings.automated_resets.enabled")) {
            System.out.println(Messenger.message("automated_resets_enabled"));
            int interval = Config.get().getInt("world.settings.automated_resets.interval") * 72000;
            BukkitTask resetWorld = new ResetWorld(this).runTaskTimer(this, worldTimer(), interval);
        }
        if (isEnabled("nether_world.settings.enabled") && isEnabled("nether_world.settings.automated_resets.enabled")) {
            int interval = Config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetNether = new ResetNetherWorld(this).runTaskTimer(this, netherTimer(), interval);
        }
        if (isEnabled("end_world.settings.enabled") && isEnabled("end_world.settings.automated_resets.enabled")) {
            int interval = Config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetEnd = new ResetEndWorld(this).runTaskTimer(this, endTimer(), interval);
        }
    }

    private void generateWorlds() {
        if (isEnabled("world.settings.enabled")) {
            new WorldGenerator().createWorld();
        }
        if (isEnabled("nether_world.settings.enabled")) {
            new WorldGeneratorNether().createWorld();
        }
        if (isEnabled("end_world.settings.enabled")) {
            new WorldGeneratorEnd().createWorld();
        }
    }

    public void consoleMessage(String message) {
        this.getServer().getConsoleSender().sendMessage(message);
    }
}