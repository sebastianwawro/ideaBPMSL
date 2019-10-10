/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller.helpers;

import java.util.regex.Pattern;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;

/**
 *
 * @author Sebastian
 */
public class ValidationHelper {
    private static final ValidationHelper _instance = new ValidationHelper();
    
    private ValidationHelper(){}
    
    public static final ValidationHelper getInstance() {
        return _instance;
    }
    
    private String removeSpaces(String input) {
        String output="";
        for (char x : input.toCharArray()) {
            if (x != ' ')
                output = output + x;
        }
        return output;
    }
    
    private int checkMinLength(String input, int length) {
        if (input.length() < length) return -1;
        else return 0;
    }
    
    private int checkMaxLength(String input, int length) {
        if (input.length() > length) return -1;
        else return 0;
    }
    
    private int checkIfNotEmpty(String input) {
        if (input == null || removeSpaces(input).equals("")) return -1;
        else return 0;
    }
    
    private int checkAllowed(String input, boolean alpha, boolean polishAlpha, boolean number, boolean specialSigns, boolean space) {
        for (char x : input.toCharArray()) {
            if (!alpha && Character.isAlphabetic(x)) return -1;
            if (!polishAlpha && isPolishAlpha(x)) return -2;
            if (!number && Character.isDigit(x)) return -3;
            if (!specialSigns && isSpecialSign(x)) return -4;
            if (!space && x == ' ') return -5;
            if (Character.isAlphabetic(x) || isPolishAlpha(x) || Character.isDigit(x) || isSpecialSign(x) || x==' ') {
                continue;
            }
            else {
                return -6;
            }
        }
        return 0;
    }
    
    private boolean isPolishAlpha(char x) {
        boolean result=false;
        switch(x) {
            case 'ą':
            case 'ę':
            case 'ć':
            case 'ż':
            case 'ź':
            case 'ó':
            case 'ś':
            case 'ł':
            case 'ń':
            case 'Ą':
            case 'Ę':
            case 'Ć':
            case 'Ż':
            case 'Ź':
            case 'Ó':
            case 'Ś':
            case 'Ł':
            case 'Ń':
                result=true;
            default:
                break;
        }
        return result;
    }
    
    private boolean isSpecialSign(char x) {
        boolean result=false;
        switch(x) {
            case '!':
            case '@':
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '(':
            case ')':
            case '{':
            case '}':
            case '[':
            case ']':
            case '"':
            case '\'':
            case ',':
            case '.':
            case '/':
            case '\\':
            case '<':
            case '>':
            case '+':
            case '=':
            case '-':
            case '_':
            case '`':
            case '~':
            case '\n':
            case '\r':
            case '\t':
                result=true;
            default:
                break;
        }
        return result;
    }
    
