/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ResponsePacket;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.UuidCheck;
import pl.edu.prz.stud.swawro.server.config.Config;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

/**
 *
 * @author Sebastian
 */
public class PacketProcessor {
    private static final PacketProcessor _instance = new PacketProcessor();
    private Map <Integer,PacketRoute> packetRouteMap;
    
    private PacketProcessor(){
        packetRouteMap = new HashMap<>();
        try {
            if (Config.SERVER_MODE.equals("master") || Config.SERVER_MODE.equals("dual")) {
                //UsersController packets
                packetRouteMap.put(101, new PacketRoute(UsersController.class, "register"));
                packetRouteMap.put(102, new PacketRoute(UsersController.class, "logIn"));
                packetRouteMap.put(103, new PacketRoute(UsersController.class, "logOut"));
                packetRouteMap.put(113, new PacketRoute(UsersController.class, "logOutOtherDevices"));
            }
            if (Config.SERVER_MODE.equals("slave") || Config.SERVER_MODE.equals("dual")) {
                //UsersController packets
                packetRouteMap.put(104, new PacketRoute(UsersController.class, "smartSearch"));
                packetRouteMap.put(105, new PacketRoute(UsersController.class, "getUserInfo"));
                packetRouteMap.put(106, new PacketRoute(UsersController.class, "syncWithServer"));
                packetRouteMap.put(107, new PacketRoute(UsersController.class, "syncRelations"));
                packetRouteMap.put(108, new PacketRoute(UsersController.class, "syncData"));
                packetRouteMap.put(109, new PacketRoute(UsersController.class, "uploadData"));
                packetRouteMap.put(110, new PacketRoute(UsersController.class, "syncSettings"));
                packetRouteMap.put(111, new PacketRoute(UsersController.class, "uploadSettings"));
                packetRouteMap.put(112, new PacketRoute(UsersController.class, "changePassword"));
                //RelationsController packets
                packetRouteMap.put(201, new PacketRoute(RelationsController.class, "approveDoctor"));
                packetRouteMap.put(202, new PacketRoute(RelationsController.class, "addAsDoctor"));
                packetRouteMap.put(203, new PacketRoute(RelationsController.class, "addAsPatient"));
                packetRouteMap.put(204, new PacketRoute(RelationsController.class, "acceptAsDoctor"));
                packetRouteMap.put(205, new PacketRoute(RelationsController.class, "acceptAsPatient"));
                packetRouteMap.put(206, new PacketRoute(RelationsController.class, "removeDoctor"));
                packetRouteMap.put(207, new PacketRoute(RelationsController.class, "removePatient"));
                //DataController packets
                packetRouteMap.put(301, new PacketRoute(DataController.class, "uploadBpMeasure"));
                packetRouteMap.put(302, new PacketRoute(DataController.class, "syncBpMeasure"));
                packetRouteMap.put(303, new PacketRoute(DataController.class, "removeBpMeasure"));
                packetRouteMap.put(304, new PacketRoute(DataController.class, "uploadHrMeasure"));
                packetRouteMap.put(305, new PacketRoute(DataController.class, "syncHrMeasure"));
                packetRouteMap.put(306, new PacketRoute(DataController.class, "removeHrMeasure"));
                packetRouteMap.put(307, new PacketRoute(DataController.class, "uploadOxyMeasure"));
                packetRouteMap.put(308, new PacketRoute(DataController.class, "syncOxyMeasure"));
                packetRouteMap.put(309, new PacketRoute(DataController.class, "removeOxyMeasure"));
                packetRouteMap.put(310, new PacketRoute(DataController.class, "uploadSleepMeasure"));
                packetRouteMap.put(311, new PacketRoute(DataController.class, "syncSleepMeasure"));
                packetRouteMap.put(312, new PacketRoute(DataController.class, "removeSleepMeasure"));
                packetRouteMap.put(313, new PacketRoute(DataController.class, "uploadDiaryEntry"));
                packetRouteMap.put(314, new PacketRoute(DataController.class, "syncDiaryEntry"));
                packetRouteMap.put(315, new PacketRoute(DataController.class, "removeDiaryEntry"));
            }
            //InternalController packets
            packetRouteMap.put(901, new PacketRoute(InternalController.class, "registerSession"));
            packetRouteMap.put(902, new PacketRoute(InternalController.class, "infoSessionExpired"));
            packetRouteMap.put(903, new PacketRoute(InternalController.class, "registerSlave"));
            packetRouteMap.put(904, new PacketRoute(InternalController.class, "removeSlave"));
        }
        catch (Exception e) {
            e.printStackTrace();
            exit(-1);
        }
    }
    
    public static final PacketProcessor getInstance() {
        return _instance;
    }

