package me.mani.clcore.server.packet;

import me.mani.clapi.connection.packet.Packet;

import java.nio.ByteBuffer;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoUpdatePacket extends Packet {

    @Override
    public byte getPacketId() {
        return 0;
    }

    @Override
    public ByteBuffer toBuffer() {
        return ByteBuffer.allocate(0);
    }

}
