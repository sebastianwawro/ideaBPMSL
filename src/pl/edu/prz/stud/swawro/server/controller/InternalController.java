package pl.edu.prz.stud.swawro.server.controller;

import com.google.gson.Gson;
import pl.edu.prz.stud.swawro.server.KryoClient;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2s.RegisterSessionResponse;
import pl.edu.prz.stud.swawro.server.config.Config;
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

    public String infoSessionExpired (String request) {

        return null;
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
