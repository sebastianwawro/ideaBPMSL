package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.Abnormal;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class AbnormalTO {
    private int userId;
    private int abType;
    private int bpHigh;
    private int bpLow;
    private int hr;
    private int oxy;
    private int deepSleepTime;
    private int deepSleepPer;
    private int date;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAbType() {
        return abType;
    }

    public void setAbType(int abType) {
        this.abType = abType;
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

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getOxy() {
        return oxy;
    }

    public void setOxy(int oxy) {
        this.oxy = oxy;
    }

    public int getDeepSleepTime() {
        return deepSleepTime;
    }

    public void setDeepSleepTime(int deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }

    public int getDeepSleepPer() {
        return deepSleepPer;
    }

    public void setDeepSleepPer(int deepSleepPer) {
        this.deepSleepPer = deepSleepPer;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public static AbnormalTO encode(Abnormal abnormal) {
        AbnormalTO abnormalTO = new AbnormalTO();
        abnormalTO.setUserId(abnormal.getUser().getId());
        abnormalTO.setAbType(abnormal.getAbType());
        abnormalTO.setBpHigh(abnormal.getBpHigh());
        abnormalTO.setBpLow(abnormal.getBpLow());
        abnormalTO.setHr(abnormal.getHr());
        abnormalTO.setOxy(abnormal.getOxy());
        abnormalTO.setDeepSleepTime(abnormal.getDeepSleepTime());
        abnormalTO.setDeepSleepPer(abnormal.getDeepSleepPer());
        abnormalTO.setDate(abnormal.getDate());
        return abnormalTO;
    }

    public Abnormal decode(){
        Abnormal abnormal = new Abnormal();
        abnormal.setUser(UserDAO.getInstance().getById(userId));
        abnormal.setAbType(abType);
        abnormal.setBpHigh(bpHigh);
        abnormal.setBpLow(bpLow);
        abnormal.setHr(hr);
        abnormal.setOxy(oxy);
        abnormal.setDeepSleepTime(deepSleepTime);
        abnormal.setDeepSleepPer(deepSleepPer);
        abnormal.setDate(date);
        return abnormal;
    }

    public Abnormal decode(User user){
        Abnormal abnormal = new Abnormal();
        abnormal.setUser(user);
        abnormal.setAbType(abType);
        abnormal.setBpHigh(bpHigh);
        abnormal.setBpLow(bpLow);
        abnormal.setHr(hr);
        abnormal.setOxy(oxy);
        abnormal.setDeepSleepTime(deepSleepTime);
        abnormal.setDeepSleepPer(deepSleepPer);
        abnormal.setDate(date);
        return abnormal;
    }
}
