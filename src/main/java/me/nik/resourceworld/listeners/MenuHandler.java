package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.holder.ResourceWorldHolder;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import org.bukkit.ChatColor;
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
        if (!(event.getInventory().getHolder() instanceof ResourceWorldHolder)) {
            return;
        }
        if (null == clickedItem) {
            return;
        }
        switch (clickedItem.getType()) {
            case ENDER_PEARL:
                new ResetTeleport().resetTP();
                player.sendMessage(Messenger.message("teleported_players"));
                break;
            case REDSTONE:
                player.closeInventory();
                player.sendMessage(Messenger.message("reloading"));
                plugin.getServer().getPluginManager().disablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                plugin.getServer().getPluginManager().enablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                player.sendMessage(Messenger.message("reloaded"));
                break;
            case DIAMOND:
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + ">> " + ChatColor.WHITE + "https://discordapp.com/invite/m7j2Y9H" + ChatColor.BLUE + " <<");
                break;
            case BARRIER:
                player.closeInventory();
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