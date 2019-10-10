package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum UserRole {
    PATIENT,
    DOCTOR,
    ADMIN;

    private static final UserRole values[] = values();

    public static UserRole[] getValues() {
        return values;
    }
}
