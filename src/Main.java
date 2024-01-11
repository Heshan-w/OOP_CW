import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Westminster Shopping Manager!");
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.print("""
                    \n***Main menu***
                    To access the customer console, please enter 1.
                    To access the manager console, please enter 2.
                    To exit the application, please enter 3.
                    Enter your choice (1, 2 or 3):\s""");
            choice = scanner.next();

            switch (choice) {
                case "1" -> {
                    System.out.println("You have selected the customer console.");
                    System.out.println("Please enter your username and password to login.");
                    System.out.print("Username: ");
                    String username = promptUsername();
                    System.out.print("Password: ");
                    String password = scanner.next();

                    // Create a new instance of the User class
                    User customer = new User(username, password);

                    // Create a new instance of the WestminsterShoppingManager class
                    WestminsterShoppingManager wsmObject = new WestminsterShoppingManager();

                    // Create a new instance of the ShoppingCart class
                    ShoppingCart shoppingCart = new ShoppingCart();

                    // Launch the customer GUI (test call which displays all inventory items)
                    launchCustomerGUI(customer, wsmObject, shoppingCart);
                }
                case "2" -> {
                    System.out.println("You have selected the manager console.");
                    WestminsterShoppingManager wsmObject = new WestminsterShoppingManager();
                    wsmObject.displayManagerConsole();
                }
                case "3" -> System.out.println("""
                            \nThank you for using Westminster Shopping Manager!
                            Exiting the application....""");
                default -> System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
            }
        } while (!choice.equals("3"));
    }

    private static void launchCustomerGUI(User customer, WestminsterShoppingManager wsmObject, ShoppingCart shoppingCartObject) {
        // Create and display the GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            CustomerGUI customerGUI = new CustomerGUI(customer, wsmObject, shoppingCartObject);
            customerGUI.setVisible(true);
        });
    }

    public static String promptUsername() {
        Scanner scanner = new Scanner(System.in);
        String username;
        // using a while loop to prompt and gather the colour until a valid colour is entered
        while (true) {
            username = scanner.nextLine().trim();
            // using the isEmpty() method to check if the entered colour is empty
            if (!username.isEmpty()) {
                // using the "matches()" method to check if the entered colour is a string
                if (username.matches("[a-zA-Z]+")) {
                    break;
                }
            }
            System.out.print("Please enter a valid username : ");
        }
        return username;
    }
}
