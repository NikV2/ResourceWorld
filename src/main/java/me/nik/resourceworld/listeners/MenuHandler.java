package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.GUIManager;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuHandler extends Manager {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (!(event.getInventory().getHolder() instanceof ResourceWorldHolder)) return;
        if (null == clickedItem) return;
        if (clickedItem.getType().equals(Material.AIR)) return;
        switch (clickedItem.getItemMeta().getDisplayName()) {
            case "§aTeleport All":
                new ResetTeleport().resetTP();
                player.sendMessage(Messenger.message("teleported_players"));
                break;
            case "§aReload":
                player.closeInventory();
                player.sendMessage(Messenger.message("reloading"));
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                plugin.getServer().getPluginManager().enablePlugin(plugin);
                player.sendMessage(Messenger.message("reloaded"));
                break;
            case "§aLooking for Support?":
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + ">> " + ChatColor.WHITE + "https://discordapp.com/invite/m7j2Y9H" + ChatColor.BLUE + " <<");
                break;
            case "§cExit":
                player.closeInventory();
                break;
            case "§aReset":
                new ResetByCommand().executeReset();
                player.closeInventory();
                break;
            case "§aGamerules":
                player.closeInventory();
                GUIManager.openGamerulesGUI(player);
                break;
            case "§eNot Always Day":
                if (configBoolean("world.settings.gamerules.not_always_day")) {
                    booleanSet("world.settings.gamerules.not_always_day", false);
                } else {
                    booleanSet("world.settings.gamerules.not_always_day", true);
                }
                saveAndReload();
                break;
            case "§eCan Mobs Spawn?":
                if (configBoolean("world.settings.gamerules.can_mobs_spawn")) {
                    booleanSet("world.settings.gamerules.can_mobs_spawn", false);
                } else {
                    booleanSet("world.settings.gamerules.can_mobs_spawn", true);
                }
                saveAndReload();
                break;
            case "§eCan Fire Spread?":
                if (configBoolean("world.settings.gamerules.can_fire_spread")) {
                    booleanSet("world.settings.gamerules.can_fire_spread", false);
                } else {
                    booleanSet("world.settings.gamerules.can_fire_spread", true);
                }
                saveAndReload();
                break;
            case "§eKeep Inventory On Death":
                if (configBoolean("world.settings.gamerules.keep_inventory_on_death")) {
                    booleanSet("world.settings.gamerules.keep_inventory_on_death", false);
                } else {
                    booleanSet("world.settings.gamerules.keep_inventory_on_death", true);
                }
                saveAndReload();
                break;
            case "§eMob Griefing":
                if (configBoolean("world.settings.gamerules.mob_griefing")) {
                    booleanSet("world.settings.gamerules.mob_griefing", false);
                } else {
                    booleanSet("world.settings.gamerules.mob_griefing", true);
                }
                saveAndReload();
                break;
            case "§eShow Death Messages":
                if (configBoolean("world.settings.gamerules.show_death_messages")) {
                    booleanSet("world.settings.gamerules.show_death_messages", false);
                } else {
                    booleanSet("world.settings.gamerules.show_death_messages", true);
                }
                saveAndReload();
                break;
            case "§cBack":
                player.closeInventory();
                GUIManager.openMainGUI(player);
                break;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuOpenedClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!(event.getInventory().getHolder() instanceof Player)) {
            return;
        }
        if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ResourceWorldHolder)) {
            return;
        }
        event.setCancelled(true);
    }
}