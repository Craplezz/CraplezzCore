package me.mani.clcore.server;

import com.google.common.util.concurrent.SettableFuture;
import me.mani.clapi.connection.client.Client;
import me.mani.clapi.connection.client.ServerConnection;
import me.mani.clapi.connection.packet.Packet;
import me.mani.clcore.bungee.ServerManager;
import me.mani.clcore.server.packet.ServerInfoDataPacket;
import me.mani.clcore.server.packet.ServerInfoUpdatePacket;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoBroadcastClient extends Client {

    private ServerManager serverManager;
    private SettableFuture<ServerConnection> serverConnectionFuture;

    public ServerInfoBroadcastClient(ServerManager serverManager) {
        super("localhost", 2424);
        this.serverManager = serverManager;

        Packet.registerPacket(ServerInfoUpdatePacket.class, (byte) 0);
        Packet.registerPacket(ServerInfoDataPacket.class, (byte) 1);

        serverConnectionFuture = SettableFuture.create();
    }

    @Override
    public void onConnect(ServerConnection serverConnection) {
        System.out.println("[SINFO] Connected!");
        serverConnectionFuture.set(serverConnection);
    }

    @Override
    public void onDisconnect(ServerConnection serverConnection) {
        serverConnectionFuture.set(null);
    }

    @Override
    public void onPacketRecieve(ServerConnection serverConnection, Packet packet) {
        System.out.println("[SINFO] Packet recieved!");
        if (packet instanceof ServerInfoDataPacket) {
            serverManager.updateServerInfo(((ServerInfoDataPacket) packet).getServerInfo());
        }
    }

    public SettableFuture<ServerConnection> getServerConnectionFuture() {
        return serverConnectionFuture;
    }

}
