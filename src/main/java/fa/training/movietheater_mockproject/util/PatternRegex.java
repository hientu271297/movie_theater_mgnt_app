package fa.training.movietheater_mockproject.util;

public class PatternRegex {
    public static final String EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String PHONE = "^\\d{9,13}$";
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,100}$";

}
