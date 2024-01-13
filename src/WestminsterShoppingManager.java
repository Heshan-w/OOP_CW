import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class WestminsterShoppingManager implements ShoppingManager {
    // declaring a list of "Product" type objects to store products entered by the manager
    private final List<Product> storeInventory;

    // constructor for the "WestminsterShoppingManager" class
    public WestminsterShoppingManager() {
        this.storeInventory = new ArrayList<>();
        // calling the "loadFromFile" method to load products from a file to the "storeInventory" list
        // Check if the file "products.dat" exists before loading from it
        if (Files.exists(Paths.get("products.dat"))) {
            // calling the "loadFromFile" method to load products from a file to the "storeInventory" list
            loadFromFile();
        }
    }

    // method to display the manager console menu(options available to the manager)
    public void displayManagerConsole() {
        // creating a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        String choice;

        // do-while loop to display the manager console menu until the user enters "6" to exit the menu
        do {
            System.out.println("\n***Manager options***");
            System.out.println("Enter 1 to add a new product");
            System.out.println("Enter 2 to remove a product");
            System.out.println("Enter 3 to print the list of products");
            System.out.println("Enter 4 to save products to a file");
            System.out.println("Enter 5 to load products from a file");
            System.out.println("Enter 6 to exit Manager console menu");
            System.out.print("Enter your choice (1-6): ");
            choice = scanner.next().trim();

            // switch statement to execute the appropriate method based on the user input
            System.out.println("You have selected option " + choice);
            switch (choice) {
                case "1" -> addProduct();
                case "2" -> removeProduct();
                case "3" -> printProductList();
                case "4" -> {
                    saveToFile();
                    System.out.println("\nProducts saved successfully");
                }
                case "5" -> loadFromFile();
                case "6" -> {
                    while (true) {
                        // string to store the user input for saving the changes made to the inventory
                        String saveOption;
                        // displays the save options until the user enters a valid one
                        System.out.print("""
                            \nYou have selected to exit the manager menu.
                            Enter 1 TO SAVE any changes made to the inventory
                            Enter 2 to exit console menu WITHOUT SAVING any changes made to the inventory
                            Enter your choice (1 or 2):\s""");
                        saveOption = scanner.next().trim();
                        // if-else statement to execute the method based on user input
                        if (saveOption.equals("1")) {
                            // calling the "saveToFile" method to save the changes made to the inventory
                            saveToFile();
                            System.out.println("\nProducts saved successfully");
                            System.out.println("Exiting the manager menu....");
                            // terminating the looping
                            break;
                        } else if (saveOption.equals("2")) {
                            System.out.println("\nChanges made to the inventory not saved");
                            System.out.println("Exiting the manager menu....");
                            break;
                        // if the user enters an invalid option, the loop continues
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                }
                // if the user enters an invalid option, the loop continues
                default -> System.out.println("Invalid choice. Please enter a valid option (1-6).");
            }
        } while (!choice.equals("6"));
    }

    // method to add a product to the "storeInventory" list
    @Override
    public void addProduct() {
        // constant variable that stores the maximum number of products that can be in stock
        int maxStock = 50;
        // checking if the number of products in the "storeInventory" list is equal to the maximum stock
        if (storeInventory.size() == maxStock) {
            // if so displaying a message to notify about it and terminating the method
            System.out.println("Maximum stock reached, cannot add more products");
            return;
        }
        // creating a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        String choice;

        // displaying the add-product specifications until the user enters "3" to return to the manager options menu
        while (true) {
            System.out.println("\nAdding a new product:");
            System.out.println("Enter 1 to add a new electronic product");
            System.out.println("Enter 2 to add a new clothing product");
            System.out.println("Enter 3 to return to the Manager options menu");
            System.out.print("Enter your choice (1-3): ");
            // using the "next()" method to read the user input
            choice = scanner.next();

            // switch statement to execute the appropriate method based on the user input
            switch (choice) {
                case "1" -> {
                    // calling the "addElectronicProduct" method to add a new electronic product
                    addElectronicProduct();
                    // terminating the looping
                    return;
                }
                case "2" -> {
                    // calling the "addClothingProduct" method to add a new clothing product
                    addClothingProduct();
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
        // prompting for and gathering the electronic product details from the user
        String productID = promptProductID(1);
        // checking if the product ID already exists in the "storeInventory" list
        // using a lambda expression to check if the product ID of the current product matches the entered product ID
        // if the product ID already exists, the "anyMatch()" method will return true and execute the if block
        if (storeInventory.stream().anyMatch(product -> product.getProductID().equals(productID))) {
            // displaying a message to indicate that the product ID already exists
            System.out.println("Product ID already exists");
            // terminating the method
            return;
        }
        System.out.print("Enter the product name: ");
        String productName = promptProductName();
        System.out.print("Enter number of available items for "
                + "'" + productName + "'"
                + " (product ID = " + productID +") : ");
        int itemsInStock = promptItemsInStock();
        System.out.print("Enter the price of the product: ");
        double price = promptPrice();
        System.out.print("Enter the brand of the product: ");
        String brand = promptBrandName();
        System.out.print("Enter the warranty period of the product (number of weeks): ");
        int warrantyPeriod = promptWarrantyPeriod();

        // creating an "Electronics" object with the gathered details
        Product product = new Electronics(productID, productName, itemsInStock, price, brand, warrantyPeriod);
        // adding the created "Electronics" object to the "storeInventory" list
        storeInventory.add(product);
        // displaying a message to indicate that the product has been added successfully
        System.out.println("Electronic product added successfully");
    }

    // method to add a new clothing product
    private void addClothingProduct() {
        // prompting for and gathering the clothing product details from the user
        String productID = promptProductID(2);
        // checking if the product ID already exists in the "storeInventory" list
        if (storeInventory.stream().anyMatch(product -> product.getProductID().equals(productID))) {
            // displaying a message to indicate that the product ID already exists
            System.out.println("Product ID already exists");
            // terminating the method
            return;
        }
        System.out.print("Enter the product name: ");
        String productName = promptProductName();
        System.out.print("Enter number of available items for "
                + "'" + productName + "'"
                + " (product ID = " + productID +") : ");
        int itemsInStock = promptItemsInStock();
        System.out.print("Enter the price of the product: ");
        double price = promptPrice();
        System.out.print("Enter the size of the product (XS, S, M, L, XL, XXL): ");
        String size = promptSize();
        System.out.print("Enter the colour of the product: ");
        String colour = promptColour();

        // creating a "Clothing" object with the gathered details
        Product product = new Clothing(productID, productName, itemsInStock, price, size, colour);
        // adding the created "Clothing" object to the "storeInventory" list
        storeInventory.add(product);
        System.out.println("Clothing product added successfully");
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
        String productType;
        // using an enhanced for loop to iterate through the "storeInventory" list avoiding the use of an index
        for (Product product : storeInventory) {
            // checking if the product ID of the current product matches the entered product ID
            if (product.getProductID().equals(productID)) {
                // using a ternary operator to determine the type of the product
                productType = (product instanceof Electronics) ? "Electronic" : "Clothing";
                // removing that product from the "storeInventory" list
                storeInventory.remove(product);
                System.out.println(productType + " Product removed successfully" + ", removed product ID : " + productID);
                // displaying the number of products remaining in the "storeInventory" list
                System.out.println("Remaining product count (Electronics & Clothing items): " + storeInventory.size());
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
        // Sorting the storeInventory based on ProductID using Comparator
        // using the "Comparator.comparing()" method to compare the product IDs of the products in the "storeInventory" list
        // using the "sort()" method to sort the "storeInventory" list based on the product IDs
        storeInventory.sort(Comparator.comparing(Product::getProductID));

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
        // try-with-resources is used to automatically close the streams
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("products.dat"))){
            // using writeObject() to write the "storeInventory" list to the file
            objectOutputStream.writeObject(storeInventory);
        // catching the exceptions thrown by the "FileOutputStream" and "ObjectOutputStream" constructors
        } catch (IOException e){
            System.out.println("\nError occurred while saving products");
        }
    }

    // method to load products from a file to the "storeInventory" list
    @Override
    public void loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("products.dat"))){
            // using clear() to clear the "storeInventory" list before loading products from the file
            storeInventory.clear();
            // using addAll() to add all the products in the file to the "storeInventory" list
            // (List<Product>) is used to cast the object returned by readObject() to a "List<Product>" type object
            storeInventory.addAll((List<Product>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e){
            System.out.println("\nError occurred while loading products");
        }
    }

    // getter for the "storeInventory" list
    public List<Product> getStoreInventory() {
        return storeInventory;
    }

    // method to prompt and gather the product ID from the user
    public String promptProductID(int type) {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String productID;

        // using a ternary operator to determine the format of the product ID based on the type of the product
        String formatMessage = (type == 1) ? "EL_XXXX" : "CL_XXXX";
        // using a ternary operator to determine the regex of the product ID based on the type of the product
        String regex = (type == 1) ? "EL_[0-9]{4}" : "CL_[0-9]{4}";

        // using a while loop to prompt and gather the product ID until a valid product ID is entered
        while (true) {
            System.out.print("Enter product ID (Format: " + formatMessage + "): ");
            productID = scanner.nextLine().trim();
            // using the "matches()" method to check if the entered product ID matches the regex
            // if product ID matches the regex, while loop is terminated and product ID is returned
            if (productID.matches(regex)) {
                break;
            }
            // displaying a message to indicate that the entered product ID is invalid
            System.out.print("Invalid product ID. Please enter a valid product ID: ");
        }
        // returning the product ID
        return productID;
    }

    // method to prompt and gather the product name from the user
    public String promptProductName() {
        String productName;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            productName = scanner.nextLine().trim();
            // using the isEmpty() method to check if the entered product name is empty
            // if not empty, loop is terminated and product name is returned
            if (!productName.isEmpty()) {
                break;
            }
            System.out.print("Product-name field cannot be empty. Please enter a valid product name : ");
        }
        return productName;
    }

    // method to prompt and gather the items in stock for a certain product from the user
    public int promptItemsInStock() {
        int itemsInStock;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // using a try-catch block to catch any exception thrown by the "nextInt()" method
            try {
                // using the "nextInt()" method to check if the entered items in stock is an integer
                itemsInStock = scanner.nextInt();
                // checking if the stock for the entered item is greater than 0
                if (itemsInStock > 0) {
                    break;
                }
                System.out.print("Items in stock cannot be 0 or less. Please enter a valid value : ");
            } catch (Exception e) {
                System.out.print("Invalid items in stock value. Please enter a valid value : ");
                // using the "next()" method to clear the scanner buffer
                scanner.next();
            }
        }
        return itemsInStock;
    }

    // method to prompt and gather the price of a product from the user
    public double promptPrice() {
        double price;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // using a try-catch block to catch any exception thrown by the "nextDouble()" method
            try {
                price = scanner.nextDouble();
                // checking if the price for the entered item is greater than 0
                if (price > 0) {
                    break;
                }
                System.out.print("Price cannot be £0 or less. Please enter a valid price : ");
            } catch (Exception e) {
                System.out.print("Invalid price. Please enter a valid price : ");
                scanner.next();
            }
        }
        return price;
    }

    // method to prompt and gather the brand name of an electronic product from the user
    public String promptBrandName() {
        String brandName;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            brandName = scanner.nextLine().trim();
            // using the isEmpty() method to check if the entered brand name is empty
            if (!brandName.isEmpty()) {
                break;
            }
            System.out.print("Brand-name field cannot be empty. Please enter a valid brand name : ");
        }
        return brandName;
    }

    // method to prompt and gather the warranty period of an electronic product from the user
    public int promptWarrantyPeriod() {
        int warrantyPeriod;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                warrantyPeriod = scanner.nextInt();
                if (warrantyPeriod > 0) {
                    // if warranty period is greater than 0, loop is terminated and the warranty period is returned
                    break;
                }
                System.out.print("Warranty period has to be 1 week or greater. Please enter a valid warranty period : ");
            } catch (Exception e) {
                System.out.print("Invalid warranty period. Please enter a valid warranty period : ");
                scanner.next();
            }
        }
        return warrantyPeriod;
    }

    // method to prompt and gather the size of a clothing product from the user
    public String promptSize() {
        // Declaring an arraylist of acceptable size options
        List<String> sizes = new ArrayList<>(Arrays.asList("XS", "S", "M", "L", "XL", "XXL"));
        Scanner scanner = new Scanner(System.in);
        String size;
        while (true) {
            // using trim() to remove any leading or trailing spaces
            // using toUpperCase() to convert the entered size to uppercase
            size = scanner.nextLine().trim().toUpperCase();
            // using contains() to check if the entered size is in the "sizes" arraylist
            if (sizes.contains(size)) {
                break;
            }
            System.out.print("Invalid size. Please enter a valid size (XS, S, M, L, XL, XXL) : ");
        }
        return size;
    }

    // method to prompt and gather the colour of a clothing product from the user
    public String promptColour() {
        Scanner scanner = new Scanner(System.in);
        String colour;
        // using a while loop to prompt and gather the colour until a valid colour is entered
        while (true) {
            colour = scanner.nextLine().trim();
            // using the isEmpty() method to check if the entered colour is empty
            if (!colour.isEmpty()) {
                // using the "matches()" method to check if the entered colour is a string
                if (colour.matches("[a-zA-Z]+")) {
                break;
                }
            }
            System.out.print("Please enter a valid colour : ");
        }
        return colour;
    }
}
