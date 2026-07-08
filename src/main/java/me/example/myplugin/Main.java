package me.example.myplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MyPlugin da bat!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MyPlugin da tat!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hello")) {
            sender.sendMessage("Xin chao tu plugin rieng cua ban!");
            return true;
        }
        return false;
    }
}
