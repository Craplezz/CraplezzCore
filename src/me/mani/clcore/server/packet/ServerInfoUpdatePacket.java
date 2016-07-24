package me.mani.clcore.server.packet;

import me.mani.clapi.connection.packet.Packet;

import java.nio.ByteBuffer;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoUpdatePacket extends Packet {

    public ServerInfoUpdatePacket(ByteBuffer byteBuffer) {}

    @Override
    public byte getPacketId() {
        return 0;
    }

    @Override
    public ByteBuffer toBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        byteBuffer.put((byte) 0);
        return byteBuffer;
    }

}
