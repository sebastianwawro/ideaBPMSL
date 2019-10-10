package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SmartSearchTO;

public class GetUserInfoResponse {
    private int statusCode;
    private SmartSearchTO userInfo;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public SmartSearchTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SmartSearchTO userInfo) {
        this.userInfo = userInfo;
    }
}
