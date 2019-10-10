package pl.edu.prz.stud.swawro.server.Model;

import javax.persistence.*;

@Entity
@Table(name="sleepmeasures")
public class SleepMeasure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private int date;

    @Column(name = "deep", nullable = false)
    private int deep;

    @Column(name = "shallow", nullable = false)
    private int shallow;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "wakeup_times", nullable = false)
    private int wakeupTimes;

    @Column(name = "measure_type", nullable = false)
    private int measureType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
