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
import pl.edu.prz.stud.swawro.server.Model.Abnormal;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class AbnormalDAO {
    private static final AbnormalDAO _instance= new AbnormalDAO();
    
    private AbnormalDAO() {}
    
    public static final AbnormalDAO getInstance() {
        return _instance;
    }

    public void add(Abnormal abnormal) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(abnormal);
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

    public void addAll(List<Abnormal> abnormals) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Abnormal abnormal : abnormals)
                session.save(abnormal);
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
    
    public List<Abnormal> getAll(int userId, int doctorId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Abnormal> abnormals = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Abnormal as ab WHERE ab.user.id = ? AND ab.doctor.id = ?");
            abnormals = hqlQuery.setParameter(0, userId).setParameter(1, doctorId).list();
            if (abnormals == null || abnormals.isEmpty())
                abnormals = null;
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
        return abnormals;
    }
    
    public List<Abnormal> getUndelivered(int userId, int doctorId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Abnormal> abnormals = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Abnormal as ab WHERE ab.user.id = ? AND ab.doctor.id = ? AND ab.id NOT IN (SELECT da.abnormal.id FROM DeliveredAbnormal as da WHERE da.doctor.id = ?)"); //where ab.id not in select da.ab.id where doctor.id=?
            abnormals = hqlQuery.setParameter(0, userId).setParameter(1, doctorId).setParameter(2, doctorId).list();
            if (abnormals == null || abnormals.isEmpty())
                abnormals = null;
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
        return abnormals;
    }
    
    public List<Abnormal> getUndelivered(int doctorId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Abnormal> abnormals = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Abnormal as ab WHERE ab.doctor.id = ? AND ab.id NOT IN (SELECT da.abnormal.id FROM DeliveredAbnormal as da WHERE da.doctor.id = ?)"); //where ab.id not in select da.ab.id where doctor.id=?
            abnormals = hqlQuery.setParameter(0, doctorId).setParameter(1, doctorId).list();
            if (abnormals == null || abnormals.isEmpty())
                abnormals = null;
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
        return abnormals;
    }
    
}
