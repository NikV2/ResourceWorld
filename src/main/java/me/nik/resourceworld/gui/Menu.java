package me.nik.resourceworld.gui;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu implements InventoryHolder {

    protected final ResourceWorld plugin;

    protected Inventory inventory;

    protected PlayerMenu playerMenu;

    public Menu(PlayerMenu playerMenu, ResourceWorld plugin) {
        this.playerMenu = playerMenu;
        this.plugin = plugin;
    }

    protected abstract String getMenuName();

    protected abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    protected abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        playerMenu.getOwner().openInventory(inventory);
    }

    protected ItemStack makeItem(Material material, int amount, String displayName, List<String> lore) {

        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatUtils.format(displayName));
        if (lore != null) {
            List<String> loreList = new ArrayList<>();
            for (String l : lore) {
                loreList.add(ChatUtils.format(l));
            }
            itemMeta.setLore(loreList);
        }
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}