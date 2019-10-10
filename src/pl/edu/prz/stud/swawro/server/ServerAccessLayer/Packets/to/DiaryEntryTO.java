package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.DiaryEntry;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class DiaryEntryTO {
    private int userId;
    private int date;
    private String description;
    private int statusOnServer;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusOnServer() {
        return statusOnServer;
    }

    public void setStatusOnServer(int statusOnServer) {
        this.statusOnServer = statusOnServer;
    }

    public static DiaryEntryTO encode(DiaryEntry diaryEntry) {
        DiaryEntryTO diaryEntryTO = new DiaryEntryTO();
        diaryEntryTO.setUserId(diaryEntry.getUser().getId());
        diaryEntryTO.setDate(diaryEntry.getDate());
        diaryEntryTO.setDescription(diaryEntry.getDescription());
        return diaryEntryTO;
    }

    public DiaryEntry decode(){
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setUser(UserDAO.getInstance().getById(userId));
        diaryEntry.setDate(date);
        diaryEntry.setDescription(description);
        return diaryEntry;
    }

    public DiaryEntry decode(User user){
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setUser(user);
        diaryEntry.setDate(date);
        diaryEntry.setDescription(description);
        return diaryEntry;
    }
}
