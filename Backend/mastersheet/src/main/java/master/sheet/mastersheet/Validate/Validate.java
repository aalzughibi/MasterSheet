package master.sheet.mastersheet.Validate;

import java.util.regex.*;

public class Validate {
    public static boolean isValidUsername(String username){
        if (username!=null){
            String regex = "^[A-Za-z]\\w{5,29}$";
            Pattern pUsername = Pattern.compile(regex);
            Matcher mUsername = pUsername.matcher(username);
            return mUsername.matches();
        }
        return false;
    }
    public static boolean isValidPassword(String password){
        if (password!=null){
            String regex = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";
            Pattern pPassword = Pattern.compile(regex);
            Matcher mPassword = pPassword.matcher(password);
            return mPassword.matches();
        }
        return false;
    }
    public static boolean isValidFirstName(String firstName){
        if (firstName!=null)
        return firstName.matches("[A-Z][a-z]*");
        return false;
    }
    public static boolean isValidLastName(String LastName){
        if (LastName!=null)
        return LastName.matches("[A-Z]+([ '-][a-zA-Z]+)*");
        return false;
    }
    public static boolean isValidEmail(String email){
        if (email!=null){
            String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            Pattern pEmail = Pattern.compile(regex);
            Matcher mEmail = pEmail.matcher(email);
            return mEmail.matches();
        }
        return false;
    }
}
