/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.hibernate.HibernateException;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.DoctorType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserDataChangeType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserRole;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.WhoProposed;
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.Model.UserAuth;
import pl.edu.prz.stud.swawro.server.Model.UserDataHistory;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.ApproveDoctorRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.ApproveDoctorResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.AddAsDoctorRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.AddAsDoctorResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.AddAsPatientRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.AddAsPatientResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.AcceptAsDoctorRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.AcceptAsDoctorResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.AcceptAsPatientRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.AcceptAsPatientResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemoveDoctorRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemoveDoctorResponse;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s.RemovePatientRequest;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c.RemovePatientResponse;
import pl.edu.prz.stud.swawro.server.controller.helpers.SecurityHelper;
import pl.edu.prz.stud.swawro.server.controller.helpers.ValidationHelper;
import pl.edu.prz.stud.swawro.server.database.dao.RelationDAO;
import pl.edu.prz.stud.swawro.server.database.dao.SettingsDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserAuthDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserDAO;
import pl.edu.prz.stud.swawro.server.database.dao.UserDataHistoryDAO;

/**
 *
 * @author Sebastian
 */
public class RelationsController {
    private static final RelationsController _instance = new RelationsController();
    
    private RelationsController(){}
    
    public static final RelationsController getInstance() {
        return _instance;
    }
    
