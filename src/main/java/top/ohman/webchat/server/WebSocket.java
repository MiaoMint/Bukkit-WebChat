package top.ohman.webchat.server;

import com.alibaba.fastjson2.JSON;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import top.ohman.webchat.WebChat;
import top.ohman.webchat.model.Chat;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WebSocket extends WebSocketServer {
    public WebSocket(int port) {
        super(new InetSocketAddress(port));
    }

    private int guestNameId = 1;
    private final HashMap<String, String> guestName = new HashMap<>();

    private static final ArrayList<org.java_websocket.WebSocket> conns = new ArrayList<>();

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
//        连接后发送临时名称
        String name = WebChat.config.getString("WebSenderNamePrefix") + guestNameId++;
        guestName.put(conn.toString(), name);
        conn.send(JSON.toJSONString(new Chat(
                "MyName",
                name,
                "system"
        )));
//        发送当前在线玩家
        conn.send(JSON.toJSONString(new Chat(
                "system",
                "online player " + Bukkit.getOnlinePlayers().size(),
                "system"
        )));
    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        guestName.remove(conn.toString());
        conns.remove(conn);
        conn.close();
        Bukkit.getLogger().info(guestName.get(conn.toString()) + "is offline");
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        String name = guestName.get(conn.toString());
        String s1 = message.replace("§", "");
        sendMsg(new Chat(
                name,
                s1));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Objects.requireNonNull(WebChat.config.getString("WebSendMessage"))
                    .replace("[name]", name).replace("[content]", s1));
        }
    }

    @Override
    public void onError(org.java_websocket.WebSocket conn, Exception ex) {
        Bukkit.getLogger().warning(conn + "err:" + ex);
        conns.remove(conn);
        conn.close();
    }

    @Override
    public void onStart() {
        Bukkit.getLogger().info("WebSocket server is start!");
    }

    private static WebSocketServer s;

    public static void startServer() {
        int port = WebChat.config.getInt("WebSocketPort");
        s = new WebSocket(port);
        s.start();
        Bukkit.getLogger().info("WebSocket started on port:" + s.getPort());
    }

    public static void stopServer() throws InterruptedException {
        Bukkit.getLogger().info("WebSocket server is stopped!");
        s.stop();
    }

    public static void sendMsg(Chat chat) {
        new Thread(() -> {
            for (org.java_websocket.WebSocket conn : conns) {
                if (conn.isOpen()) {
                    conn.send(JSON.toJSONString(chat));
                }
            }
        }).start();
    }
}
