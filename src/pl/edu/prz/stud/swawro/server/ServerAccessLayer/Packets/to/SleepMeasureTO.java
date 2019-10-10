package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.SleepMeasure;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class SleepMeasureTO {
    private int userId;
    private int date;
    private int deep;
    private int shallow;
    private int total;
    private int wakeupTimes;
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

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getShallow() {
        return shallow;
    }

    public void setShallow(int shallow) {
        this.shallow = shallow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWakeupTimes() {
        return wakeupTimes;
    }

    public void setWakeupTimes(int wakeupTimes) {
        this.wakeupTimes = wakeupTimes;
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

    public static SleepMeasureTO encode(SleepMeasure sleepMeasure) {
        SleepMeasureTO sleepMeasureTO = new SleepMeasureTO();
        sleepMeasureTO.setUserId(sleepMeasure.getUser().getId());
        sleepMeasureTO.setDate(sleepMeasure.getDate());
        sleepMeasureTO.setDeep(sleepMeasure.getDeep());
        sleepMeasureTO.setShallow(sleepMeasure.getShallow());
        sleepMeasureTO.setTotal(sleepMeasure.getTotal());
        sleepMeasureTO.setWakeupTimes(sleepMeasure.getWakeupTimes());
        sleepMeasureTO.setMeasureType(sleepMeasure.getMeasureType());
        return sleepMeasureTO;
    }

    public SleepMeasure decode() {
        SleepMeasure sleepMeasure = new SleepMeasure();
        sleepMeasure.setUser(UserDAO.getInstance().getById(userId));
        sleepMeasure.setDate(date);
        sleepMeasure.setDeep(deep);
        sleepMeasure.setShallow(shallow);
        sleepMeasure.setTotal(total);
        sleepMeasure.setWakeupTimes(wakeupTimes);
        sleepMeasure.setMeasureType(measureType);
        return sleepMeasure;
    }

    public SleepMeasure decode(User user) {
        SleepMeasure sleepMeasure = new SleepMeasure();
        sleepMeasure.setUser(user);
        sleepMeasure.setDate(date);
        sleepMeasure.setDeep(deep);
        sleepMeasure.setShallow(shallow);
        sleepMeasure.setTotal(total);
        sleepMeasure.setWakeupTimes(wakeupTimes);
        sleepMeasure.setMeasureType(measureType);
        return sleepMeasure;
    }
}
