package pl.edu.prz.stud.swawro.server.Model;

import javax.persistence.*;
import java.util.List;

@Entity(name="User")
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "date_born", nullable = false)
    private int dateBorn;

    @Column(name = "date_registered", nullable = false)
    private int dateRegistered;

    @Column(name = "role", nullable = false)
    private int role;

    @Column(name = "is_approved", nullable = false)
    private int isApproved;

    @Column(name = "auto_sms", nullable = false)
    private int autoSms;

    @Column(name = "auto_mail", nullable = false)
    private int autoMail;

    @Column(name = "relations_uid", nullable = false)
    private int relationsUid;

    @Column(name = "user_data_uid", nullable = false)
    private int userDataUid;

    @Column(name = "settings_uid", nullable = false)
    private int settingsUid;

    //@OneToMany(targetEntity = pl.edu.prz.stud.swawro.server.Model.BpMeasure.class, fetch = FetchType.EAGER, mappedBy = "user") //bo BpMeasure ma getUser
    //private List<BpMeasure> bpMeasures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(int dateBorn) {
        this.dateBorn = dateBorn;
    }

    public int getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(int dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getAutoSms() {
        return autoSms;
    }

    public void setAutoSms(int autoSms) {
        this.autoSms = autoSms;
    }

    public int getAutoMail() {
        return autoMail;
    }

    public void setAutoMail(int autoMail) {
        this.autoMail = autoMail;
    }

    public int getRelationsUid() {
        return relationsUid;
    }

    public void setRelationsUid(int relationsUid) {
        this.relationsUid = relationsUid;
    }

    public int getUserDataUid() {
        return userDataUid;
    }

    public void setUserDataUid(int userDataUid) {
        this.userDataUid = userDataUid;
    }

    public int getSettingsUid() {
        return settingsUid;
    }

    public void setSettingsUid(int settingsUid) {
        this.settingsUid = settingsUid;
    }
}
