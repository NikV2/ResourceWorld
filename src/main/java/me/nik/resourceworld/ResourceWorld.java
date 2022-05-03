package me.nik.resourceworld;

import me.nik.resourceworld.api.ResourceWorldType;
import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.files.commentedfiles.CommentedFileConfiguration;
import me.nik.resourceworld.gui.GuiListener;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.PapiHook;
import me.nik.resourceworld.managers.UpdateChecker;
import me.nik.resourceworld.managers.custom.CustomWorld;
import me.nik.resourceworld.metrics.MetricsLite;
import me.nik.resourceworld.modules.ListenerModule;
import me.nik.resourceworld.modules.impl.LeaveWorld;
import me.nik.resourceworld.modules.impl.disabledcommands.CommandsEnd;
import me.nik.resourceworld.modules.impl.disabledcommands.CommandsNether;
import me.nik.resourceworld.modules.impl.disabledcommands.CommandsWorld;
import me.nik.resourceworld.modules.impl.portals.PortalEnd;
import me.nik.resourceworld.modules.impl.portals.PortalNether;
import me.nik.resourceworld.modules.impl.suffocation.Suffocation;
import me.nik.resourceworld.modules.impl.suffocation.SuffocationEnd;
import me.nik.resourceworld.modules.impl.suffocation.SuffocationNether;
import me.nik.resourceworld.tasks.AlwaysDay;
import me.nik.resourceworld.tasks.ResetEndWorld;
import me.nik.resourceworld.tasks.ResetNetherWorld;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.utils.ChatUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResourceWorld extends JavaPlugin {

    private static ResourceWorld plugin;
    private static Economy econ = null;

    private final Config config = new Config(this);
    private final Data data = new Data();
    private final Lang lang = new Lang();

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

    private final Map<ResourceWorldType, CustomWorld> resourceWorlds = new HashMap<>();

    private final List<ListenerModule> listenerModules = new ArrayList<>();

    @Override
    public void onDisable() {

        //DisInitialize modules
        this.listenerModules.forEach(ListenerModule::shutdown);

        //Store Time Left
        if (Config.Setting.WORLD_RESETS_ENABLED.getBoolean() && Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_STORE_TIME.getBoolean()) {
            this.data.get().set("world.timer", Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 72000L - (System.currentTimeMillis() - this.data.get().getLong("world.millis")) / 1000D * 20D);
        }
        if (Config.Setting.NETHER_RESETS_ENABLED.getBoolean() && Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_STORE_TIME.getBoolean()) {
            this.data.get().set("nether.timer", Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 72000L - (System.currentTimeMillis() - this.data.get().getLong("nether.millis")) / 1000D * 20D);
        }
        if (Config.Setting.END_RESETS_ENABLED.getBoolean() && Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_STORE_TIME.getBoolean()) {
            this.data.get().set("end.timer", Config.Setting.END_RESETS_INTERVAL.getInt() * 72000L - (System.currentTimeMillis() - this.data.get().getLong("end.millis")) / 1000D * 20D);
        }

        this.data.save();

        //Reload Files
        this.config.reset();
        this.lang.reload();
        this.lang.save();

        HandlerList.unregisterAll(this);
        this.getServer().getScheduler().cancelTasks(this);
        plugin = null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public Map<ResourceWorldType, CustomWorld> getResourceWorlds() {
        return resourceWorlds;
    }

    @Override
    public void onEnable() {
        plugin = this;

        //Startup Message
        this.getServer().getConsoleSender().sendMessage(STARTUP_MESSAGE);

        //Load Files
        this.config.setup();

        this.lang.setup(this);
        this.lang.addDefaults();
        this.lang.get().options().copyDefaults(true);
        this.lang.save();

        this.data.setup(this);
        this.data.addDefaults();
        this.data.get().options().copyDefaults(true);
        this.data.save();

        //Load Vault if found
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) return;
            econ = rsp.getProvider();
        }

        getCommand("resource").setExecutor(new CommandManager(this));

        PluginManager pm = this.getServer().getPluginManager();

        //Load listener modules
        this.listenerModules.addAll(Arrays.asList(
                new LeaveWorld(this),
                new Suffocation(this),
                new SuffocationEnd(this),
                new SuffocationNether(this),
                new PortalEnd(this),
                new PortalNether(this),
                new CommandsEnd(this),
                new CommandsNether(this),
                new CommandsWorld(this)
        ));

        //Initialize them
        this.listenerModules.forEach(ListenerModule::load);

        //Don't be an idiot Nik, Always register this Listener
        pm.registerEvents(new GuiListener(), this);

        //Handle resource worlds
        this.resourceWorlds.clear();

        if (Config.Setting.WORLD_ENABLED.getBoolean()) {
            this.resourceWorlds.put(
                    ResourceWorldType.RESOURCE_WORLD,
                    new CustomWorld(Config.Setting.WORLD_NAME.getString(),
                            Difficulty.valueOf(Config.Setting.WORLD_DIFFICULTY.getString()),
                            WorldType.valueOf(Config.Setting.WORLD_TYPE.getString()),
                            World.Environment.valueOf(Config.Setting.WORLD_ENVIRONMENT.getString()),
                            Config.Setting.WORLD_GENERATE_STRUCTURES.getBoolean(),
                            Config.Setting.WORLD_SEED_ENABLED.getBoolean(),
                            Config.Setting.WORLD_SEED.getLong(),
                            Config.Setting.WORLD_BORDER_ENABLED.getBoolean(),
                            Config.Setting.WORLD_BORDER_SIZE.getInt(),
                            Config.Setting.WORLD_PVP.getBoolean(),
                            Config.Setting.WORLD_KEEP_INVENTORY.getBoolean(),
                            ResourceWorldType.RESOURCE_WORLD).generate());
        }

        if (Config.Setting.NETHER_ENABLED.getBoolean()) {
            this.resourceWorlds.put(
                    ResourceWorldType.RESOURCE_NETHER,
                    new CustomWorld(Config.Setting.NETHER_NAME.getString(),
                            Difficulty.valueOf(Config.Setting.NETHER_DIFFICULTY.getString()),
                            WorldType.valueOf(Config.Setting.NETHER_TYPE.getString()),
                            World.Environment.valueOf(Config.Setting.NETHER_ENVIRONMENT.getString()),
                            Config.Setting.NETHER_GENERATE_STRUCTURES.getBoolean(),
                            Config.Setting.NETHER_SEED_ENABLED.getBoolean(),
                            Config.Setting.NETHER_SEED.getLong(),
                            Config.Setting.NETHER_BORDER_ENABLED.getBoolean(),
                            Config.Setting.NETHER_BORDER_SIZE.getInt(),
                            Config.Setting.NETHER_PVP.getBoolean(),
                            Config.Setting.NETHER_KEEP_INVENTORY.getBoolean(),
                            ResourceWorldType.RESOURCE_NETHER).generate());
        }

        if (Config.Setting.END_ENABLED.getBoolean()) {
            this.resourceWorlds.put(
                    ResourceWorldType.RESOURCE_END,
                    new CustomWorld(Config.Setting.END_NAME.getString(),
                            Difficulty.valueOf(Config.Setting.END_DIFFICULTY.getString()),
                            WorldType.valueOf(Config.Setting.END_TYPE.getString()),
                            World.Environment.valueOf(Config.Setting.END_ENVIRONMENT.getString()),
                            Config.Setting.END_GENERATE_STRUCTURES.getBoolean(),
                            Config.Setting.END_SEED_ENABLED.getBoolean(),
                            Config.Setting.END_SEED.getLong(),
                            Config.Setting.END_BORDER_ENABLED.getBoolean(),
                            Config.Setting.END_BORDER_SIZE.getInt(),
                            Config.Setting.END_PVP.getBoolean(),
                            Config.Setting.END_KEEP_INVENTORY.getBoolean(),
                            ResourceWorldType.RESOURCE_END).generate());
        }

        //Handle timestamps

        if (Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_STORE_TIME.getBoolean() && this.data.get().getLong("world.millis") <= 0) {
            this.data.get().set("world.millis", System.currentTimeMillis());
            this.data.save();
            this.data.reload();
        }
        if (Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_STORE_TIME.getBoolean() && this.data.get().getLong("nether.millis") <= 0) {
            this.data.get().set("nether.millis", System.currentTimeMillis());
            this.data.save();
            this.data.reload();
        }
        if (Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_STORE_TIME.getBoolean() && this.data.get().getLong("end.millis") <= 0) {
            this.data.get().set("end.millis", System.currentTimeMillis());
            this.data.save();
            this.data.reload();
        }

        this.data.get().set("world.papi", System.currentTimeMillis());
        this.data.get().set("nether.papi", System.currentTimeMillis());
        this.data.get().set("end.papi", System.currentTimeMillis());

        //Initialize tasks

        if (Config.Setting.WORLD_ENABLED.getBoolean() && Config.Setting.WORLD_RESETS_ENABLED.getBoolean()) {

            long timer;

            if (!Config.Setting.WORLD_STORE_TIME.getBoolean()) {
                timer = Config.Setting.WORLD_RESETS_INTERVAL.getLong() * 72000L;
            } else if (this.data.get().getLong("world.timer") <= 0) {
                timer = Config.Setting.WORLD_RESETS_INTERVAL.getLong() * 72000L;
            } else {
                timer = this.data.get().getLong("world.timer");
            }

            new ResetWorld(this).runTaskTimer(this,
                    timer,
                    Config.Setting.WORLD_RESETS_INTERVAL.getLong() * 72000L);
        }

        if (Config.Setting.NETHER_ENABLED.getBoolean() && Config.Setting.NETHER_RESETS_ENABLED.getBoolean()) {

            long timer;

            if (!Config.Setting.NETHER_STORE_TIME.getBoolean()) {
                timer = Config.Setting.NETHER_RESETS_INTERVAL.getLong() * 72000L;
            } else if (this.data.get().getLong("nether.timer") <= 0) {
                timer = Config.Setting.NETHER_RESETS_INTERVAL.getLong() * 72000L;
            } else {
                timer = this.data.get().getLong("nether.timer");
            }

            new ResetNetherWorld(this).runTaskTimer(this,
                    timer,
                    Config.Setting.NETHER_RESETS_INTERVAL.getLong() * 72000L);
        }

        if (Config.Setting.END_ENABLED.getBoolean() && Config.Setting.END_RESETS_ENABLED.getBoolean()) {

            long timer;

            if (!Config.Setting.END_STORE_TIME.getBoolean()) {
                timer = Config.Setting.END_RESETS_INTERVAL.getLong() * 72000L;
            } else if (this.data.get().getLong("end.timer") <= 0) {
                timer = Config.Setting.END_RESETS_INTERVAL.getLong() * 72000L;
            } else {
                timer = this.data.get().getLong("end.timer");
            }

            new ResetEndWorld(this).runTaskTimer(this,
                    timer,
                    Config.Setting.END_RESETS_INTERVAL.getLong() * 72000L);
        }

        if (Config.Setting.WORLD_ALWAYS_DAY.getBoolean()) new AlwaysDay().runTaskTimer(this, 1200L, 1200L);

        //Check for updates
        if (Config.Setting.SETTINGS_CHECK_FOR_UPDATES.getBoolean()) {
            new UpdateChecker(this).runTaskAsynchronously(this);
        } else ChatUtils.consoleMessage(MsgType.UPDATE_DISABLED.getMessage());

        //PlaceholderAPI
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiHook(this).register();
        }

        //BStats
        new MetricsLite(this, 6981);
    }

    public CustomWorld getResourceWorld(ResourceWorldType type) {
        return this.resourceWorlds.get(type);
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
}