package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum WhoProposed {
    PATIENT,
    DOCTOR;

    private static final WhoProposed values[] = values();

    public static WhoProposed[] getValues() {
        return values;
    }
}
