/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.Model;

import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserDataChangeType;
import pl.edu.prz.stud.swawro.server.controller.helpers.SecurityHelper;

import javax.persistence.*;

/**
 *
 * @author Sebastian
 */

@Entity
@Table(name="userdatahistory")
public class UserDataHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "date_changed", nullable = false)
    private int dateChanged;

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

    @Column(name = "role", nullable = false)
    private int role;

    @Column(name = "is_approved", nullable = false)
    private int isApproved;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(int dateChanged) {
        this.dateChanged = dateChanged;
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

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
    
    public static UserDataHistory copyCurrent(User user, UserDataChangeType udct) {
        UserDataHistory userDataHistory = new UserDataHistory();
        userDataHistory.setDateChanged(SecurityHelper.getInstance().getCurrentTime());
        userDataHistory.setType(udct.ordinal());
        userDataHistory.setUser(user);
        userDataHistory.setName(user.getName());
        userDataHistory.setSurname(user.getSurname());
        userDataHistory.setEmail(user.getEmail());
        userDataHistory.setPhone(user.getPhone());
        userDataHistory.setRole(user.getRole());
        userDataHistory.setIsApproved(user.getIsApproved());
        return userDataHistory;
    }
}
