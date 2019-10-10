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
import pl.edu.prz.stud.swawro.server.Model.BpMeasure;
import pl.edu.prz.stud.swawro.server.Model.HrMeasure;
import pl.edu.prz.stud.swawro.server.Model.OxyMeasure;
import pl.edu.prz.stud.swawro.server.Model.SleepMeasure;
import pl.edu.prz.stud.swawro.server.Model.DiaryEntry;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.MeasureType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.StatusOnServer;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserRole;
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.Model.UserAuth;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadBpMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadBpMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncBpMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncBpMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveBpMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveBpMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadHrMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadHrMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncHrMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncHrMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveHrMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveHrMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadOxyMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadOxyMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncOxyMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncOxyMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveOxyMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveOxyMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadSleepMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadSleepMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncSleepMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncSleepMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveSleepMeasureRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveSleepMeasureResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.UploadDiaryEntryRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.UploadDiaryEntryResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.SyncDiaryEntryRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.SyncDiaryEntryResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveDiaryEntryRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveDiaryEntryResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.BpMeasureTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.HrMeasureTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.OxyMeasureTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SleepMeasureTO;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.DiaryEntryTO;
import pl.edu.prz.stud.swawro.server.controller.helpers.AbnormalHelper;
import pl.edu.prz.stud.swawro.server.controller.helpers.ValidationHelper;
import pl.edu.prz.stud.swawro.server.database.dao.AbnormalDAO;
import pl.edu.prz.stud.swawro.server.database.dao.BpMeasureDAO;
import pl.edu.prz.stud.swawro.server.database.dao.HrMeasureDAO;
import pl.edu.prz.stud.swawro.server.database.dao.OxyMeasureDAO;
import pl.edu.prz.stud.swawro.server.database.dao.SleepMeasureDAO;
import pl.edu.prz.stud.swawro.server.database.dao.DiaryEntryDAO;
import pl.edu.prz.stud.swawro.server.database.dao.RelationDAO;
import pl.edu.prz.stud.swawro.server.database.dao.SettingsDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserAuthDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;

/**
 *
 * @author Sebastian
 */
public class DataController {
    private static final DataController _instance = new DataController();
    
    public DataController(){}
    
    public static final DataController getInstance() {
        return _instance;
    }
    
