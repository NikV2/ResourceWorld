package me.nik.resourceworld.gui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();

        ItemStack item = e.getCurrentItem();

        if (!(holder instanceof Menu) || item == null || item.getType() == Material.AIR) return;

        e.setCancelled(true);

        Menu menu = (Menu) holder;
        menu.handleMenu(e);
    }
}