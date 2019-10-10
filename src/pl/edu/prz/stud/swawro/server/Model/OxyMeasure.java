package pl.edu.prz.stud.swawro.server.Model;

import javax.persistence.*;

@Entity
@Table (name="oxymeasures")
public class OxyMeasure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private int date;

    @Column(name = "oxy", nullable = false)
    private int oxy;

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
}
