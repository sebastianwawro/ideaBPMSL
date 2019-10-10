package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SleepMeasureTO;

public class RemoveSleepMeasureRequest {
    private int packetId=312;
    private String uuid;
    private String login;
    private List<SleepMeasureTO> sleepMeasures;

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

    public List<SleepMeasureTO> getSleepMeasures() {
        return sleepMeasures;
    }

    public void setSleepMeasures(List<SleepMeasureTO> sleepMeasures) {
        this.sleepMeasures = sleepMeasures;
    }
}