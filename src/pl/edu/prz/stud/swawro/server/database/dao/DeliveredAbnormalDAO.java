/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.database.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.prz.stud.swawro.server.Model.DeliveredAbnormal;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class DeliveredAbnormalDAO {
    private static final DeliveredAbnormalDAO _instance= new DeliveredAbnormalDAO();
    
    private DeliveredAbnormalDAO() {}
    
    public static final DeliveredAbnormalDAO getInstance() {
        return _instance;
    }

    public void add(DeliveredAbnormal deliveredAbnormal) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(deliveredAbnormal);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            //session.close(); //finally powinno się wykonanć nawet przy throw
            throw e;
        } finally {
            session.close();
        }
    }

    public void addAll(List<DeliveredAbnormal> deliveredAbnormals) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (DeliveredAbnormal deliveredAbnormal : deliveredAbnormals)
                session.save(deliveredAbnormal);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            //session.close(); //finally powinno się wykonanć nawet przy throw
            throw e;
        } finally {
            session.close();
        }
    }
    
    public List<DeliveredAbnormal> getByDoctorId(int doctorId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<DeliveredAbnormal> deliveredAbnormals = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM DeliveredAbnormal as da WHERE da.doctor.id = ?");
            deliveredAbnormals = hqlQuery.setParameter(0, doctorId).list();
            if (deliveredAbnormals == null || deliveredAbnormals.isEmpty())
                deliveredAbnormals = null;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
        return deliveredAbnormals;
    }
    
}
