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
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class RelationDAO {
    private static final RelationDAO _instance= new RelationDAO();
    
    private RelationDAO() {}
    
    public static final RelationDAO getInstance() {
        return _instance;
    }

    public void add(Relation relation) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Relation as rel WHERE rel.doctor.id = ?0 AND rel.user.id = ?1 AND rel.isActive = 1");
            hqlQuery.setParameter(0, relation.getDoctor().getId());
            hqlQuery.setParameter(1, relation.getUser().getId());
            List<Relation> relationsFound = hqlQuery.list();
            if (relationsFound == null || relationsFound.isEmpty()) //nie ma aktywnej? to dodaj
                session.save(relation);
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

    public void update(Relation relation) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(relation);
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
    
    public Relation get(int doctorId, int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Relation relation = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Relation as rel WHERE rel.isActive = 1 AND rel.doctor.id = ?0 AND rel.user.id = ?1");
            List<Relation> relations = hqlQuery.setParameter(0, doctorId).setParameter(1, userId).list();
            if (relations == null || relations.isEmpty()) {
                relations = null;
            }
            else relation = relations.get(0);
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
        return relation;
    }
    
    public List<Relation> getAllForUserId(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Relation> relations = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Relation as rel WHERE rel.isActive = 1 AND (rel.doctor.id = ?0 OR rel.user.id = ?1)");
            relations = hqlQuery.setParameter(0, userId).setParameter(1, userId).list();
            if (relations == null || relations.isEmpty()) {
                relations = null;
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
        return relations;
    }
    
    public List<Relation> getAllForPatient(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Relation> relations = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Relation as rel WHERE rel.isActive = 1 AND rel.user.id = ?0");
            relations = hqlQuery.setParameter(0, userId).list();
            if (relations == null || relations.isEmpty()) {
                relations = null;
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
        return relations;
    }
    
}
