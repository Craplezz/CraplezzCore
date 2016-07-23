package me.mani.clcore.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mani.clcore.Core;
import me.mani.clcore.server.ServerInfoBroadcastClient;
import me.mani.clcore.server.packet.ServerInfoUpdatePacket;
import me.mani.clcore.util.CachedServerInfo;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private ServerInfoBroadcastClient broadcastClient;
    private Map<String, CachedServerInfo> serverInfos = new HashMap<>();

    public ServerManager() {
        broadcastClient = new ServerInfoBroadcastClient(this);
    }

    public void updateServerInfo(CachedServerInfo serverInfo) {
        serverInfos.put(serverInfo.getServerName(), serverInfo);
    }

    /**
     * Should be called on start, stop, motd change and online player change.
     */
    public void broadcastServerInfoUpdate() {
        broadcastClient.addListener((serverConnection -> {
            System.out.println("[SINFO] Sending packet!");
            if (serverConnection != null) {
                serverConnection.sendPacket(new ServerInfoUpdatePacket());
            }
        }));
    }

    public CachedServerInfo getServerInfo(String serverName) {
        return serverInfos.get(serverName);
    }

    public boolean connect(Player player, String serverName) {
        CachedServerInfo serverInfo;
        if ((serverInfo = getServerInfo(serverName)) != null && !serverInfo.isOffline()) {
            connectRawly(player, serverName);
            return true;
        }
        return false;
    }

    public void connectRawly(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }

}
