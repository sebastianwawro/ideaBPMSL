/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.RequestPacket;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ResponsePacket;
import pl.edu.prz.stud.swawro.server.config.Config;
import pl.edu.prz.stud.swawro.server.controller.PacketProcessor;

import java.io.IOException;

/**
 *
 * @author Sebastian
 */
public class KryoInternalServer {
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static class KryoRequest {
        public String text;
    }
    private static class KryoResponse {
        public String text;
    }
    private final static KryoInternalServer _instance = new KryoInternalServer();
    private Server server;

    private KryoInternalServer() {
        //startKryoServer();
    }
    
    public final static KryoInternalServer getInstance() {
        return _instance;
    }

    public void startKryoServer() {
        server = new Server(BUFFER_SIZE, BUFFER_SIZE);
        Kryo kryo = server.getKryo();
        kryo.register(KryoRequest.class);
        kryo.register(KryoResponse.class);
        server.start();
        try {
            server.bind(Integer.valueOf(Config.INTERNAL_PORT));
            System.out.println("Kryo internal server is listening on :" + Config.INTERNAL_PORT);
        }
        catch (IOException e) {
            System.out.println(e.toString());
            System.exit(-1);
        }
        
        server.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
               if (object instanceof KryoRequest) {
                  KryoRequest request = (KryoRequest)object;
                  System.out.println("KryoServer got request: " + request.text);

                  String responseStr = parseRequest(request.text);
                  System.out.println("KryoServer send response: " + responseStr);
                  
                  KryoResponse response = new KryoResponse();
                  response.text = responseStr;
                  connection.sendTCP(response);
               }
            }
        });
    }
    
    private String parseRequest(String request) {
        String response = null;
        
        Gson gson = new Gson();
        RequestPacket requestPacket = new RequestPacket();
        ResponsePacket responsePacket = new ResponsePacket();
        try {
            //request = unpackString(request);
            requestPacket = gson.fromJson(request, RequestPacket.class);
            if (requestPacket.getPacketId()==0) throw new JsonSyntaxException("Inconsistent data");
            
            //parsowanie stringa
            response = PacketProcessor.getInstance().processInternalPacket(request, requestPacket.getPacketId());
        }
        catch (Exception e) {
            responsePacket.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            System.out.println(e.toString());
        }
        if(response == null) response = gson.toJson(responsePacket);
        
        return response;
    }
    
}

