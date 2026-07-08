package me.MouIcchatta.order;

import me.MouIcchatta.order.command.OrderCommand;
import me.MouIcchatta.order.gui.ConfirmMenu;
import me.MouIcchatta.order.gui.OrderMenu;
import me.MouIcchatta.order.listener.MenuListener;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class MouIcchattaOrder extends JavaPlugin {

    private static MouIcchattaOrder instance;
    private OrderMenu orderMenu;
    private ConfirmMenu confirmMenu;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.orderMenu = new OrderMenu(this);
        this.confirmMenu = new ConfirmMenu(this);

        PluginCommand command = getCommand("order");
        if (command != null) {
            command.setExecutor(new OrderCommand(this));
        }

        getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        getLogger().info("MouIcchattaOrder da bat!");
    }

    public static MouIcchattaOrder getInstance() {
        return instance;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public ConfirmMenu getConfirmMenu() {
        return confirmMenu;
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String prefix() {
        return color(getConfig().getString("settings.prefix", "&6&lORDER &8» &f"));
    }
}
