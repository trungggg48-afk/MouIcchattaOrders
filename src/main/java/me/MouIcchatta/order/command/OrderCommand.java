package me.MouIcchatta.order.command;

import me.MouIcchatta.order.MouIcchattaOrder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OrderCommand implements CommandExecutor {

    private final MouIcchattaOrder plugin;

    public OrderCommand(MouIcchattaOrder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.player-only", "&cLệnh này chỉ dùng trong game.")));
            return true;
        }

        if (!player.hasPermission("mouicchattaorder.use")) {
            player.sendMessage(plugin.prefix() + plugin.color(plugin.getConfig().getString("messages.no-permission", "&cBạn không có quyền dùng lệnh này.")));
            return true;
        }

        plugin.getOrderMenu().open(player);
        return true;
    }
}
