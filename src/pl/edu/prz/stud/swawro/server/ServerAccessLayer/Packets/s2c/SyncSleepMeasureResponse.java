package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SleepMeasureTO;

public class SyncSleepMeasureResponse {
    private int statusCode;
    private String login;
    private List<SleepMeasureTO> sleepMeasures;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
