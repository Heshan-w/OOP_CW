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
                \nTo access the customer console, please enter 1.
                To access the manager console, please enter 2.
                To exit the application, please enter 3.
                Enter your choice (1, 2 or 3):\s""");
            choice = scanner.next();

            switch (choice) {
                case "1" -> {
                    System.out.println("You have selected the customer console.");
                    System.out.println("Please enter your username and password to login.");
                    System.out.print("Username: ");
                    String username = scanner.next();
                    System.out.print("Password: ");
                    String password = scanner.next();
                    User customer = new User(username, password);

                    WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
                    westminsterShoppingManager.addProduct();
                    westminsterShoppingManager.addProduct();
                    List<Product> availableProducts = westminsterShoppingManager.getStoreInventory();

                    launchCustomerGUI(customer, availableProducts);
                }
                case "2" -> {
                    System.out.println("You have selected the manager console.");
                    WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
                    westminsterShoppingManager.displayManagerConsole();
                }
                case "3" -> System.out.println("""
                            \nThank you for using Westminster Shopping Manager!
                            Exiting the application....""");
                default -> System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
            }
        } while (!choice.equals("3"));
    }

    private static void launchCustomerGUI(User customer, List<Product> availableProducts) {
        // Create and display the GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            CustomerGUI customerGUI = new CustomerGUI(customer, availableProducts);
            customerGUI.setVisible(true);
        });
    }
}
