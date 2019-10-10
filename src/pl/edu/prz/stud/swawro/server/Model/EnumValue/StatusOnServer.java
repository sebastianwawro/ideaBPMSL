package pl.edu.prz.stud.swawro.server.Model.EnumValue;

public enum StatusOnServer {
    NOT_SYNCED,
    SYNCED,
    TO_BE_DELETED;

    private static final StatusOnServer values[] = values();

    public static StatusOnServer[] getValues() {
        return values;
    }
}
