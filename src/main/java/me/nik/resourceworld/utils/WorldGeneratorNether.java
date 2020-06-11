package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGeneratorNether {

    World world;

    private final String worldName;
    private final WorldType type;
    private final World.Environment environment;
    private final boolean useBorder;
    private final int border;
    private final boolean pvp;
    private final Difficulty difficulty;
    private final int monsterLimit;
    private final boolean loadSpawn;
    private final boolean keepInventory;

    public WorldGeneratorNether(ResourceWorld plugin) {
        this.worldName = plugin.getConfig().getString("nether_world.settings.world_name");
        this.type = WorldType.valueOf(plugin.getConfig().getString("nether_world.settings.world_type"));
        this.environment = World.Environment.valueOf(plugin.getConfig().getString("nether_world.settings.environment"));
        this.useBorder = plugin.getConfig().getBoolean("nether_world.settings.world_border.enabled");
        this.border = plugin.getConfig().getInt("nether_world.settings.world_border.size");
        this.pvp = plugin.getConfig().getBoolean("nether_world.settings.allow_pvp");
        this.difficulty = Difficulty.valueOf(plugin.getConfig().getString("nether_world.settings.difficulty"));
        this.monsterLimit = plugin.getConfig().getInt("nether_world.settings.entities.max_monsters");
        this.loadSpawn = plugin.getConfig().getBoolean("nether_world.settings.keep_spawn_loaded");
        this.keepInventory = plugin.getConfig().getBoolean("world.settings.keep_inventory_on_death");
    }

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(type);
            wc.environment(environment);
            world = wc.createWorld();
            final World resourceNether = Bukkit.getWorld(worldName);
            if (useBorder) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(border);
            }
            resourceNether.setPVP(pvp);
            resourceNether.setDifficulty(difficulty);
            resourceNether.setMonsterSpawnLimit(monsterLimit);
            resourceNether.setKeepSpawnInMemory(loadSpawn);
            Bukkit.getWorlds().add(resourceNether);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (keepInventory) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception e) {
            Messenger.consoleMessage("There was an error attempting to Generate a new World, Please contact the Author with the Following Error");
            e.printStackTrace();
        }
    }
}
