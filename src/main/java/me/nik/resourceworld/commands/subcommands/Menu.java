package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import me.nik.resourceworld.utils.ColourUtils;
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

public class Menu extends SubCommand {
    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getDescription() {
        return "Open the Resource World Menu!";
    }

    @Override
    public String getSyntax() {
        return "/Resource Menu";
    }

    @Override
    public void perform(Player player, String[] args) {
        Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("no_perm")));
        } else {
            if (args.length > 0) {
                Inventory gui = Bukkit.createInventory(new ResourceWorldHolder(), 36, ColourUtils.format(Lang.get().getString("gui_name")));

                //Items Here
                ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
                ItemStack reload = new ItemStack(Material.REDSTONE);
                ItemStack close = new ItemStack(Material.BARRIER);
                ItemStack support = new ItemStack(Material.DIAMOND);

                //Item Data
                ItemMeta teleport_meta = teleport.getItemMeta();
                teleport_meta.setDisplayName(ChatColor.GREEN + "Teleport All");
                ArrayList<String> teleport_lore = new ArrayList<>();
                teleport_lore.add(" ");
                teleport_lore.add(ChatColor.GRAY + "Teleport all the");
                teleport_lore.add(ChatColor.GRAY + "Players that are in");
                teleport_lore.add(ChatColor.GRAY + "The Resource World");
                teleport_lore.add(ChatColor.GRAY + "Back to spawn.");
                teleport_meta.setLore(teleport_lore);
                teleport.setItemMeta(teleport_meta);

                ItemMeta reload_meta = reload.getItemMeta();
                reload_meta.setDisplayName(ChatColor.GREEN + "Reload");
                ArrayList<String> reload_lore = new ArrayList<>();
                reload_lore.add(" ");
                reload_lore.add(ChatColor.GRAY + "WARNING!");
                reload_lore.add(" ");
                reload_lore.add(ChatColor.GRAY + "This may cause Lag.");
                reload_meta.setLore(reload_lore);
                reload.setItemMeta(reload_meta);

                ItemMeta support_meta = support.getItemMeta();
                support_meta.setDisplayName(ChatColor.GREEN + "Looking for Support?");
                support.setItemMeta(support_meta);

                ItemMeta close_meta = close.getItemMeta();
                close_meta.setDisplayName(ChatColor.RED + "Exit");
                close.setItemMeta(close_meta);

                //Add the Items
                gui.setItem(31, close);
                gui.setItem(16, support);
                gui.setItem(14, reload);
                gui.setItem(12, teleport);
                player.openInventory(gui);
                final Player pAnonymous = player;
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
                        server_meta.setDisplayName(ChatColor.AQUA + "Server Status");
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
                }.runTaskTimer(ResourceWorld.getPlugin(ResourceWorld.class), 0, 10);
            }
        }
    }
}