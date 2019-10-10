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
import pl.edu.prz.stud.swawro.server.Model.BpMeasure;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class BpMeasureDAO {
    private static final BpMeasureDAO _instance= new BpMeasureDAO();
    
    private BpMeasureDAO() {}
    
    public static final BpMeasureDAO getInstance() {
        return _instance;
    }

    public void add(BpMeasure bpMeasure) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ? AND bpm.bpHigh = ? AND bpm.bpLow = ? AND bpm.date = ?");
            hqlQuery.setParameter(0, bpMeasure.getUser().getId());
            hqlQuery.setParameter(1, bpMeasure.getBpHigh());
            hqlQuery.setParameter(2, bpMeasure.getBpLow());
            hqlQuery.setParameter(3, bpMeasure.getDate());
            List<BpMeasure> bpMeasuresFound = hqlQuery.list();
            if (bpMeasuresFound == null || bpMeasuresFound.isEmpty())
                session.save(bpMeasure);
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

    public List<BpMeasure> addAll(List<BpMeasure> bpMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<BpMeasure> bpMeasuresAdded = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            for (BpMeasure bpMeasure : bpMeasures) {
                Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ? AND bpm.bpHigh = ? AND bpm.bpLow = ? AND bpm.date = ?");
                hqlQuery.setParameter(0, bpMeasure.getUser().getId());
                hqlQuery.setParameter(1, bpMeasure.getBpHigh());
                hqlQuery.setParameter(2, bpMeasure.getBpLow());
                hqlQuery.setParameter(3, bpMeasure.getDate());
                List<BpMeasure> bpMeasuresFound = hqlQuery.list();
                if (bpMeasuresFound == null || bpMeasuresFound.isEmpty()) {
                    session.save(bpMeasure);
                    bpMeasuresAdded.add(bpMeasure);
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
        if (bpMeasuresAdded.isEmpty()) bpMeasuresAdded = null;
        return bpMeasuresAdded;
    }

    public List<BpMeasure> get(int userId, int defaultGet, int dateStart, int dateEnd) { //check!
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<BpMeasure> bpMeasuresFound = null;
        try {
            tx = session.beginTransaction();
            if (defaultGet==1) {
                Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ? ORDER BY bpm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setMaxResults(10);
                bpMeasuresFound = hqlQuery.list();
            }
            else {
                Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ? AND bpm.date BETWEEN ? AND ? ORDER BY bpm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setParameter(1, dateStart);
                hqlQuery.setParameter(2, dateEnd);
                bpMeasuresFound = hqlQuery.list();
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
        return bpMeasuresFound;
    }

    public void removeById(List<BpMeasure> bpMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (BpMeasure bpMeasure : bpMeasures)
                session.delete(bpMeasure);
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

    public void removeByUserId(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<BpMeasure> bpMeasures;
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ?");
            bpMeasures = hqlQuery.setParameter(0, userId).list();
            for (int i = 0; i < bpMeasures.size(); i++) {
                BpMeasure bpMeasure = bpMeasures.get(i);
                session.delete(bpMeasure);
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

    public void removeByComplex(List<BpMeasure> bpMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (BpMeasure bpMeasure : bpMeasures) {
                Query hqlQuery = session.createQuery("FROM BpMeasure as bpm WHERE bpm.user.id = ? AND bpm.bpHigh = ? AND bpm.bpLow = ? AND bpm.date = ?");
                hqlQuery.setParameter(0, bpMeasure.getUser().getId());
                hqlQuery.setParameter(1, bpMeasure.getBpHigh());
                hqlQuery.setParameter(2, bpMeasure.getBpLow());
                hqlQuery.setParameter(3, bpMeasure.getDate());
                List<BpMeasure> bpMeasuresToDelete = hqlQuery.list();
                for (int i = 0; i < bpMeasuresToDelete.size(); i++) {
                    BpMeasure bpMeasureToDelete = bpMeasuresToDelete.get(i);
                    session.delete(bpMeasureToDelete);
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
