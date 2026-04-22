import java.util.Scanner;

public class PasswordValidator {

    public static boolean checkPassword(String password) {

        boolean Uppercase = false;
        boolean Digit = false;

        if (password.length() < 8) {
            System.out.println("Password is too short (minimum 8 characters required)");
            return false;
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                Uppercase = true;
            }

            if (Character.isDigit(ch)) {
                Digit = true;
            }
        }

        if (!Uppercase) {
            System.out.println(" Password must contain at least one uppercase letter.");
        }

        if (!Digit) {
            System.out.println("Password must contain at least one digit.");
        }

        // final validation
        if (Uppercase && Digit) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String userPassword;

        while (true) {
            System.out.print("Enter your password: ");
            userPassword = sc.nextLine();

            boolean isValid = checkPassword(userPassword);

            if (isValid) {
                System.out.println("Password accepted. Strong password!");
                System.out.println("your final password is :"+userPassword);
                break;
            } else {
                System.out.println(" Please try again.\n");
            }
        }
        sc.close();
    }
}
