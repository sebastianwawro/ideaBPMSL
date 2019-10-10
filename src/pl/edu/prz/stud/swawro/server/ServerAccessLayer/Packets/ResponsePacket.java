package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets;

public class ResponsePacket {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
