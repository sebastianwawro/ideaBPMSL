package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SmartSearchTO;

public class SmartSearchResponse {
    private int statusCode;
    private List<SmartSearchTO> smartSearchList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<SmartSearchTO> getSmartSearchList() {
        return smartSearchList;
    }

    public void setSmartSearchList(List<SmartSearchTO> smartSearchList) {
        this.smartSearchList = smartSearchList;
    }
}
