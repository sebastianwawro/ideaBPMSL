package pl.edu.prz.stud.swawro.server.Model;

import javax.persistence.*;

@Entity
@Table (name="relations")
public class Relation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_start", nullable = false)
    private int dateStart;

    @Column(name = "date_end", nullable = false)
    private int dateEnd;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "is_active", nullable = false)
    private int isActive;

    @Column(name = "who_proposed", nullable = false)
    private int whoProposed;

    @Column(name = "is_approved", nullable = false)
    private int isApproved;

    @Column(name = "is_readwrite", nullable = false)
    private int isReadWrite;

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

    public int getDateStart() {
        return dateStart;
    }

    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
    }

    public int getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getWhoProposed() {
        return whoProposed;
    }

    public void setWhoProposed(int whoProposed) {
        this.whoProposed = whoProposed;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getIsReadWrite() {
        return isReadWrite;
    }

    public void setIsReadWrite(int isReadWrite) {
        this.isReadWrite = isReadWrite;
    }
}
