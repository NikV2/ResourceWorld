package me.nik.resourceworld;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.Drowning;
import me.nik.resourceworld.listeners.GuiListener;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.disabledcommands.CommandsEnd;
import me.nik.resourceworld.listeners.disabledcommands.CommandsNether;
import me.nik.resourceworld.listeners.disabledcommands.CommandsWorld;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawning;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawningEnd;
import me.nik.resourceworld.listeners.entityspawning.EntitySpawningNether;
import me.nik.resourceworld.listeners.explosion.Explosion;
import me.nik.resourceworld.listeners.explosion.ExplosionEnd;
import me.nik.resourceworld.listeners.explosion.ExplosionNether;
import me.nik.resourceworld.listeners.portals.PortalEnd;
import me.nik.resourceworld.listeners.portals.PortalNether;
import me.nik.resourceworld.listeners.suffocation.Suffocation;
import me.nik.resourceworld.listeners.suffocation.SuffocationEnd;
import me.nik.resourceworld.listeners.suffocation.SuffocationNether;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.PapiHook;
import me.nik.resourceworld.managers.UpdateChecker;
import me.nik.resourceworld.managers.commentedfiles.CommentedFileConfiguration;
import me.nik.resourceworld.metrics.MetricsLite;
import me.nik.resourceworld.tasks.AlwaysDay;
import me.nik.resourceworld.tasks.ResetEndWorld;
import me.nik.resourceworld.tasks.ResetNetherWorld;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ResourceWorld extends JavaPlugin {

    private static ResourceWorld plugin;
    private static Economy econ = null;

    private Config config;
    private Data data;
    private Lang lang;

    public static ResourceWorld getInstance() {
        return plugin;
    }

    private final String[] STARTUP_MESSAGE = {
            " ",
            ChatColor.GREEN + "Resource World v" + this.getDescription().getVersion(),
            " ",
            ChatColor.WHITE + "     Author: Nik",
            " "
    };

    @Override
    public void onDisable() {

        //Store Time Left
        storeTimeLeft();

        //Reload Files
        config.reset();
        lang.reload();
        lang.save();
        data.reload();
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.config = new Config(this);
        this.data = new Data();
        this.lang = new Lang();

        //Load Files
        loadFiles();

        //Load Vault if found
        setupEconomy();

        //Startup Message
        this.getServer().getConsoleSender().sendMessage(STARTUP_MESSAGE);

        getCommand("resource").setExecutor(new CommandManager(this));

        manageMillis();

        initializeListeners();

        generateWorlds();

        startIntervals();

        initializeTasks();

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiHook(this).register();
        }

        new MetricsLite(this, 6981);

        PaperLib.suggestPaper(this);
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return;
        econ = rsp.getProvider();
    }

    public CommentedFileConfiguration getConfiguration() {
        return config.getConfig();
    }

    public FileConfiguration getLang() {
        return lang.get();
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

    private void initializeTasks() {
        if (Config.Setting.SETTINGS_CHECK_FOR_UPDATES.getBoolean()) {
            BukkitTask updateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            Messenger.consoleMessage(MsgType.UPDATE_DISABLED.getMessage());
        }

        if (Config.Setting.WORLD_ALWAYS_DAY.getBoolean()) {
            BukkitTask alwaysDay = new AlwaysDay().runTaskTimer(this, 1200, 1200);
        }
    }

    private void manageMillis() {
        if (Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_STORE_TIME.getBoolean() && data.get().getLong("world.millis") == 0) {
            data.get().set("world.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
        if (Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_STORE_TIME.getBoolean() && data.get().getLong("nether.millis") == 0) {
            data.get().set("nether.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
        if (Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_STORE_TIME.getBoolean() && data.get().getLong("end.millis") == 0) {
            data.get().set("end.millis", System.currentTimeMillis());
            data.save();
            data.reload();
        }
        data.get().set("world.papi", System.currentTimeMillis());
        data.get().set("nether.papi", System.currentTimeMillis());
        data.get().set("end.papi", System.currentTimeMillis());
    }

    private void storeTimeLeft() {
        if (Config.Setting.WORLD_RESETS_ENABLED.getBoolean() && Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_STORE_TIME.getBoolean()) {
            data.get().set("world.timer", Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 72000 - (System.currentTimeMillis() - data.get().getLong("world.millis")) / 1000D * 20D);
        }
        if (Config.Setting.NETHER_RESETS_ENABLED.getBoolean() && Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_STORE_TIME.getBoolean()) {
            data.get().set("nether.timer", Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 72000 - (System.currentTimeMillis() - data.get().getLong("nether.millis")) / 1000D * 20D);
        }
        if (Config.Setting.END_RESETS_ENABLED.getBoolean() && Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_STORE_TIME.getBoolean()) {
            data.get().set("end.timer", Config.Setting.END_RESETS_INTERVAL.getInt() * 72000 - (System.currentTimeMillis() - data.get().getLong("end.millis")) / 1000D * 20D);
        }
        data.save();
    }

    private long worldTimer() {
        if (!Config.Setting.WORLD_STORE_TIME.getBoolean()) {
            return Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 72000;
        } else if (data.get().getLong("world.timer") <= 0) {
            return Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 72000;
        } else {
            return data.get().getLong("world.timer");
        }
    }

    private long netherTimer() {
        if (!Config.Setting.NETHER_STORE_TIME.getBoolean()) {
            return Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 72000;
        } else if (data.get().getLong("nether.timer") <= 0) {
            return Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 72000;
        } else {
            return data.get().getLong("nether.timer");
        }
    }

    private long endTimer() {
        if (!Config.Setting.END_STORE_TIME.getBoolean()) {
            return Config.Setting.END_RESETS_INTERVAL.getInt() * 72000;
        } else if (data.get().getLong("end.timer") <= 0) {
            return Config.Setting.END_RESETS_INTERVAL.getInt() * 72000;
        } else {
            return data.get().getLong("end.timer");
        }
    }

    private void loadFiles() {
        config.setup();

        lang.setup(this);
        lang.addDefaults();
        lang.get().options().copyDefaults(true);
        lang.save();

        data.setup(this);
        data.addDefaults();
        data.get().options().copyDefaults(true);
        data.save();
    }

    private void initializeListeners() {
        PluginManager pm = this.getServer().getPluginManager();

        if (Config.Setting.WORLD_DISABLE_SUFFOCATION.getBoolean()) {
            pm.registerEvents(new Suffocation(), this);
        }
        if (Config.Setting.NETHER_DISABLE_SUFFOCATION.getBoolean()) {
            pm.registerEvents(new SuffocationNether(), this);
        }
        if (Config.Setting.END_DISABLE_SUFFOCATION.getBoolean()) {
            pm.registerEvents(new SuffocationEnd(), this);
        }
        if (Config.Setting.WORLD_DISABLE_DROWNING.getBoolean()) {
            pm.registerEvents(new Drowning(), this);
        }
        if (Config.Setting.WORLD_DISABLE_ENTITY_SPAWNING.getBoolean()) {
            pm.registerEvents(new EntitySpawning(), this);
        }
        if (Config.Setting.WORLD_DISABLED_COMMANDS_ENABLED.getBoolean()) {
            pm.registerEvents(new CommandsWorld(), this);
        }
        if (Config.Setting.NETHER_DISABLE_ENTITY_SPAWNING.getBoolean()) {
            pm.registerEvents(new EntitySpawningNether(), this);
        }
        if (Config.Setting.END_DISABLE_ENTITY_SPAWNING.getBoolean()) {
            pm.registerEvents(new EntitySpawningEnd(), this);
        }
        if (Config.Setting.NETHER_DISABLED_COMMANDS_ENABLED.getBoolean()) {
            pm.registerEvents(new CommandsNether(), this);
        }
        if (Config.Setting.SETTINGS_TELEPORT_TO_SPAWN.getBoolean()) {
            pm.registerEvents(new LeaveInWorld(), this);
        }
        if (Config.Setting.WORLD_DISABLE_EXPLOSIONS.getBoolean()) {
            pm.registerEvents(new Explosion(), this);
        }
        if (Config.Setting.END_DISABLED_COMMANDS_ENABLED.getBoolean()) {
            pm.registerEvents(new CommandsEnd(), this);
        }
        if (Config.Setting.NETHER_DISABLE_EXPLOSIONS.getBoolean()) {
            pm.registerEvents(new ExplosionNether(), this);
        }
        if (Config.Setting.END_DISABLE_EXPLOSIONS.getBoolean()) {
            pm.registerEvents(new ExplosionEnd(), this);
        }
        if (Config.Setting.NETHER_PORTALS_ENABLED.getBoolean()) {
            pm.registerEvents(new PortalNether(), this);
        }
        if (Config.Setting.END_PORTALS_ENABLED.getBoolean()) {
            pm.registerEvents(new PortalEnd(), this);
        }
        //Don't be an idiot Nik, Always register this Listener
        pm.registerEvents(new GuiListener(), this);
    }

    private void startIntervals() {
        if (Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_RESETS_ENABLED.getBoolean()) {
            int interval = Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 72000;
            BukkitTask resetWorld = new ResetWorld(this).runTaskTimer(this, worldTimer(), interval);
        }
        if (Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_RESETS_ENABLED.getBoolean()) {
            int interval = Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 72000;
            BukkitTask resetNether = new ResetNetherWorld(this).runTaskTimer(this, netherTimer(), interval);
        }
        if (Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_RESETS_ENABLED.getBoolean()) {
            int interval = Config.Setting.END_RESETS_INTERVAL.getInt() * 72000;
            BukkitTask resetEnd = new ResetEndWorld(this).runTaskTimer(this, endTimer(), interval);
        }
    }

    private void generateWorlds() {
        if (Config.Setting.WORLD_ENABLED.getBoolean()) {
            new WorldGenerator().createWorld();
        }
        if (Config.Setting.NETHER_ENABLED.getBoolean()) {
            new WorldGeneratorNether().createWorld();
        }
        if (Config.Setting.END_ENABLED.getBoolean()) {
            new WorldGeneratorEnd().createWorld();
        }
    }
}