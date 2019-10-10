package pl.edu.prz.stud.swawro.server.Model;

import pl.edu.prz.stud.swawro.server.Model.EnumValue.BpHTType;

import javax.persistence.*;

@Entity
@Table(name="settings")
public class Settings {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bp_high_ab", nullable = false)
    private int bpHighAb;

    @Column(name = "bp_low_ab", nullable = false)
    private int bpLowAb;

    @Column(name = "hr_high_ab", nullable = false)
    private int hrHighAb;

    @Column(name = "hr_low_ab", nullable = false)
    private int hrLowAb;

    @Column(name = "oxy_ab", nullable = false)
    private int oxyAb;

    @Column(name = "deep_sleep_ab", nullable = false)
    private int deepSleepAb;

    @Column(name = "cust_bp_high_min", nullable = false)
    private int custBpHighMin;

    @Column(name = "cust_bp_high_max", nullable = false)
    private int custBpHighMax;

    @Column(name = "cust_bp_low_min", nullable = false)
    private int custBpLowMin;

    @Column(name = "cust_bp_low_max", nullable = false)
    private int custBpLowMax;

    @Column(name = "cust_hr_min", nullable = false)
    private int custHrMin;

    @Column(name = "cust_hr_max", nullable = false)
    private int custHrMax;

    @Column(name = "cust_deep_sleep_min_time", nullable = false)
    private int custDeepSleepMinTime;

    @Column(name = "cust_deep_sleep_min_per", nullable = false)
    private int custDeepSleepMinPer;

    @Column(name = "cust_oxy_min", nullable = false)
    private int custOxyMin;

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
    
    public void restoreDefault() {
        bpHighAb=BpHTType.STAGE_3.ordinal();
        bpLowAb=1;
        hrHighAb=1;
        hrLowAb=1;
        oxyAb=1;
        deepSleepAb=1;
        custBpHighMax=-1;
        custBpHighMin=-1;
        custBpLowMax=-1;
        custBpLowMin=-1;
        custHrMax=-1;
        custHrMin=-1;
        custOxyMin=-1;
        custDeepSleepMinTime=-1;
        custDeepSleepMinPer=-1;
    }
}
