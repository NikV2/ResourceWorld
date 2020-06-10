package me.nik.resourceworld.gui.menus;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.gui.Menu;
import me.nik.resourceworld.gui.PlayerMenuUtility;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsGui extends Menu {
    public WorldsGui(PlayerMenuUtility playerMenuUtility, ResourceWorld plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    protected String getMenuName() {
        return Messenger.message(MsgType.WORLDS_GUI_NAME);
    }

    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§aResource World":
                if (WorldUtils.worldExists()) {
                    p.closeInventory();
                    new ResetByCommand(plugin).executeReset();
                }
                break;
            case "§cNether World":
                if (Config.get().getBoolean("nether_world.settings.enabled")) {
                    p.closeInventory();
                    new ResetByCommand(plugin).executeNetherReset();
                }
                break;
            case "§9End World":
                if (Config.get().getBoolean("end_world.settings.enabled")) {
                    p.closeInventory();
                    new ResetByCommand(plugin).executeEndReset();
                }
                break;
            case "§cBack":
                p.closeInventory();
                new MainGui(playerMenuUtility, plugin).open();
                break;
        }
    }

    @Override
    protected void setMenuItems() {
        ItemStack resource = makeItem(Material.DIRT, 1, "§aResource World", null);
        ItemStack nether = makeItem(Material.NETHERRACK, 1, "§cNether World", null);
        ItemStack end = makeItem(Material.OBSIDIAN, 1, "§9End World", null);
        ItemStack back = makeItem(Material.BARRIER, 1, "§cBack", null);

        inventory.setItem(11, nether);
        inventory.setItem(13, resource);
        inventory.setItem(15, end);
        inventory.setItem(31, back);
    }
}
