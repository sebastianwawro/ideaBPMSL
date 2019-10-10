package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum DoctorType {
    PRIMARY,
    AUXILARY,
    ASSOCIATE,
    INSIGHT_TO_DOCUMENTATION;

    private static final DoctorType values[] = values();

    public static DoctorType[] getValues() {
        return values;
    }
}
