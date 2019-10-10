package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s;

import pl.edu.prz.stud.swawro.server.config.ServerInfo;

public class InfoSessionExpiredRequest {
    private int packetId=902;
    private ServerInfo serverInfo;

    public int getPacketId() {
        return packetId;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
