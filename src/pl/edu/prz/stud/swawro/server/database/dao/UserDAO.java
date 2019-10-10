/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.database.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Sebastian
 */
public class UserDAO {
    private static final UserDAO _instance= new UserDAO();
    
    private UserDAO() {}
    
    public static final UserDAO getInstance() {
        return _instance;
    }

    public void add(User user) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(user);
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

    public void addAll(List<User> users) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (int i=0; i<users.size(); i++) {
                session.save(users.get(i));
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

    public void removeById(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        //List<User> users = null;
        User user = null;
        try {
            tx = session.beginTransaction();
            user = (User)session.get(User.class, userId);
            session.delete(user);
            /*
            Query hqlQuery = session.createQuery("FROM User as user WHERE user.id = ?");
            users = hqlQuery.setParameter(0, userId).list();
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                session.delete(user);
            }
            */
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

    public List<User> getAll() {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<User> users = null;
        try {
            tx = session.beginTransaction();
            users = session.createQuery("FROM User").list();
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
        return users;
    }
    
    public User auth(String login, String password) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        User user = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM User as user WHERE user.login = ?0 AND user.password = ?1");
            List<User> users = hqlQuery.setParameter(0, login).setParameter(1, password).list();
            if (users == null || users.isEmpty()) {
                user = null;
            }
            else {
                user = users.get(0);
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
        return user;
    }
    
    public User getByLogin(String login) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        User user = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM User as user WHERE user.login = ?0");
            List<User> users = hqlQuery.setParameter(0, login).list();
            if (users == null || users.isEmpty()) {
                user = null;
            }
            else {
                user = users.get(0);
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
        return user;
    }
    
    public List<User> getBySmart(String[] smart) { //NIE WIADOMO CZY OR
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<User> users = null;

        try {
            tx = session.beginTransaction();
            Criteria query = session.createCriteria(User.class); //TODO: depracted
            //List<Criterion> crits = new ArrayList<>();
            Criterion[] crits = new Criterion[smart.length*3];
            int j=0;
            for (int i=0; i<smart.length; i++) {
                Criterion x = Restrictions.like("login", smart[i], MatchMode.ANYWHERE);
                Criterion y = Restrictions.like("name", smart[i], MatchMode.ANYWHERE);
                Criterion z = Restrictions.like("surname", smart[i], MatchMode.ANYWHERE);
                crits[j++] = x;
                crits[j++] = y;
                crits[j++] = z;
            }
            query.add(Restrictions.or(crits)); //(Criterion[])crits.toArray()
            //Query hqlQuery = session.createQuery("FROM User as user WHERE user.login in (:logins) OR user.name in (:names) OR user.surname IN (:surnames)");
            //users = hqlQuery.setParameterList("logins", smart).list();
            users = query.list();
            if (users == null || users.isEmpty()) {
                users = null;
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
        return users;
    }
    
    public User getById(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        User user = null;

        try {
            tx = session.beginTransaction();
            user = (User)session.get(User.class, userId);
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
        return user;
    }
    
    public void update(User user) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(user);
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
