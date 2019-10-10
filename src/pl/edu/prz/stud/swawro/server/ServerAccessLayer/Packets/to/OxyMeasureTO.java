package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.OxyMeasure;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class OxyMeasureTO {
    private int userId;
    private int date;
    private int oxy;
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

    public int getOxy() {
        return oxy;
    }

    public void setOxy(int oxy) {
        this.oxy = oxy;
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

    public static OxyMeasureTO encode(OxyMeasure oxyMeasure) {
        OxyMeasureTO oxyMeasureTO = new OxyMeasureTO();
        oxyMeasureTO.setUserId(oxyMeasure.getUser().getId());
        oxyMeasureTO.setDate(oxyMeasure.getDate());
        oxyMeasureTO.setOxy(oxyMeasure.getOxy());
        oxyMeasureTO.setMeasureType(oxyMeasure.getMeasureType());
        return oxyMeasureTO;
    }

    public OxyMeasure decode () {
        OxyMeasure oxyMeasure = new OxyMeasure();
        oxyMeasure.setUser(UserDAO.getInstance().getById(userId));
        oxyMeasure.setDate(date);
        oxyMeasure.setOxy(oxy);
        oxyMeasure.setMeasureType(measureType);
        return oxyMeasure;
    }

    public OxyMeasure decode (User user) {
        OxyMeasure oxyMeasure = new OxyMeasure();
        oxyMeasure.setUser(user);
        oxyMeasure.setDate(date);
        oxyMeasure.setOxy(oxy);
        oxyMeasure.setMeasureType(measureType);
        return oxyMeasure;
    }
}
