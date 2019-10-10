/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.Model.EnumValue;

/**
 *
 * @author Sebastian
 */
public enum UserDataChangeType {
    REGISTER,
    UPDATE;

    private static final UserDataChangeType values[] = values();

    public static UserDataChangeType[] getValues() {
        return values;
    }
}
