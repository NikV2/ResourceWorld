package me.nik.resourceworld.api;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import me.nik.resourceworld.utils.Messenger;
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

    private static Inventory mainGUI;
    private static Inventory settingsGUI;
    public ResourceWorld plugin;

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
        mainGUI.setItem(33, settings);
        mainGUI.setItem(31, reset);
        mainGUI.setItem(29, support);
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

    public void openSettingsGUI(Player p) {
        Inventory settingsGUI = Bukkit.createInventory(new ResourceWorldHolder(), 54, Messenger.format(Lang.get().getString("settings_gui_name")));
        p.openInventory(settingsGUI);
        final Player pNon = p;
        new BukkitRunnable() {

            @Override
            public void run() {
                InventoryView guiView = pNon.getOpenInventory();
                if (!(guiView.getTopInventory().getHolder() instanceof ResourceWorldHolder)) {
                    cancel();
                    return;
                }
                //always day
                ItemStack day = new ItemStack(Material.PAPER, 1);
                ItemMeta day_meta = day.getItemMeta();
                day_meta.setDisplayName("§eAlways Day");
                ArrayList<String> dayLore = new ArrayList<>();
                dayLore.add("");
                dayLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.always_day"));
                dayLore.add("");
                dayLore.add(ChatColor.WHITE + "Would you like it to");
                dayLore.add(ChatColor.WHITE + "Always be day in the Resource World?");
                day_meta.setLore(dayLore);
                day.setItemMeta(day_meta);

                //disable entity spawn
                ItemStack mobspawn = new ItemStack(Material.PAPER, 1);
                ItemMeta mobspawn_meta = mobspawn.getItemMeta();
                mobspawn_meta.setDisplayName("§eDisable Entity Spawning");
                ArrayList<String> moblore = new ArrayList<>();
                moblore.add("");
                moblore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.disable_entity_spawning"));
                moblore.add("");
                moblore.add(ChatColor.WHITE + "Would you like Mobs to");
                moblore.add(ChatColor.WHITE + "Not spawn in the Resource World?");
                mobspawn_meta.setLore(moblore);
                mobspawn.setItemMeta(mobspawn_meta);

                //check for updates
                ItemStack updates = new ItemStack(Material.PAPER, 1);
                ItemMeta updates_meta = updates.getItemMeta();
                updates_meta.setDisplayName("§eCheck for Updates");
                ArrayList<String> updatesLore = new ArrayList<>();
                updatesLore.add("");
                updatesLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("settings.check_for_updates"));
                updatesLore.add("");
                updatesLore.add(ChatColor.WHITE + "Would you like to check for");
                updatesLore.add(ChatColor.WHITE + "Updates on Server Restarts?");
                updates_meta.setLore(updatesLore);
                updates.setItemMeta(updates_meta);

                //ore regeneration
                ItemStack orere = new ItemStack(Material.PAPER, 1);
                ItemMeta orere_meta = orere.getItemMeta();
                orere_meta.setDisplayName("§eBlock Generation");
                ArrayList<String> orereLore = new ArrayList<>();
                orereLore.add("");
                orereLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.block_regeneration.enabled"));
                orereLore.add("");
                orereLore.add(ChatColor.WHITE + "Would you like certain blocks to");
                orereLore.add(ChatColor.WHITE + "Regenerate after a certain amount of time?");
                orere_meta.setLore(orereLore);
                orere.setItemMeta(orere_meta);

                //disabled commands
                ItemStack discmd = new ItemStack(Material.PAPER, 1);
                ItemMeta discmd_meta = discmd.getItemMeta();
                discmd_meta.setDisplayName("§eDisabled Commands");
                ArrayList<String> discmdLore = new ArrayList<>();
                discmdLore.add("");
                discmdLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("disabled_commands.enabled"));
                discmdLore.add("");
                discmdLore.add(ChatColor.WHITE + "Would you like certain commands to");
                discmdLore.add(ChatColor.WHITE + "Be Disabled inside the Resource World?");
                discmd_meta.setLore(discmdLore);
                discmd.setItemMeta(discmd_meta);

                //automated resets
                ItemStack dm = new ItemStack(Material.PAPER, 1);
                ItemMeta dm_meta = dm.getItemMeta();
                dm_meta.setDisplayName("§eAutomated Resets");
                ArrayList<String> dmLore = new ArrayList<>();
                dmLore.add("");
                dmLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.automated_resets.enabled"));
                dmLore.add("");
                dmLore.add(ChatColor.WHITE + "Would you like to Reset the");
                dmLore.add(ChatColor.WHITE + "Resource World Automatically?");
                dm_meta.setLore(dmLore);
                dm.setItemMeta(dm_meta);

                ItemStack back = new ItemStack(Material.BARRIER, 1);
                ItemMeta back_meta = back.getItemMeta();
                back_meta.setDisplayName("§cBack");
                back.setItemMeta(back_meta);

                settingsGUI.setItem(11, day);
                settingsGUI.setItem(13, mobspawn);
                settingsGUI.setItem(15, updates);
                settingsGUI.setItem(29, orere);
                settingsGUI.setItem(31, discmd);
                settingsGUI.setItem(33, dm);
                settingsGUI.setItem(49, back);
            }
        }.runTaskTimer(plugin, 1, 5);
    }
}
