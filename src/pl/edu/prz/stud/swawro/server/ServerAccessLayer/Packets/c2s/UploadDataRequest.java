package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

public class UploadDataRequest {
    private int packetId = 109;
    private String uuid;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private int dateBorn;
    private int role;
    private int autoSms;
    private int autoMail;

    public int getPacketId() {
        return packetId;
    }

    private void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
