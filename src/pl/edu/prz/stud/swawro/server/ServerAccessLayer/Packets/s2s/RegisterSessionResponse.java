package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s;

public class RegisterSessionResponse {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
