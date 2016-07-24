package me.mani.clcore.server.packet;

import me.mani.clapi.connection.packet.Packet;

import java.nio.ByteBuffer;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoUpdatePacket extends Packet {

    /**
     * Constructor for {@link me.mani.clapi.connection.packet.PacketSerializer}
     *
     * @param byteBuffer The {@link ByteBuffer} holding the data
     */
    public ServerInfoUpdatePacket(ByteBuffer byteBuffer) {}

    public ServerInfoUpdatePacket() {}

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