    public String approveDoctor(String request) { //UserDataHistory //WARNING: SETTINGS/DATA/RELATION ID nie sa updatowane
        String response = null;
        Gson gson = new Gson();
        ApproveDoctorRequest approveDoctorRequest;
        ApproveDoctorResponse approveDoctorResponse = new ApproveDoctorResponse();
        try {
            approveDoctorRequest = gson.fromJson(request, ApproveDoctorRequest.class);
        } 
        catch (JsonSyntaxException e) {
            approveDoctorResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(approveDoctorResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(approveDoctorRequest.getUuid());
        if (statusCode < 0) {
            approveDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(approveDoctorResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(approveDoctorRequest.getDoctorLogin());
        if (statusCode < 0) {
            approveDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(approveDoctorResponse);
        }
        
        UserAuth userAuth = null;
        User doctor = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(approveDoctorRequest.getUuid());
            if (userAuth == null) {
                approveDoctorResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(approveDoctorResponse);
            }
            if (userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                approveDoctorResponse.setStatusCode(ErrorCode.ROLE_INVALID.getErrorCode());
                return gson.toJson(approveDoctorResponse);
            }
            
            doctor = UserDAO.getInstance().getByLogin(approveDoctorRequest.getDoctorLogin());
            if (doctor == null) {
                approveDoctorResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(approveDoctorResponse);
            }
            if (doctor.getIsApproved() == 1) {
                approveDoctorResponse.setStatusCode(ErrorCode.DOCTOR_ALREADY_APPROVED.getErrorCode());
                return gson.toJson(approveDoctorResponse);
            }
            doctor.setIsApproved(1);
            UserDataHistory userDataHistory = UserDataHistory.copyCurrent(doctor, UserDataChangeType.UPDATE);
            UserDAO.getInstance().update(doctor);
            UserDataHistoryDAO.getInstance().add(userDataHistory);
        }
        catch (HibernateException e) {
            approveDoctorResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(approveDoctorResponse);
        }
        
        approveDoctorResponse.setStatusCode(0);
        response = gson.toJson(approveDoctorResponse);
        return response;
        
    }
    
    public String addAsDoctor(String request) {
        String response = null;
        Gson gson = new Gson();
        AddAsDoctorRequest addAsDoctorRequest;
        AddAsDoctorResponse addAsDoctorResponse = new AddAsDoctorResponse();
        try {
            addAsDoctorRequest = gson.fromJson(request, AddAsDoctorRequest.class);
        } 
        catch (JsonSyntaxException e) {
            addAsDoctorResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(addAsDoctorResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(addAsDoctorRequest.getUuid());
        if (statusCode < 0) {
            addAsDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(addAsDoctorResponse);
        }
        if (addAsDoctorRequest.getDoctorType() < 0 || addAsDoctorRequest.getDoctorType() >= DoctorType.getValues().length) {
            addAsDoctorResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
            return gson.toJson(addAsDoctorResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(addAsDoctorRequest.getDoctorLogin());
        if (statusCode < 0) {
            addAsDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(addAsDoctorResponse);
        }
        
        UserAuth userAuth = null;
        User doctor = null;
        Relation relation = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(addAsDoctorRequest.getUuid());
            if (userAuth == null) {
                addAsDoctorResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(addAsDoctorResponse);
            }
        
            doctor = UserDAO.getInstance().getByLogin(addAsDoctorRequest.getDoctorLogin());
            if (doctor == null || doctor.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                addAsDoctorResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(addAsDoctorResponse);
            }
            if (doctor.getRole() == UserRole.DOCTOR.ordinal() && doctor.getIsApproved() == 0) {
                addAsDoctorResponse.setStatusCode(ErrorCode.DOCTOR_NOT_APPROVED.getErrorCode());
                return gson.toJson(addAsDoctorResponse);
            }
            if (doctor.getRole() == UserRole.PATIENT.ordinal() && addAsDoctorRequest.getDoctorType() != DoctorType.INSIGHT_TO_DOCUMENTATION.ordinal()) {
                addAsDoctorResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(addAsDoctorResponse);
            }
            
            relation = RelationDAO.getInstance().get(doctor.getId(), userAuth.getUser().getId());
            if (relation != null) {
                addAsDoctorResponse.setStatusCode(ErrorCode.RELATION_ALREADY_EXISTS.getErrorCode());
                return gson.toJson(addAsDoctorResponse);
            }
            
            relation = new Relation();
            relation.setDoctor(doctor);
            relation.setUser(userAuth.getUser());
            relation.setDateStart(SecurityHelper.getInstance().getCurrentTime());
            relation.setDateEnd(-1);
            relation.setIsActive(1);
            relation.setIsApproved(0);
            relation.setIsReadWrite(0);
            relation.setType(addAsDoctorRequest.getDoctorType());
            relation.setWhoProposed(WhoProposed.PATIENT.ordinal());
            
            RelationDAO.getInstance().add(relation);
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            doctor.setRelationsUid(doctor.getRelationsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(doctor);
        }
        catch (HibernateException e) {
            addAsDoctorResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(addAsDoctorResponse);
        }
        
        addAsDoctorResponse.setStatusCode(0);
        response = gson.toJson(addAsDoctorResponse);
        return response;
        
    }
    
    public String addAsPatient(String request) { //doktor może tylko wtedy gdy approveda ma
        String response = null;
        Gson gson = new Gson();
        AddAsPatientRequest addAsPatientRequest;
        AddAsPatientResponse addAsPatientResponse = new AddAsPatientResponse();
        try {
            addAsPatientRequest = gson.fromJson(request, AddAsPatientRequest.class);
        } 
        catch (JsonSyntaxException e) {
            addAsPatientResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(addAsPatientResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(addAsPatientRequest.getUuid());
        if (statusCode < 0) {
            addAsPatientResponse.setStatusCode(statusCode);
            return gson.toJson(addAsPatientResponse);
        }
        if (addAsPatientRequest.getDoctorType() < 0 || addAsPatientRequest.getDoctorType() >= DoctorType.getValues().length) {
            addAsPatientResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
            return gson.toJson(addAsPatientResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(addAsPatientRequest.getPatientLogin());
        if (statusCode < 0) {
            addAsPatientResponse.setStatusCode(statusCode);
            return gson.toJson(addAsPatientResponse);
        }
        
        UserAuth userAuth = null;
        User patient = null;
        Relation relation = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(addAsPatientRequest.getUuid());
            if (userAuth == null) {
                addAsPatientResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(addAsPatientResponse);
            }
            if (userAuth.getUser().getRole() == UserRole.DOCTOR.ordinal() && userAuth.getUser().getIsApproved() == 0) {
                addAsPatientResponse.setStatusCode(ErrorCode.DOCTOR_NOT_APPROVED.getErrorCode());
                return gson.toJson(addAsPatientResponse);
            }
            if (userAuth.getUser().getRole() == UserRole.PATIENT.ordinal() && addAsPatientRequest.getDoctorType() != DoctorType.INSIGHT_TO_DOCUMENTATION.ordinal()) {
                addAsPatientResponse.setStatusCode(ErrorCode.CORRUPED_ENUM_DATA.getErrorCode());
                return gson.toJson(addAsPatientResponse);
            }
        
            patient = UserDAO.getInstance().getByLogin(addAsPatientRequest.getPatientLogin());
            if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                addAsPatientResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(addAsPatientResponse);
            }
            
            relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
            if (relation != null) {
                addAsPatientResponse.setStatusCode(ErrorCode.RELATION_ALREADY_EXISTS.getErrorCode());
                return gson.toJson(addAsPatientResponse);
            }
            
            relation = new Relation();
            relation.setDoctor(userAuth.getUser());
            relation.setUser(patient);
            relation.setDateStart(SecurityHelper.getInstance().getCurrentTime());
            relation.setDateEnd(-1);
            relation.setIsActive(1);
            relation.setIsApproved(0);
            relation.setIsReadWrite(0);
            relation.setType(addAsPatientRequest.getDoctorType());
            relation.setWhoProposed(WhoProposed.DOCTOR.ordinal());
            
            RelationDAO.getInstance().add(relation);
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            patient.setRelationsUid(patient.getRelationsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(patient);
        }
        catch (HibernateException e) {
            addAsPatientResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(addAsPatientResponse);
        }
        
        addAsPatientResponse.setStatusCode(0);
        response = gson.toJson(addAsPatientResponse);
        return response;
        
    }
    
    public String acceptAsDoctor(String request) { //dodaj settings domyślne
        String response = null;
        Gson gson = new Gson();
        AcceptAsDoctorRequest acceptAsDoctorRequest;
        AcceptAsDoctorResponse acceptAsDoctorResponse = new AcceptAsDoctorResponse();
        try {
            acceptAsDoctorRequest = gson.fromJson(request, AcceptAsDoctorRequest.class);
        } 
        catch (JsonSyntaxException e) {
            acceptAsDoctorResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(acceptAsDoctorResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(acceptAsDoctorRequest.getUuid());
        if (statusCode < 0) {
            acceptAsDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(acceptAsDoctorResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(acceptAsDoctorRequest.getDoctorLogin());
        if (statusCode < 0) {
            acceptAsDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(acceptAsDoctorResponse);
        }
        
        UserAuth userAuth = null;
        User doctor = null;
        Relation relation = null;
        Settings settings = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(acceptAsDoctorRequest.getUuid());
            if (userAuth == null) {
                acceptAsDoctorResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(acceptAsDoctorResponse);
            }
        
            doctor = UserDAO.getInstance().getByLogin(acceptAsDoctorRequest.getDoctorLogin());
            if (doctor == null || doctor.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                acceptAsDoctorResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(acceptAsDoctorResponse);
            }
            if (doctor.getRole() == UserRole.DOCTOR.ordinal() && doctor.getIsApproved() == 0) {
                acceptAsDoctorResponse.setStatusCode(ErrorCode.DOCTOR_NOT_APPROVED.getErrorCode());
                return gson.toJson(acceptAsDoctorResponse);
            }
            
            relation = RelationDAO.getInstance().get(doctor.getId(), userAuth.getUser().getId());
            if (relation == null) {
                acceptAsDoctorResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                return gson.toJson(acceptAsDoctorResponse);
            }
            if (relation.getWhoProposed() != WhoProposed.DOCTOR.ordinal()) {
                acceptAsDoctorResponse.setStatusCode(ErrorCode.ACCEPT_NOT_ALLOWED.getErrorCode());
                return gson.toJson(acceptAsDoctorResponse);
            }
            relation.setIsApproved(1);
            
            RelationDAO.getInstance().update(relation);
            
            settings = SettingsDAO.getInstance().get(doctor.getId(), userAuth.getUser().getId());
            if (settings == null) {
                settings = new Settings();
                settings.setDoctor(doctor);
                settings.setUser(userAuth.getUser());
                settings.restoreDefault();
                SettingsDAO.getInstance().add(settings);
            }
            /*else {
                settings.restoreDefault();
                SettingsDAO.getInstance().update(settings);
            }*/
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            doctor.setRelationsUid(doctor.getRelationsUid()+1);
            userAuth.getUser().setSettingsUid(userAuth.getUser().getSettingsUid()+1);
            doctor.setSettingsUid(doctor.getSettingsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(doctor);
        }
        catch (HibernateException e) {
            acceptAsDoctorResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(acceptAsDoctorResponse);
        }
        
        acceptAsDoctorResponse.setStatusCode(0);
        response = gson.toJson(acceptAsDoctorResponse);
        return response;
        
    }
    
    public String acceptAsPatient(String request) {
        String response = null;
        Gson gson = new Gson();
        AcceptAsPatientRequest acceptAsPatientRequest;
        AcceptAsPatientResponse acceptAsPatientResponse = new AcceptAsPatientResponse();
        try {
            acceptAsPatientRequest = gson.fromJson(request, AcceptAsPatientRequest.class);
        } 
        catch (JsonSyntaxException e) {
            acceptAsPatientResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(acceptAsPatientResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(acceptAsPatientRequest.getUuid());
        if (statusCode < 0) {
            acceptAsPatientResponse.setStatusCode(statusCode);
            return gson.toJson(acceptAsPatientResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(acceptAsPatientRequest.getPatientLogin());
        if (statusCode < 0) {
            acceptAsPatientResponse.setStatusCode(statusCode);
            return gson.toJson(acceptAsPatientResponse);
        }
        
        UserAuth userAuth = null;
        User patient = null;
        Relation relation = null;
        Settings settings = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(acceptAsPatientRequest.getUuid());
            if (userAuth == null) {
                acceptAsPatientResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(acceptAsPatientResponse);
            }
        
            patient = UserDAO.getInstance().getByLogin(acceptAsPatientRequest.getPatientLogin());
            if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                acceptAsPatientResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(acceptAsPatientResponse);
            }
            if (userAuth.getUser().getRole() == UserRole.DOCTOR.ordinal() && userAuth.getUser().getIsApproved() == 0) {
                acceptAsPatientResponse.setStatusCode(ErrorCode.DOCTOR_NOT_APPROVED.getErrorCode());
                return gson.toJson(acceptAsPatientResponse);
            }
            
            relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
            if (relation == null) {
                acceptAsPatientResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                return gson.toJson(acceptAsPatientResponse);
            }
            if (relation.getWhoProposed() != WhoProposed.PATIENT.ordinal()) {
                acceptAsPatientResponse.setStatusCode(ErrorCode.ACCEPT_NOT_ALLOWED.getErrorCode());
                return gson.toJson(acceptAsPatientResponse);
            }
            relation.setIsApproved(1);
            
            RelationDAO.getInstance().update(relation);
            
            settings = SettingsDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
            if (settings == null) {
                settings = new Settings();
                settings.setDoctor(userAuth.getUser());
                settings.setUser(patient);
                settings.restoreDefault();
                SettingsDAO.getInstance().add(settings);
            }
            /*else {
                settings.restoreDefault();
                SettingsDAO.getInstance().update(settings);
            }*/
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            patient.setRelationsUid(patient.getRelationsUid()+1);
            userAuth.getUser().setSettingsUid(userAuth.getUser().getSettingsUid()+1);
            patient.setSettingsUid(patient.getSettingsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(patient);
        }
        catch (HibernateException e) {
            acceptAsPatientResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(acceptAsPatientResponse);
        }
        
        acceptAsPatientResponse.setStatusCode(0);
        response = gson.toJson(acceptAsPatientResponse);
        return response;
        
    }
    
    public String removeDoctor(String request) {
        String response = null;
        Gson gson = new Gson();
        RemoveDoctorRequest removeDoctorRequest;
        RemoveDoctorResponse removeDoctorResponse = new RemoveDoctorResponse();
        try {
            removeDoctorRequest = gson.fromJson(request, RemoveDoctorRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removeDoctorResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removeDoctorResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removeDoctorRequest.getUuid());
        if (statusCode < 0) {
            removeDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(removeDoctorResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removeDoctorRequest.getDoctorLogin());
        if (statusCode < 0) {
            removeDoctorResponse.setStatusCode(statusCode);
            return gson.toJson(removeDoctorResponse);
        }
        
        UserAuth userAuth = null;
        User doctor = null;
        Relation relation = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removeDoctorRequest.getUuid());
            if (userAuth == null) {
                removeDoctorResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removeDoctorResponse);
            }
        
            doctor = UserDAO.getInstance().getByLogin(removeDoctorRequest.getDoctorLogin());
            if (doctor == null || doctor.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                removeDoctorResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(removeDoctorResponse);
            }
            
            relation = RelationDAO.getInstance().get(doctor.getId(), userAuth.getUser().getId());
            if (relation == null) {
                removeDoctorResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                return gson.toJson(removeDoctorResponse);
            }
            
            relation.setDateEnd(SecurityHelper.getInstance().getCurrentTime());
            relation.setIsActive(0);
            
            RelationDAO.getInstance().update(relation);
            
            Settings set = SettingsDAO.getInstance().get(relation.getDoctor().getId(), relation.getUser().getId());
            SettingsDAO.getInstance().remove(set);
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            doctor.setRelationsUid(doctor.getRelationsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(doctor);
        }
        catch (HibernateException e) {
            removeDoctorResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removeDoctorResponse);
        }
        
        removeDoctorResponse.setStatusCode(0);
        response = gson.toJson(removeDoctorResponse);
        return response;
    }
    
    public String removePatient(String request) {
        String response = null;
        Gson gson = new Gson();
        RemovePatientRequest removePatientRequest;
        RemovePatientResponse removePatientResponse = new RemovePatientResponse();
        try {
            removePatientRequest = gson.fromJson(request, RemovePatientRequest.class);
        } 
        catch (JsonSyntaxException e) {
            removePatientResponse.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
            return gson.toJson(removePatientResponse);
        }
        int statusCode;
        
        statusCode = ValidationHelper.getInstance().validateUuid(removePatientRequest.getUuid());
        if (statusCode < 0) {
            removePatientResponse.setStatusCode(statusCode);
            return gson.toJson(removePatientResponse);
        }
        statusCode = ValidationHelper.getInstance().validateUsername(removePatientRequest.getPatientLogin());
        if (statusCode < 0) {
            removePatientResponse.setStatusCode(statusCode);
            return gson.toJson(removePatientResponse);
        }
        
        UserAuth userAuth = null;
        User patient = null;
        Relation relation = null;
        try {
            userAuth = UserAuthDAO.getInstance().getByUuid(removePatientRequest.getUuid());
            if (userAuth == null) {
                removePatientResponse.setStatusCode(ErrorCode.UUID_INVALID.getErrorCode());
                return gson.toJson(removePatientResponse);
            }
        
            patient = UserDAO.getInstance().getByLogin(removePatientRequest.getPatientLogin());
            if (patient == null || patient.getRole()==UserRole.ADMIN.ordinal() && userAuth.getUser().getRole() != UserRole.ADMIN.ordinal()) {
                removePatientResponse.setStatusCode(ErrorCode.USERNAME_NOT_FOUND.getErrorCode());
                return gson.toJson(removePatientResponse);
            }
            
            relation = RelationDAO.getInstance().get(userAuth.getUser().getId(), patient.getId());
            if (relation == null) {
                removePatientResponse.setStatusCode(ErrorCode.RELATION_DOES_NOT_EXIST.getErrorCode());
                return gson.toJson(removePatientResponse);
            }
            
            relation.setDateEnd(SecurityHelper.getInstance().getCurrentTime());
            relation.setIsActive(0);
            
            RelationDAO.getInstance().update(relation);
            
            Settings set = SettingsDAO.getInstance().get(relation.getDoctor().getId(), relation.getUser().getId());
            SettingsDAO.getInstance().remove(set);
            
            userAuth.getUser().setRelationsUid(userAuth.getUser().getRelationsUid()+1);
            patient.setRelationsUid(patient.getRelationsUid()+1);
            UserDAO.getInstance().update(userAuth.getUser());
            UserDAO.getInstance().update(patient);
        }
        catch (HibernateException e) {
            removePatientResponse.setStatusCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            return gson.toJson(removePatientResponse);
        }
        
        removePatientResponse.setStatusCode(0);
        response = gson.toJson(removePatientResponse);
        return response;
        
    }
    
}
