/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.Model;

import com.esotericsoftware.kryo.serializers.FieldSerializer;

import javax.persistence.*;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Sebastian
 */

@Entity
@Table (name="deliveredabnormals")
public class DeliveredAbnormal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.User.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(targetEntity = pl.edu.prz.stud.swawro.server.Model.Abnormal.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "abnormal_id", nullable = false)
    private Abnormal abnormal;

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

    public Abnormal getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Abnormal abnormal) {
        this.abnormal = abnormal;
    }
    
}
