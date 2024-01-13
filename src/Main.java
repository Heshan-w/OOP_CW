import javax.swing.*;
import java.util.Scanner;

public class Main {
    // entry point of the application
    public static void main(String[] args) {
        System.out.println("Welcome to Westminster Shopping Manager!");
        Scanner scanner = new Scanner(System.in);
        String choice;

        // using a do-while loop to display the main menu until the user chooses to exit the application
        do {
            System.out.print("""
                    \n***Main menu***
                    To access the customer console, please enter 1.
                    To access the manager console, please enter 2.
                    To exit the application, please enter 3.
                    Enter your choice (1, 2 or 3):\s""");
            choice = scanner.next();

            // option to access the customer console
            switch (choice) {
                case "1" -> {
                    System.out.println("\nYou have selected the customer console.");
                    // prompt the username and password to create a new instance of the User class
                    System.out.println("Please enter your username and password to login.");
                    System.out.print("Username: ");
                    String username = promptUsername();
                    System.out.print("Password: ");
                    String password = scanner.next();

                    // Creating a new instance of the User class
                    User customer = new User(username, password);

                    // Create a new instance of the WestminsterShoppingManager class to access the inventory
                    WestminsterShoppingManager wsmObject = new WestminsterShoppingManager();

                    // Create a new instance of the ShoppingCart class to store selected products into the shopping cart
                    ShoppingCart shoppingCart = new ShoppingCart();

                    // Launch the customer GUI with the created instances
                    launchCustomerGUI(customer, wsmObject, shoppingCart);
                }
                // option to access the manager console
                case "2" -> {
                    System.out.println("You have selected the manager console.");
                    // Create a new instance of the WestminsterShoppingManager class to access the inventory
                    // and utilise manager console methods
                    WestminsterShoppingManager wsmObject = new WestminsterShoppingManager();
                    // calling the "displayManagerConsole" method to display and access the manager console
                    wsmObject.displayManagerConsole();
                }
                // option to exit the application
                case "3" -> System.out.println("""
                            \nThank you for using Westminster Shopping Manager!
                            Exiting the application....""");
                // default case to handle invalid choices
                default -> System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
            }
        } while (!choice.equals("3"));
    }

    // method to launch the customer GUI
    private static void launchCustomerGUI(User customer, WestminsterShoppingManager wsmObject, ShoppingCart shoppingCartObject) {
        // Create and display the GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            // Create a new instance of the CustomerGUI class to display the customer GUI
            CustomerGUI customerGUI = new CustomerGUI(customer, wsmObject, shoppingCartObject);
            // making the GUI visible
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
