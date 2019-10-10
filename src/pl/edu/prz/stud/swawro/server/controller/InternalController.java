package pl.edu.prz.stud.swawro.server.controller;

import com.google.gson.Gson;
import pl.edu.prz.stud.swawro.server.KryoClient;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.InfoSessionExpiredRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.InfoSessionExpiredResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionResponse;
import pl.edu.prz.stud.swawro.server.config.Config;
import pl.edu.prz.stud.swawro.server.config.ServerInfo;
import pl.edu.prz.stud.swawro.server.config.SessionInfo;

import java.util.logging.Logger;

public class InternalController {
    private static final Logger LOGGER = Logger.getLogger(InternalController.class.getName());
    public InternalController(){}

    public String registerSession (String request) {
        Gson gson = new Gson();
        RegisterSessionResponse registerSessionResponse = new RegisterSessionResponse();
        RegisterSessionRequest registerSessionRequest = new RegisterSessionRequest();
        SessionInfo sessionInfo = new SessionInfo();
        try {
            registerSessionRequest = gson.fromJson(request, RegisterSessionRequest.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            registerSessionResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(registerSessionResponse);
        }

        sessionInfo.setUuid(registerSessionRequest.getUuid());
        sessionInfo.setExpireTime(registerSessionRequest.getExpireTime());
        Config.ALLOWED_SESSIONS.add(sessionInfo);
        registerSessionResponse.setStatusCode(0);

        String response = gson.toJson(registerSessionResponse);
        return response;
    }

    private synchronized int decreaseConnectionsForServer(ServerInfo serverInfo) {
        for (ServerInfo current : Config.SLAVES_INFO) {
            if (current.getIp().equals(serverInfo.getIp())
                    && current.getHttpPort().equals(serverInfo.getHttpPort())
                    && current.getKryoPort().equals(serverInfo.getKryoPort())
                    && current.getInternalPort().equals(serverInfo.getInternalPort())) {
                current.setConnections(current.getConnections()-1);
                return 0;
            }
        }
        return -1;
    }

    public String infoSessionExpired (String request) {
        Gson gson = new Gson();
        InfoSessionExpiredResponse infoSessionExpiredResponse = new InfoSessionExpiredResponse();
        InfoSessionExpiredRequest infoSessionExpiredRequest = new InfoSessionExpiredRequest();
        try {
            infoSessionExpiredRequest = gson.fromJson(request, InfoSessionExpiredRequest.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            infoSessionExpiredResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(infoSessionExpiredResponse);
        }

        infoSessionExpiredResponse.setStatusCode(decreaseConnectionsForServer(infoSessionExpiredRequest.getServerInfo()));

        String response = gson.toJson(infoSessionExpiredResponse);
        return response;
    }

    public String registerSlave (String request) {
        //TODO: not implemented yet
        return null;
    }

    public String removeSlave (String request) {
        //TODO: not implemented yet
        return null;
    }
}
