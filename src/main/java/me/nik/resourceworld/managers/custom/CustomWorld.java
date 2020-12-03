package me.nik.resourceworld.managers.custom;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.discord.Discord;
import me.nik.resourceworld.utils.MiscUtils;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CustomWorld {

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

    public void generate() {
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
    }

    public boolean reset() {
        if (Bukkit.getWorld(this.name) == null) return false;

        ResourceWorld plugin = ResourceWorld.getInstance();

        switch (this.resourceWorldType) {
            case RESOURCE_END:
                Bukkit.broadcastMessage(MsgType.RESETTING_THE_END.getMessage());

                if (Config.Setting.END_STORE_TIME.getBoolean()) {
                    plugin.getData().set("end.millis", System.currentTimeMillis());
                }
                break;
            case RESOURCE_WORLD:
                Bukkit.broadcastMessage(MsgType.RESETTING_THE_WORLD.getMessage());

                if (Config.Setting.WORLD_STORE_TIME.getBoolean()) {
                    plugin.getData().set("world.millis", System.currentTimeMillis());
                }
                break;
            case RESOURCE_NETHER:
                Bukkit.broadcastMessage(MsgType.RESETTING_THE_NETHER.getMessage());

                if (Config.Setting.NETHER_STORE_TIME.getBoolean()) {
                    plugin.getData().set("nether.millis", System.currentTimeMillis());
                }
                break;
        }

        World world = Bukkit.getWorld(this.name);

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() > 0) {
            for (Player p : players) {
                if (!p.getWorld().equals(world)) continue;
                p.teleport(Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation());
                p.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
            }
        }

        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);

        WorldUtils.deleteDirectory(world.getWorldFolder());

        generate();

        switch (this.resourceWorldType) {
            case RESOURCE_END:
                if (Config.Setting.END_COMMANDS_ENABLED.getBoolean()) {
                    for (String cmd : Config.Setting.END_COMMANDS_COMMANDS.getStringList()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                }

                Bukkit.broadcastMessage(MsgType.END_HAS_BEEN_RESET.getMessage());

                if (Config.Setting.SETTINGS_DISCORD_END.getBoolean()) {
                    Discord discord = new Discord("Resource World", "The Resource End has been Reset!", Color.BLUE);
                    discord.sendNotification();
                }

                plugin.getData().set("end.papi", System.currentTimeMillis());
                break;
            case RESOURCE_WORLD:
                if (Config.Setting.WORLD_COMMANDS_ENABLED.getBoolean()) {
                    for (String cmd : Config.Setting.WORLD_COMMANDS_COMMANDS.getStringList()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                }

                Bukkit.broadcastMessage(MsgType.WORLD_HAS_BEEN_RESET.getMessage());

                if (Config.Setting.SETTINGS_DISCORD_WORLD.getBoolean()) {
                    Discord discord = new Discord("Resource World", "The Resource World has been Reset!", Color.GREEN);
                    discord.sendNotification();
                }

                plugin.getData().set("world.papi", System.currentTimeMillis());
                break;
            case RESOURCE_NETHER:
                if (Config.Setting.NETHER_COMMANDS_ENABLED.getBoolean()) {
                    for (String cmd : Config.Setting.NETHER_COMMANDS_COMMANDS.getStringList()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                }

                Bukkit.broadcastMessage(MsgType.NETHER_HAS_BEEN_RESET.getMessage());

                if (Config.Setting.SETTINGS_DISCORD_NETHER.getBoolean()) {
                    Discord discord = new Discord("Resource World", "The Resource Nether has been Reset!", Color.RED);
                    discord.sendNotification();
                }

                plugin.getData().set("nether.papi", System.currentTimeMillis());
                break;
        }

        plugin.saveData();
        plugin.reloadData();

        return true;
    }
}