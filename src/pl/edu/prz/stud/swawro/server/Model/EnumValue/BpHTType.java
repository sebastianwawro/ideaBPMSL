package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum BpHTType { //stage 1 2 3
    DISABLED,
    STAGE_1,
    STAGE_2,
    STAGE_3;

    private static final BpHTType values[] = values();

    public static BpHTType[] getValues() {
        return values;
    }
}
