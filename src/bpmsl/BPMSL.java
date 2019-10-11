/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmsl;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import pl.edu.prz.stud.swawro.server.KryoInternalServer;
import pl.edu.prz.stud.swawro.server.KryoServer;
import pl.edu.prz.stud.swawro.server.SimpleHttpServer;
import pl.edu.prz.stud.swawro.server.controller.PacketProcessor;
import pl.edu.prz.stud.swawro.server.database.MyHibernateUtil;
import pl.edu.prz.stud.swawro.server.config.Config;

import static java.lang.System.exit;

/**
 *
 * @author Sebastian
 * TODO: hashmap redirection
 * TODO: load sessions on start (only active & it needs to know to which server session belongs)
 */
public class BPMSL {
    private static final Logger LOGGER = Logger.getLogger(BPMSL.class.getName());
    private static Semaphore kryoSemi = new Semaphore(1);
    private static Semaphore kryoIntSemi = new Semaphore(1);
    private static Semaphore servSemi = new Semaphore(1);

    public static void startWebServer() {
        if (servSemi.tryAcquire())  {
            Thread thread = new Thread(){
                public void run(){
                    try {
                        SimpleHttpServer.startServer();
                        System.out.println("Http server is listening on :" + Config.HTTP_PORT);
                    }
                    catch (Exception e) {
                        System.out.println("Cannot start http server!");
                    }
                }
            };
            thread.start();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Loading Config...");
        Config.loadConfig();
        LOGGER.info("Config loaded!");
        //exit(0);

        LOGGER.info("Loading Routes...");
        PacketProcessor.getInstance();
        LOGGER.info("Routes loaded!");

        LOGGER.info("Preparing Session Factory...");
        if (MyHibernateUtil.getSessionFactory() != null) {
            LOGGER.info("Session Factory is ready!");
        }

        BPMSL.startWebServer();
        
        if (kryoSemi.tryAcquire()) KryoServer.getInstance().startKryoServer();
        if (kryoIntSemi.tryAcquire()) KryoInternalServer.getInstance().startKryoServer();
        //bufferSize, myhibutil do drugiej przenies
    }
    
}
