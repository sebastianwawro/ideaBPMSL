package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.BpMeasureTO;

public class SyncBpMeasureResponse {
    private int statusCode;
    private String login;
    private List<BpMeasureTO> bpMeasures;

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

    public List<BpMeasureTO> getBpMeasures() {
        return bpMeasures;
    }

    public void setBpMeasures(List<BpMeasureTO> bpMeasures) {
        this.bpMeasures = bpMeasures;
    }
}
