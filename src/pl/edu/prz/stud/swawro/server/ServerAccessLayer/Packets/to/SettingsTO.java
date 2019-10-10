package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class SettingsTO {
    private int doctorId;
    private int userId;
    private int bpHighAb;
    private int bpLowAb;
    private int hrHighAb;
    private int hrLowAb;
    private int oxyAb;
    private int deepSleepAb;
    private int custBpHighMin;
    private int custBpHighMax;
    private int custBpLowMin;
    private int custBpLowMax;
    private int custHrMin;
    private int custHrMax;
    private int custDeepSleepMinTime;
    private int custDeepSleepMinPer;
    private int custOxyMin;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBpHighAb() {
        return bpHighAb;
    }

    public void setBpHighAb(int bpHighAb) {
        this.bpHighAb = bpHighAb;
    }

    public int getBpLowAb() {
        return bpLowAb;
    }

    public void setBpLowAb(int bpLowAb) {
        this.bpLowAb = bpLowAb;
    }

    public int getHrHighAb() {
        return hrHighAb;
    }

    public void setHrHighAb(int hrHighAb) {
        this.hrHighAb = hrHighAb;
    }

    public int getHrLowAb() {
        return hrLowAb;
    }

    public void setHrLowAb(int hrLowAb) {
        this.hrLowAb = hrLowAb;
    }

    public int getOxyAb() {
        return oxyAb;
    }

    public void setOxyAb(int oxyAb) {
        this.oxyAb = oxyAb;
    }

    public int getDeepSleepAb() {
        return deepSleepAb;
    }

    public void setDeepSleepAb(int deepSleepAb) {
        this.deepSleepAb = deepSleepAb;
    }

    public int getCustBpHighMin() {
        return custBpHighMin;
    }

    public void setCustBpHighMin(int custBpHighMin) {
        this.custBpHighMin = custBpHighMin;
    }

    public int getCustBpHighMax() {
        return custBpHighMax;
    }

    public void setCustBpHighMax(int custBpHighMax) {
        this.custBpHighMax = custBpHighMax;
    }

    public int getCustBpLowMin() {
        return custBpLowMin;
    }

    public void setCustBpLowMin(int custBpLowMin) {
        this.custBpLowMin = custBpLowMin;
    }

    public int getCustBpLowMax() {
        return custBpLowMax;
    }

    public void setCustBpLowMax(int custBpLowMax) {
        this.custBpLowMax = custBpLowMax;
    }

    public int getCustHrMin() {
        return custHrMin;
    }

    public void setCustHrMin(int custHrMin) {
        this.custHrMin = custHrMin;
    }

    public int getCustHrMax() {
        return custHrMax;
    }

    public void setCustHrMax(int custHrMax) {
        this.custHrMax = custHrMax;
    }

    public int getCustDeepSleepMinTime() {
        return custDeepSleepMinTime;
    }

    public void setCustDeepSleepMinTime(int custDeepSleepMinTime) {
        this.custDeepSleepMinTime = custDeepSleepMinTime;
    }

    public int getCustDeepSleepMinPer() {
        return custDeepSleepMinPer;
    }

    public void setCustDeepSleepMinPer(int custDeepSleepMinPer) {
        this.custDeepSleepMinPer = custDeepSleepMinPer;
    }

    public int getCustOxyMin() {
        return custOxyMin;
    }

    public void setCustOxyMin(int custOxyMin) {
        this.custOxyMin = custOxyMin;
    }

    public static SettingsTO encode(Settings settings) {
        SettingsTO settingsTO = new SettingsTO();
        settingsTO.setDoctorId(settings.getDoctor().getId());
        settingsTO.setUserId(settings.getUser().getId());
        settingsTO.setBpHighAb(settings.getBpHighAb());
        settingsTO.setBpLowAb(settings.getBpLowAb());
        settingsTO.setHrHighAb(settings.getHrHighAb());
        settingsTO.setHrLowAb(settings.getHrLowAb());
        settingsTO.setOxyAb(settings.getOxyAb());
        settingsTO.setDeepSleepAb(settings.getDeepSleepAb());
        settingsTO.setCustBpHighMax(settings.getCustBpHighMax());
        settingsTO.setCustBpHighMin(settings.getCustBpHighMin());
        settingsTO.setCustBpLowMax(settings.getCustBpLowMax());
        settingsTO.setCustBpLowMin(settings.getCustBpLowMin());
        settingsTO.setCustHrMax(settings.getCustHrMax());
        settingsTO.setCustHrMin(settings.getCustHrMin());
        settingsTO.setCustOxyMin(settings.getCustOxyMin());
        settingsTO.setCustDeepSleepMinTime(settings.getCustDeepSleepMinTime());
        settingsTO.setCustDeepSleepMinPer(settings.getCustDeepSleepMinPer());
        return settingsTO;
    }

    public Settings decode(){
        Settings settings = new Settings();
        settings.setDoctor(UserDAO.getInstance().getById(doctorId));
        settings.setUser(UserDAO.getInstance().getById(userId));
        settings.setBpHighAb(bpHighAb);
        settings.setBpLowAb(bpLowAb);
        settings.setHrHighAb(hrHighAb);
        settings.setHrLowAb(hrLowAb);
        settings.setOxyAb(oxyAb);
        settings.setDeepSleepAb(deepSleepAb);
        settings.setCustBpHighMax(custBpHighMax);
        settings.setCustBpHighMin(custBpHighMin);
        settings.setCustBpLowMax(custBpLowMax);
        settings.setCustBpLowMin(custBpLowMin);
        settings.setCustHrMax(custHrMax);
        settings.setCustHrMin(custHrMin);
        settings.setCustOxyMin(custOxyMin);
        settings.setCustDeepSleepMinTime(custDeepSleepMinTime);
        settings.setCustDeepSleepMinPer(custDeepSleepMinPer);
        return settings;
    }

    public Settings decode(User doctor){
        Settings settings = new Settings();
        settings.setDoctor(doctor);
        settings.setUser(UserDAO.getInstance().getById(userId));
        settings.setBpHighAb(bpHighAb);
        settings.setBpLowAb(bpLowAb);
        settings.setHrHighAb(hrHighAb);
        settings.setHrLowAb(hrLowAb);
        settings.setOxyAb(oxyAb);
        settings.setDeepSleepAb(deepSleepAb);
        settings.setCustBpHighMax(custBpHighMax);
        settings.setCustBpHighMin(custBpHighMin);
        settings.setCustBpLowMax(custBpLowMax);
        settings.setCustBpLowMin(custBpLowMin);
        settings.setCustHrMax(custHrMax);
        settings.setCustHrMin(custHrMin);
        settings.setCustOxyMin(custOxyMin);
        settings.setCustDeepSleepMinTime(custDeepSleepMinTime);
        settings.setCustDeepSleepMinPer(custDeepSleepMinPer);
        return settings;
    }
}
