package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class RemoveDoctorRequest {
    private int packetId=206;
    private String uuid;
    private String doctorLogin;

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

    public String getDoctorLogin() {
        return doctorLogin;
    }

    public void setDoctorLogin(String doctorLogin) {
        this.doctorLogin = doctorLogin;
    }
}
