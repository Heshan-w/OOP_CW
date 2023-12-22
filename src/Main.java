import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Westminster Shopping Manager!");
        Scanner scanner = new Scanner(System.in);
        String choice;

        System.out.print("""
                To access the customer console, please enter 1.
                To access the manager console, please enter 2.
                Enter your choice (1 or 2):\s""");

        choice = scanner.next();
        if (choice.equals("1")) {
            System.out.println("You have selected the customer console.");
            System.out.println("Please enter your username and password to login.");
            System.out.print("Username: ");
            String username = scanner.next();
            System.out.print("Password: ");
            String password = scanner.next();
            User customer = new User(username, password);

            // Create an instance of WestminsterShoppingManager
            WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
            // Add products to the availableProducts list
            westminsterShoppingManager.addProduct();
            westminsterShoppingManager.addProduct();
            // Get the availableProducts list
            List<Product> availableProducts = westminsterShoppingManager.getStoreInventory();

            // Launch the customer GUI with the availableProducts list
            launchCustomerGUI(customer, availableProducts);
        } else if (choice.equals("2")) {
            System.out.println("You have selected the manager console.");
            WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
            westminsterShoppingManager.displayManagerConsole();
        } else {
            System.out.println("Invalid choice. Please enter a valid option (1 or 2).");
        }

        System.out.println("Thank you for using Westminster Shopping Manager!");
    }

    private static void launchCustomerGUI(User customer, List<Product> availableProducts) {
        // Create and display the GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            CustomerGUI customerGUI = new CustomerGUI(customer, availableProducts);
            customerGUI.setVisible(true);
        });
    }
}
