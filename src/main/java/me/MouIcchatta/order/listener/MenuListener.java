package me.MouIcchatta.order.listener;

import me.MouIcchatta.order.MouIcchattaOrder;
import me.MouIcchatta.order.model.OrderAction;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class MenuListener implements Listener {

    private final MouIcchattaOrder plugin;

    public MenuListener(MouIcchattaOrder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;

        String title = event.getView().getTitle();
        String orderTitle = plugin.color(plugin.getConfig().getString("menu.title", "&8Order"));
        String confirmTitle = plugin.color(plugin.getConfig().getString("confirm-menu.title", "&8Xác nhận mua"));

        if (title.equals(orderTitle)) {
            event.setCancelled(true);

            OrderAction action = plugin.getOrderMenu().getActionBySlot(event.getRawSlot());
            if (action == null) return;

            playConfiguredSound(player, "sounds.click");

            boolean useConfirm = plugin.getConfig().getBoolean("settings.use-confirm-menu", true);
            if (useConfirm) {
                plugin.getConfirmMenu().open(player, action, event.getCurrentItem());
            } else {
                processPurchase(player, action);
            }
            return;
        }

        if (title.equals(confirmTitle)) {
            event.setCancelled(true);

            int confirmSlot = plugin.getConfig().getInt("confirm-menu.confirm-item.slot", 11);
            int cancelSlot = plugin.getConfig().getInt("confirm-menu.cancel-item.slot", 15);

            if (event.getRawSlot() == confirmSlot) {
                OrderAction action = plugin.getConfirmMenu().getPendingAction(player);
                if (action != null) {
                    processPurchase(player, action);
                    plugin.getConfirmMenu().clear(player);
                }
                return;
            }

            if (event.getRawSlot() == cancelSlot) {
                plugin.getConfirmMenu().clear(player);
                playConfiguredSound(player, "sounds.cancel");
                player.closeInventory();
            }
        }
    }

    private void processPurchase(Player player, OrderAction action) {
        FileConfiguration config = plugin.getConfig();

        for (String line : config.getStringList("messages.bought-message")) {
            player.sendMessage(plugin.color(
                    line.replace("%display%", action.getDisplay())
                            .replace("%price%", action.getPrice())
                            .replace("%link%", action.getLink())
                            .replace("%id%", action.getId())
            ));
        }

        String confirm = config.getString("messages.confirm-message", "&aBạn đã xác nhận đơn hàng %display%");
        player.sendMessage(plugin.prefix() + plugin.color(confirm.replace("%display%", action.getDisplay())));

        List<String> commands = action.getConsoleCommands();
        for (String cmd : commands) {
            String parsed = cmd.replace("%player%", player.getName())
                    .replace("%uuid%", player.getUniqueId().toString())
                    .replace("%display%", action.getDisplay())
                    .replace("%id%", action.getId());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
        }

        playConfiguredSound(player, "sounds.confirm");

        try {
            player.sendTitle(
                    plugin.color(config.getString("messages.bought-title", "&aĐã chọn đơn hàng!")),
                    plugin.color(config.getString("messages.bought-subtitle", "&fKiểm tra chat để xem hướng dẫn")),
                    10, 50, 10
            );
        } catch (Throwable ignored) {
        }

        if (config.getBoolean("settings.close-after-purchase", true)) {
            player.closeInventory();
        }
    }

    private void playConfiguredSound(Player player, String path) {
        String soundName = plugin.getConfig().getString(path);
        if (soundName == null || soundName.isBlank()) return;

        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1f, 1f);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
