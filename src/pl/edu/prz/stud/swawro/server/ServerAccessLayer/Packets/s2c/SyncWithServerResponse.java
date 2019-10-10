package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.AbnormalTO;

public class SyncWithServerResponse {
    private int statusCode;
    private int userDataUid;
    private int relationsUid;
    private int settingsUid;
    private List<AbnormalTO> abnormalTOList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getUserDataUid() {
        return userDataUid;
    }

    public void setUserDataUid(int userDataUid) {
        this.userDataUid = userDataUid;
    }

    public int getRelationsUid() {
        return relationsUid;
    }

    public void setRelationsUid(int relationsUid) {
        this.relationsUid = relationsUid;
    }

    public int getSettingsUid() {
        return settingsUid;
    }

    public void setSettingsUid(int settingsUid) {
        this.settingsUid = settingsUid;
    }

    public List<AbnormalTO> getAbnormalTOList() {
        return abnormalTOList;
    }

    public void setAbnormalTOList(List<AbnormalTO> abnormalTOList) {
        this.abnormalTOList = abnormalTOList;
    }
}
