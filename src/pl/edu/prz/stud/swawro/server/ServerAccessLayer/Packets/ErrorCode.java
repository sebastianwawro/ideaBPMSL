package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets;

public enum ErrorCode {
    INCONSISTENT_DATA(-1, "Niezgodne dane"),
    NONEXISTENT_PACKET(-2, "Nieistniejący pakiet"),
    PROCESSING_ERROR(-3, "Błąd przetwarzania"),
    INTERNAL_ERROR(-4, "Błąd wewnętrzny serwera"), //todo: internal error jak baza wywali
    NOT_LOGGED_IN(-5, "Nie jesteś zalogowany"),
    USERNAME_EMPTY(-6, "Nazwa użytkownika jest pusta"),
    USERNAME_TOO_SHORT(-7, "Nazwa użytkownika jest za krótka"),
    USERNAME_TOO_LONG(-8, "Nazwa użytkownika jest za długa"),
    USERNAME_UNALLOWED_SIGNS (-9, "Nazwa użytkownika zawiera niedozwolone znaki"),
    PASSWORD_EMPTY(-10, "Hasło jest puste"),
    PASSWORD_TOO_SHORT(-11, "Hasło jest za krótkie"),
    PASSWORD_TOO_LONG(-12, "Hasło jest za długie"),
    PASSWORD_UNALLOWED_SIGNS (-13, "Hasło zawiera niedozwolone znaki"),
    NAME_EMPTY(-14, "Imię jest puste"),
    NAME_TOO_SHORT(-15, "Imię jest za krótkie"),
    NAME_TOO_LONG(-16, "Imię jest za długie"),
    NAME_UNALLOWED_SIGNS (-17, "Imię zawiera niedozwolone znaki"),
    SURNAME_EMPTY(-18, "Nazwisko jest puste"),
    SURNAME_TOO_SHORT(-19, "Nazwisko jest za krótkie"),
    SURNAME_TOO_LONG(-20, "Nazwisko jest za długie"),
    SURNAME_UNALLOWED_SIGNS (-21, "Nazwisko zawiera niedozwolone znaki"),
    EMAIL_EMPTY(-22, "Email jest pusty"),
    EMAIL_TOO_SHORT(-23, "Email jest za krótki"),
    EMAIL_TOO_LONG(-24, "Email jest za długi"),
    EMAIL_NOT_VALID (-25, "Email zawiera niedozwolone znaki"),
    PHONE_EMPTY(-26, "Numer telefonu jest pusty"),
    PHONE_TOO_SHORT(-27, "Numer telefonu jest za krótki"),
    PHONE_TOO_LONG(-28, "Numer telefonu jest za długi"),
    PHONE_UNALLOWED_SIGNS (-29, "Numer telefonu zawiera niedozwolone znaki"),
    DIARY_EMPTY(-30, "Wpis jest pusty"),
    DIARY_TOO_SHORT(-31, "Wpis jest za krótki"),
    DIARY_TOO_LONG(-32, "Wpis jest za długi"),
    DIARY_UNALLOWED_SIGNS (-33, "Wpis zawiera niedozwolone znaki"),
    CORRUPED_ENUM_DATA (-34, "Niepoprawne dane"),
    INVALID_DATE(-35, "Niepoprawna data"),
    DAMAGED_MEASURE(-36, "Uszkodzony pomiar"),
    USERNAME_TAKEN(-37, "Nazwa użytkownika jest zajęta"),
    ROLE_INVALID(-38, "Niepoprawna rola"),
    USERNAME_NOT_FOUND(-39, "Nie znaleziono użytkownika"),
    PASSWORD_INVALID(-40, "Niepoprawne hasło"),
    UUID_INVALID(-41, "Błąd sesji, zaloguj się ponownie"),
    SEARCHSTRING_EMPTY(-42, "Wpis jest pusty"),
    SEARCHSTRING_TOO_SHORT(-43, "Wpis jest za krótki"),
    SEARCHSTRING_TOO_LONG(-44, "Wpis jest za długi"),
    SEARCHSTRING_UNALLOWED_SIGNS (-45, "Wpis zawiera niedozwolone znaki"),
    USERS_NOT_FOUND(-46, "Nikogo nie znaleziono"),
    NOTHING_RECEIVED(-47, "Nie otrzymano żadnych danych"),
    SETTINGS_FATAL_INVALID(-48, "Przesłano całkowicie uszkodzone ustawienia"),
    BPHTTYPE_INVALID(-49, "Złe domyślne ustawienie nadciśnienia tętniczego"),
    BPLOWABTYPE_INVALID(-50, "Złe domyślne ustawienie niedociśnienia tętnicznego"),
    HRHIGHABTYPE_INVALID(-51, "Złe domyślne ustawienie nadpulsu"),
    HRLOWABTYPE_INVALID(-52, "Złe domyślne ustawienie podpulsu"),
    OXYLOWABTYPE_INVALID(-53, "Złe domyślne ustawienie tlenu we krwi"),
    DEEPSLEEPLOWABTYPE_INVALID(-54, "Złe domyślne ustawienie głębokiego snu"),
    SETTINGS_HACK_ATTEMPT(-55, "Próba ingerencji w cudze dane!"),
    DOCTOR_ALREADY_APPROVED(-56, "Doktor został już zatwierdzony"),
    DOCTOR_NOT_APPROVED(-57, "Doktor nie został jeszcze zatwierdzony przez administrację"),
    RELATION_ALREADY_EXISTS(-58, "Powiązanie już istnieje"),
    RELATION_DOES_NOT_EXIST(-59, "Powiązanie nie istnieje"),
    ACCEPT_NOT_ALLOWED(-60, "Nie możesz zatwierdzić powiązania które samemu zaproponowałeś"),
    RELATION_NOT_APPROVED(-61, "Powiązanie nie zostało zatwierdzone przez drugą stronę"),
    RELATION_NOT_READWRITE(-62, "Nie masz uprawnień zapisu");

    private int errorCode;
    private String description;

    private ErrorCode(int e, String d) {
        errorCode=e;
        description=d;
    }

    private static final ErrorCode values[] = values();

    private static ErrorCode[] getValues() {
        return values;
    }

    public static String decryptError(int err) {
        String desc="Nieznany błąd";
        for (ErrorCode ec : getValues()) {
            if (ec.errorCode == err) {
                desc = ec.description;
                break;
            }
        }
        return desc;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
