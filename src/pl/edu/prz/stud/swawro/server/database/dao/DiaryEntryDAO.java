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
import pl.edu.prz.stud.swawro.server.Model.DiaryEntry;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;

/**
 *
 * @author Sebastian
 */
public class DiaryEntryDAO {
    private static final DiaryEntryDAO _instance= new DiaryEntryDAO();
    
    private DiaryEntryDAO() {}
    
    public static final DiaryEntryDAO getInstance() {
        return _instance;
    }

    public List<DiaryEntry> addAll(List<DiaryEntry> diaryEntries) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<DiaryEntry> diaryEntriesAdded = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            for (DiaryEntry diaryEntry : diaryEntries) {
                Query hqlQuery = session.createQuery("FROM DiaryEntry as de WHERE de.user.id = ? AND de.description = ? AND de.date = ?");
                hqlQuery.setParameter(0, diaryEntry.getUser().getId());
                hqlQuery.setParameter(1, diaryEntry.getDescription());
                hqlQuery.setParameter(2, diaryEntry.getDate());
                List<DiaryEntry> diaryEntriesFound = hqlQuery.list();
                if (diaryEntriesFound == null || diaryEntriesFound.isEmpty()) {
                    session.save(diaryEntry);
                    diaryEntriesAdded.add(diaryEntry);
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
        if (diaryEntriesAdded.isEmpty()) diaryEntriesAdded = null;
        return diaryEntriesAdded;
    }

    public List<DiaryEntry> get(int userId, int defaultGet, int dateStart, int dateEnd) { //check!
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<DiaryEntry> diaryEntriesFound = null;
        try {
            tx = session.beginTransaction();
            if (defaultGet==1) {
                Query hqlQuery = session.createQuery("FROM DiaryEntry as de WHERE de.user.id = ? ORDER BY de.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setMaxResults(10);
                diaryEntriesFound = hqlQuery.list();
            }
            else {
                Query hqlQuery = session.createQuery("FROM DiaryEntry as de WHERE de.user.id = ? AND de.date BETWEEN ? AND ? ORDER BY de.date desc");
                hqlQuery.setParameter(0, userId);
                hqlQuery.setParameter(1, dateStart);
                hqlQuery.setParameter(2, dateEnd);
                diaryEntriesFound = hqlQuery.list();
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
        return diaryEntriesFound;
    }

    public void removeByComplex(List<DiaryEntry> diaryEntries) {
        Session session = MyHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (DiaryEntry diaryEntry : diaryEntries) {
                Query hqlQuery = session.createQuery("FROM DiaryEntry as de WHERE de.user.id = ? AND de.description = ? AND de.date = ?");
                hqlQuery.setParameter(0, diaryEntry.getUser().getId());
                hqlQuery.setParameter(1, diaryEntry.getDescription());
                hqlQuery.setParameter(2, diaryEntry.getDate());
                List<DiaryEntry> diaryEntriesToDelete = hqlQuery.list();
                for (int i = 0; i < diaryEntriesToDelete.size(); i++) {
                    DiaryEntry diaryEntryToDelete = diaryEntriesToDelete.get(i);
                    session.delete(diaryEntryToDelete);
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
