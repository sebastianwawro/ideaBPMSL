package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to;

import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

public class RelationTO {
    private int doctorId;
    private int userId;
    private String login;
    private int dateStart;
    private int dateEnd;
    private int type;
    private int isActive;
    private int whoProposed;
    private int isApproved;
    private int isReadWrite;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public static RelationTO encode(Relation relation) {
        RelationTO relationTO = new RelationTO();
        relationTO.setDoctorId(relation.getDoctor().getId());
        relationTO.setUserId(relation.getUser().getId());
        relationTO.setDateStart(relation.getDateStart());
        relationTO.setDateEnd(relation.getDateEnd());
        relationTO.setType(relation.getType());
        relationTO.setIsActive(relation.getIsActive());
        relationTO.setWhoProposed(relation.getWhoProposed());
        relationTO.setIsApproved(relation.getIsApproved());
        relationTO.setIsReadWrite(relation.getIsReadWrite());
        return relationTO;
    }

    public Relation decode () {
        Relation relation = new Relation();
        relation.setDoctor(UserDAO.getInstance().getById(doctorId));
        relation.setUser(UserDAO.getInstance().getById(userId));
        relation.setDateStart(dateStart);
        relation.setDateEnd(dateEnd);
        relation.setType(type);
        relation.setIsActive(isActive);
        relation.setWhoProposed(whoProposed);
        relation.setIsApproved(isApproved);
        relation.setIsReadWrite(isReadWrite);
        return relation;
    }
}
