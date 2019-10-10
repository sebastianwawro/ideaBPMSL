package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.OxyMeasureTO;

public class UploadOxyMeasureRequest {
    private int packetId=307;
    private String uuid;
    private String login;
    private List<OxyMeasureTO> oxyMeasures;

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

    public List<OxyMeasureTO> getOxyMeasures() {
        return oxyMeasures;
    }

    public void setOxyMeasures(List<OxyMeasureTO> oxyMeasures) {
        this.oxyMeasures = oxyMeasures;
    }
}
