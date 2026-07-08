package me.MouIcchatta.order.gui;

import me.MouIcchatta.order.MouIcchattaOrder;
import me.MouIcchatta.order.model.OrderAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ConfirmMenu {

    private final MouIcchattaOrder plugin;
    private final Map<UUID, OrderAction> pendingActions = new HashMap<>();

    public ConfirmMenu(MouIcchattaOrder plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, OrderAction action, ItemStack previewSource) {
        pendingActions.put(player.getUniqueId(), action);

        String title = plugin.color(plugin.getConfig().getString("confirm-menu.title", "&8Xác nhận mua"));
        int size = plugin.getConfig().getInt("confirm-menu.size", 27);
        Inventory inv = Bukkit.createInventory(null, size, title);

        fillBackground(inv);

        int previewSlot = plugin.getConfig().getInt("confirm-menu.preview-slot", 13);
        inv.setItem(previewSlot, previewSource.clone());

        inv.setItem(
                plugin.getConfig().getInt("confirm-menu.confirm-item.slot", 11),
                createItem(
                        parseMaterial(plugin.getConfig().getString("confirm-menu.confirm-item.material", "LIME_WOOL")),
                        plugin.color(plugin.getConfig().getString("confirm-menu.confirm-item.name", "&aXác nhận")),
                        colorList(plugin.getConfig().getStringList("confirm-menu.confirm-item.lore"))
                )
        );

        inv.setItem(
                plugin.getConfig().getInt("confirm-menu.cancel-item.slot", 15),
                createItem(
                        parseMaterial(plugin.getConfig().getString("confirm-menu.cancel-item.material", "RED_WOOL")),
                        plugin.color(plugin.getConfig().getString("confirm-menu.cancel-item.name", "&cHủy")),
                        colorList(plugin.getConfig().getStringList("confirm-menu.cancel-item.lore"))
                )
        );

        player.openInventory(inv);
    }

    public OrderAction getPendingAction(Player player) {
        return pendingActions.get(player.getUniqueId());
    }

    public void clear(Player player) {
        pendingActions.remove(player.getUniqueId());
    }

    private void fillBackground(Inventory inv) {
        ItemStack filler = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", Collections.emptyList());
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }
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
}
