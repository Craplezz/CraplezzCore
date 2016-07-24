package me.mani.clcore.server.packet;

import me.mani.clapi.connection.packet.Packet;
import me.mani.clcore.util.CachedServerInfo;

import java.nio.ByteBuffer;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoDataPacket extends Packet {

    private CachedServerInfo serverInfo;

    public ServerInfoDataPacket(CachedServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfoDataPacket(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.getInt()];
        byteBuffer.get(bytes);
        String serverName = new String(bytes);
        int onlinePlayers = byteBuffer.getInt();
        int maxPlayers = byteBuffer.getInt();
        bytes = new byte[byteBuffer.getInt()];
        byteBuffer.get(bytes);
        String motd = new String(bytes);
        boolean isOffline = byteBuffer.get() == (byte) 1;
        serverInfo = new CachedServerInfo(serverName, maxPlayers, onlinePlayers, motd);
        serverInfo.setOffline(isOffline);
    }

    @Override
    public byte getPacketId() {
        return 1;
    }

    @Override
    public ByteBuffer toBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + serverInfo.getServerName().getBytes().length + 8 + 4 + serverInfo.getMotd().getBytes().length + 1);
        byteBuffer.putInt(serverInfo.getServerName().getBytes().length);
        byteBuffer.put(serverInfo.getServerName().getBytes());
        byteBuffer.putInt(serverInfo.getOnlinePlayers());
        byteBuffer.putInt(serverInfo.getMaxPlayers());
        byteBuffer.putInt(serverInfo.getMotd().getBytes().length);
        byteBuffer.put(serverInfo.getMotd().getBytes());
        byteBuffer.put((byte) (serverInfo.isOffline() ? 1 : 0));
        return byteBuffer;
    }

    public CachedServerInfo getServerInfo() {
        return serverInfo;
    }
}
