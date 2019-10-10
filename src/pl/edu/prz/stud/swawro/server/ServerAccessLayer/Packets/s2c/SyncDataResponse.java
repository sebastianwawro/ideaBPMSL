package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

public class SyncDataResponse {
    private int statusCode;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private int dateBorn;
    private int role;
    private int dateRegistered;
    private int isApproved;
    private int autoSms;
    private int autoMail;
    private int userId; 

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(int dateRegistered) {
        this.dateRegistered = dateRegistered;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
