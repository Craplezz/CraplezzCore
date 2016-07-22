package me.mani.clcore.server;

import me.mani.clapi.connection.client.Client;
import me.mani.clapi.connection.client.ServerConnection;
import me.mani.clapi.connection.packet.Packet;
import me.mani.clcore.bungee.ServerManager;
import me.mani.clcore.server.packet.ServerInfoDataPacket;
import me.mani.clcore.server.packet.ServerInfoUpdatePacket;

import java.util.concurrent.CompletableFuture;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoBroadcastClient extends Client {

    private ServerManager serverManager;
    private CompletableFuture<ServerConnection> serverConnectionFuture;

    public ServerInfoBroadcastClient(ServerManager serverManager) {
        super("localhost", 2424);
        this.serverManager = serverManager;

        Packet.registerPacket(ServerInfoUpdatePacket.class, (byte) 0);
        Packet.registerPacket(ServerInfoDataPacket.class, (byte) 1);

        serverConnectionFuture = new CompletableFuture<>();
    }

    @Override
    public void onConnect(ServerConnection serverConnection) {
        serverConnectionFuture.complete(serverConnection);
    }

    @Override
    public void onDisconnect(ServerConnection serverConnection) {
        serverConnectionFuture.complete(null);
    }

    @Override
    public void onPacketRecieve(ServerConnection serverConnection, Packet packet) {
        if (packet instanceof ServerInfoDataPacket) {
            serverManager.updateServerInfo(((ServerInfoDataPacket) packet).getServerInfo());
        }
    }

    public CompletableFuture<ServerConnection> getServerConnectionFuture() {
        return serverConnectionFuture;
    }

}
