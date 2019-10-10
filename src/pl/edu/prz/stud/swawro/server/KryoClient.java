package pl.edu.prz.stud.swawro.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import pl.edu.prz.stud.swawro.server.config.ServerInfo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class KryoClient {
    private static final Logger LOGGER = Logger.getLogger(KryoClient.class.getName());
    private static final KryoClient ourInstance = new KryoClient();
    private static final int BUFFER_SIZE = 512 * 1024;
    private Semaphore semi = new Semaphore(0); //semi wewnętrzne, trzeba jeszcze zewnętrzne
    private Client client;
    private String responseStr;

    private static class KryoRequest {
        public String text;
    }

    private static class KryoResponse {
        public String text;
    }

    private KryoClient(){}

    public static final KryoClient getInstance() {
        return ourInstance;
    }

    public synchronized String sendRequestToServerImproved(final String requestStr, final ServerInfo serverInfo) {
        responseStr = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KryoClient.getInstance().sendRequestToServer(requestStr, serverInfo);
                }
                catch (Exception e) {
                    responseStr = null;
                    semi.release();
                }
            }
        }).start();
        try {
            semi.acquire();
            if (responseStr == null) throw new Exception("Failed to send request to server");
        }
        catch (Exception e) {
            e.printStackTrace();
            exit(-1); //TODO: instead of exit generate error response
        }
        closeConnection();
        return responseStr;
    }

    public void sendRequestToServer(String requestStr, ServerInfo serverInfo) throws Exception {
        client = new Client(BUFFER_SIZE, BUFFER_SIZE);
        Kryo kryo = client.getKryo();
        kryo.register(KryoRequest.class);
        kryo.register(KryoResponse.class);
        client.start();
        try {
            client.connect(5000, serverInfo.getIp(), Integer.valueOf(serverInfo.getInternalPort()));
        } catch (IOException e) {
            throw new Exception("Cannot connect to internal server");
        }

        KryoRequest request = new KryoRequest();
        request.text = requestStr;
        client.sendTCP(request);
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof KryoResponse) {
                    KryoResponse response = (KryoResponse) object;
                    LOGGER.info("Client got response: " + response.text);
                    responseStr=response.text;
                    semi.release();
                }
            }
        });
    }

    public void closeConnection() {
        client.close();
    }
}

