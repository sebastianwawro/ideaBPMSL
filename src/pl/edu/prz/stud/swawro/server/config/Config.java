package pl.edu.prz.stud.swawro.server.config;

import com.esotericsoftware.kryonet.Server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pl.edu.prz.stud.swawro.server.KryoClient;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RegisterRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RegisterResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.System.exit;
import static java.lang.System.in;

public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    public static String SERVER_MODE;
    public static String KRYO_PORT;
    public static String HTTP_PORT;
    public static String INTERNAL_PORT;
    public static long SESSION_TIME = 1200000; //TODO: SESSION 20 minutes
    public static Boolean DYNAMIC_SLAVE_REGISTRATION;
    public static ServerInfo MASTER_INFO;
    public static List<ServerInfo> SLAVES_INFO;
    public static List<SessionInfo> ALLOWED_SESSIONS;
    private static int lastUsedSlave = 0;

    private Config() {}

    public static void loadConfig () {

        try {
            SLAVES_INFO = new ArrayList<>();
            ALLOWED_SESSIONS = new ArrayList<>();
            startSessionWatcherThread();


            final Properties Settings = new Properties();
            final InputStream is = new FileInputStream(new File("Config.ini"));
            Settings.load(is);
            is.close();

            SERVER_MODE = Settings.getProperty("ServerMode", "dual");
            LOGGER.info("Config: " + "ServerMode" + "=" + SERVER_MODE);

            KRYO_PORT = Settings.getProperty("KryoPort", "8888");
            LOGGER.info("Config: " + "KryoPort" + "=" + KRYO_PORT);

            HTTP_PORT = Settings.getProperty("HttpPort", "8000");
            LOGGER.info("Config: " + "HttpPort" + "=" + HTTP_PORT);

            INTERNAL_PORT = Settings.getProperty("InternalPort", "1234");
            LOGGER.info("Config: " + "InternalPort" + "=" + INTERNAL_PORT);

            DYNAMIC_SLAVE_REGISTRATION = Boolean.valueOf(Settings.getProperty("DynamicSlaveRegistration", "false"));
            LOGGER.info("Config: " + "DynamicSlaveRegistration" + "=" + DYNAMIC_SLAVE_REGISTRATION);

            String masterInfo = Settings.getProperty("MasterConfig", "127.0.0.1:8000:8888:1234:500:100;");
            MASTER_INFO = parseServerInfo(masterInfo);
            LOGGER.info("Config: " + "MasterConfig" + "=" + masterInfo);

            if (DYNAMIC_SLAVE_REGISTRATION.equals(false)) {
                String slavesInfo = Settings.getProperty("SlavesConfig", "");
                parseSlavesInfo(slavesInfo);
            }

        }
        catch (Exception e) {e.printStackTrace(); exit(-1);}
    }

    private static ServerInfo parseServerInfo (String info) throws Exception {
        ServerInfo serverInfo = new ServerInfo();
        final StringTokenizer st = new StringTokenizer(info, ":");
        if (st.hasMoreTokens()) {
            serverInfo.setIp(st.nextToken());
        }
        else throw new Exception("Invalid info loaded");
        if (st.hasMoreTokens()) {
            serverInfo.setKryoPort(st.nextToken());
        }
        else throw new Exception("Invalid info loaded");
        if (st.hasMoreTokens()) {
            serverInfo.setHttpPort(st.nextToken());
        }
        else throw new Exception("Invalid info loaded");
        if (st.hasMoreTokens()) {
            serverInfo.setInternalPort(st.nextToken());
        }
        else throw new Exception("Invalid info loaded");
        if (st.hasMoreTokens()) {
            serverInfo.setMaxConnections(Integer.valueOf(st.nextToken()));
        }
        else throw new Exception("Invalid info loaded");
        if (st.hasMoreTokens()) {
            serverInfo.setImporance(Integer.valueOf(st.nextToken()));
        }
        else throw new Exception("Invalid info loaded");
        return serverInfo;
    }

    private static void parseSlavesInfo (String info) throws Exception {
        final StringTokenizer st = new StringTokenizer(info, ";");
        while (st.hasMoreTokens()) {
            String current = st.nextToken();
            SLAVES_INFO.add(parseServerInfo(current));
            LOGGER.info("Config: Added to slave list - " + current);
        }
    }

    private static void startSessionWatcherThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (SessionInfo info : ALLOWED_SESSIONS) {
                    if (info.getExpireTime() < System.currentTimeMillis()) {
                        ALLOWED_SESSIONS.remove(info); //TODO: send info about expired session! atm useless
                    }
                }
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e) {
                    e.printStackTrace(); //?!?
                }
            }
        }).start();
    }

    public static ServerInfo attachNewSessionToSlave(String uuid) {
        int slavesCount = SLAVES_INFO.size();
        do {
            lastUsedSlave++;
            if (lastUsedSlave >= slavesCount) {
                lastUsedSlave = 0;
            }
        } while (SLAVES_INFO.get(lastUsedSlave).getConnections() >= SLAVES_INFO.get(lastUsedSlave).getMaxConnections());
        ServerInfo myServer = SLAVES_INFO.get(lastUsedSlave);

        Gson gson = new Gson();
        RegisterSessionRequest registerSessionRequest = new RegisterSessionRequest();
        registerSessionRequest.setUuid(uuid);
        registerSessionRequest.setExpireTime(System.currentTimeMillis() + Config.SESSION_TIME);
        String request = gson.toJson(registerSessionRequest);

        String response = KryoClient.getInstance().sendRequestToServerImproved(request , myServer);
        RegisterSessionResponse registerSessionResponse = new RegisterSessionResponse();
        try {
            registerSessionResponse = gson.fromJson(response, RegisterSessionResponse.class);
            if (registerSessionResponse.getStatusCode() != 0) throw new Exception("Failed to register session"); //TODO: try register session on other slave
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        LOGGER.info("Session created!"); //TODO: impr

        return myServer;
    }
}
