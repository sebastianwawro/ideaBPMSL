package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum MeasureType {
    MANUAL,
    EXTERNAL,
    FROM_DEVICE;

    private static final MeasureType values[] = values();

    public static MeasureType[] getValues() {
        return values;
    }
}
