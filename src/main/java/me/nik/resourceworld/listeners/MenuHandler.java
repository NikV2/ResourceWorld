package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.GUIManager;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldUtils;
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
                if (!new WorldUtils().worldExists()) {
                    player.sendMessage(Messenger.message("not_exist"));
                } else {
                    new ResetByCommand().executeReset();
                    player.closeInventory();
                }
                break;
            case "§aSettings":
                player.closeInventory();
                new GUIManager().openSettingsGUI(player);
                break;
            case "§eAlways Day":
                booleanSet("world.settings.always_day", !configBoolean("world.settings.always_day"));
                saveAndReload();
                break;
            case "§eDisable Entity Spawning":
                booleanSet("world.settings.disable_entity_spawning", !configBoolean("world.settings.disable_entity_spawning"));
                saveAndReload();
                break;
            case "§eCheck for Updates":
                booleanSet("settings.check_for_updates", !configBoolean("settings.check_for_updates"));
                saveAndReload();
                break;
            case "§eBlock Generation":
                booleanSet("world.settings.block_regeneration.enabled", !configBoolean("world.settings.block_regeneration.enabled"));
                saveAndReload();
                break;
            case "§eDisabled Commands":
                booleanSet("disabled_commands.enabled", !configBoolean("disabled_commands.enabled"));
                saveAndReload();
                break;
            case "§eAutomated Resets":
                booleanSet("world.settings.automated_resets.enabled", !configBoolean("world.settings.automated_resets.enabled"));
                saveAndReload();
                break;
            case "§cBack":
                player.closeInventory();
                new GUIManager().openMainGUI(player);
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