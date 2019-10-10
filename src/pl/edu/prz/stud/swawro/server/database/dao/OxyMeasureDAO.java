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
import pl.edu.prz.stud.swawro.server.Model.OxyMeasure;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class OxyMeasureDAO {
    private static final OxyMeasureDAO _instance= new OxyMeasureDAO();
    
    private OxyMeasureDAO() {}
    
    public static final OxyMeasureDAO getInstance() {
        return _instance;
    }

    public List<OxyMeasure> addAll(List<OxyMeasure> oxyMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<OxyMeasure> oxyMeasuresAdded = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            for (OxyMeasure oxyMeasure : oxyMeasures) {
                Query hqlQuery = session.createQuery("FROM OxyMeasure as om WHERE om.user.id = ? AND om.oxy = ? AND om.date = ?");
                hqlQuery.setParameter(0, oxyMeasure.getUser().getId());
                hqlQuery.setParameter(1, oxyMeasure.getOxy());
                hqlQuery.setParameter(2, oxyMeasure.getDate());
                List<OxyMeasure> oxyMeasuresFound = hqlQuery.list();
                if (oxyMeasuresFound == null || oxyMeasuresFound.isEmpty()) {
                    session.save(oxyMeasure);
                    oxyMeasuresAdded.add(oxyMeasure);
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
        if (oxyMeasuresAdded.isEmpty()) oxyMeasuresAdded = null;
        return oxyMeasuresAdded;
    }

    public List<OxyMeasure> get(int userId, int defaultGet, int dateStart, int dateEnd) { //check!
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<OxyMeasure> oxyMeasuresFound = null;
        try {
            tx = session.beginTransaction();
            if (defaultGet==1) {
                Query hqlQuery = session.createQuery("FROM OxyMeasure as om WHERE om.user.id = ? ORDER BY om.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setMaxResults(10);
                oxyMeasuresFound = hqlQuery.list();
            }
            else {
                Query hqlQuery = session.createQuery("FROM OxyMeasure as om WHERE om.user.id = ? AND om.date BETWEEN ? AND ? ORDER BY om.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setParameter(1, dateStart);
                hqlQuery.setParameter(2, dateEnd);
                oxyMeasuresFound = hqlQuery.list();
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
        return oxyMeasuresFound;
    }

    public void removeByComplex(List<OxyMeasure> oxyMeasures) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (OxyMeasure oxyMeasure : oxyMeasures) {
                Query hqlQuery = session.createQuery("FROM OxyMeasure as om WHERE om.user.id = ? AND om.oxy = ? AND om.date = ?");
                hqlQuery.setParameter(0, oxyMeasure.getUser().getId());
                hqlQuery.setParameter(1, oxyMeasure.getOxy());
                hqlQuery.setParameter(2, oxyMeasure.getDate());
                List<OxyMeasure> oxyMeasuresToDelete = hqlQuery.list();
                for (int i = 0; i < oxyMeasuresToDelete.size(); i++) {
                    OxyMeasure oxyMeasureToDelete = oxyMeasuresToDelete.get(i);
                    session.delete(oxyMeasureToDelete);
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
