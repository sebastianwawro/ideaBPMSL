package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets;

public class RequestPacket {
    private int packetId=0;

    public int getPacketId() {
        return packetId;
    }

    private void setPacketId(int packetId) {
        this.packetId = packetId;
    }
}