    public int validateUsername(String username) {
        if (checkIfNotEmpty(username) < 0) return ErrorCode.USERNAME_EMPTY.getErrorCode();
        if (checkMinLength(username, 6) < 0) return ErrorCode.USERNAME_TOO_SHORT.getErrorCode();
        if (checkMaxLength(username, 20) < 0) return ErrorCode.USERNAME_TOO_LONG.getErrorCode();
        if (checkAllowed(username, true, false, true, false, false) < 0) return ErrorCode.USERNAME_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validatePassword(String password) {
        if (checkIfNotEmpty(password) < 0) return ErrorCode.PASSWORD_EMPTY.getErrorCode();
        if (checkMinLength(password, 6) < 0) return ErrorCode.PASSWORD_TOO_SHORT.getErrorCode();
        if (checkMaxLength(password, 20) < 0) return ErrorCode.PASSWORD_TOO_LONG.getErrorCode();
        if (checkAllowed(password, true, true, true, true, false) < 0) return ErrorCode.PASSWORD_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validateName(String name) {
        if (checkIfNotEmpty(name) < 0) return ErrorCode.NAME_EMPTY.getErrorCode();
        if (checkMinLength(name, 2) < 0) return ErrorCode.NAME_TOO_SHORT.getErrorCode();
        if (checkMaxLength(name, 40) < 0) return ErrorCode.NAME_TOO_LONG.getErrorCode();
        if (checkAllowed(name, true, true, false, false, false) < 0) return ErrorCode.NAME_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validateSurname(String surname) {
        if (checkIfNotEmpty(surname) < 0) return ErrorCode.SURNAME_EMPTY.getErrorCode();
        if (checkMinLength(surname, 2) < 0) return ErrorCode.SURNAME_TOO_SHORT.getErrorCode();
        if (checkMaxLength(surname, 40) < 0) return ErrorCode.SURNAME_TOO_LONG.getErrorCode();
        if (checkAllowed(surname, true, true, false, false, false) < 0) return ErrorCode.SURNAME_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    private boolean isValidEmailAddress(String email) {
        /*boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;*/
        Pattern ptr = Pattern.compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");
        return ptr.matcher(email).matches();
    }
    
    public int validateEmail(String email) {
        if (checkIfNotEmpty(email) < 0) return ErrorCode.EMAIL_EMPTY.getErrorCode();
        if (checkMinLength(email, 2) < 0) return ErrorCode.EMAIL_TOO_SHORT.getErrorCode();
        if (checkMaxLength(email, 40) < 0) return ErrorCode.EMAIL_TOO_LONG.getErrorCode();
        if (!isValidEmailAddress(email)) return ErrorCode.EMAIL_NOT_VALID.getErrorCode();
        return 0;
    }
    
    public int validatePhone(String phone) {
        if (checkIfNotEmpty(phone) < 0) return ErrorCode.PHONE_EMPTY.getErrorCode();
        if (checkMinLength(phone, 8) < 0) return ErrorCode.PHONE_TOO_SHORT.getErrorCode();
        if (checkMaxLength(phone, 20) < 0) return ErrorCode.PHONE_TOO_LONG.getErrorCode();
        if (phone.charAt(0) == '+') {
            String subPhone = phone.substring(1, phone.length());
            if (checkAllowed(subPhone, false, false, true, false, false) < 0) return ErrorCode.PHONE_UNALLOWED_SIGNS.getErrorCode();
        }
        else if (checkAllowed(phone, false, false, true, false, false) < 0) return ErrorCode.PHONE_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validateBpMeasure(int measure) {
        if (measure<=0 || measure>500) return ErrorCode.DAMAGED_MEASURE.getErrorCode();
        return 0;
    }
    
    public int validateHrMeasure(int measure) {
        if (measure<=0 || measure>500) return ErrorCode.DAMAGED_MEASURE.getErrorCode();
        return 0;
    }
    
    public int validateOxyMeasure(int measure) {
        if (measure<=0 || measure>100) return ErrorCode.DAMAGED_MEASURE.getErrorCode();
        return 0;
    }
    
    public int validateSleepMeasure(int measure) {
        if (measure<=0) return ErrorCode.DAMAGED_MEASURE.getErrorCode();
        return 0;
    }
    
    public int validateSleepPerMeasure(int measure) {
        if (measure<=0 || measure>100) return ErrorCode.DAMAGED_MEASURE.getErrorCode();
        return 0;
    }
    
    public int validateDiaryEntry(String diaryEntry) {
        if (checkIfNotEmpty(diaryEntry) < 0) return ErrorCode.DIARY_EMPTY.getErrorCode();
        if (checkMinLength(diaryEntry, 20) < 0) return ErrorCode.DIARY_TOO_SHORT.getErrorCode();
        if (checkMaxLength(diaryEntry, 2000) < 0) return ErrorCode.DIARY_TOO_LONG.getErrorCode();
        if (checkAllowed(diaryEntry, true, true, true, true, true) < 0) return ErrorCode.DIARY_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validateSearchString(String diaryEntry) {
        if (checkIfNotEmpty(diaryEntry) < 0) return ErrorCode.SEARCHSTRING_EMPTY.getErrorCode();
        if (checkMinLength(diaryEntry, 2) < 0) return ErrorCode.SEARCHSTRING_TOO_SHORT.getErrorCode();
        if (checkMaxLength(diaryEntry, 200) < 0) return ErrorCode.SEARCHSTRING_TOO_LONG.getErrorCode();
        if (checkAllowed(diaryEntry, true, true, true, true, true) < 0) return ErrorCode.SEARCHSTRING_UNALLOWED_SIGNS.getErrorCode();
        return 0;
    }
    
    public int validateUuid(String uuid) {
        if (checkIfNotEmpty(uuid) < 0) return ErrorCode.UUID_INVALID.getErrorCode();
        if (checkMinLength(uuid, 20) < 0) return ErrorCode.UUID_INVALID.getErrorCode();
        if (checkMaxLength(uuid, 200) < 0) return ErrorCode.UUID_INVALID.getErrorCode();
        return 0;
    }
    
    public int validateDate(int measure) {
        if (measure<0 || measure>(SecurityHelper.getInstance().getCurrentTime()+172800)) return ErrorCode.INVALID_DATE.getErrorCode();
        return 0; //było od 1514764800 ale z datą urodzenia byłby problem
    }
    
}
