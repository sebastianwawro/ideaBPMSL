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
import pl.edu.prz.stud.swawro.server.Model.HrMeasure;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class HrMeasureDAO {
    private static final HrMeasureDAO _instance= new HrMeasureDAO();
    
    private HrMeasureDAO() {}
    
    public static final HrMeasureDAO getInstance() {
        return _instance;
    }

    public List<HrMeasure> addAll(List<HrMeasure> hrMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<HrMeasure> hrMeasuresAdded = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            for (HrMeasure hrMeasure : hrMeasures) {
                Query hqlQuery = session.createQuery("FROM HrMeasure as hrm WHERE hrm.user.id = ?0 AND hrm.hr = ?1 AND hrm.date = ?2");
                hqlQuery.setParameter(0, hrMeasure.getUser().getId());
                hqlQuery.setParameter(1, hrMeasure.getHr());
                hqlQuery.setParameter(2, hrMeasure.getDate());
                List<HrMeasure> hrMeasuresFound = hqlQuery.list();
                if (hrMeasuresFound == null || hrMeasuresFound.isEmpty()) {
                    session.save(hrMeasure);
                    hrMeasuresAdded.add(hrMeasure);
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
        if (hrMeasuresAdded.isEmpty()) hrMeasuresAdded = null;
        return hrMeasuresAdded;
    }

    public List<HrMeasure> get(int userId, int defaultGet, int dateStart, int dateEnd) { //check!
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<HrMeasure> hrMeasuresFound = null;
        try {
            tx = session.beginTransaction();
            if (defaultGet==1) {
                Query hqlQuery = session.createQuery("FROM HrMeasure as hrm WHERE hrm.user.id = ?0 ORDER BY hrm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setMaxResults(10);
                hrMeasuresFound = hqlQuery.list();
            }
            else {
                Query hqlQuery = session.createQuery("FROM HrMeasure as hrm WHERE hrm.user.id = ?0 AND hrm.date BETWEEN ?1 AND ?2 ORDER BY hrm.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setParameter(1, dateStart);
                hqlQuery.setParameter(2, dateEnd);
                hrMeasuresFound = hqlQuery.list();
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
        return hrMeasuresFound;
    }

    public void removeByComplex(List<HrMeasure> hrMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (HrMeasure hrMeasure : hrMeasures) {
                Query hqlQuery = session.createQuery("FROM HrMeasure as hrm WHERE hrm.user.id = ?0 AND hrm.hr = ?1 AND hrm.date = ?2");
                hqlQuery.setParameter(0, hrMeasure.getUser().getId());
                hqlQuery.setParameter(1, hrMeasure.getHr());
                hqlQuery.setParameter(2, hrMeasure.getDate());
                List<HrMeasure> hrMeasuresToDelete = hqlQuery.list();
                for (int i = 0; i < hrMeasuresToDelete.size(); i++) {
                    HrMeasure hrMeasureToDelete = hrMeasuresToDelete.get(i);
                    session.delete(hrMeasureToDelete);
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
