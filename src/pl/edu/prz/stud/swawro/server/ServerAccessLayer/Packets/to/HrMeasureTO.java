package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.HrMeasure;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class HrMeasureTO {
    private int userId;
    private int date;
    private int hr;
    private int measureType;
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

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMeasureType() {
        return measureType;
    }

    public void setMeasureType(int measureType) {
        this.measureType = measureType;
    }

    public int getStatusOnServer() {
        return statusOnServer;
    }

    public void setStatusOnServer(int statusOnServer) {
        this.statusOnServer = statusOnServer;
    }

    public static HrMeasureTO encode(HrMeasure hrMeasure) {
        HrMeasureTO hrMeasureTO = new HrMeasureTO();
        hrMeasureTO.setUserId(hrMeasure.getUser().getId());
        hrMeasureTO.setDate(hrMeasure.getDate());
        hrMeasureTO.setHr(hrMeasure.getHr());
        hrMeasureTO.setMeasureType(hrMeasure.getMeasureType());
        return hrMeasureTO;
    }

    public HrMeasure decode() {
        HrMeasure hrMeasure = new HrMeasure();
        hrMeasure.setUser(UserDAO.getInstance().getById(userId));
        hrMeasure.setDate(date);
        hrMeasure.setHr(hr);
        hrMeasure.setMeasureType(measureType);
        return hrMeasure;
    }

    public HrMeasure decode(User user) {
        HrMeasure hrMeasure = new HrMeasure();
        hrMeasure.setUser(user);
        hrMeasure.setDate(date);
        hrMeasure.setHr(hr);
        hrMeasure.setMeasureType(measureType);
        return hrMeasure;
    }
}
