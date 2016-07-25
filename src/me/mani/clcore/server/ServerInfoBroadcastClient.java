package me.mani.clcore.server;

import me.mani.clapi.connection.client.Client;
import me.mani.clapi.connection.client.ServerConnection;
import me.mani.clapi.connection.packet.Packet;
import me.mani.clcore.bungee.ServerManager;
import me.mani.clcore.server.packet.ServerInfoDataPacket;
import me.mani.clcore.server.packet.ServerInfoUpdatePacket;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoBroadcastClient extends Client {

    private ServerManager serverManager;
    private Set<Consumer<ServerConnection>> listeners = new HashSet<>();
    private ServerConnection serverConnection;
    private boolean isConnected;

    public ServerInfoBroadcastClient(ServerManager serverManager) {
        super("craplezz.de", 2424);
        this.serverManager = serverManager;

        Packet.registerPacket(ServerInfoUpdatePacket.class, (byte) 0);
        Packet.registerPacket(ServerInfoDataPacket.class, (byte) 1);
    }

    @Override
    public void onConnect(ServerConnection serverConnection) {
        System.out.println("[SINFO] Connected!");
        this.serverConnection = serverConnection;
        isConnected = true;
        for (Consumer<ServerConnection> listener : listeners) {
            listener.accept(serverConnection);
        }
    }

    @Override
    public void onDisconnect(ServerConnection serverConnection) {
        this.serverConnection = null;
        isConnected = false;
        for (Consumer<ServerConnection> listener : listeners) {
            listener.accept(null);
        }
    }

    @Override
    public void onPacketRecieve(ServerConnection serverConnection, Packet packet) {
        System.out.println("[SINFO] Packet recieved!");
        if (packet instanceof ServerInfoDataPacket) {
            serverManager.updateServerInfo(((ServerInfoDataPacket) packet).getServerInfo());
        }
    }

    public void addListener(Consumer<ServerConnection> listener) {
        if (isConnected) {
            listener.accept(serverConnection);
        }
        else {
            listeners.add(listener);
        }
    }

}
