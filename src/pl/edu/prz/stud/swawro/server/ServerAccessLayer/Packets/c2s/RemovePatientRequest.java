package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class RemovePatientRequest {
    private int packetId=207;
    private String uuid;
    private String patientLogin;

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

    public String getPatientLogin() {
        return patientLogin;
    }

    public void setPatientLogin(String patientLogin) {
        this.patientLogin = patientLogin;
    }
}