    public synchronized String processInternalPacket(String packet, int packetId){
        String response = null;
        Gson gson = new Gson();
        PacketRoute packetRoute = null;
        if (packetId > 900) packetRoute = packetRouteMap.get(packetId);
        if (packetRoute == null) {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
            response = gson.toJson(responsePacket);
        }
        else {
            try {
                response = packetRoute.tryInvoke(packet);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                try {
                    throw new Exception("Processing error");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                ResponsePacket responsePacket = new ResponsePacket();
                responsePacket.setStatusCode(ErrorCode.PROCESSING_ERROR.getErrorCode());
                response = gson.toJson(responsePacket);
            }
        }
        return response;
    }

    private int checkSession(String packet) {
        Gson gson = new Gson();
        UuidCheck uuidCheck = null;

        try {
            uuidCheck = gson.fromJson(packet, UuidCheck.class);
        }
        catch (JsonSyntaxException e) {
            return -1;
        }

        if (!Config.doesSessionExist(uuidCheck.getUuid())) {
            return -2;
        }

        return 0;
    }

    public String processPacket(String packet, int packetId) {
        if (Config.USE_OLD_PACKET_PROCESSOR)
            return processPacketOld(packet, packetId);
        else
            return processPacketNew(packet, packetId);
    }

    public synchronized String processPacketNew(String packet, int packetId){
        //TODO: if slave then check if session exists!
        String response = null;
        Gson gson = new Gson();
        PacketRoute packetRoute = null;
        if (packetId < 900) packetRoute = packetRouteMap.get(packetId);

        if (Config.SERVER_MODE.equals("slave") && checkSession(packet) < 0) {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.NOT_LOGGED_IN.getErrorCode());
            response = gson.toJson(responsePacket);
            return response;
        }

        if (packetRoute == null) {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
            response = gson.toJson(responsePacket);
        }
        else {
            try {
                response = packetRoute.tryInvoke(packet);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                try {
                    throw new Exception("Processing error");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                ResponsePacket responsePacket = new ResponsePacket();
                responsePacket.setStatusCode(ErrorCode.PROCESSING_ERROR.getErrorCode());
                response = gson.toJson(responsePacket);
            }
        }
        return response;
    }
    
    public synchronized String processPacketOld(String packet, int packetId){
        String response = null;
        Gson gson = new Gson();

        if (Config.SERVER_MODE.equals("slave") && checkSession(packet) < 0) {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.NOT_LOGGED_IN.getErrorCode());
            response = gson.toJson(responsePacket);
            return response;
        }

        if (packetId > 100 && packetId < 200) {
            UsersController usersController = UsersController.getInstance();
            switch (packetId) {
                case 101:
                    response = usersController.register(packet);
                    break;
                case 102:
                    response = usersController.logIn(packet);
                    break;
                case 103:
                    response = usersController.logOut(packet);
                    break;
                case 104:
                    response = usersController.smartSearch(packet);
                    break;
                case 105:
                    response = usersController.getUserInfo(packet);
                    break;
                case 106:
                    response = usersController.syncWithServer(packet);
                    break;
                case 107:
                    response = usersController.syncRelations(packet);
                    break;
                case 108:
                    response = usersController.syncData(packet);
                    break;
                case 109:
                    response = usersController.uploadData(packet);
                    break;
                case 110:
                    response = usersController.syncSettings(packet);
                    break;
                case 111:
                    response = usersController.uploadSettings(packet);
                    break;
                case 112:
                    response = usersController.changePassword(packet);
                    break;
                case 113:
                    response = usersController.logOutOtherDevices(packet);
                    break;
                default:
                    ResponsePacket responsePacket = new ResponsePacket();
                    responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
                    response = gson.toJson(responsePacket);
                    break;
            }
        }
        else if (packetId > 200 && packetId < 300) {
            RelationsController relationsController = RelationsController.getInstance();
            switch (packetId) {
                case 201:
                    response = relationsController.approveDoctor(packet);
                    break;
                case 202:
                    response = relationsController.addAsDoctor(packet);
                    break;
                case 203:
                    response = relationsController.addAsPatient(packet);
                    break;
                case 204:
                    response = relationsController.acceptAsDoctor(packet);
                    break;
                case 205:
                    response = relationsController.acceptAsPatient(packet);
                    break;
                case 206:
                    response = relationsController.removeDoctor(packet);
                    break;
                case 207:
                    response = relationsController.removePatient(packet);
                    break;
                default:
                    ResponsePacket responsePacket = new ResponsePacket();
                    responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
                    response = gson.toJson(responsePacket);
                    break;
            }
        }
        else if (packetId > 300 && packetId < 400) {
            DataController dataController = DataController.getInstance();
            switch (packetId) {
                case 301:
                    response = dataController.uploadBpMeasure(packet);
                    break;
                case 302:
                    response = dataController.syncBpMeasure(packet);
                    break;
                case 303:
                    response = dataController.removeBpMeasure(packet);
                    break;
                case 304:
                    response = dataController.uploadHrMeasure(packet);
                    break;
                case 305:
                    response = dataController.syncHrMeasure(packet);
                    break;
                case 306:
                    response = dataController.removeHrMeasure(packet);
                    break;
                case 307:
                    response = dataController.uploadOxyMeasure(packet);
                    break;
                case 308:
                    response = dataController.syncOxyMeasure(packet);
                    break;
                case 309:
                    response = dataController.removeOxyMeasure(packet);
                    break;
                case 310:
                    response = dataController.uploadSleepMeasure(packet);
                    break;
                case 311:
                    response = dataController.syncSleepMeasure(packet);
                    break;
                case 312:
                    response = dataController.removeSleepMeasure(packet);
                    break;
                case 313:
                    response = dataController.uploadDiaryEntry(packet);
                    break;
                case 314:
                    response = dataController.syncDiaryEntry(packet);
                    break;
                case 315:
                    response = dataController.removeDiaryEntry(packet);
                    break;
                default:
                    ResponsePacket responsePacket = new ResponsePacket();
                    responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
                    response = gson.toJson(responsePacket);
                    break;
            }
        }
        else {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.NONEXISTENT_PACKET.getErrorCode());
            response = gson.toJson(responsePacket);
        }
        if (response == null) {
            ResponsePacket responsePacket = new ResponsePacket();
            responsePacket.setStatusCode(ErrorCode.PROCESSING_ERROR.getErrorCode());
            response = gson.toJson(responsePacket);
        }
        return response;
    }
    
}
