/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.database;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Sebastian
 */
public class MyHibernateUtil {
    private static SessionFactory sessionFactory = null;
    private static long lastCreationTime = 0;
    
/*    private static void createSessionFactory() {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }*/

    private static void createSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("/hibernate.cfg.xml").build();
            Metadata metaData = new MetadataSources(standardRegistry)
                    .getMetadataBuilder().build();
            sessionFactory = metaData.getSessionFactoryBuilder().build();
        } catch (Throwable th) {

            System.err.println("Enitial SessionFactory creation failed" + th);

            throw new ExceptionInInitializerError(th);

        }
    }

    public static void createSessionFactory2() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
    
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.out.println("Initializing SessionFactory...");
            createSessionFactory();
            lastCreationTime = System.currentTimeMillis();
        }
        
        if (lastCreationTime < System.currentTimeMillis() - 3600000) {
            System.out.println("Refreshing SessionFactory...");
            sessionFactory.close();
            sessionFactory=null;
            createSessionFactory();
            lastCreationTime = System.currentTimeMillis();
        }
        
        return sessionFactory;
    }
}
