/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import pl.edu.prz.stud.swawro.server.Model.Abnormal;
import pl.edu.prz.stud.swawro.server.Model.DeliveredAbnormal;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.BpHTType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserDataChangeType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserRole;
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.Model.UserAuth;
import pl.edu.prz.stud.swawro.server.Model.UserDataHistory;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.ChangePasswordRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RegisterRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RegisterResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.LogInRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.LogInResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.LogOutRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.LogOutResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SmartSearchRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SmartSearchResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.GetUserInfoRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.LogOutOtherDevicesRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.GetUserInfoResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncWithServerRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncWithServerResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncRelationsRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncRelationsResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncDataRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncDataResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadDataRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadDataResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncSettingsRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncSettingsResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadSettingsRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.ChangePasswordResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.LogOutOtherDevicesResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadSettingsResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.AbnormalTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.RelationTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SettingsTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SmartSearchTO;
import pl.edu.prz.stud.swawro.server.config.Config;
import pl.edu.prz.stud.swawro.server.config.ServerInfo;
import pl.edu.prz.stud.swawro.server.controller.helpers.AbnormalHelper;
import pl.edu.prz.stud.swawro.server.controller.helpers.RelationHelper;
import pl.edu.prz.stud.swawro.server.controller.helpers.SecurityHelper;
import pl.edu.prz.stud.swawro.server.controller.helpers.ValidationHelper;
import pl.edu.prz.stud.swawro.server.database.dao.AbnormalDAO;
import pl.edu.prz.stud.swawro.server.database.dao.DeliveredAbnormalDAO;
import pl.edu.prz.stud.swawro.server.database.dao.RelationDAO;
import pl.edu.prz.stud.swawro.server.database.dao.SettingsDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserAuthDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserDataHistoryDAO;

/**
 *
 * @author Sebastian
 */
public class UsersController {
    private static final UsersController _instance = new UsersController();
    
    public UsersController(){}
    
    public static final UsersController getInstance() {
        return _instance;
    }
    
