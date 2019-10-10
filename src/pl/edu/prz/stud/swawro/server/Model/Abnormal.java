package pl.edu.prz.stud.swawro.server.Model;

import javax.persistence.*;

@Entity
@Table (name="abnormals")
public class Abnormal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ab_type", nullable = false)
    private int abType;

    @Column(name = "bp_high", nullable = false)
    private int bpHigh;

    @Column(name = "bp_low", nullable = false)
    private int bpLow;

    @Column(name = "hr", nullable = false)
    private int hr;

    @Column(name = "oxy", nullable = false)
    private int oxy;

    @Column(name = "deep_sleep_time", nullable = false)
    private int deepSleepTime;

    @Column(name = "deep_sleep_per", nullable = false)
    private int deepSleepPer;

    @Column(name = "date", nullable = false)
    private int date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
