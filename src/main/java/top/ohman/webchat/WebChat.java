package top.ohman.webchat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import top.ohman.webchat.server.WebSocket;

public final class WebChat extends JavaPlugin {
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        config = getConfig();
        WebSocket.startServer();
        Bukkit.getPluginManager().registerEvents(new Listener(), this);
        getLogger().info("WebChat is enable");
    }


    @Override
    public void onDisable() {
        super.onDisable();
        try {
            WebSocket.stopServer();
        } catch (InterruptedException e) {
            getLogger().warning(e.getMessage());
            throw new RuntimeException(e);
        }
        getLogger().info("WebChat is disable");

    }
}
