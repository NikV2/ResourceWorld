package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.ResetTeleport;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class MenuHandler implements Listener {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(ColourUtils.format(Lang.get().getString("gui_name")))) {
            switch (e.getCurrentItem().getType()) {
                case ENDER_PEARL:
                    new ResetTeleport().resetTP();
                    player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("teleported_players")));
                    break;
                case REDSTONE:
                    player.closeInventory();
                    player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("reloading")));
                    plugin.getServer().getPluginManager().disablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                    plugin.getServer().getPluginManager().enablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                    player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("reloaded")));
                    System.gc();
                    break;
                case DIAMOND:
                    player.closeInventory();
                    player.sendMessage(ChatColor.BLUE + ">> " + ChatColor.WHITE + "https://discordapp.com/invite/m7j2Y9H" + ChatColor.BLUE + " <<");
                    break;
                case BARRIER:
                    player.closeInventory();
                    break;
            }
            e.setCancelled(true);
        }
    }
}