    public String uploadBpMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadBpMeasureRequest uploadBpMeasureRequest;
        UploadBpMeasureResponse uploadBpMeasureResponse = new UploadBpMeasureResponse();
        try {
            uploadBpMeasureRequest = gson.fromJson(request, UploadBpMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadBpMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadBpMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadBpMeasureRequest.getUuid());
        if (statusCode < 0) {
            uploadBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadBpMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(uploadBpMeasureRequest.getLogin());
        if (statusCode < 0) {
            uploadBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadBpMeasureResponse);
        }
        for (BpMeasureTO bp : uploadBpMeasureRequest.getBpMeasures()) {
            statusCode = ValidationHelper.getInstance().validateBpMeasure(bp.getBpHigh());
            if (statusCode < 0) {
                uploadBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadBpMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateBpMeasure(bp.getBpLow());
            if (statusCode < 0) {
                uploadBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadBpMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(bp.getDate());
            if (statusCode < 0) {
                uploadBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadBpMeasureResponse);
            }
            if (bp.getMeasureType() < 0 || bp.getMeasureType() >= MeasureType.getValues().length) {
                uploadBpMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadBpMeasureResponse);
            }
            if (bp.getStatusOnServer() < 0 || bp.getStatusOnServer() >= StatusOnServer.getValues().length) {
                uploadBpMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadBpMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<Relation> relationList = null;
        List<Settings> settingsList = null;
        List<BpMeasure> bpMeasures = new ArrayList<>();
        List<BpMeasure> bpMeasuresAdded = null;
        List<Abnormal> abnormals = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadBpMeasureRequest.getUuid());
            if (userAuth == null) {
                uploadBpMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(uploadBpMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(uploadBpMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(uploadBpMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    uploadBpMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(uploadBpMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    uploadBpMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(uploadBpMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    uploadBpMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(uploadBpMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    uploadBpMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(uploadBpMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (BpMeasureTO mto : uploadBpMeasureRequest.getBpMeasures()) {
                bpMeasures.add(mto.decode(dataOwner));
            }
            
            bpMeasuresAdded = BpMeasureDAO.getInstance().addAll(bpMeasures); //ale skąd wiemy dla których robić abnormale? nie wiemy, trzeba je wywalać z pamięci
            
            if(bpMeasuresAdded != null) {
                relationList = RelationDAO.getInstance().getAllForPatient(dataOwner.getId());
                settingsList = SettingsDAO.getInstance().getAllForPatient(dataOwner.getId());
                    
                if (relationList != null && settingsList != null) {
                    abnormals = AbnormalHelper.getInstance().prepareAbnormalListForBpMeasures(relationList, settingsList, bpMeasuresAdded);
                    if(abnormals != null && !abnormals.isEmpty()) AbnormalDAO.getInstance().addAll(abnormals);
                }
            }
        }
        catch (HibernateException e) {
            uploadBpMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadBpMeasureResponse);
        }
        
        uploadBpMeasureResponse.setStatusCode(0);
        response = gson.toJson(uploadBpMeasureResponse);
        return response;
    }
    
    public String syncBpMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncBpMeasureRequest syncBpMeasureRequest;
        SyncBpMeasureResponse syncBpMeasureResponse = new SyncBpMeasureResponse();
        try {
            syncBpMeasureRequest = gson.fromJson(request, SyncBpMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncBpMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncBpMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncBpMeasureRequest.getUuid());
        if (statusCode < 0) {
            syncBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncBpMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(syncBpMeasureRequest.getLogin());
        if (statusCode < 0) {
            syncBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncBpMeasureResponse);
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<BpMeasureTO> measuresToSend = new ArrayList<>();
        List<BpMeasure> bpMeasures = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncBpMeasureRequest.getUuid());
            if (userAuth == null) {
                syncBpMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(syncBpMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(syncBpMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(syncBpMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    syncBpMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(syncBpMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    syncBpMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(syncBpMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    syncBpMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(syncBpMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            bpMeasures = BpMeasureDAO.getInstance().get(dataOwner.getId(), syncBpMeasureRequest.getDefaultSync(), syncBpMeasureRequest.getDateStart(), syncBpMeasureRequest.getDateEnd());
            
            for (BpMeasure measure : bpMeasures) {
                measuresToSend.add(BpMeasureTO.encode(measure));
            }
        }
        catch (HibernateException e) {
            syncBpMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncBpMeasureResponse);
        }
        
        syncBpMeasureResponse.setStatusCode(0);
        syncBpMeasureResponse.setLogin(syncBpMeasureRequest.getLogin());
        syncBpMeasureResponse.setBpMeasures(measuresToSend);
        response = gson.toJson(syncBpMeasureResponse);
        return response;
    }
    
    public String removeBpMeasure(String request){
        String response = null;
        Gson gson = new Gson();
        RemoveBpMeasureRequest removeBpMeasureRequest;
        RemoveBpMeasureResponse removeBpMeasureResponse = new RemoveBpMeasureResponse();
        try {
            removeBpMeasureRequest = gson.fromJson(request, RemoveBpMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeBpMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeBpMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeBpMeasureRequest.getUuid());
        if (statusCode < 0) {
            removeBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeBpMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeBpMeasureRequest.getLogin());
        if (statusCode < 0) {
            removeBpMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeBpMeasureResponse);
        }
        for (BpMeasureTO bp : removeBpMeasureRequest.getBpMeasures()) {
            statusCode = ValidationHelper.getInstance().validateBpMeasure(bp.getBpHigh());
            if (statusCode < 0) {
                removeBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeBpMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateBpMeasure(bp.getBpLow());
            if (statusCode < 0) {
                removeBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeBpMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(bp.getDate());
            if (statusCode < 0) {
                removeBpMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeBpMeasureResponse);
            }
            if (bp.getMeasureType() < 0 || bp.getMeasureType() >= MeasureType.getValues().length) {
                removeBpMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeBpMeasureResponse);
            }
            if (bp.getStatusOnServer() < 0 || bp.getStatusOnServer() >= StatusOnServer.getValues().length) {
                removeBpMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeBpMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<BpMeasure> bpMeasures = new ArrayList<>();
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeBpMeasureRequest.getUuid());
            if (userAuth == null) {
                removeBpMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeBpMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(removeBpMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(removeBpMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    removeBpMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(removeBpMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    removeBpMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(removeBpMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    removeBpMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(removeBpMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    removeBpMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(removeBpMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (BpMeasureTO mto : removeBpMeasureRequest.getBpMeasures()) {
                bpMeasures.add(mto.decode(dataOwner));
            }
            
            BpMeasureDAO.getInstance().removeByComplex(bpMeasures);
        }
        catch (HibernateException e) {
            removeBpMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeBpMeasureResponse);
        }
        
        removeBpMeasureResponse.setStatusCode(0);
        response = gson.toJson(removeBpMeasureResponse);
        return response;
    }
    
    public String uploadHrMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadHrMeasureRequest uploadHrMeasureRequest;
        UploadHrMeasureResponse uploadHrMeasureResponse = new UploadHrMeasureResponse();
        try {
            uploadHrMeasureRequest = gson.fromJson(request, UploadHrMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadHrMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadHrMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadHrMeasureRequest.getUuid());
        if (statusCode < 0) {
            uploadHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadHrMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(uploadHrMeasureRequest.getLogin());
        if (statusCode < 0) {
            uploadHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadHrMeasureResponse);
        }
        for (HrMeasureTO hr : uploadHrMeasureRequest.getHrMeasures()) {
            statusCode = ValidationHelper.getInstance().validateHrMeasure(hr.getHr());
            if (statusCode < 0) {
                uploadHrMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadHrMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(hr.getDate());
            if (statusCode < 0) {
                uploadHrMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadHrMeasureResponse);
            }
            if (hr.getMeasureType() < 0 || hr.getMeasureType() >= MeasureType.getValues().length) {
                uploadHrMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadHrMeasureResponse);
            }
            if (hr.getStatusOnServer() < 0 || hr.getStatusOnServer() >= StatusOnServer.getValues().length) {
                uploadHrMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadHrMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<Relation> relationList = null;
        List<Settings> settingsList = null;
        List<HrMeasure> hrMeasures = new ArrayList<>();
        List<HrMeasure> hrMeasuresAdded = null;
        List<Abnormal> abnormals = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadHrMeasureRequest.getUuid());
            if (userAuth == null) {
                uploadHrMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(uploadHrMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(uploadHrMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(uploadHrMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    uploadHrMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(uploadHrMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    uploadHrMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(uploadHrMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    uploadHrMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(uploadHrMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    uploadHrMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(uploadHrMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (HrMeasureTO mto : uploadHrMeasureRequest.getHrMeasures()) {
                hrMeasures.add(mto.decode(dataOwner));
            }
            
            hrMeasuresAdded = HrMeasureDAO.getInstance().addAll(hrMeasures); //ale skąd wiemy dla których robić abnormale? nie wiemy, trzeba je wywalać z pamięci
            
            if(hrMeasuresAdded != null) {
                relationList = RelationDAO.getInstance().getAllForPatient(dataOwner.getId());
                settingsList = SettingsDAO.getInstance().getAllForPatient(dataOwner.getId());
                    
                if (relationList != null && settingsList != null) {
                    abnormals = AbnormalHelper.getInstance().prepareAbnormalListForHrMeasures(relationList, settingsList, hrMeasuresAdded);
                    if(abnormals != null && !abnormals.isEmpty()) AbnormalDAO.getInstance().addAll(abnormals);
                }
            }
        }
        catch (HibernateException e) {
            uploadHrMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadHrMeasureResponse);
        }
        
        uploadHrMeasureResponse.setStatusCode(0);
        response = gson.toJson(uploadHrMeasureResponse);
        return response;
    }
    
    public String syncHrMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncHrMeasureRequest syncHrMeasureRequest;
        SyncHrMeasureResponse syncHrMeasureResponse = new SyncHrMeasureResponse();
        try {
            syncHrMeasureRequest = gson.fromJson(request, SyncHrMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncHrMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncHrMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncHrMeasureRequest.getUuid());
        if (statusCode < 0) {
            syncHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncHrMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(syncHrMeasureRequest.getLogin());
        if (statusCode < 0) {
            syncHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncHrMeasureResponse);
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<HrMeasureTO> measuresToSend = new ArrayList<>();
        List<HrMeasure> hrMeasures = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncHrMeasureRequest.getUuid());
            if (userAuth == null) {
                syncHrMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(syncHrMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(syncHrMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(syncHrMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    syncHrMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(syncHrMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    syncHrMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(syncHrMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    syncHrMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(syncHrMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            hrMeasures = HrMeasureDAO.getInstance().get(dataOwner.getId(), syncHrMeasureRequest.getDefaultSync(), syncHrMeasureRequest.getDateStart(), syncHrMeasureRequest.getDateEnd());
            
            for (HrMeasure measure : hrMeasures) {
                measuresToSend.add(HrMeasureTO.encode(measure));
            }
        }
        catch (HibernateException e) {
            syncHrMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncHrMeasureResponse);
        }
        
        syncHrMeasureResponse.setStatusCode(0);
        syncHrMeasureResponse.setLogin(syncHrMeasureRequest.getLogin());
        syncHrMeasureResponse.setHrMeasures(measuresToSend);
        response = gson.toJson(syncHrMeasureResponse);
        return response;
    }
    
    public String removeHrMeasure(String request){
        String response = null;
        Gson gson = new Gson();
        RemoveHrMeasureRequest removeHrMeasureRequest;
        RemoveHrMeasureResponse removeHrMeasureResponse = new RemoveHrMeasureResponse();
        try {
            removeHrMeasureRequest = gson.fromJson(request, RemoveHrMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeHrMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeHrMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeHrMeasureRequest.getUuid());
        if (statusCode < 0) {
            removeHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeHrMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeHrMeasureRequest.getLogin());
        if (statusCode < 0) {
            removeHrMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeHrMeasureResponse);
        }
        for (HrMeasureTO hr : removeHrMeasureRequest.getHrMeasures()) {
            statusCode = ValidationHelper.getInstance().validateHrMeasure(hr.getHr());
            if (statusCode < 0) {
                removeHrMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeHrMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(hr.getDate());
            if (statusCode < 0) {
                removeHrMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeHrMeasureResponse);
            }
            if (hr.getMeasureType() < 0 || hr.getMeasureType() >= MeasureType.getValues().length) {
                removeHrMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeHrMeasureResponse);
            }
            if (hr.getStatusOnServer() < 0 || hr.getStatusOnServer() >= StatusOnServer.getValues().length) {
                removeHrMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeHrMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<HrMeasure> hrMeasures = new ArrayList<>();
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeHrMeasureRequest.getUuid());
            if (userAuth == null) {
                removeHrMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeHrMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(removeHrMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(removeHrMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    removeHrMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(removeHrMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    removeHrMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(removeHrMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    removeHrMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(removeHrMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    removeHrMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(removeHrMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (HrMeasureTO mto : removeHrMeasureRequest.getHrMeasures()) {
                hrMeasures.add(mto.decode(dataOwner));
            }
            
            HrMeasureDAO.getInstance().removeByComplex(hrMeasures);
        }
        catch (HibernateException e) {
            removeHrMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeHrMeasureResponse);
        }
        
        removeHrMeasureResponse.setStatusCode(0);
        response = gson.toJson(removeHrMeasureResponse);
        return response;
    }
    
    public String uploadOxyMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadOxyMeasureRequest uploadOxyMeasureRequest;
        UploadOxyMeasureResponse uploadOxyMeasureResponse = new UploadOxyMeasureResponse();
        try {
            uploadOxyMeasureRequest = gson.fromJson(request, UploadOxyMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadOxyMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadOxyMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadOxyMeasureRequest.getUuid());
        if (statusCode < 0) {
            uploadOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadOxyMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(uploadOxyMeasureRequest.getLogin());
        if (statusCode < 0) {
            uploadOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadOxyMeasureResponse);
        }
        for (OxyMeasureTO oxy : uploadOxyMeasureRequest.getOxyMeasures()) {
            statusCode = ValidationHelper.getInstance().validateOxyMeasure(oxy.getOxy());
            if (statusCode < 0) {
                uploadOxyMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadOxyMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(oxy.getDate());
            if (statusCode < 0) {
                uploadOxyMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadOxyMeasureResponse);
            }
            if (oxy.getMeasureType() < 0 || oxy.getMeasureType() >= MeasureType.getValues().length) {
                uploadOxyMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadOxyMeasureResponse);
            }
            if (oxy.getStatusOnServer() < 0 || oxy.getStatusOnServer() >= StatusOnServer.getValues().length) {
                uploadOxyMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadOxyMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<Relation> relationList = null;
        List<Settings> settingsList = null;
        List<OxyMeasure> oxyMeasures = new ArrayList<>();
        List<OxyMeasure> oxyMeasuresAdded = null;
        List<Abnormal> abnormals = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadOxyMeasureRequest.getUuid());
            if (userAuth == null) {
                uploadOxyMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(uploadOxyMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(uploadOxyMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(uploadOxyMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    uploadOxyMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(uploadOxyMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    uploadOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(uploadOxyMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    uploadOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(uploadOxyMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    uploadOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(uploadOxyMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (OxyMeasureTO mto : uploadOxyMeasureRequest.getOxyMeasures()) {
                oxyMeasures.add(mto.decode(dataOwner));
            }
            
            oxyMeasuresAdded = OxyMeasureDAO.getInstance().addAll(oxyMeasures); //ale skąd wiemy dla których robić abnormale? nie wiemy, trzeba je wywalać z pamięci
            
            if(oxyMeasuresAdded != null) {
                relationList = RelationDAO.getInstance().getAllForPatient(dataOwner.getId());
                settingsList = SettingsDAO.getInstance().getAllForPatient(dataOwner.getId());
                    
                if (relationList != null && settingsList != null) {
                    abnormals = AbnormalHelper.getInstance().prepareAbnormalListForOxyMeasures(relationList, settingsList, oxyMeasuresAdded);
                    if(abnormals != null && !abnormals.isEmpty()) AbnormalDAO.getInstance().addAll(abnormals);
                }
            }
        }
        catch (HibernateException e) {
            uploadOxyMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadOxyMeasureResponse);
        }
        
        uploadOxyMeasureResponse.setStatusCode(0);
        response = gson.toJson(uploadOxyMeasureResponse);
        return response;
    }
    
    public String syncOxyMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncOxyMeasureRequest syncOxyMeasureRequest;
        SyncOxyMeasureResponse syncOxyMeasureResponse = new SyncOxyMeasureResponse();
        try {
            syncOxyMeasureRequest = gson.fromJson(request, SyncOxyMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncOxyMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncOxyMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncOxyMeasureRequest.getUuid());
        if (statusCode < 0) {
            syncOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncOxyMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(syncOxyMeasureRequest.getLogin());
        if (statusCode < 0) {
            syncOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncOxyMeasureResponse);
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<OxyMeasureTO> measuresToSend = new ArrayList<>();
        List<OxyMeasure> oxyMeasures = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncOxyMeasureRequest.getUuid());
            if (userAuth == null) {
                syncOxyMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(syncOxyMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(syncOxyMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(syncOxyMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    syncOxyMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(syncOxyMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    syncOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(syncOxyMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    syncOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(syncOxyMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            oxyMeasures = OxyMeasureDAO.getInstance().get(dataOwner.getId(), syncOxyMeasureRequest.getDefaultSync(), syncOxyMeasureRequest.getDateStart(), syncOxyMeasureRequest.getDateEnd());
            
            for (OxyMeasure measure : oxyMeasures) {
                measuresToSend.add(OxyMeasureTO.encode(measure));
            }
        }
        catch (HibernateException e) {
            syncOxyMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncOxyMeasureResponse);
        }
        
        syncOxyMeasureResponse.setStatusCode(0);
        syncOxyMeasureResponse.setLogin(syncOxyMeasureRequest.getLogin());
        syncOxyMeasureResponse.setOxyMeasures(measuresToSend);
        response = gson.toJson(syncOxyMeasureResponse);
        return response;
    }
    
    public String removeOxyMeasure(String request){
        String response = null;
        Gson gson = new Gson();
        RemoveOxyMeasureRequest removeOxyMeasureRequest;
        RemoveOxyMeasureResponse removeOxyMeasureResponse = new RemoveOxyMeasureResponse();
        try {
            removeOxyMeasureRequest = gson.fromJson(request, RemoveOxyMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeOxyMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeOxyMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeOxyMeasureRequest.getUuid());
        if (statusCode < 0) {
            removeOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeOxyMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeOxyMeasureRequest.getLogin());
        if (statusCode < 0) {
            removeOxyMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeOxyMeasureResponse);
        }
        for (OxyMeasureTO oxy : removeOxyMeasureRequest.getOxyMeasures()) {
            statusCode = ValidationHelper.getInstance().validateOxyMeasure(oxy.getOxy());
            if (statusCode < 0) {
                removeOxyMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeOxyMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(oxy.getDate());
            if (statusCode < 0) {
                removeOxyMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeOxyMeasureResponse);
            }
            if (oxy.getMeasureType() < 0 || oxy.getMeasureType() >= MeasureType.getValues().length) {
                removeOxyMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeOxyMeasureResponse);
            }
            if (oxy.getStatusOnServer() < 0 || oxy.getStatusOnServer() >= StatusOnServer.getValues().length) {
                removeOxyMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeOxyMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<OxyMeasure> oxyMeasures = new ArrayList<>();
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeOxyMeasureRequest.getUuid());
            if (userAuth == null) {
                removeOxyMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeOxyMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(removeOxyMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(removeOxyMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    removeOxyMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(removeOxyMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    removeOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(removeOxyMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    removeOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(removeOxyMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    removeOxyMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(removeOxyMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (OxyMeasureTO mto : removeOxyMeasureRequest.getOxyMeasures()) {
                oxyMeasures.add(mto.decode(dataOwner));
            }
            
            OxyMeasureDAO.getInstance().removeByComplex(oxyMeasures);
        }
        catch (HibernateException e) {
            removeOxyMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeOxyMeasureResponse);
        }
        
        removeOxyMeasureResponse.setStatusCode(0);
        response = gson.toJson(removeOxyMeasureResponse);
        return response;
    }
    
    public String uploadSleepMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadSleepMeasureRequest uploadSleepMeasureRequest;
        UploadSleepMeasureResponse uploadSleepMeasureResponse = new UploadSleepMeasureResponse();
        try {
            uploadSleepMeasureRequest = gson.fromJson(request, UploadSleepMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadSleepMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadSleepMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadSleepMeasureRequest.getUuid());
        if (statusCode < 0) {
            uploadSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadSleepMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(uploadSleepMeasureRequest.getLogin());
        if (statusCode < 0) {
            uploadSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(uploadSleepMeasureResponse);
        }
        for (SleepMeasureTO sl : uploadSleepMeasureRequest.getSleepMeasures()) {
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getDeep());
            if (statusCode < 0) {
                uploadSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getShallow());
            if (statusCode < 0) {
                uploadSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getTotal());
            if (statusCode < 0) {
                uploadSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSleepMeasureResponse);
            }
            if (sl.getWakeupTimes() < 0) {
                uploadSleepMeasureResponse.setStatusCode(ErrorCode.DAMAGED_MEASURE.ordinal());
                return gson.toJson(uploadSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(sl.getDate());
            if (statusCode < 0) {
                uploadSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(uploadSleepMeasureResponse);
            }
            if (sl.getMeasureType() < 0 || sl.getMeasureType() >= MeasureType.getValues().length) {
                uploadSleepMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadSleepMeasureResponse);
            }
            if (sl.getStatusOnServer() < 0 || sl.getStatusOnServer() >= StatusOnServer.getValues().length) {
                uploadSleepMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadSleepMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<Relation> relationList = null;
        List<Settings> settingsList = null;
        List<SleepMeasure> sleepMeasures = new ArrayList<>();
        List<SleepMeasure> sleepMeasuresAdded = null;
        List<Abnormal> abnormals = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadSleepMeasureRequest.getUuid());
            if (userAuth == null) {
                uploadSleepMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(uploadSleepMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(uploadSleepMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(uploadSleepMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    uploadSleepMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(uploadSleepMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    uploadSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(uploadSleepMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    uploadSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(uploadSleepMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    uploadSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(uploadSleepMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (SleepMeasureTO mto : uploadSleepMeasureRequest.getSleepMeasures()) {
                sleepMeasures.add(mto.decode(dataOwner));
            }
            
            sleepMeasuresAdded = SleepMeasureDAO.getInstance().addAll(sleepMeasures); //ale skąd wiemy dla których robić abnormale? nie wiemy, trzeba je wywalać z pamięci
            
            if(sleepMeasuresAdded != null) {
                relationList = RelationDAO.getInstance().getAllForPatient(dataOwner.getId());
                settingsList = SettingsDAO.getInstance().getAllForPatient(dataOwner.getId());
                    
                if (relationList != null && settingsList != null) {
                    abnormals = AbnormalHelper.getInstance().prepareAbnormalListForSleepMeasures(relationList, settingsList, sleepMeasuresAdded);
                    if(abnormals != null && !abnormals.isEmpty()) AbnormalDAO.getInstance().addAll(abnormals);
                }
            }
        }
        catch (HibernateException e) {
            uploadSleepMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadSleepMeasureResponse);
        }
        
        uploadSleepMeasureResponse.setStatusCode(0);
        response = gson.toJson(uploadSleepMeasureResponse);
        return response;
    }
    
    public String syncSleepMeasure(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncSleepMeasureRequest syncSleepMeasureRequest;
        SyncSleepMeasureResponse syncSleepMeasureResponse = new SyncSleepMeasureResponse();
        try {
            syncSleepMeasureRequest = gson.fromJson(request, SyncSleepMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncSleepMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncSleepMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncSleepMeasureRequest.getUuid());
        if (statusCode < 0) {
            syncSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncSleepMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(syncSleepMeasureRequest.getLogin());
        if (statusCode < 0) {
            syncSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(syncSleepMeasureResponse);
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<SleepMeasureTO> measuresToSend = new ArrayList<>();
        List<SleepMeasure> sleepMeasures = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncSleepMeasureRequest.getUuid());
            if (userAuth == null) {
                syncSleepMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(syncSleepMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(syncSleepMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(syncSleepMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    syncSleepMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(syncSleepMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    syncSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(syncSleepMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    syncSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(syncSleepMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            sleepMeasures = SleepMeasureDAO.getInstance().get(dataOwner.getId(), syncSleepMeasureRequest.getDefaultSync(), syncSleepMeasureRequest.getDateStart(), syncSleepMeasureRequest.getDateEnd());
            
            for (SleepMeasure measure : sleepMeasures) {
                measuresToSend.add(SleepMeasureTO.encode(measure));
            }
        }
        catch (HibernateException e) {
            syncSleepMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncSleepMeasureResponse);
        }
        
        syncSleepMeasureResponse.setStatusCode(0);
        syncSleepMeasureResponse.setLogin(syncSleepMeasureRequest.getLogin());
        syncSleepMeasureResponse.setSleepMeasures(measuresToSend);
        response = gson.toJson(syncSleepMeasureResponse);
        return response;
    }
    
    public String removeSleepMeasure(String request){
        String response = null;
        Gson gson = new Gson();
        RemoveSleepMeasureRequest removeSleepMeasureRequest;
        RemoveSleepMeasureResponse removeSleepMeasureResponse = new RemoveSleepMeasureResponse();
        try {
            removeSleepMeasureRequest = gson.fromJson(request, RemoveSleepMeasureRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeSleepMeasureResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeSleepMeasureResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeSleepMeasureRequest.getUuid());
        if (statusCode < 0) {
            removeSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeSleepMeasureResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeSleepMeasureRequest.getLogin());
        if (statusCode < 0) {
            removeSleepMeasureResponse.setStatusCode(statusCode);
            return gson.toJson(removeSleepMeasureResponse);
        }
        for (SleepMeasureTO sl : removeSleepMeasureRequest.getSleepMeasures()) {
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getDeep());
            if (statusCode < 0) {
                removeSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getShallow());
            if (statusCode < 0) {
                removeSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateSleepMeasure(sl.getTotal());
            if (statusCode < 0) {
                removeSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeSleepMeasureResponse);
            }
            if (sl.getWakeupTimes() < 0) {
                removeSleepMeasureResponse.setStatusCode(ErrorCode.DAMAGED_MEASURE.ordinal());
                return gson.toJson(removeSleepMeasureResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(sl.getDate());
            if (statusCode < 0) {
                removeSleepMeasureResponse.setStatusCode(statusCode);
                return gson.toJson(removeSleepMeasureResponse);
            }
            if (sl.getMeasureType() < 0 || sl.getMeasureType() >= MeasureType.getValues().length) {
                removeSleepMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeSleepMeasureResponse);
            }
            if (sl.getStatusOnServer() < 0 || sl.getStatusOnServer() >= StatusOnServer.getValues().length) {
                removeSleepMeasureResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeSleepMeasureResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<SleepMeasure> sleepMeasures = new ArrayList<>();
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeSleepMeasureRequest.getUuid());
            if (userAuth == null) {
                removeSleepMeasureResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeSleepMeasureResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(removeSleepMeasureRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(removeSleepMeasureRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    removeSleepMeasureResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(removeSleepMeasureResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    removeSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(removeSleepMeasureResponse);
                }
                if (relation.getIsApproved() == 0) {
                    removeSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(removeSleepMeasureResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    removeSleepMeasureResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(removeSleepMeasureResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (SleepMeasureTO mto : removeSleepMeasureRequest.getSleepMeasures()) {
                sleepMeasures.add(mto.decode(dataOwner));
            }
            
            SleepMeasureDAO.getInstance().removeByComplex(sleepMeasures);
        }
        catch (HibernateException e) {
            removeSleepMeasureResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeSleepMeasureResponse);
        }
        
        removeSleepMeasureResponse.setStatusCode(0);
        response = gson.toJson(removeSleepMeasureResponse);
        return response;
    }
    
    public String uploadDiaryEntry(String request) {
        String response = null;
        Gson gson = new Gson();
        UploadDiaryEntryRequest uploadDiaryEntryRequest;
        UploadDiaryEntryResponse uploadDiaryEntryResponse = new UploadDiaryEntryResponse();
        try {
            uploadDiaryEntryRequest = gson.fromJson(request, UploadDiaryEntryRequest.class);
        } 
        catch (JsonSyntaxException e) {
            uploadDiaryEntryResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(uploadDiaryEntryResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(uploadDiaryEntryRequest.getUuid());
        if (statusCode < 0) {
            uploadDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDiaryEntryResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(uploadDiaryEntryRequest.getLogin());
        if (statusCode < 0) {
            uploadDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(uploadDiaryEntryResponse);
        }
        for (DiaryEntryTO di : uploadDiaryEntryRequest.getDiaryEntries()) {
            statusCode = ValidationHelper.getInstance().validateDiaryEntry(di.getDescription());
            if (statusCode < 0) {
                uploadDiaryEntryResponse.setStatusCode(statusCode);
                return gson.toJson(uploadDiaryEntryResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(di.getDate());
            if (statusCode < 0) {
                uploadDiaryEntryResponse.setStatusCode(statusCode);
                return gson.toJson(uploadDiaryEntryResponse);
            }
            if (di.getStatusOnServer() < 0 || di.getStatusOnServer() >= StatusOnServer.getValues().length) {
                uploadDiaryEntryResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(uploadDiaryEntryResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<Relation> relationList = null;
        List<Settings> settingsList = null;
        List<DiaryEntry> diaryEntries = new ArrayList<>();
        List<DiaryEntry> diaryEntriesAdded = null;
        List<Abnormal> abnormals = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(uploadDiaryEntryRequest.getUuid());
            if (userAuth == null) {
                uploadDiaryEntryResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(uploadDiaryEntryResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(uploadDiaryEntryRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(uploadDiaryEntryRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    uploadDiaryEntryResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(uploadDiaryEntryResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    uploadDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(uploadDiaryEntryResponse);
                }
                if (relation.getIsApproved() == 0) {
                    uploadDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(uploadDiaryEntryResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    uploadDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(uploadDiaryEntryResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (DiaryEntryTO mto : uploadDiaryEntryRequest.getDiaryEntries()) {
                diaryEntries.add(mto.decode(dataOwner));
            }
            
            DiaryEntryDAO.getInstance().addAll(diaryEntries);
        }
        catch (HibernateException e) {
            uploadDiaryEntryResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(uploadDiaryEntryResponse);
        }
        
        uploadDiaryEntryResponse.setStatusCode(0);
        response = gson.toJson(uploadDiaryEntryResponse);
        return response;
    }
    
    public String syncDiaryEntry(String request) {
        String response = null;
        Gson gson = new Gson();
        SyncDiaryEntryRequest syncDiaryEntryRequest;
        SyncDiaryEntryResponse syncDiaryEntryResponse = new SyncDiaryEntryResponse();
        try {
            syncDiaryEntryRequest = gson.fromJson(request, SyncDiaryEntryRequest.class);
        } 
        catch (JsonSyntaxException e) {
            syncDiaryEntryResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(syncDiaryEntryResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(syncDiaryEntryRequest.getUuid());
        if (statusCode < 0) {
            syncDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(syncDiaryEntryResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(syncDiaryEntryRequest.getLogin());
        if (statusCode < 0) {
            syncDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(syncDiaryEntryResponse);
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<DiaryEntryTO> measuresToSend = new ArrayList<>();
        List<DiaryEntry> diaryEntries = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(syncDiaryEntryRequest.getUuid());
            if (userAuth == null) {
                syncDiaryEntryResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(syncDiaryEntryResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(syncDiaryEntryRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(syncDiaryEntryRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    syncDiaryEntryResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(syncDiaryEntryResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    syncDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(syncDiaryEntryResponse);
                }
                if (relation.getIsApproved() == 0) {
                    syncDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(syncDiaryEntryResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            diaryEntries = DiaryEntryDAO.getInstance().get(dataOwner.getId(), syncDiaryEntryRequest.getDefaultSync(), syncDiaryEntryRequest.getDateStart(), syncDiaryEntryRequest.getDateEnd());
            
            for (DiaryEntry measure : diaryEntries) {
                measuresToSend.add(DiaryEntryTO.encode(measure));
            }
        }
        catch (HibernateException e) {
            syncDiaryEntryResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(syncDiaryEntryResponse);
        }
        
        syncDiaryEntryResponse.setStatusCode(0);
        syncDiaryEntryResponse.setLogin(syncDiaryEntryRequest.getLogin());
        syncDiaryEntryResponse.setDiaryEntries(measuresToSend);
        response = gson.toJson(syncDiaryEntryResponse);
        return response;
    }
    
    public String removeDiaryEntry(String request){
        String response = null;
        Gson gson = new Gson();
        RemoveDiaryEntryRequest removeDiaryEntryRequest;
        RemoveDiaryEntryResponse removeDiaryEntryResponse = new RemoveDiaryEntryResponse();
        try {
            removeDiaryEntryRequest = gson.fromJson(request, RemoveDiaryEntryRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeDiaryEntryResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeDiaryEntryResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeDiaryEntryRequest.getUuid());
        if (statusCode < 0) {
            removeDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(removeDiaryEntryResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeDiaryEntryRequest.getLogin());
        if (statusCode < 0) {
            removeDiaryEntryResponse.setStatusCode(statusCode);
            return gson.toJson(removeDiaryEntryResponse);
        }
        for (DiaryEntryTO di : removeDiaryEntryRequest.getDiaryEntries()) {
            statusCode = ValidationHelper.getInstance().validateDiaryEntry(di.getDescription());
            if (statusCode < 0) {
                removeDiaryEntryResponse.setStatusCode(statusCode);
                return gson.toJson(removeDiaryEntryResponse);
            }
            statusCode = ValidationHelper.getInstance().validateDate(di.getDate());
            if (statusCode < 0) {
                removeDiaryEntryResponse.setStatusCode(statusCode);
                return gson.toJson(removeDiaryEntryResponse);
            }
            if (di.getStatusOnServer() < 0 || di.getStatusOnServer() >= StatusOnServer.getValues().length) {
                removeDiaryEntryResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(removeDiaryEntryResponse);
            }
        }
        
        UserAuth userAuth = null;
        User dataOwner = null;
        User patient = null;
        Relation relation = null;
        List<DiaryEntry> diaryEntries = new ArrayList<>();
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeDiaryEntryRequest.getUuid());
            if (userAuth == null) {
                removeDiaryEntryResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeDiaryEntryResponse);
            }
            
            if (!userAuth.getUser().getLogin().equals(removeDiaryEntryRequest.getLogin())) {
        
                patient = UserDAO.getInstance().getByLogin(removeDiaryEntryRequest.getLogin());
                if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                    removeDiaryEntryResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                    return gson.toJson(removeDiaryEntryResponse);
                }
            
                relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
                if (relation == null) {
                    removeDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                    return gson.toJson(removeDiaryEntryResponse);
                }
                if (relation.getIsApproved() == 0) {
                    removeDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_NOT_APPROVED.getErrorCode());
                    return gson.toJson(removeDiaryEntryResponse);
                }
                if (relation.getIsReadWrite() == 0) {
                    removeDiaryEntryResponse.setStatusCode(ErrorCode.RELATION_NOT_READWRITE.getErrorCode());
                    return gson.toJson(removeDiaryEntryResponse);
                }
                
                dataOwner = patient;
            }
            else dataOwner = userAuth.getUser();
                
            for (DiaryEntryTO mto : removeDiaryEntryRequest.getDiaryEntries()) {
                diaryEntries.add(mto.decode(dataOwner));
            }
            
            DiaryEntryDAO.getInstance().removeByComplex(diaryEntries);
        }
        catch (HibernateException e) {
            removeDiaryEntryResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeDiaryEntryResponse);
        }
        
        removeDiaryEntryResponse.setStatusCode(0);
        response = gson.toJson(removeDiaryEntryResponse);
        return response;
    }
    
}
