package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

public class SmartSearchTO {
    private int userId;
    private String login;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private int role;
    private int isMyDoctor;
    private int isMyDoctorApproved;
    private int doctorType;
    private int isReadWriteDoctor;
    private int isReadWriteDoctorApproved;
    private int isMyPatient;
    private int isMyPatientApproved;
    private int myselfType;
    private int isReadWritePatient;
    private int isReadWritePatientApproved;
    private int isApproved;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getIsMyDoctor() {
        return isMyDoctor;
    }

    public void setIsMyDoctor(int isMyDoctor) {
        this.isMyDoctor = isMyDoctor;
    }

    public int getIsMyDoctorApproved() {
        return isMyDoctorApproved;
    }

    public void setIsMyDoctorApproved(int isMyDoctorApproved) {
        this.isMyDoctorApproved = isMyDoctorApproved;
    }

    public int getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(int doctorType) {
        this.doctorType = doctorType;
    }

    public int getIsReadWriteDoctor() {
        return isReadWriteDoctor;
    }

    public void setIsReadWriteDoctor(int isReadWriteDoctor) {
        this.isReadWriteDoctor = isReadWriteDoctor;
    }

    public int getIsReadWriteDoctorApproved() {
        return isReadWriteDoctorApproved;
    }

    public void setIsReadWriteDoctorApproved(int isReadWriteDoctorApproved) {
        this.isReadWriteDoctorApproved = isReadWriteDoctorApproved;
    }

    public int getIsMyPatient() {
        return isMyPatient;
    }

    public void setIsMyPatient(int isMyPatient) {
        this.isMyPatient = isMyPatient;
    }

    public int getIsMyPatientApproved() {
        return isMyPatientApproved;
    }

    public void setIsMyPatientApproved(int isMyPatientApproved) {
        this.isMyPatientApproved = isMyPatientApproved;
    }

    public int getMyselfType() {
        return myselfType;
    }

    public void setMyselfType(int myselfType) {
        this.myselfType = myselfType;
    }

    public int getIsReadWritePatient() {
        return isReadWritePatient;
    }

    public void setIsReadWritePatient(int isReadWritePatient) {
        this.isReadWritePatient = isReadWritePatient;
    }

    public int getIsReadWritePatientApproved() {
        return isReadWritePatientApproved;
    }

    public void setIsReadWritePatientApproved(int isReadWritePatientApproved) {
        this.isReadWritePatientApproved = isReadWritePatientApproved;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
}
