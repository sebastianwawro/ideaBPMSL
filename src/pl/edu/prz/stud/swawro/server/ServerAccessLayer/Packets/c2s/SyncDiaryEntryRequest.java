package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class SyncDiaryEntryRequest {
    private int packetId=314;
    private String uuid;
    private String login;
    private int defaultSync;
    private int dateStart;
    private int dateEnd;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getDefaultSync() {
        return defaultSync;
    }

    public void setDefaultSync(int defaultSync) {
        this.defaultSync = defaultSync;
    }

    public int getDateStart() {
        return dateStart;
    }

    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
    }

    public int getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }
}
