/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.database.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.prz.stud.swawro.server.Model.SleepMeasure;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class SleepMeasureDAO {
    private static final SleepMeasureDAO _instance= new SleepMeasureDAO();
    
    private SleepMeasureDAO() {}
    
    public static final SleepMeasureDAO getInstance() {
        return _instance;
    }

    public List<SleepMeasure> addAll(List<SleepMeasure> sleepMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<SleepMeasure> sleepMeasuresAdded = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            for (SleepMeasure sleepMeasure : sleepMeasures) {
                Query hqlQuery = session.createQuery("FROM SleepMeasure as sm WHERE sm.user.id = ?0 AND sm.deep = ?1 AND sm.shallow = ?2 AND sm.total = ?3 AND sm.wakeupTimes = ?4 AND sm.date = ?5");
                hqlQuery.setParameter(0, sleepMeasure.getUser().getId());
                hqlQuery.setParameter(1, sleepMeasure.getDeep());
                hqlQuery.setParameter(2, sleepMeasure.getShallow());
                hqlQuery.setParameter(3, sleepMeasure.getTotal());
                hqlQuery.setParameter(4, sleepMeasure.getWakeupTimes());
                hqlQuery.setParameter(5, sleepMeasure.getDate());
                List<SleepMeasure> sleepMeasuresFound = hqlQuery.list();
                if (sleepMeasuresFound == null || sleepMeasuresFound.isEmpty()) {
                    session.save(sleepMeasure);
                    sleepMeasuresAdded.add(sleepMeasure);
                }
            }
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
        if (sleepMeasuresAdded.isEmpty()) sleepMeasuresAdded = null;
        return sleepMeasuresAdded;
    }

    public List<SleepMeasure> get(int userId, int defaultGet, int dateStart, int dateEnd) { //check!
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<SleepMeasure> sleepMeasuresFound = null;
        try {
            tx = session.beginTransaction();
            if (defaultGet==1) {
                Query hqlQuery = session.createQuery("FROM SleepMeasure as sm WHERE sm.user.id = ?0 ORDER BY sm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setMaxResults(10);
                sleepMeasuresFound = hqlQuery.list();
            }
            else {
                Query hqlQuery = session.createQuery("FROM SleepMeasure as sm WHERE sm.user.id = ?0 AND sm.date BETWEEN ?1 AND ?2 ORDER BY sm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setParameter(1, dateStart);
                hqlQuery.setParameter(2, dateEnd);
                sleepMeasuresFound = hqlQuery.list();
            }
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
        return sleepMeasuresFound;
    }

    public void removeByComplex(List<SleepMeasure> sleepMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (SleepMeasure sleepMeasure : sleepMeasures) {
                Query hqlQuery = session.createQuery("FROM SleepMeasure as sm WHERE sm.user.id = ?0 AND sm.deep = ?1 AND sm.shallow = ?2 AND sm.total = ?3 AND sm.wakeupTimes = ?4 AND sm.date = ?5");
                hqlQuery.setParameter(0, sleepMeasure.getUser().getId());
                hqlQuery.setParameter(1, sleepMeasure.getDeep());
                hqlQuery.setParameter(2, sleepMeasure.getShallow());
                hqlQuery.setParameter(3, sleepMeasure.getTotal());
                hqlQuery.setParameter(4, sleepMeasure.getWakeupTimes());
                hqlQuery.setParameter(5, sleepMeasure.getDate());
                List<SleepMeasure> sleepMeasuresToDelete = hqlQuery.list();
                for (int i = 0; i < sleepMeasuresToDelete.size(); i++) {
                    SleepMeasure sleepMeasureToDelete = sleepMeasuresToDelete.get(i);
                    session.delete(sleepMeasureToDelete);
                }
            }
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
    }
    
}
