package ub.andrewyernau;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIMenu implements Listener {

    public static Inventory gui;


    static UltimateBlood plugin;

    public GUIMenu(UltimateBlood plugin) {
        GUIMenu.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void openGUI(Player player) {
        gui = Bukkit.createInventory(null, 27,
                ChatColor.DARK_RED + "UltimateBleeding");

        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BOLD + plugin.getMessagesConfig().getString("messages.special_bandage", "§aSpecial Bandage"));
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(ChatColor.AQUA + plugin.getMessagesConfig().getString("messages.click_to_get_bandage", "Click to get the bandage"));
        itemMeta.setLore(Lore);
        itemMeta.addEnchant(Enchantment.MENDING, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);

        gui.setItem(0, itemStack);

        player.openInventory(gui);

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (!e.getInventory().equals(gui))
            return;
        if (e.getCurrentItem() == null)
            return;
        if (e.getCurrentItem().getItemMeta() == null)
            return;
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;

        e.setCancelled(true);

        if (e.getSlot() == 0 && e.getCurrentItem().getType() == Material.PAPER) {
            if (p.hasPermission("ub.toggle")) {
                ItemStack itemStack = plugin.createBandage();
                p.getInventory().addItem(itemStack);
            }

        }
    }

}