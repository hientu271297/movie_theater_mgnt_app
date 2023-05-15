package fa.training.movietheater_mockproject.util;

import fa.training.movietheater_mockproject.enums.AppConstant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConvertUtil {
    public static LocalDate stringToLocalDate(String str) {
        DateTimeFormatter dft = DateTimeFormatter.ofPattern(AppConstant.DATE_PATTERN);
        try {
            return LocalDate.parse(str, dft);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Double stringToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Integer stringToInteger(String number){
        try{
            return Integer.parseInt(number);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
