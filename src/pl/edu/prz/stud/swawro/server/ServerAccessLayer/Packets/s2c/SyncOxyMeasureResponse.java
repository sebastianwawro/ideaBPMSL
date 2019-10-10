package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.OxyMeasureTO;

public class SyncOxyMeasureResponse {
    private int statusCode;
    private String login;
    private List<OxyMeasureTO> oxyMeasures;

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

    public List<OxyMeasureTO> getOxyMeasures() {
        return oxyMeasures;
    }

    public void setOxyMeasures(List<OxyMeasureTO> oxyMeasures) {
        this.oxyMeasures = oxyMeasures;
    }
}
