package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class LogInRequest {
    private int packetId=102;
    private String login;
    private String password;
    private int doNotLogOut;

    public int getPacketId() {
        return packetId;
    }

    private void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDoNotLogOut() {
        return doNotLogOut;
    }

    public void setDoNotLogOut(int doNotLogOut) {
        this.doNotLogOut = doNotLogOut;
    }
}
