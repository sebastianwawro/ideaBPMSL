/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller.helpers;

import java.util.List;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.UserRole;
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.Model.User;
import pl.edu.prz.stud.swawro.server.Model.UserAuth;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.SmartSearchTO;

/**
 *
 * @author Sebastian
 */
public class RelationHelper {
    private static final RelationHelper _instance = new RelationHelper();
    
    private RelationHelper(){}
    
    public static final RelationHelper getInstance() {
        return _instance;
    }
    
    public SmartSearchTO parseSearch(UserAuth userAuth, User foundUser, List<Relation> relations) {
        SmartSearchTO smartSearchTO = new SmartSearchTO();
        smartSearchTO.setUserId(foundUser.getId());
        smartSearchTO.setLogin(foundUser.getLogin());
        smartSearchTO.setRole(foundUser.getRole());
        smartSearchTO.setIsMyDoctor(0);
        smartSearchTO.setIsMyDoctorApproved(0);
        smartSearchTO.setIsMyPatient(0);
        smartSearchTO.setIsMyPatientApproved(0);
        smartSearchTO.setIsApproved(foundUser.getIsApproved());
        //todo doctorType myselfType - currently unused

        if (relations != null) {
            //check if is my doctor
            for (Relation rel : relations) {
                if (rel.getUser().getId() != userAuth.getUser().getId()) {
                    continue;
                }
                if (rel.getDoctor().getId() != foundUser.getId()) {
                    continue;
                }
                smartSearchTO.setIsMyDoctor(1);
                if (rel.getIsApproved() == 1) {
                    smartSearchTO.setIsMyDoctorApproved(1);
                }
            }

            //check if is my patient if (smartSearchTO.getIsMyDoctor() == 0) źle bo może być w dwie strony
            for (Relation rel : relations) {
                if (rel.getDoctor().getId() != userAuth.getUser().getId()) {
                    continue;
                }
                if (rel.getUser().getId() != foundUser.getId()) {
                    continue;
                }
                smartSearchTO.setIsMyPatient(1);
                if (rel.getIsApproved() == 1) {
                    smartSearchTO.setIsMyPatientApproved(1);
                }
            }
        }

        if (foundUser.getRole() == UserRole.DOCTOR.ordinal() || 
                smartSearchTO.getIsMyDoctor() == 1 && smartSearchTO.getIsMyDoctorApproved()== 1 ||  //w dwie strony może być!
                smartSearchTO.getIsMyPatient()== 1 && smartSearchTO.getIsMyPatientApproved()== 1 ) {
            smartSearchTO.setName(foundUser.getName());
            smartSearchTO.setSurname(foundUser.getSurname());
            smartSearchTO.setEmail(foundUser.getEmail());
            smartSearchTO.setPhone(foundUser.getPhone());
        }
        return smartSearchTO;
    }
    
}
