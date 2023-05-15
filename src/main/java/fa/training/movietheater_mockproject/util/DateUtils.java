package fa.training.movietheater_mockproject.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateUtils {
    public static LocalDate stringToDate(String date, String format){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(date,dtf);
        } catch (DateTimeParseException e){
            return null;
        }
    }
    public static LocalDate stringToDate(String date){
        return stringToDate(date,"uuuu-MM-dd");
    }
}
