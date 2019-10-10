package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s;

public class RegisterSessionRequest {
    private int packetId=901;
    private String uuid;
    private long expireTime;

    public int getPacketId() {
        return packetId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
