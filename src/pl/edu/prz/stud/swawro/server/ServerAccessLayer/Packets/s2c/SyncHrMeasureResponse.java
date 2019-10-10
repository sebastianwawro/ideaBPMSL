package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.HrMeasureTO;

public class SyncHrMeasureResponse {
    private int statusCode;
    private String login;
    private List<HrMeasureTO> hrMeasures;

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

    public List<HrMeasureTO> getHrMeasures() {
        return hrMeasures;
    }

    public void setHrMeasures(List<HrMeasureTO> hrMeasures) {
        this.hrMeasures = hrMeasures;
    }
}
