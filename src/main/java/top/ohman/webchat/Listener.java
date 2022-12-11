package top.ohman.webchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import top.ohman.webchat.model.Chat;
import top.ohman.webchat.server.WebSocket;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!WebChat.config.getBoolean("EnableJoinServerMessage")){
            return;
        }
        WebSocket.sendMsg(new Chat(
                e.getPlayer().getName(),
                "加入了游戏",
                "system"
        ));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        WebSocket.sendMsg(new Chat(e.getPlayer().getName(),
                e.getMessage()));
    }

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent e) {
        if (!WebChat.config.getBoolean("EnableAdvancementMessage")){
            return;
        }
        WebSocket.sendMsg(new Chat(
                e.getPlayer().getName(),
                "完成进度：" + e.getAdvancement().getKey().getKey(),
                "system"
        ));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!WebChat.config.getBoolean("EnableDeathMessage")){
            return;
        }
        WebSocket.sendMsg(new Chat(
                e.getEntity().getName(),
                e.getDeathMessage(),
                "system"
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!WebChat.config.getBoolean("EnableQuitServerMessage")){
            return;
        }
        WebSocket.sendMsg(new Chat(
                e.getPlayer().getName(),
                "离开了服务器",
                "system"
        ));
    }
}
