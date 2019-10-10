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
import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class SettingsDAO {
    private static final SettingsDAO _instance= new SettingsDAO();
    
    private SettingsDAO() {}
    
    public static final SettingsDAO getInstance() {
        return _instance;
    }

    public void add(Settings settings) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Settings as st WHERE st.doctor.id = ? AND st.user.id = ?");
            hqlQuery.setParameter(0, settings.getDoctor().getId());
            hqlQuery.setParameter(1, settings.getUser().getId());
            List<Settings> settingsFound = hqlQuery.list();
            if (settingsFound == null || settingsFound.isEmpty()) //nie ma aktywnej? to dodaj
                session.save(settings);
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

    public void update(Settings settings) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(settings);
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
    
    public Settings get(int doctorId, int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Settings setting = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Settings as st WHERE st.doctor.id = ? AND st.user.id = ?");
            List<Settings> settings = hqlQuery.setParameter(0, doctorId).setParameter(1, userId).list();
            if (settings == null || settings.isEmpty())
                settings = null;
            else setting = settings.get(0);
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
        return setting;
    }
    
    public List<Settings> getAllForDoctorId(int doctorId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Settings> settings = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Settings as st WHERE st.doctor.id = ?");
            settings = hqlQuery.setParameter(0, doctorId).list();
            if (settings == null || settings.isEmpty())
                settings = null;
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
        return settings;
    }
    
    public List<Settings> getAllForPatient(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Settings> settings = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Settings as st WHERE st.user.id = ?");
            settings = hqlQuery.setParameter(0, userId).list();
            if (settings == null || settings.isEmpty())
                settings = null;
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
        return settings;
    }
    
    public List<Settings> getAllForUserId(int userId) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Settings> settings = null;

        try {
            tx = session.beginTransaction();
            Query hqlQuery = session.createQuery("FROM Settings as st WHERE st.user.id = ? OR st.doctor.id = ?");
            settings = hqlQuery.setParameter(0, userId).setParameter(1, userId).list();
            if (settings == null || settings.isEmpty())
                settings = null;
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
        return settings;
    }
    
    public void remove(Settings set) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(set);
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
