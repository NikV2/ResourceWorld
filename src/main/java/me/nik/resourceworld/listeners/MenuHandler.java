package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuHandler implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(ColourUtils.format(Lang.get().getString("gui_name")))) {
            e.setCancelled(true);
        }
    }
}