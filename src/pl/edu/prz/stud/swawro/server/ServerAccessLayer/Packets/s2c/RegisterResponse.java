package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

public class RegisterResponse {
    private int statusCode;
    private int userId;
    private int dateRegistered;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(int dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
