package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class GUIManager {

    private final ResourceWorld plugin;

    public GUIManager(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    public void openMainGUI(Player p) {

        Inventory mainGUI = Bukkit.createInventory(new ResourceWorldHolder(), 54, Messenger.format(Lang.get().getString("gui_name")));

        //Items Here
        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemStack reload = new ItemStack(Material.REDSTONE);
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemStack support = new ItemStack(Material.DIAMOND);
        ItemStack reset = new ItemStack(Material.EMERALD);
        ItemStack settings = new ItemStack(Material.BOOK);

        //Item Data
        ItemMeta teleport_meta = teleport.getItemMeta();
        teleport_meta.setDisplayName("§aTeleport All");
        ArrayList<String> teleport_lore = new ArrayList<>();
        teleport_lore.add(" ");
        teleport_lore.add(ChatColor.GRAY + "Teleport all the");
        teleport_lore.add(ChatColor.GRAY + "Players that are in");
        teleport_lore.add(ChatColor.GRAY + "The Resource World");
        teleport_lore.add(ChatColor.GRAY + "Back to spawn.");
        teleport_meta.setLore(teleport_lore);
        teleport.setItemMeta(teleport_meta);

        ItemMeta reload_meta = reload.getItemMeta();
        reload_meta.setDisplayName("§aReload");
        ArrayList<String> reload_lore = new ArrayList<>();
        reload_lore.add(" ");
        reload_lore.add(ChatColor.GRAY + "WARNING!");
        reload_lore.add(" ");
        reload_lore.add(ChatColor.GRAY + "This may cause Lag.");
        reload_meta.setLore(reload_lore);
        reload.setItemMeta(reload_meta);

        ItemMeta support_meta = support.getItemMeta();
        support_meta.setDisplayName("§aLooking for Support?");
        support.setItemMeta(support_meta);

        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName("§cExit");
        close.setItemMeta(close_meta);

        ItemMeta reset_meta = reset.getItemMeta();
        reset_meta.setDisplayName("§aReset");
        ArrayList<String> reset_lore = new ArrayList<>();
        reset_lore.add("");
        reset_lore.add(ChatColor.GRAY + "Would you like to reset");
        reset_lore.add(ChatColor.GRAY + "The Resource World?");
        reset_meta.setLore(reset_lore);
        reset.setItemMeta(reset_meta);

        ItemMeta settings_meta = settings.getItemMeta();
        settings_meta.setDisplayName("§aSettings");
        settings.setItemMeta(settings_meta);

        //Add the Items
        mainGUI.setItem(49, close);
        mainGUI.setItem(32, reset);
        mainGUI.setItem(30, support);
        mainGUI.setItem(15, reload);
        mainGUI.setItem(13, teleport);
        p.openInventory(mainGUI);
        final Player pAnonymous = p;
        new BukkitRunnable() {
            @Override
            public void run() {
                InventoryView guiView = pAnonymous.getOpenInventory();
                if (!(guiView.getTopInventory().getHolder() instanceof ResourceWorldHolder)) {
                    cancel();
                    return;
                }
                //GUI Items
                ItemStack server = new ItemStack(Material.BEACON);
                //GUI Item Metadata + Lore
                ItemMeta server_meta = server.getItemMeta();
                server_meta.setDisplayName("§bServer Status");
                ArrayList<String> server_lore = new ArrayList<>();
                server_lore.add(ChatColor.GREEN + "> System Information:");
                server_lore.add(ChatColor.GRAY + "Server Version: " + Bukkit.getServer().getVersion());
                server_lore.add(ChatColor.GRAY + "CPU Proccessors: " + Runtime.getRuntime().availableProcessors());
                server_lore.add(ChatColor.GRAY + "Memory: " + Runtime.getRuntime().maxMemory() / 1024L / 1024L + "/" + Runtime.getRuntime().freeMemory() / 1024L / 1024L);
                server_meta.setLore(server_lore);
                server.setItemMeta(server_meta);
                mainGUI.setItem(11, server);
            }
        }.runTaskTimer(plugin, 1, 10);
    }

    public void openWorldsGUI(Player p) {
        Inventory worldsGUI = Bukkit.createInventory(new ResourceWorldHolder(), 36, Messenger.format(Lang.get().getString("worlds_gui_name")));

        ItemStack rw = new ItemStack(Material.DIRT, 1);
        ItemStack nether = new ItemStack(Material.NETHERRACK, 1);
        ItemStack end = new ItemStack(Material.OBSIDIAN, 1);
        ItemStack back = new ItemStack(Material.BARRIER, 1);

        ItemMeta rw_meta = rw.getItemMeta();
        rw_meta.setDisplayName("§aResource World");
        rw.setItemMeta(rw_meta);

        ItemMeta nether_meta = nether.getItemMeta();
        nether_meta.setDisplayName("§cNether World");
        nether.setItemMeta(nether_meta);

        ItemMeta end_meta = end.getItemMeta();
        end_meta.setDisplayName("§9End World");
        end.setItemMeta(end_meta);

        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName("§cBack");
        back.setItemMeta(back_meta);

        worldsGUI.setItem(11, nether);
        worldsGUI.setItem(13, rw);
        worldsGUI.setItem(15, end);
        worldsGUI.setItem(31, back);

        p.openInventory(worldsGUI);
    }
}
