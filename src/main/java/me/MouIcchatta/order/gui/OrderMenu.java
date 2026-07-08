package me.MouIcchatta.order.gui;

import me.MouIcchatta.order.MouIcchattaOrder;
import me.MouIcchatta.order.model.OrderAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class OrderMenu {

    private final MouIcchattaOrder plugin;
    private final Map<Integer, OrderAction> actionsBySlot = new HashMap<>();

    public OrderMenu(MouIcchattaOrder plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        actionsBySlot.clear();

        String title = plugin.color(plugin.getConfig().getString("menu.title", "&8Order"));
        int size = plugin.getConfig().getInt("menu.size", 27);

        Inventory inv = Bukkit.createInventory(null, size, title);

        if (plugin.getConfig().getBoolean("menu.filler.enabled", true)) {
            Material fillerMat = parseMaterial(plugin.getConfig().getString("menu.filler.material", "BLACK_STAINED_GLASS_PANE"));
            String fillerName = plugin.color(plugin.getConfig().getString("menu.filler.name", " "));
            ItemStack filler = createItem(fillerMat, fillerName, Collections.emptyList());

            for (int i = 0; i < size; i++) {
                inv.setItem(i, filler);
            }
        }

        ConfigurationSection items = plugin.getConfig().getConfigurationSection("menu.items");
        if (items != null) {
            for (String key : items.getKeys(false)) {
                String path = "menu.items." + key;

                int slot = plugin.getConfig().getInt(path + ".slot");
                Material material = parseMaterial(plugin.getConfig().getString(path + ".material", "STONE"));
                String display = plugin.color(plugin.getConfig().getString(path + ".display", key));
                String price = plugin.getConfig().getString(path + ".price", "N/A");
                String link = plugin.getConfig().getString(path + ".link", "N/A");
                List<String> lore = colorList(plugin.getConfig().getStringList(path + ".lore"));
                List<String> commands = plugin.getConfig().getStringList(path + ".console-commands");

                ItemStack item = createItem(material, display, lore);
                inv.setItem(slot, item);

                actionsBySlot.put(slot, new OrderAction(key, display, price, link, commands));
            }
        }

        player.openInventory(inv);
        playSound(player, "sounds.open-menu");
    }

    public OrderAction getActionBySlot(int slot) {
        return actionsBySlot.get(slot);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }

        return item;
    }

    private List<String> colorList(List<String> input) {
        List<String> out = new ArrayList<>();
        for (String s : input) {
            out.add(plugin.color(s));
        }
        return out;
    }

    private Material parseMaterial(String name) {
        Material material = Material.matchMaterial(name);
        return material != null ? material : Material.STONE;
    }

    private void playSound(Player player, String path) {
        String soundName = plugin.getConfig().getString(path);
        if (soundName == null || soundName.isBlank()) return;

        try {
            player.playSound(player.getLocation(), org.bukkit.Sound.valueOf(soundName), 1f, 1f);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
