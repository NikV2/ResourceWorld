package me.nik.resourceworld.gui.menus;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.gui.Menu;
import me.nik.resourceworld.gui.PlayerMenuUtility;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MainGui extends Menu {
    public MainGui(PlayerMenuUtility playerMenuUtility, ResourceWorld plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    protected String getMenuName() {
        return Messenger.format(Lang.get().getString("gui_name"));
    }

    @Override
    protected int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§aTeleport All":
                new ResetTeleport().resetTP();
                p.sendMessage(Messenger.message("teleported_players"));
                break;
            case "§aReload":
                p.closeInventory();
                p.sendMessage(Messenger.message("reloading"));
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                plugin.getServer().getPluginManager().enablePlugin(plugin);
                p.sendMessage(Messenger.message("reloaded"));
                break;
            case "§aLooking for Support?":
                p.closeInventory();
                p.sendMessage(ChatColor.BLUE + ">> " + ChatColor.WHITE + "https://discordapp.com/invite/m7j2Y9H" + ChatColor.BLUE + " <<");
                break;
            case "§cExit":
                p.closeInventory();
                break;
            case "§aReset":
                p.closeInventory();
                new WorldsGui(playerMenuUtility, plugin).open();
                break;
        }
    }

    @Override
    protected void setMenuItems() {
        ArrayList<String> teleportLore = new ArrayList<>();
        teleportLore.add(" ");
        teleportLore.add(ChatColor.GRAY + "Teleport all the");
        teleportLore.add(ChatColor.GRAY + "Players that are in");
        teleportLore.add(ChatColor.GRAY + "The Resource World");
        teleportLore.add(ChatColor.GRAY + "Back to spawn.");
        ItemStack teleport = makeItem(Material.ENDER_PEARL, 1, "§aTeleport All", teleportLore);

        ArrayList<String> reloadLore = new ArrayList<>();
        reloadLore.add(" ");
        reloadLore.add(ChatColor.GRAY + "WARNING!");
        reloadLore.add(" ");
        reloadLore.add(ChatColor.GRAY + "This may cause Lag.");
        ItemStack reload = makeItem(Material.REDSTONE, 1, "§aReload", reloadLore);

        ItemStack support = makeItem(Material.DIAMOND, 1, "§aLooking for Support?", null);
        ItemStack exit = makeItem(Material.BARRIER, 1, "§cExit", null);

        ArrayList<String> resetLore = new ArrayList<>();
        resetLore.add("");
        resetLore.add(ChatColor.GRAY + "Would you like to reset");
        resetLore.add(ChatColor.GRAY + "The Resource World?");
        ItemStack reset = makeItem(Material.EMERALD, 1, "§aReset", resetLore);

        inventory.setItem(13, teleport);
        inventory.setItem(15, reload);
        inventory.setItem(30, support);
        inventory.setItem(32, reset);
        inventory.setItem(49, exit);

        new BukkitRunnable() {
            public void run() {
                if (!(inventory.getHolder() instanceof Menu) || inventory == null) {
                    cancel();
                    return;
                }
                ArrayList<String> serverLore = new ArrayList<>();
                serverLore.add(ChatColor.GREEN + "> System Information:");
                serverLore.add(ChatColor.GRAY + "Server Version: " + plugin.getServer().getVersion());
                serverLore.add(ChatColor.GRAY + "CPU Proccessors: " + Runtime.getRuntime().availableProcessors());
                serverLore.add(ChatColor.GRAY + "Memory: " + Runtime.getRuntime().maxMemory() / 1024L / 1024L + "/" + Runtime.getRuntime().freeMemory() / 1024L / 1024L);
                ItemStack server = makeItem(Material.BEACON, 1, "§bServer Status", serverLore);

                inventory.setItem(11, server);
            }
        }.runTaskTimer(plugin, 1, 10);
    }
}
