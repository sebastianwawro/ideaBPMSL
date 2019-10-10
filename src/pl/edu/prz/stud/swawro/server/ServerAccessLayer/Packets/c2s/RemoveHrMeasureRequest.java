package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.HrMeasureTO;

public class RemoveHrMeasureRequest {
    private int packetId=306;
    private String uuid;
    private String login;
    private List<HrMeasureTO> hrMeasures;

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

    public List<HrMeasureTO> getHrMeasures() {
        return hrMeasures;
    }

    public void setHrMeasures(List<HrMeasureTO> hrMeasures) {
        this.hrMeasures = hrMeasures;
    }
}
