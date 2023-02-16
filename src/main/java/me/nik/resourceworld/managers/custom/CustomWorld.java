package me.nik.resourceworld.managers.custom;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.ResourceWorldType;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.ChatUtils;
import me.nik.resourceworld.utils.MiscUtils;
import me.nik.resourceworld.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CustomWorld implements Listener {

    private final ResourceWorldType resourceWorldType;
    private final String name;
    private final Difficulty difficulty;
    private final WorldType type;
    private final World.Environment environment;
    private final boolean structures;
    private final boolean useSeed;
    private final long seed;
    private final boolean useBorder;
    private final int borderSize;
    private final boolean pvp;
    private final boolean keepInventory;

    public CustomWorld(String name, Difficulty difficulty, WorldType type, World.Environment environment, boolean structures, boolean useSeed, long seed, boolean useBorder, int borderSize, boolean pvp, boolean keepInventory, ResourceWorldType resourceWorldType) {
        this.name = name;
        this.difficulty = difficulty;
        this.type = type;
        this.environment = environment;
        this.structures = structures;
        this.useSeed = useSeed;
        this.seed = seed;
        this.useBorder = useBorder;
        this.borderSize = borderSize;
        this.pvp = pvp;
        this.keepInventory = keepInventory;
        this.resourceWorldType = resourceWorldType;
    }

    public String getName() {
        return name;
    }

    public CustomWorld generate() {

        WorldCreator wc = new WorldCreator(this.name);

        wc.type(this.type);

        wc.generateStructures(this.structures);

        wc.environment(this.environment);

        if (this.useSeed) wc.seed(this.seed);

        wc.createWorld();

        World rw = Bukkit.getWorld(this.name);

        if (this.useBorder) {

            WorldBorder wb = rw.getWorldBorder();

            wb.setCenter(0, 0);

            wb.setSize(this.borderSize);

        }

        rw.setPVP(this.pvp);

        rw.setDifficulty(this.difficulty);

        Bukkit.getWorlds().add(rw);

        if (!MiscUtils.isLegacy() && this.keepInventory) rw.setGameRule(GameRule.KEEP_INVENTORY, true);

        return this;
    }

    public void reset() {

        ChatUtils.consoleMessage("Executing world reset...");

        //Phase 1

        World world = Bukkit.getWorld(this.name);

        if (world == null) return;

        Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().getUID().equals(world.getUID())).forEach(player -> {

            player.teleport(Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation());

            player.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
        });

        ResourceWorld plugin = ResourceWorld.getInstance();

        Bukkit.getPluginManager().registerEvents(this, plugin);

        ChatUtils.consoleMessage("Phase 1 completed...");

        //Phase 2
        TaskUtils.taskLater(() -> {

            switch (this.resourceWorldType) {

                case RESOURCE_END:

                    Bukkit.broadcastMessage(MsgType.RESETTING_THE_END.getMessage());

                    if (Config.Setting.END_STORE_TIME.getBoolean()) {
                        plugin.getData().set("end.millis", System.currentTimeMillis());
                    }

                    plugin.getData().set("end.papi", System.currentTimeMillis());

                    break;

                case RESOURCE_WORLD:

                    Bukkit.broadcastMessage(MsgType.RESETTING_THE_WORLD.getMessage());

                    if (Config.Setting.WORLD_STORE_TIME.getBoolean()) {
                        plugin.getData().set("world.millis", System.currentTimeMillis());
                    }

                    plugin.getData().set("world.papi", System.currentTimeMillis());

                    break;

                case RESOURCE_NETHER:

                    Bukkit.broadcastMessage(MsgType.RESETTING_THE_NETHER.getMessage());

                    if (Config.Setting.NETHER_STORE_TIME.getBoolean()) {
                        plugin.getData().set("nether.millis", System.currentTimeMillis());
                    }

                    plugin.getData().set("nether.papi", System.currentTimeMillis());

                    break;
            }

            plugin.saveData();
            plugin.reloadData();
            plugin.saveData();

            Bukkit.unloadWorld(world, false);

            MiscUtils.deleteDirectory(world.getWorldFolder());

            ChatUtils.consoleMessage("Phase 2 completed...");

            //Phase 3
            TaskUtils.taskLater(() -> {

                generate();

                ChatUtils.consoleMessage("Phase 3 completed...");

                //Phase 4
                TaskUtils.taskLater(() -> {

                    switch (this.resourceWorldType) {

                        case RESOURCE_END:

                            if (Config.Setting.END_COMMANDS_ENABLED.getBoolean()) {

                                for (String cmd : Config.Setting.END_COMMANDS_COMMANDS.getStringList()) {

                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                }
                            }

                            Bukkit.broadcastMessage(MsgType.END_HAS_BEEN_RESET.getMessage());

                            break;

                        case RESOURCE_WORLD:
                            if (Config.Setting.WORLD_COMMANDS_ENABLED.getBoolean()) {
                                for (String cmd : Config.Setting.WORLD_COMMANDS_COMMANDS.getStringList()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                }
                            }

                            Bukkit.broadcastMessage(MsgType.WORLD_HAS_BEEN_RESET.getMessage());

                            break;
                        case RESOURCE_NETHER:

                            if (Config.Setting.NETHER_COMMANDS_ENABLED.getBoolean()) {

                                for (String cmd : Config.Setting.NETHER_COMMANDS_COMMANDS.getStringList()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                }
                            }

                            Bukkit.broadcastMessage(MsgType.NETHER_HAS_BEEN_RESET.getMessage());

                            break;
                    }

                    HandlerList.unregisterAll(this);

                    ChatUtils.consoleMessage("All phases completed!");

                }, 160L);

            }, 80L);

        }, 80L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldTeleport(PlayerTeleportEvent e) {
        if (e.getTo().getWorld().getName().equals(this.name)) e.setCancelled(true);
    }
}