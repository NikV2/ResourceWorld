package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
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

    public MenuHandler(ResourceWorld plugin) {
        super(plugin);
    }

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
                player.closeInventory();
                new GUIManager().openWorldsGUI(player);
                break;
            case "§aResource World":
                if (worldExists()) {
                    player.closeInventory();
                    new ResetByCommand(plugin).executeReset();
                }
                break;
            case "§cNether World":
                if (configBoolean("nether_world.settings.enabled")) {
                    player.closeInventory();
                    new ResetByCommand(plugin).executeNetherReset();
                }
                break;
            case "§9End World":
                if (configBoolean("end_world.settings.enabled")) {
                    player.closeInventory();
                    new ResetByCommand(plugin).executeEndReset();
                }
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