package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.DiaryEntryTO;

public class SyncDiaryEntryResponse {
    private int statusCode;
    private String login;
    private List<DiaryEntryTO> diaryEntries;

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

    public List<DiaryEntryTO> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(List<DiaryEntryTO> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }
}
