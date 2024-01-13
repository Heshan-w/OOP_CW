import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartGUI extends JFrame {
    //create and initialize variables at the top of the class to make them accessible to all methods
    private final User customer;
    private final ShoppingCart shoppingCartObject;
    private final List<Product> productsInCart;
    private final List<String> usernames;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel newCustomerDiscountLabel;
    private JLabel productTypeDiscountAmountLabel;
    private JLabel finalTotalLabel;
    // Create boolean variables to check if the customer is eligible for a discount
    boolean newCustomerDiscount;
    boolean productTypeDiscount;

    public ShoppingCartGUI(User customer, ShoppingCart shoppingCartObject) {
        this.customer = customer;
        this.shoppingCartObject = shoppingCartObject;

        // create an array list to store the list of usernames of new customers
        usernames = new ArrayList<>();
        // create an array list to store the list of products in the shopping cart
        productsInCart = shoppingCartObject.getSelectedProducts();

        // Check if the "new_customers.txt" file exists and if so, read the usernames from the file
        // this file will store the usernames of new customers
        if (Files.exists(Paths.get("new_customers.txt"))) {
            // calling the "readUsernamesFromFile" method to load existing customers' usernames from a text-file
            readUsernamesFromFile();
        }

        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        initUI();

        setLocationRelativeTo(null);
    }

    // Create a method to check if the customer is a new customer
    private void identifyNewCustomers() {
        // Check if the customer's username is in the list of usernames
        if (!(usernames.contains(customer.getUsername()))) {
            // If the customer is a new customer, set the newCustomerDiscount boolean to true
            newCustomerDiscount = true;
            // Add the customer's username to the list of usernames
            usernames.add(customer.getUsername());
            // Update the new customer discount label with the discount amount
            newCustomerDiscountLabel.setText("New customer discount: - £" + (shoppingCartObject.calculateTotalPrice(productsInCart) * 0.10));
            // Save the new customer's username to a text file
            saveNewCustomerUsername(customer.getUsername().strip());
        }
    }

    // Create a method to save the new customer's username to a text file
    //saving it this way makes sure that new customers are not eligible for the discount more than once
    private void saveNewCustomerUsername(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("new_customers.txt", true))) {
            writer.write(username);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing username to new customer file: " + e.getMessage());
        }
    }

    // Create a method to read the usernames from the text file
    private void readUsernamesFromFile() {
        // Clear the list of usernames before reading from the file
        usernames.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("new_customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Add each username to the list of usernames
                usernames.add(line.strip());
            }
        } catch (IOException e) {
            System.out.println("Unable to read usernames from file: " + e.getMessage());
        }
    }

    // Create a method to calculate the product type discount
    // if there are more than 3 items of the same type, the customer gets a 20% discount
    private void calculateProductTypeDiscount() {
        int clothingCount = 0;
        int electronicsCount = 0;
        // checking if the product is clothing or electronics and incrementing the count
        for (Product product : productsInCart) {
            if (product instanceof Clothing) {
                clothingCount++;
            } else if (product instanceof Electronics) {
                electronicsCount++;
            }
        }
        // calculating the discount amount
        double discount = shoppingCartObject.calculateTotalPrice(productsInCart) * 0.20;
        if(clothingCount >= 3){
            // updating the product type discount label with the discount amount
            productTypeDiscountAmountLabel.setText("Three Items In The Same Category Discount: - £" + String.format("%.2f", discount));
            // setting the product type discount boolean to true
            productTypeDiscount = true;
        } else if (electronicsCount >= 3){
            productTypeDiscountAmountLabel.setText("Three Items In The Same Category Discount: - £" + String.format("%.2f", discount));
            productTypeDiscount = true;
        }
    }

    // Create a method to initialize the UI
    private void initUI() {
        setLayout(new BorderLayout());
        // Create a table model with columns: "Product," "Quantity," and "Price(£)"
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Product", "Quantity", "Price(£)"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable cartTable = new JTable(tableModel);

        // Create a custom cell renderer to add borders to each side of the cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) component).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                return component;
            }
        };

        // Set the custom cell renderer for each column
        for (int i = 0; i < cartTable.getColumnCount(); i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Create a scroll pane for the table
        JScrollPane tableScrollPane = new JScrollPane(cartTable);

        // Create a panel to display the total price and new customer discount at the bottom center
        JPanel totalPanel = new JPanel(new GridLayout(4, 1));
        totalPanel.setPreferredSize(new Dimension(getWidth(), 200));

        // Total label
        totalLabel = new JLabel(" Total: £0.00");
        totalLabel.setHorizontalAlignment(JLabel.CENTER);
        totalPanel.add(totalLabel, BorderLayout.NORTH);

        // New customer discount label
        newCustomerDiscountLabel = new JLabel(" New customer discount: £0.00");
        newCustomerDiscountLabel.setHorizontalAlignment(JLabel.CENTER);
        totalPanel.add(newCustomerDiscountLabel, BorderLayout.CENTER);

        // Product type discount label
        productTypeDiscountAmountLabel = new JLabel(" Three Items In The Same Category Discount: £0.00");
        productTypeDiscountAmountLabel.setHorizontalAlignment(JLabel.CENTER);
        totalPanel.add(productTypeDiscountAmountLabel, BorderLayout.SOUTH);

        // Final total label
        finalTotalLabel = new JLabel(" Final total: £0.00");
        finalTotalLabel.setHorizontalAlignment(JLabel.CENTER);
        totalPanel.add(finalTotalLabel, BorderLayout.SOUTH);

        // Adding the total panel to the frame's bottom half
        add(totalPanel, BorderLayout.SOUTH);

        // Adding the table to the frame
        add(tableScrollPane, BorderLayout.CENTER);

        // Populate the table with product details
        populateCartTable();
        identifyNewCustomers();
        getCostDetails();
    }

    private void populateCartTable() {
        // Clear existing rows in the table
        tableModel.setRowCount(0);

        // Populate the table with product details
        for (Product product : productsInCart) {
            Object[] rowData = new Object[3];
            rowData[0] = getProductDetails(product);
            rowData[1] = 1;
            rowData[2] = product.getPrice();
            tableModel.addRow(rowData);
        }
    }

    private String getProductDetails(Product product) {
        // Build a string with product details based on the product type
        if (product instanceof Clothing clothing) {
            return String.format(
                    clothing.getProductID()
                            + ", " + clothing.getProductName()
                            + ", " + clothing.getSize()
                            + ", " + clothing.getColour());
        } else if (product instanceof Electronics electronics) {
            return String.format(
                    electronics.getProductID()
                            + ", \n" + electronics.getProductName()
                            + ", \n" + electronics.getBrand()
                            + ", \n" + electronics.getWarrantyPeriod() + " weeks");
        } else {
            return "";
        }
    }

    public void getCostDetails() {
        // Calculate the total price of all products in the cart
        double totalPrice = shoppingCartObject.calculateTotalPrice(productsInCart);
        // Update the total label with the calculated total price
        totalLabel.setText("Total: £" + totalPrice);
        // call the method to calculate the same product type discount
        calculateProductTypeDiscount();
        // Calculate the final price based on the discounts
        double finalPrice;
        if (newCustomerDiscount && productTypeDiscount) {
            finalPrice = totalPrice * 0.70 ;
        } else if (newCustomerDiscount) {
            finalPrice = totalPrice * 0.90;
        } else if (productTypeDiscount) {
            finalPrice = totalPrice* 0.80;
        } else {
            finalPrice = totalPrice;
        }
        // Update the final total label with the calculated final price
        finalTotalLabel.setText(" Final total: £" + String.format("%.2f", finalPrice));
    }
}