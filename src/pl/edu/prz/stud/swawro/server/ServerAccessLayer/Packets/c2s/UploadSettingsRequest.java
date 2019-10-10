package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SettingsTO;

public class UploadSettingsRequest {
    private int packetId = 111;
    private String uuid;
    private List<SettingsTO> settingsTOList;

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

    public List<SettingsTO> getSettingsTOList() {
        return settingsTOList;
    }

    public void setSettingsTOList(List<SettingsTO> settingsTOList) {
        this.settingsTOList = settingsTOList;
    }
}
