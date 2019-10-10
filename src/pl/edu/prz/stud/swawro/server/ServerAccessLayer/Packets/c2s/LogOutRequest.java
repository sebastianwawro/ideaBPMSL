package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class LogOutRequest {
    private int packetId=103;
    private String uuid;

    public int getPacketId() {
        return packetId;
    }

    private void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
