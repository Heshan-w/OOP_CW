import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class WestminsterShoppingManager implements ShoppingManager {
    // declaring a list of "Product" type objects to store products entered by the manager
    private final List<Product> storeInventory;

    // constructor for the "WestminsterShoppingManager" class
    public WestminsterShoppingManager() {
        this.storeInventory = new ArrayList<>();
    }

    // method to display the manager console menu(options available to the manager)
    public void displayManagerConsole() {
        // calling the "loadFromFile" method to load products from a file to the "storeInventory" list
        // Check if the file "products.dat" exists before loading from it
        if (Files.exists(Paths.get("products.dat"))) {
            // calling the "loadFromFile" method to load products from a file to the "storeInventory" list
            loadFromFile();
        }
        // creating a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        String choice;

        // do-while loop to display the manager console menu until the user enters "6" to exit the menu
        do {
            System.out.println("\n***Manager options***");
            System.out.println("Enter 1 to add a new product");
            System.out.println("Enter 2 to Remove a product");
            System.out.println("Enter 3 to Print the list of products");
            System.out.println("Enter 4 to Save products to a file");
            System.out.println("Enter 5 to Load products from a file");
            System.out.println("Enter 6 to Exit menu");
            System.out.print("Enter your choice (1-6): ");
            choice = scanner.next();

            // switch statement to execute the appropriate method based on the user input
            System.out.println("You have selected option " + choice);
            switch (choice) {
                case "1" -> addProduct();
                case "2" -> removeProduct();
                case "3" -> printProductList();
                case "4" -> saveToFile();
                case "5" -> loadFromFile();
                case "6" -> System.out.println("Exiting the manager menu....");
                default -> System.out.println("Invalid choice. Please enter a valid option (1-6).");
            }
        } while (!choice.equals("6"));
    }

    // method to add a product to the "storeInventory" list
    @Override
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        // displaying the add-product specifications until the user enters "3" to return to the manager options menu
        while (true) {
            System.out.println("\nAdding a new product:");
            System.out.println("Enter 1 to add a new electronic product");
            System.out.println("Enter 2 to add a new clothing product");
            System.out.println("Enter 3 to return to the Manager options menu");
            System.out.print("Enter your choice (1-3): ");
            choice = scanner.next();

            // switch statement to execute the appropriate method based on the user input
            switch (choice) {
                case "1" -> {
                    // calling the "addElectronicProduct" method to add a new electronic product
                    addElectronicProduct();
                    // displaying a message to indicate that the product has been added successfully
                    System.out.println("Electronic product added successfully");
                    // terminating the looping
                    return;
                }
                case "2" -> {
                    // calling the "addClothingProduct" method to add a new clothing product
                    addClothingProduct();
                    System.out.println("Clothing product added successfully");
                    return;
                }
                case "3" -> {
                    // displaying a message to indicate that the user will be returning to the manager options menu
                    System.out.println("returning to the Manager options menu....");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a valid option (1-3).");
            }
        }
    }

    // method to add a new electronic product
    private void addElectronicProduct() {
        Scanner scanner = new Scanner(System.in);

        // prompting for and gathering the electronic product details from the user
        System.out.print("\nEnter the product ID: ");
        String productID = scanner.next();
        scanner.nextLine();
        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter updated stock value for " + productName + ": ");
        int itemsInStock = scanner.nextInt();
        System.out.print("Enter the price of the product: ");
        double price = scanner.nextDouble();
        System.out.print("Enter the brand of the product: ");
        scanner.nextLine();
        String brand = scanner.nextLine();
        System.out.print("Enter the warranty period of the product: ");
        int warrantyPeriod = scanner.nextInt();

        // creating an "Electronics" object with the gathered details
        Product product = new Electronics(productID, productName, itemsInStock, price, brand, warrantyPeriod);
        // adding the created "Electronics" object to the "storeInventory" list
        storeInventory.add(product);
    }

    // method to add a new clothing product
    private void addClothingProduct() {
        Scanner scanner = new Scanner(System.in);

        // prompting for and gathering the clothing product details from the user
        System.out.print("\nEnter the product ID: ");
        String productID = scanner.next();
        scanner.nextLine();
        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter updated stock value for " + productName + ": ");
        int itemsInStock = scanner.nextInt();
        System.out.print("Enter the price of the product: ");
        double price = scanner.nextDouble();
        System.out.print("Enter the size of the product: ");
        String size = scanner.next();
        System.out.print("Enter the colour of the product: ");
        String colour = scanner.next();

        // creating a "Clothing" object with the gathered details
        Product product = new Clothing(productID, productName, itemsInStock, price, size, colour);
        // adding the created "Clothing" object to the "storeInventory" list
        storeInventory.add(product);
    }

    // method to remove a product from the "storeInventory" list
    @Override
    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        // prompting for and gathering the product ID of the product to be removed from the user
        System.out.print("\nEnter the product ID of the product you want to remove: ");
        String productID = scanner.next();

        // using a flag variable to indicate whether the product has been found or not
        boolean itemFound = false;
        // using an enhanced for loop to iterate through the "storeInventory" list avoiding the use of an index
        for (Product product : storeInventory) {
            // checking if the product ID of the current product matches the entered product ID
            if (product.getProductID().equals(productID)) {
                // removing that product from the "storeInventory" list
                storeInventory.remove(product);
                System.out.println("Product removed successfully" + ", removed product ID : " + productID);
                // setting the "itemFound" variable to true to indicate that the product has been found
                itemFound = true;
                break;
            }
        }
        // displaying a message indicating that the product has not been found if the "itemFound" variable is false
        if (!itemFound) {
            System.out.println("Product not found" + ", searched product ID : " + productID);
        }
    }

    // method to print the details of products in the "storeInventory" list sorted by product ID alphabetically
    @Override
    public void printProductList() {
        System.out.println("\n***Product List***");
        if (storeInventory.isEmpty()) {
            System.out.println("No products available");
            return;
        }
        for (Product product : storeInventory) {
            if (product instanceof Electronics) {
                System.out.println("\nElectronic\n" + "Product ID: " + product.getProductID()
                        + "\nProduct Name: " + product.getProductName() + "\nItems in stock: "
                        + product.getItemsInStock() + "\nPrice: " + product.getPrice() + "\nBrand: "
                        + ((Electronics) product).getBrand() + "\nWarranty Period: "
                        + ((Electronics) product).getWarrantyPeriod());
            } else if (product instanceof Clothing) {
                /* "((Clothing) product)." is used to cast the "product" object to a "Clothing" object
                so that the "Clothing" class specific methods can be used */
                System.out.println("\nClothing\n" + "Product ID: " + product.getProductID()
                        + "\nProduct Name: " + product.getProductName() + "\nItems in stock: "
                        + product.getItemsInStock() + "\nPrice: " + product.getPrice() + "\nSize: "
                        + ((Clothing) product).getSize() + "\nColour: " + ((Clothing) product).getColour()
                );
            }
        }
    }

    // method to save the products in the "storeInventory" list to a file
    @Override
    public void saveToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("products.dat"))){
            objectOutputStream.writeObject(storeInventory);
            System.out.println("Products saved successfully");
        } catch (IOException e){
            System.out.println("Error occurred while saving products");
            e.printStackTrace();
        }
    }

    // method to load products from a file to the "storeInventory" list
    @Override
    public void loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("products.dat"))){
            storeInventory.clear();
            storeInventory.addAll((List<Product>) objectInputStream.readObject());
            System.out.println("Products loaded successfully");
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Error occurred while loading products");
            e.printStackTrace();
        }
    }

    public List<Product> getStoreInventory() {
        return storeInventory;
    }
}
