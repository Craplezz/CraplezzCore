package me.mani.clcore.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mani.clcore.Core;
import me.mani.clcore.server.ServerInfoBroadcastClient;
import me.mani.clcore.server.packet.ServerInfoDataPacket;
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
    private CachedServerInfo ownServerInfo;

    public ServerManager(CachedServerInfo ownServerInfo) {
        this.ownServerInfo = ownServerInfo;
        broadcastClient = new ServerInfoBroadcastClient(this);
    }

    public void updateServerInfo(CachedServerInfo serverInfo) {
        serverInfos.put(serverInfo.getServerName(), serverInfo);
    }

    public void requestServerInfoUpdate() {
        broadcastClient.addListener((serverConnection -> {
            System.out.println("[SINFO] Sending packet!");
            System.out.println(serverConnection);
            if (serverConnection != null) {
                serverConnection.sendPacket(new ServerInfoUpdatePacket());
            }
        }));
    }

    public void broadcastServerInfoData() {
        broadcastClient.addListener(serverConnection -> {
            if (serverConnection != null) {
                serverConnection.sendPacket(new ServerInfoDataPacket(ownServerInfo));
            }
        });
    }

    public void broadcastOnlinePlayers(int onlinePlayers) {
        ownServerInfo.setOnlinePlayers(onlinePlayers);
        broadcastServerInfoData();
    }

    public void broadcastMaxPlayers(int maxPlayers) {
        ownServerInfo.setMaxPlayers(maxPlayers);
        broadcastServerInfoData();
    }

    public void broadcastMotd(String motd) {
        ownServerInfo.setMotd(motd);
        broadcastServerInfoData();
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
