package me.nik.resourceworld.listeners;

import me.nik.resourceworld.gui.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory().getHolder() == null) return;

        InventoryHolder holder = e.getClickedInventory().getHolder();

        if (!(holder instanceof Menu)) return;

        if (e.getCurrentItem() == null) return;

        e.setCancelled(true);

        Menu menu = (Menu) holder;
        menu.handleMenu(e);
    }

}
