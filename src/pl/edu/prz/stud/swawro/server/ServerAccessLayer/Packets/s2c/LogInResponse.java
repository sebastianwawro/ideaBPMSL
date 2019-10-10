package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.ServerInfoTO;
import pl.edu.prz.stud.swawro.server.config.ServerInfo;

public class LogInResponse {
    private int statusCode;
    private String uuid;
    private ServerInfo serverInfo;

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

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
