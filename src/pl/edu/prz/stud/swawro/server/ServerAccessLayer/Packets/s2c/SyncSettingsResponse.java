package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SettingsTO;

public class SyncSettingsResponse {
    private int statusCode;
    private List<SettingsTO> settingsTOList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<SettingsTO> getSettingsTOList() {
        return settingsTOList;
    }

    public void setSettingsTOList(List<SettingsTO> settingsTOList) {
        this.settingsTOList = settingsTOList;
    }
}
