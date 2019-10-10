package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.DiaryEntryTO;

public class UploadDiaryEntryRequest {
    private int packetId=313;
    private String uuid;
    private String login;
    private List<DiaryEntryTO> diaryEntries;

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

    public List<DiaryEntryTO> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(List<DiaryEntryTO> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }
}
