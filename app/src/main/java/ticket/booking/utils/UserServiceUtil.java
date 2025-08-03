package ticket.booking.utils;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {
    // Utility methods for user service can be added here
    // For example, methods to validate user input, hash passwords, etc.

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean isUserRegistered(String email, String password) {
        // Placeholder for user registration check logic
        // In a real application, this would check against a database or user list
        return isValidEmail(email) && isValidPassword(password);
    }

    public static boolean checkPassword(String inputPassword, String storedHashedPassword) {
        // Placeholder for password checking logic
        // In a real application, this would compare the hashed input password with the stored hashed password
        return BCrypt.checkpw(inputPassword, storedHashedPassword);
    }

    // Add more utility methods as needed
}
