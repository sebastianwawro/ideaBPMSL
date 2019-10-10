package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum AbnormalType {
    BP_HT_STAGE_1,
    BP_HT_STAGE_2,
    BP_HT_STAGE_3,
    BP_LOW,
    HR_HIGH,
    HR_LOW,
    OXY_LOW,
    DEEP_SLEEP_LOW_TIME,
    DEEP_SLEEP_LOW_PER,
    BP_CUSTOM_HIGH,
    BP_CUSTOM_LOW,
    HR_CUSTOM_HIGH,
    HR_CUSTOM_LOW,
    OXY_CUSTOM_LOW,
    DEEP_SLEEP_CUSTOM_LOW_TIME,
    DEEP_SLEEP_CUSTOM_LOW_PER;

    private static final AbnormalType values[] = values();

    public static AbnormalType[] getValues() {
        return values;
    }
}
