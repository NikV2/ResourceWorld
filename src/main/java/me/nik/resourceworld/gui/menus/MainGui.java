package me.nik.resourceworld.gui.menus;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.gui.Menu;
import me.nik.resourceworld.gui.PlayerMenu;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.custom.CustomWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainGui extends Menu {
    public MainGui(PlayerMenu playerMenu, ResourceWorld plugin) {
        super(playerMenu, plugin);
    }

    @Override
    protected String getMenuName() {
        return MsgType.GUI_NAME.getMessage();
    }

    @Override
    protected int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getSlot()) {
            case 13:

                List<String> worlds = this.plugin.getResourceWorlds().values().stream().map(CustomWorld::getName).collect(Collectors.toList());

                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> worlds.stream().anyMatch(world -> player.getWorld().getName().equals(world)))
                        .forEach(player -> {
                            player.teleport(Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation());
                            player.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
                        });

                p.sendMessage(MsgType.TELEPORTED_PLAYERS.getMessage());
                break;
            case 15:
                p.closeInventory();
                plugin.onDisable();
                plugin.onEnable();
                p.sendMessage(MsgType.RELOADED.getMessage());
                break;
            case 30:
                p.closeInventory();
                p.sendMessage(ChatColor.BLUE + ">> " + ChatColor.WHITE + "https://github.com/NikV2/ResourceWorld/issues/new/choose" + ChatColor.BLUE + " <<");
                break;
            case 49:
                p.closeInventory();
                break;
            case 32:
                p.closeInventory();
                new WorldsGui(playerMenu, plugin).open();
                break;
        }
    }

    @Override
    protected void setMenuItems() {
        ArrayList<String> teleportLore = new ArrayList<>();
        teleportLore.add("");
        teleportLore.add("&7Teleport all the");
        teleportLore.add("&7Players that are in");
        teleportLore.add("&7The Resource Worlds");
        teleportLore.add("&7Back to spawn.");
        ItemStack teleport = makeItem(Material.ENDER_PEARL, 1, "&aTeleport All", teleportLore);

        ArrayList<String> reloadLore = new ArrayList<>();
        reloadLore.add(" ");
        reloadLore.add("&7WARNING!");
        reloadLore.add(" ");
        reloadLore.add("&7Reloading may cause issues.");
        ItemStack reload = makeItem(Material.REDSTONE, 1, "&aReload", reloadLore);

        ItemStack support = makeItem(Material.DIAMOND, 1, "&aLooking for Support?", null);
        ItemStack exit = makeItem(Material.BARRIER, 1, "&cExit", null);

        ArrayList<String> resetLore = new ArrayList<>();
        resetLore.add("");
        resetLore.add("&7Would you like to reset");
        resetLore.add("&7The Resource World?");
        ItemStack reset = makeItem(Material.EMERALD, 1, "&aReset", resetLore);

        inventory.setItem(13, teleport);
        inventory.setItem(15, reload);
        inventory.setItem(30, support);
        inventory.setItem(32, reset);
        inventory.setItem(49, exit);
    }
}