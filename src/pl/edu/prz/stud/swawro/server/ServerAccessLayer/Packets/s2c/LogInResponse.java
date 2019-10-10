package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

public class LogInResponse {
    private int statusCode;
    private String uuid;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