    public String register(String request) {
        String response = null;
        Gson gson = new Gson();
        RegisterRequest registerRequest;
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            registerRequest = gson.fromJson(request, RegisterRequest.class);
        } 
        catch (JsonSyntaxException e) {
            registerResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(registerResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUsername(registerRequest.getLogin());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePassword(registerRequest.getPassword());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validateName(registerRequest.getName());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validateSurname(registerRequest.getSurname());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validateEmail(registerRequest.getEmail());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
                return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePhone(registerRequest.getPhone());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        statusCode = ValidationHelper.getInstance().validateDate(registerRequest.getDateBorn());
        if (statusCode < 0) {
            registerResponse.setStatusCode(statusCode);
            return gson.toJson(registerResponse);
        }
        if (registerRequest.getRole() < 0 || registerRequest.getRole() >= UserRole.getValues().length || registerRequest.getRole() == UserRole.ADMIN.ordinal()) {  //TODO: można lepiej
            registerResponse.setStatusCode(ErrorCode.ROLE_INVALID.getErrorCode());
            return gson.toJson(registerResponse);
        }
        
        User user = null;
        try {
            user = UserDAO.getInstance().getByLogin(registerRequest.getLogin());
        }
        catch (HibernateException e) {
            registerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(registerResponse);
        }
        
        if (user != null) {
            registerResponse.setStatusCode(ErrorCode.USERNAME_TAKEN.getErrorCode());
            return gson.toJson(registerResponse);
        }
        
        user = new User();
        user.setId(0);
        user.setLogin(registerRequest.getLogin());
        user.setSalt(SecurityHelper.getInstance().getRandomSalt());
        try {
            user.setPassword(SecurityHelper.getInstance().getSHA256(registerRequest.getPassword()+user.getSalt()));
        }
        catch (Exception e) {
            registerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(registerResponse);
        }
        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setDateBorn(registerRequest.getDateBorn());
        user.setDateRegistered(SecurityHelper.getInstance().getCurrentTime());
        user.setRole(registerRequest.getRole());
        user.setIsApproved(0);
        user.setAutoSms(0);
        user.setAutoMail(0);
        user.setRelationsUid(0);
        user.setUserDataUid(0);
        user.setSettingsUid(0);
        
        UserDataHistory userDataHistory = UserDataHistory.copyCurrent(user, UserDataChangeType.REGISTER);
        try {
            UserDAO.getInstance().add(user);
            UserDataHistoryDAO.getInstance().add(userDataHistory);
        }
        catch (HibernateException e) {
            registerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(registerResponse);
        }
        
        registerResponse.setUserId(user.getId());
        registerResponse.setDateRegistered(user.getDateRegistered());
        registerResponse.setStatusCode(0);
        response = gson.toJson(registerResponse);
        return response;
    }
    
    public String logIn(String request) {
        String response = null;
        Gson gson = new Gson();
        LogInRequest logInRequest;
        LogInResponse logInResponse = new LogInResponse();
        try {
            logInRequest = gson.fromJson(request, LogInRequest.class);
        } 
        catch (JsonSyntaxException e) {
            logInResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(logInResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUsername(logInRequest.getLogin());
        if (statusCode < 0) {
            logInResponse.setStatusCode(statusCode);
            return gson.toJson(logInResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePassword(logInRequest.getPassword());
        if (statusCode < 0) {
            logInResponse.setStatusCode(statusCode);
            return gson.toJson(logInResponse);
        }
        
        User user = null;
        try {
            user = UserDAO.getInstance().getByLogin(logInRequest.getLogin());
        }
        catch (HibernateException e) {
            logInResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(logInResponse);
        }
        
        if (user == null) {
            logInResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
            return gson.toJson(logInResponse);
        }
        
        try {
            if(!user.getPassword().equals(SecurityHelper.getInstance().getSHA256(logInRequest.getPassword()+user.getSalt()))) {
                logInResponse.setStatusCode(ErrorCode.PASSWORD_INVALID.getErrorCode());
                return gson.toJson(logInResponse);
            }
        }
        catch (Exception e) {
            logInResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(logInResponse);
        }
        
        UserAuth userAuth = new UserAuth();
        userAuth.setId(0);
        userAuth.setUser(user);
        userAuth.setUuid(SecurityHelper.getInstance().getRandomUuid());
        
        try {
            UserAuthDAO.getInstance().add(userAuth);
        }
        catch (HibernateException e) {
            logInResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(logInResponse);
        }

        //TODO:EXTENDER
        ServerInfo serverInfo = Config.attachNewSessionToSlave(userAuth.getUuid());
        if (serverInfo == null) {
            logInResponse.setStatusCode(-99999);
            return gson.toJson(logInResponse);
        }
        //TODO: ATTACH INFO TO logInResponse
        //TODO:EXTENDER
        
        logInResponse.setStatusCode(0);
        logInResponse.setUuid(userAuth.getUuid());
        response = gson.toJson(logInResponse);
        return response;
    }
    
    public String logOut(String request) {
        String response = null;
        Gson gson = new Gson();
        LogOutRequest logOutRequest;
        LogOutResponse logOutResponse = new LogOutResponse();
        try {
            logOutRequest = gson.fromJson(request, LogOutRequest.class);
        } 
        catch (JsonSyntaxException e) {
            logOutResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(logOutResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(logOutRequest.getUuid());
        if (statusCode < 0) {
            logOutResponse.setStatusCode(statusCode);
            return gson.toJson(logOutResponse);
        }
        
        UserAuth userAuth = null;
        try {
            //userAuth = UserAuthDAO.getInstance().getByUuid(logOutRequest.getUuid()); i potem .remove(userAuth);
            UserAuthDAO.getInstance().removeByUuid(logOutRequest.getUuid());
        }
        catch (HibernateException e) {
            logOutResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(logOutResponse);
        }
        
        logOutResponse.setStatusCode(0);
        response = gson.toJson(logOutResponse);
        return response;
    }
    
    public String smartSearch(String request) {
        String response = null;
        Gson gson = new Gson();
        SmartSearchRequest smartSearchRequest;
        SmartSearchResponse smartSearchResponse = new SmartSearchResponse();
        try {
            smartSearchRequest = gson.fromJson(request, SmartSearchRequest.class);
        } 
        catch (JsonSyntaxException e) {
            smartSearchResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(smartSearchRequest.getUuid());
        if (statusCode < 0) {
            smartSearchResponse.setStatusCode(statusCode);
            return gson.toJson(smartSearchResponse);
        }
        
        statusCode = ValidationHelper.getInstance().validateSearchString(smartSearchRequest.getSearchString());
        if (statusCode < 0) {
            smartSearchResponse.setStatusCode(statusCode);
            return gson.toJson(smartSearchResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(smartSearchRequest.getUuid());
        }
        catch (HibernateException e) {
            smartSearchResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        if (userAuth == null) {
            smartSearchResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        List<Relation> relations = null;
        try {
            relations = RelationDAO.getInstance().getAllForUserId(userAuth.getUser().getId());
        }
        catch (HibernateException e) {
            smartSearchResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        String[] searchStrings = smartSearchRequest.getSearchString().split(" ");
        
        List<User> users = null;
        try {
            users = UserDAO.getInstance().getBySmart(searchStrings);
        }
        catch (HibernateException e) {
            smartSearchResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        if (users == null || users.isEmpty()) {
            smartSearchResponse.setStatusCode(ErrorCode.USERS_NOT_FOUND.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        List<SmartSearchTO> smartSearchList = new ArrayList<>();
        for (User foundUser : users) {
            if (userAuth.getUser().getRole() != UserRole.ADMIN.ordinal() && foundUser.getRole() == UserRole.ADMIN.ordinal()) {
                continue;
            }
            if (userAuth.getUser().getRole() != UserRole.ADMIN.ordinal() && foundUser.getRole() == UserRole.DOCTOR.ordinal() && foundUser.getIsApproved() == 0) {
                continue;
            }
            smartSearchList.add(RelationHelper.getInstance().parseSearch(userAuth, foundUser, relations));
        }
        
        if (smartSearchList.isEmpty()) {
            smartSearchResponse.setStatusCode(ErrorCode.USERS_NOT_FOUND.getErrorCode());
            return gson.toJson(smartSearchResponse);
        }
        
        smartSearchResponse.setStatusCode(0);
        smartSearchResponse.setSmartSearchList(smartSearchList);
        response = gson.toJson(smartSearchResponse);
        return response;
    }
    
    public String getUserInfo(String request) {
        String response = null;
        Gson gson = new Gson();
        GetUserInfoRequest getUserInfoRequest;
        GetUserInfoResponse getUserInfoResponse = new GetUserInfoResponse();
        try {
            getUserInfoRequest = gson.fromJson(request, GetUserInfoRequest.class);
        } 
        catch (JsonSyntaxException e) {
            getUserInfoResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(getUserInfoRequest.getUuid());
        if (statusCode < 0) {
            getUserInfoResponse.setStatusCode(statusCode);
            return gson.toJson(getUserInfoResponse);
        }
        
        if(getUserInfoRequest.getLogin().equals("#getById")) {
            if (getUserInfoRequest.getUserId() <= 0) {
                getUserInfoResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.ordinal());
                return gson.toJson(getUserInfoResponse);
            }
        }
        else {
            statusCode = ValidationHelper.getInstance().validateUsername(getUserInfoRequest.getLogin());
            if (statusCode < 0) {
                getUserInfoResponse.setStatusCode(statusCode);
                return gson.toJson(getUserInfoResponse);
            }
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(getUserInfoRequest.getUuid());
        }
        catch (HibernateException e) {
            getUserInfoResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        
        if (userAuth == null) {
            getUserInfoResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        
        List<Relation> relations = null;
        try {
            relations = RelationDAO.getInstance().getAllForUserId(userAuth.getUser().getId());
        }
        catch (HibernateException e) {
            getUserInfoResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        
        User foundUser = null;
        try {
            if (getUserInfoRequest.getLogin().equals("#getById")) {
                foundUser = UserDAO.getInstance().getById(getUserInfoRequest.getUserId());
            }
            else {
                foundUser = UserDAO.getInstance().getByLogin(getUserInfoRequest.getLogin());
            }
        }
        catch (HibernateException e) {
            getUserInfoResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        
        if (foundUser == null) {
            getUserInfoResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        if (userAuth.getUser().getRole() != UserRole.ADMIN.ordinal() && foundUser.getRole() == UserRole.ADMIN.ordinal()) {
            getUserInfoResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        if (userAuth.getUser().getRole() != UserRole.ADMIN.ordinal() && foundUser.getRole() == UserRole.DOCTOR.ordinal() && foundUser.getIsApproved() == 0) {
            getUserInfoResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
            return gson.toJson(getUserInfoResponse);
        }
        
        SmartSearchTO smartSearchTO = RelationHelper.getInstance().parseSearch(userAuth, foundUser, relations);
        
        getUserInfoResponse.setStatusCode(0);
        getUserInfoResponse.setUserInfo(smartSearchTO);
        response = gson.toJson(getUserInfoResponse);
        return response;
        
    }
    
    public String syncWithServer(String request) { //może dodać doctor id żeby nie wybuchło (+ selekcja zaraz po nadejściu pomiaru)
        String response = null;
        Gson gson = new Gson();
        SyncWithServerRequest syncWithServerRequest;
        SyncWithServerResponse syncWithServerResponse = new SyncWithServerResponse();
        try {
            syncWithServerRequest = gson.fromJson(request, SyncWithServerRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncWithServerResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncWithServerResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncWithServerRequest.getUuid());
        if (statusCode < 0) {
            syncWithServerResponse.setStatusCode(statusCode);
            return gson.toJson(syncWithServerResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncWithServerRequest.getUuid());
        }
        catch (HibernateException e) {
            syncWithServerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncWithServerResponse);
        }
        
        if (userAuth == null) {
            syncWithServerResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(syncWithServerResponse);
        }
        
        List<Abnormal> abnormals = null;
        try {
            abnormals = AbnormalDAO.getInstance().getUndelivered(userAuth.getUser().getId());
        }
        catch (HibernateException e) {
            syncWithServerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncWithServerResponse);
        }
        
        if (abnormals == null) {
            syncWithServerResponse.setStatusCode(0);
            syncWithServerResponse.setAbnormalTOList(null);
            syncWithServerResponse.setRelationsUid(userAuth.getUser().getRelationsUid());
            syncWithServerResponse.setSettingsUid(userAuth.getUser().getSettingsUid());
            syncWithServerResponse.setUserDataUid(userAuth.getUser().getUserDataUid());
            return gson.toJson(syncWithServerResponse);
        }
        
        List<DeliveredAbnormal> deliveredAbnormals = new ArrayList<>();
        for (Abnormal ab : abnormals) {
            DeliveredAbnormal deliveredAbnormal = new DeliveredAbnormal();
            deliveredAbnormal.setAbnormal(ab);
            deliveredAbnormal.setDoctor(userAuth.getUser());
            deliveredAbnormals.add(deliveredAbnormal);
        }
        try {
            DeliveredAbnormalDAO.getInstance().addAll(deliveredAbnormals);
        }
        catch (HibernateException e) {
            syncWithServerResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncWithServerResponse);
        }
        
        List<AbnormalTO> abnormalsList = new ArrayList<>();
        for (Abnormal ab : abnormals) {
            abnormalsList.add(AbnormalTO.encode(ab));
        }
        
        syncWithServerResponse.setStatusCode(0);
        syncWithServerResponse.setAbnormalTOList(abnormalsList);
        syncWithServerResponse.setRelationsUid(userAuth.getUser().getRelationsUid());
        syncWithServerResponse.setSettingsUid(userAuth.getUser().getSettingsUid());
        syncWithServerResponse.setUserDataUid(userAuth.getUser().getUserDataUid());
        response = gson.toJson(syncWithServerResponse);
        return response;
    }
    
    public String syncRelations(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncRelationsRequest syncRelationsRequest;
        SyncRelationsResponse syncRelationsResponse = new SyncRelationsResponse();
        try {
            syncRelationsRequest = gson.fromJson(request, SyncRelationsRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncRelationsResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncRelationsResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncRelationsRequest.getUuid());
        if (statusCode < 0) {
            syncRelationsResponse.setStatusCode(statusCode);
            return gson.toJson(syncRelationsResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncRelationsRequest.getUuid());
        }
        catch (HibernateException e) {
            syncRelationsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncRelationsResponse);
        }
        
        if (userAuth == null) {
            syncRelationsResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(syncRelationsResponse);
        }
        
        List<Relation> relations = null;
        try {
            relations = RelationDAO.getInstance().getAllForUserId(userAuth.getUser().getId());
        }
        catch (HibernateException e) {
            syncRelationsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncRelationsResponse);
        }
        
        if (relations == null) {
            syncRelationsResponse.setStatusCode(0);
            syncRelationsResponse.setRelationTOList(null);
            return gson.toJson(syncRelationsResponse);
        }
        
        List<RelationTO> relationsList = new ArrayList<>();
        for (Relation rel : relations) {
            relationsList.add(RelationTO.encode(rel));
        }
        
        syncRelationsResponse.setStatusCode(0);
        syncRelationsResponse.setRelationTOList(relationsList);
        response = gson.toJson(syncRelationsResponse);
        return response;
        
    }
    
    public String syncData(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncDataRequest syncDataRequest;
        SyncDataResponse syncDataResponse = new SyncDataResponse();
        try {
            syncDataRequest = gson.fromJson(request, SyncDataRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncDataResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncDataResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncDataRequest.getUuid());
        if (statusCode < 0) {
            syncDataResponse.setStatusCode(statusCode);
            return gson.toJson(syncDataResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncDataRequest.getUuid());
        }
        catch (HibernateException e) {
            syncDataResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncDataResponse);
        }
        
        if (userAuth == null) {
            syncDataResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(syncDataResponse);
        }
        
        syncDataResponse.setStatusCode(0);
        syncDataResponse.setName(userAuth.getUser().getName());
        syncDataResponse.setSurname(userAuth.getUser().getSurname());
        syncDataResponse.setEmail(userAuth.getUser().getEmail());
        syncDataResponse.setPhone(userAuth.getUser().getPhone());
        syncDataResponse.setRole(userAuth.getUser().getRole());
        syncDataResponse.setDateBorn(userAuth.getUser().getDateBorn());
        syncDataResponse.setDateRegistered(userAuth.getUser().getDateRegistered());
        syncDataResponse.setUserId(userAuth.getUser().getId());
        syncDataResponse.setAutoSms(userAuth.getUser().getAutoSms());
        syncDataResponse.setAutoMail(userAuth.getUser().getAutoMail());
        syncDataResponse.setIsApproved(userAuth.getUser().getIsApproved());
        response = gson.toJson(syncDataResponse);
        return response;
    }
    
    public String uploadData(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadDataRequest uploadDataRequest;
        UploadDataResponse uploadDataResponse = new UploadDataResponse();
        try {
            uploadDataRequest = gson.fromJson(request, UploadDataRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadDataResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadDataRequest.getUuid());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        statusCode = ValidationHelper.getInstance().validateName(uploadDataRequest.getName());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        statusCode = ValidationHelper.getInstance().validateSurname(uploadDataRequest.getSurname());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        statusCode = ValidationHelper.getInstance().validateEmail(uploadDataRequest.getEmail());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePhone(uploadDataRequest.getPhone());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        statusCode = ValidationHelper.getInstance().validateDate(uploadDataRequest.getDateBorn());
        if (statusCode < 0) {
            uploadDataResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDataResponse);
        }
        if (uploadDataRequest.getRole() < 0 || uploadDataRequest.getRole() >= UserRole.getValues().length) {
            uploadDataResponse.setStatusCode(ErrorCode.ROLE_INVALID.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        if (uploadDataRequest.getAutoSms() < 0 || uploadDataRequest.getAutoSms() > 1) {
            uploadDataResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        if (uploadDataRequest.getAutoMail() < 0 || uploadDataRequest.getAutoMail() > 1) {
            uploadDataResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadDataRequest.getUuid());
        }
        catch (HibernateException e) {
            uploadDataResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        
        if (userAuth == null) {
            uploadDataResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        
        if(uploadDataRequest.getRole() == UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
            uploadDataResponse.setStatusCode(ErrorCode.ROLE_INVALID.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        
        userAuth.getUser().setName(uploadDataRequest.getName());
        userAuth.getUser().setSurname(uploadDataRequest.getSurname());
        userAuth.getUser().setEmail(uploadDataRequest.getEmail());
        userAuth.getUser().setPhone(uploadDataRequest.getPhone());
        userAuth.getUser().setDateBorn(uploadDataRequest.getDateBorn());
        if (uploadDataRequest.getRole() != UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != uploadDataRequest.getRole()) {
            userAuth.getUser().setRole(uploadDataRequest.getRole());
            userAuth.getUser().setIsApproved(0);
        }
        userAuth.getUser().setAutoSms(uploadDataRequest.getAutoSms());
        userAuth.getUser().setAutoMail(uploadDataRequest.getAutoMail());
        
        UserDataHistory userDataHistory = UserDataHistory.copyCurrent(userAuth.getUser(), UserDataChangeType.UPDATE);
        try {
            
            userAuth.getUser().setUserDataUid(userAuth.getUser().getUserDataUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDataHistoryDAO.getInstance().add(userDataHistory);
        }
        catch (HibernateException e) {
            uploadDataResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadDataResponse);
        }
        
        uploadDataResponse.setStatusCode(0);
        //uploadDataResponse.setUserDataUid(userAuth.getUser().getUserDataUid());
        response = gson.toJson(uploadDataResponse);
        return response;
    }
    
    public String syncSettings(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncSettingsRequest syncSettingsRequest;
        SyncSettingsResponse syncSettingsResponse = new SyncSettingsResponse();
        try {
            syncSettingsRequest = gson.fromJson(request, SyncSettingsRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncSettingsResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncSettingsResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncSettingsRequest.getUuid());
        if (statusCode < 0) {
            syncSettingsResponse.setStatusCode(statusCode);
            return gson.toJson(syncSettingsResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncSettingsRequest.getUuid());
        }
        catch (HibernateException e) {
            syncSettingsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncSettingsResponse);
        }
        
        if (userAuth == null) {
            syncSettingsResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(syncSettingsResponse);
        }
        
        List<Settings> settingsList = null;
        List<Relation> relationsList = null;
        try {
            settingsList = SettingsDAO.getInstance().getAllForUserId(userAuth.getUser().getId());
            relationsList = RelationDAO.getInstance().getAllForUserId(userAuth.getUser().getId());
        }
        catch (HibernateException e) {
            syncSettingsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncSettingsResponse);
        }
        
        List<Settings> activeSettings = null;
        if (settingsList != null && relationsList != null)
            activeSettings = AbnormalHelper.getInstance().getActiveSettings(relationsList, settingsList);
        
        List<SettingsTO> settingsTOList = null;
        if (activeSettings != null) {
            settingsTOList = new ArrayList<>();
            for (Settings st : activeSettings) {
                settingsTOList.add(SettingsTO.encode(st));
            }
        }
        
        syncSettingsResponse.setStatusCode(0);
        syncSettingsResponse.setSettingsTOList(settingsTOList);
        response = gson.toJson(syncSettingsResponse);
        return response;
    }
    
    public String uploadSettings(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadSettingsRequest uploadSettingsRequest;
        UploadSettingsResponse uploadSettingsResponse = new UploadSettingsResponse();
        try {
            uploadSettingsRequest = gson.fromJson(request, UploadSettingsRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadSettingsResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadSettingsResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadSettingsRequest.getUuid());
        if (statusCode < 0) {
            uploadSettingsResponse.setStatusCode(statusCode);
            return gson.toJson(uploadSettingsResponse);
        }
        if (uploadSettingsRequest.getSettingsTOList() == null || uploadSettingsRequest.getSettingsTOList().isEmpty()) {
            uploadSettingsResponse.setStatusCode(ErrorCode.NOTHING_RECEIVED.getErrorCode());
            return gson.toJson(uploadSettingsResponse);
        }
        for (SettingsTO st : uploadSettingsRequest.getSettingsTOList()) {
            if (st.getBpHighAb() < 0 && st.getBpHighAb() >= BpHTType.getValues().length) { //enumy można sprawdzać od 0 do getValues.length
                uploadSettingsResponse.setStatusCode(ErrorCode.BPHTTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getBpLowAb() != 0 && st.getBpLowAb() != 1) {
                uploadSettingsResponse.setStatusCode(ErrorCode.BPLOWABTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getHrHighAb()!= 0 && st.getHrHighAb() != 1) {
                uploadSettingsResponse.setStatusCode(ErrorCode.HRHIGHABTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getHrLowAb()!= 0 && st.getHrLowAb() != 1) {
                uploadSettingsResponse.setStatusCode(ErrorCode.HRLOWABTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getOxyAb()!= 0 && st.getOxyAb() != 1) {
                uploadSettingsResponse.setStatusCode(ErrorCode.OXYLOWABTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getDeepSleepAb()!= 0 && st.getDeepSleepAb() != 1) {
                uploadSettingsResponse.setStatusCode(ErrorCode.DEEPSLEEPLOWABTYPE_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustBpHighMax() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateBpMeasure(st.getCustBpHighMax());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustBpHighMin()== -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateBpMeasure(st.getCustBpHighMin());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustBpLowMax() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateBpMeasure(st.getCustBpLowMax());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustBpLowMin() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateBpMeasure(st.getCustBpLowMin());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustHrMax()== -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateHrMeasure(st.getCustHrMax());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustHrMin() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateHrMeasure(st.getCustHrMin());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustOxyMin() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateOxyMeasure(st.getCustOxyMin());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustDeepSleepMinTime()== -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateSleepMeasure(st.getCustDeepSleepMinTime());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
            if (st.getCustDeepSleepMinPer() == -1) statusCode=0;
            else statusCode = ValidationHelper.getInstance().validateSleepPerMeasure(st.getCustDeepSleepMinPer());
            if (statusCode < 0) {
                uploadSettingsResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSettingsResponse);
            }
        }
        // todo: WALIDACJA EXTEND
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadSettingsRequest.getUuid());
        }
        catch (HibernateException e) {
            uploadSettingsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadSettingsResponse);
        }
        
        if (userAuth == null) {
            uploadSettingsResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
            return gson.toJson(uploadSettingsResponse);
        }
        
        List<SettingsTO> settingsTOList = uploadSettingsRequest.getSettingsTOList();
        for (SettingsTO st : settingsTOList) {
            if (st.getDoctorId() != userAuth.getUser().getId()) {
                uploadSettingsResponse.setStatusCode(ErrorCode.SETTINGS_HACK_ATTEMPT.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
        }
        
        try {
            List<Settings> settingsList = new ArrayList<>();
            for (SettingsTO st : settingsTOList) {
                Settings set = st.decode(userAuth.getUser());
                if (set.getUser() != null && set.getDoctor() != null)
                    settingsList.add(set);
            }

            if (settingsList == null) {
                uploadSettingsResponse.setStatusCode(ErrorCode.SETTINGS_FATAL_INVALID.getErrorCode());
                return gson.toJson(uploadSettingsResponse);
            }
        
            for (Settings set : settingsList) {
                Settings possibleCurrent = SettingsDAO.getInstance().get(set.getDoctor().getId(), set.getUser().getId());
                if (possibleCurrent == null) {
                    SettingsDAO.getInstance().add(set); //można zrobić tworzenie settings przy stworzeniu(czytaj potwierdzeniu) relacji - wtedy zawsze będzie update
                }
                else {
                    set.setId(possibleCurrent.getId());
                    set.setDoctor(possibleCurrent.getDoctor());
                    set.setUser(possibleCurrent.getUser());
                    SettingsDAO.getInstance().update(set);
                }
                
                //User doctor = set.getDoctor();
                User user = set.getUser();
                //doctor.setSettingsUid(doctor.getSettingsUid()+1);
                user.setSettingsUid(user.getSettingsUid()+1);
                //UserDAO.getInstance().update(doctor);
                UserDAO.getInstance().update(user);
            }
            
            userAuth.getUser().setSettingsUid(userAuth.getUser().getSettingsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
        }
        catch (HibernateException e) {
            uploadSettingsResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadSettingsResponse);
        }
        
        uploadSettingsResponse.setStatusCode(0);
        //uploadSettingsResponse.setSettingsUid(userAuth.getUser().getSettingsUid());
        response = gson.toJson(uploadSettingsResponse);
        return response;
    }
    
    public String changePassword(String request) {
        String response = null;
        Gson gson = new Gson();
        ChangePasswordRequest changePasswordRequest;
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
        try {
            changePasswordRequest = gson.fromJson(request, ChangePasswordRequest.class);
        } 
        catch (JsonSyntaxException e) {
            changePasswordResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(changePasswordResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(changePasswordRequest.getUuid());
        if (statusCode < 0) {
            changePasswordResponse.setStatusCode(statusCode);
            return gson.toJson(changePasswordResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePassword(changePasswordRequest.getOldPassword());
        if (statusCode < 0) {
            changePasswordResponse.setStatusCode(statusCode);
            return gson.toJson(changePasswordResponse);
        }
        statusCode = ValidationHelper.getInstance().validatePassword(changePasswordRequest.getNewPassword());
        if (statusCode < 0) {
            changePasswordResponse.setStatusCode(statusCode);
            return gson.toJson(changePasswordResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(changePasswordRequest.getUuid());
        }
        catch (HibernateException e) {
            changePasswordResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(changePasswordResponse);
        }
        
        if (userAuth == null) {
            changePasswordResponse.setStatusCode(ErrorCode.NOT_LOGGED_IN.getErrorCode());
            return gson.toJson(changePasswordResponse);
        }
        
        User user = userAuth.getUser();
        
        try {
            if(!user.getPassword().equals(SecurityHelper.getInstance().getSHA256(changePasswordRequest.getOldPassword()+user.getSalt()))) {
                changePasswordResponse.setStatusCode(ErrorCode.PASSWORD_INVALID.getErrorCode());
                return gson.toJson(changePasswordResponse);
            }
            
            String newSalt = SecurityHelper.getInstance().getRandomSalt();
            String newPass = SecurityHelper.getInstance().getSHA256(changePasswordRequest.getNewPassword()+newSalt);
            user.setSalt(newSalt);
            user.setPassword(newPass);
            
            UserDAO.getInstance().update(user);
            
            if (changePasswordRequest.isLogOutOtherDevices()) {
                UserAuthDAO.getInstance().removeOtherByUuid(user.getId(), userAuth.getUuid());
            }
        }
        catch (Exception e) {
            changePasswordResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(changePasswordResponse);
        }
        
        changePasswordResponse.setStatusCode(0);
        response = gson.toJson(changePasswordResponse);
        return response;
    }
    
    public String logOutOtherDevices(String request) {
        String response = null;
        Gson gson = new Gson();
        LogOutOtherDevicesRequest logOutOtherDevicesRequest; 
        LogOutOtherDevicesResponse logOutOtherDevicesResponse = new LogOutOtherDevicesResponse();
        try {
            logOutOtherDevicesRequest = gson.fromJson(request, LogOutOtherDevicesRequest.class);
        } 
        catch (JsonSyntaxException e) {
            logOutOtherDevicesResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(logOutOtherDevicesResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(logOutOtherDevicesRequest.getUuid());
        if (statusCode < 0) {
            logOutOtherDevicesResponse.setStatusCode(statusCode);
            return gson.toJson(logOutOtherDevicesResponse);
        }
        
        UserAuth userAuth = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(logOutOtherDevicesRequest.getUuid());
            if (userAuth == null || userAuth.getUser() == null) throw new Exception("not found");
            UserAuthDAO.getInstance().removeOtherByUuid(userAuth.getUser().getId(), logOutOtherDevicesRequest.getUuid());
        }
        catch (HibernateException e) {
            logOutOtherDevicesResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(logOutOtherDevicesResponse);
        }
        catch (Exception e) {
            logOutOtherDevicesResponse.setStatusCode(ErrorCode.NOT_LOGGED_IN.getErrorCode());
            return gson.toJson(logOutOtherDevicesResponse);
        }
        
        logOutOtherDevicesResponse.setStatusCode(0);
        response = gson.toJson(logOutOtherDevicesResponse);
        return response;
    }
}
