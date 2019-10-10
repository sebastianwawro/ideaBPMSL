package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.BpMeasure;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class BpMeasureTO {
    private int userId;
    private int date;
    private int bpHigh;
    private int bpLow;
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

    public int getBpHigh() {
        return bpHigh;
    }

    public void setBpHigh(int bpHigh) {
        this.bpHigh = bpHigh;
    }

    public int getBpLow() {
        return bpLow;
    }

    public void setBpLow(int bpLow) {
        this.bpLow = bpLow;
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

    public static BpMeasureTO encode(BpMeasure bpMeasure) {
        BpMeasureTO bpMeasureTO = new BpMeasureTO();
        bpMeasureTO.setUserId(bpMeasure.getUser().getId());
        bpMeasureTO.setDate(bpMeasure.getDate());
        bpMeasureTO.setBpHigh(bpMeasure.getBpHigh());
        bpMeasureTO.setBpLow(bpMeasure.getBpLow());
        bpMeasureTO.setMeasureType(bpMeasure.getMeasureType());
        return bpMeasureTO;
    }

    public BpMeasure decode(){
        BpMeasure bpMeasure = new BpMeasure();
        bpMeasure.setUser(UserDAO.getInstance().getById(userId));
        bpMeasure.setDate(date);
        bpMeasure.setBpHigh(bpHigh);
        bpMeasure.setBpLow(bpLow);
        bpMeasure.setMeasureType(measureType);
        return bpMeasure;
    }

    public BpMeasure decode(User user){
        BpMeasure bpMeasure = new BpMeasure();
        bpMeasure.setUser(user);
        bpMeasure.setDate(date);
        bpMeasure.setBpHigh(bpHigh);
        bpMeasure.setBpLow(bpLow);
        bpMeasure.setMeasureType(measureType);
        return bpMeasure;
    }
}
