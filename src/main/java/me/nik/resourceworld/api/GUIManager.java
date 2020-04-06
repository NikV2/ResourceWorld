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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class GUIManager {

    private static Inventory mainGUI;
    private static Inventory gamerulesGUI;
    private static Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);

    public static void openMainGUI(Player p) {

        Inventory mainGUI = Bukkit.createInventory(new ResourceWorldHolder(), 45, Messenger.format(Lang.get().getString("gui_name")));

        //Items Here
        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemStack reload = new ItemStack(Material.REDSTONE);
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemStack support = new ItemStack(Material.DIAMOND);
        ItemStack reset = new ItemStack(Material.REDSTONE_TORCH);
        ItemStack gamerules = new ItemStack(Material.BOOK);

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

        ItemMeta gamerules_meta = gamerules.getItemMeta();
        gamerules_meta.setDisplayName("§aGamerules");
        gamerules.setItemMeta(gamerules_meta);

        //Add the Items
        mainGUI.setItem(40, close);
        mainGUI.setItem(24, gamerules);
        mainGUI.setItem(20, reset);
        mainGUI.setItem(16, support);
        mainGUI.setItem(14, reload);
        mainGUI.setItem(12, teleport);
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
                guiView.setItem(10, server);
                pAnonymous.updateInventory();
            }
        }.runTaskTimer(plugin, 1, 10);
    }

    public static void openGamerulesGUI(Player p) {
        Inventory gamerulesGUI = Bukkit.createInventory(new ResourceWorldHolder(), 54, Messenger.format(Lang.get().getString("gamerules_gui_name")));
        p.openInventory(gamerulesGUI);
        final Player pNon = p;
        new BukkitRunnable() {

            @Override
            public void run() {

                //not always day gamerule
                ItemStack day = new ItemStack(Material.PAPER, 1);
                ItemMeta day_meta = day.getItemMeta();
                day_meta.setDisplayName("§eNot Always Day");
                ArrayList<String> dayLore = new ArrayList<>();
                dayLore.add("");
                dayLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.not_always_day"));
                dayLore.add("");
                dayLore.add(ChatColor.WHITE + "Would you like it to");
                dayLore.add(ChatColor.WHITE + "Always be day in the Resource World?");
                day_meta.setLore(dayLore);
                day.setItemMeta(day_meta);

                //can mobs spawn gamerule
                ItemStack mobspawn = new ItemStack(Material.PAPER, 1);
                ItemMeta mobspawn_meta = mobspawn.getItemMeta();
                mobspawn_meta.setDisplayName("§eCan Mobs Spawn?");
                ArrayList<String> moblore = new ArrayList<>();
                moblore.add("");
                moblore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.can_mobs_spawn"));
                moblore.add("");
                moblore.add(ChatColor.WHITE + "Would you like Mobs to");
                moblore.add(ChatColor.WHITE + "Spawn in the Resource World?");
                mobspawn_meta.setLore(moblore);
                mobspawn.setItemMeta(mobspawn_meta);

                //can fire spread gamerule
                ItemStack fire = new ItemStack(Material.PAPER, 1);
                ItemMeta fire_meta = fire.getItemMeta();
                fire_meta.setDisplayName("§eCan Fire Spread?");
                ArrayList<String> fireLore = new ArrayList<>();
                fireLore.add("");
                fireLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.can_fire_spread"));
                fireLore.add("");
                fireLore.add(ChatColor.WHITE + "Would you like fire to spread");
                fireLore.add(ChatColor.WHITE + "In the Resource World?");
                fire_meta.setLore(fireLore);
                fire.setItemMeta(fire_meta);

                //keep inventory gamerule
                ItemStack ki = new ItemStack(Material.PAPER, 1);
                ItemMeta ki_meta = ki.getItemMeta();
                ki_meta.setDisplayName("§eKeep Inventory On Death");
                ArrayList<String> kiLore = new ArrayList<>();
                kiLore.add("");
                kiLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.keep_inventory_on_death"));
                kiLore.add("");
                kiLore.add(ChatColor.WHITE + "Would you like players to keep their");
                kiLore.add(ChatColor.WHITE + "Items if they die in the Resource World?");
                ki_meta.setLore(kiLore);
                ki.setItemMeta(ki_meta);

                //mob griefing gamerule
                ItemStack mg = new ItemStack(Material.PAPER, 1);
                ItemMeta mg_meta = mg.getItemMeta();
                mg_meta.setDisplayName("§eMob Griefing");
                ArrayList<String> mgLore = new ArrayList<>();
                mgLore.add("");
                mgLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.mob_griefing"));
                mgLore.add("");
                mgLore.add(ChatColor.WHITE + "Would you like Mobs to be able");
                mgLore.add(ChatColor.WHITE + "To modify Blocks inside the Resource World?");
                mg_meta.setLore(mgLore);
                mg.setItemMeta(mg_meta);

                //death messages gamerule
                ItemStack dm = new ItemStack(Material.PAPER, 1);
                ItemMeta dm_meta = dm.getItemMeta();
                dm_meta.setDisplayName("§eShow Death Messages");
                ArrayList<String> dmLore = new ArrayList<>();
                dmLore.add("");
                dmLore.add(ChatColor.GRAY + "Currently set to: " + ChatColor.GREEN + Config.get().getBoolean("world.settings.gamerules.show_death_messages"));
                dmLore.add("");
                dmLore.add(ChatColor.WHITE + "Would you like players to see Death Messages");
                dmLore.add(ChatColor.WHITE + "If they die inside the Resource World?");
                dm_meta.setLore(dmLore);
                dm.setItemMeta(dm_meta);

                ItemStack back = new ItemStack(Material.BARRIER, 1);
                ItemMeta back_meta = back.getItemMeta();
                back_meta.setDisplayName("§cBack");
                back.setItemMeta(back_meta);

                gamerulesGUI.setItem(11, day);
                gamerulesGUI.setItem(13, mobspawn);
                gamerulesGUI.setItem(15, fire);
                gamerulesGUI.setItem(29, ki);
                gamerulesGUI.setItem(31, mg);
                gamerulesGUI.setItem(33, dm);
                gamerulesGUI.setItem(49, back);
            }
        }.runTaskTimer(plugin, 1, 5);
    }
}
