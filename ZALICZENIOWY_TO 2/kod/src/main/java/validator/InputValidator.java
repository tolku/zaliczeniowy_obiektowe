package validator;

import java.sql.Date;
import java.time.LocalDate;

import static java.lang.System.exit;

public class InputValidator {
    private static final String INPUT_NOT_VALID = "INVALID INPUT";
    private static final String INPUT_EMPTY = "EMPTY INPUT";
    private static final String INPUT_TOO_LONG = "INPUT IS TOO LONG";
    private static final String DATA_FORMAT_INCORRECT = "DATA FORMAT IS INCORRECT";
    private static final String INCORRECT_YEAR = "INCORRECT YEAR VALUE, IT CANNOT BE GREATER THAN TODAYS DATE";
    private static final String EMPTY_PESEL_NUMBER = "PESEL NUMBER CANNOT BE EMPTY";
    private static final String INCORRECT_PESEL_NUMBER = "PESEL NUMBER IS INCORRECT";

    public static int validateMenuInput(String input) {
        int convertedInput;
        try {
            convertedInput = Integer.parseInt(input);
            if (convertedInput >= 0 && convertedInput <= 12) {
                return convertedInput;
            }
        } catch (Exception exception) {
            exception.getCause();

        }
        System.out.println(INPUT_NOT_VALID);
        return 0;
    }

    public static String validateString(String input) {
        if (input.isEmpty()) {
            System.out.println(INPUT_EMPTY);
            exit(0);
        }
        if (input.length() >= 100) {
            System.out.println(INPUT_TOO_LONG);
            exit(0);
        }
        return input;
    }

    public static boolean validateBoolean(String input) {
        return Boolean.parseBoolean(input);
    }

    public static Date validateDate(String input) {
        Date date = null;
        try {
            date = Date.valueOf(input);
            if (date.compareTo(Date.valueOf(LocalDate.now())) > 0) {
                throw new IllegalStateException(INCORRECT_YEAR);
            }
        } catch (Exception exception) {
            System.out.println(DATA_FORMAT_INCORRECT);
            exit(-1);
        }
        return date;
    }

    public static Date validateDeathDate(String input){
        if (input.isEmpty()){
            return null;
        }
        return validateDate(input);
    }

    public static int validatePeselNumber(String input){
        int validatedInput = 0;
        if (input.isEmpty()){
            System.out.println(EMPTY_PESEL_NUMBER);
            exit(-1);
        }
        try {
            validatedInput = Integer.parseInt(input);
        } catch (Exception exception){
            System.out.println(INCORRECT_PESEL_NUMBER);
            exit(-1);
        }
        return validatedInput;
    }

    public static int validateInt(String input) {
        return ("true").equals(input) ? 1 : 0;
    }
}
