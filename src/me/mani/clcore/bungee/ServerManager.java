package me.mani.clcore.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import me.mani.clapi.connection.client.ServerConnection;
import me.mani.clcore.Core;
import me.mani.clcore.server.ServerInfoBroadcastClient;
import me.mani.clcore.server.packet.ServerInfoUpdatePacket;
import me.mani.clcore.util.CachedServerInfo;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
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
        Futures.addCallback(broadcastClient.getServerConnectionFuture(), new FutureCallback<ServerConnection>() {

            @Override
            public void onSuccess(@Nullable ServerConnection serverConnection) {
                System.out.println("[SINFO] Sending packet!");
                System.out.println(serverConnection);
                if (serverConnection != null) {
                    serverConnection.sendPacket(new ServerInfoUpdatePacket());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

        });
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
