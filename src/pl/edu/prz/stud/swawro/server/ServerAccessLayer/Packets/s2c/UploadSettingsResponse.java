package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

public class UploadSettingsResponse {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
