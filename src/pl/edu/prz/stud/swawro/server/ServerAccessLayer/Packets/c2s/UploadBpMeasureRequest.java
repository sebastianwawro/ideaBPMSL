package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.BpMeasureTO;

public class UploadBpMeasureRequest {
    private int packetId=301;
    private String uuid;
    private String login;
    private List<BpMeasureTO> bpMeasures;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<BpMeasureTO> getBpMeasures() {
        return bpMeasures;
    }

    public void setBpMeasures(List<BpMeasureTO> bpMeasures) {
        this.bpMeasures = bpMeasures;
    }
}
