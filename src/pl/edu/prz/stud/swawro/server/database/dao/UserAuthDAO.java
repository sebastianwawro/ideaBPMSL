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
import pl.edu.prz.stud.swawro.server.Model.UserAuth;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class UserAuthDAO {
    private static final UserAuthDAO _instance= new UserAuthDAO();
    
    private UserAuthDAO() {}
    
    public static final UserAuthDAO getInstance() {
        return _instance;
    }

    public void add(UserAuth userAuth) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(userAuth);
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
    
    public UserAuth getByUuid(String uuid) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserAuth userAuth = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM UserAuth as ua WHERE ua.uuid = ?0");
            List<UserAuth> userAuths = hqlQuery.setParameter(0, uuid).list();
            if (userAuths == null || userAuths.isEmpty()) {
                userAuth = null;
            }
            else {
                userAuth = userAuths.get(0);
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
        return userAuth;
    }
    
    public void removeByUuid(String uuid) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM UserAuth as ua WHERE ua.uuid = ?0");
            List<UserAuth> userAuths = hqlQuery.setParameter(0, uuid).list();
            for (UserAuth userAuth : userAuths) {
                session.delete(userAuth);
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
    
    public void removeOtherByUuid(int userId, String uuid) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM UserAuth as ua WHERE ua.user.id = ?0 AND ua.uuid != ?1");
            List<UserAuth> userAuths = hqlQuery.setParameter(0, userId).setParameter(1, uuid).list();
            for (UserAuth userAuth : userAuths) {
                session.delete(userAuth);
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
    
    public void removeByUserId(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM UserAuth as ua WHERE ua.user.id = ?0");
            List<UserAuth> userAuths = hqlQuery.setParameter(0, userId).list();
            for (UserAuth userAuth : userAuths) {
                session.delete(userAuth);
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
