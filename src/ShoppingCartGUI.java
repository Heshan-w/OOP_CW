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
    private User customer;
    private ShoppingCart shoppingCartObject;
    private List<Product> productsInCart;
    private List<String> usernames;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel newCustomerDiscountLabel;
    private JLabel productTypeDiscountAmountLabel;
    private JLabel finalTotalLabel;
    boolean newCustomerDiscount = false;
    boolean productTypeDiscount = false;

    public ShoppingCartGUI(User customer, ShoppingCart shoppingCartObject) {
        this.customer = customer;
        this.shoppingCartObject = shoppingCartObject;

        usernames = new ArrayList<>();
        productsInCart = shoppingCartObject.getSelectedProducts();

        if (Files.exists(Paths.get("new_customers.txt"))) {
            // calling the "readUsernamesFromFile" method to load existing customers' usernames from a text-file
            readUsernamesFromFile();
        }

        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        initUI();

        // Move the identification of new customers after initializing UI components
        setLocationRelativeTo(null);
    }

    private void identifyNewCustomers() {
        if (!(usernames.contains(customer.getUsername()))) {
            newCustomerDiscount = true;
            usernames.add(customer.getUsername());
            newCustomerDiscountLabel.setText("New customer discount: - £" + (shoppingCartObject.calculateTotalPrice(productsInCart) * 0.10));
            // Save the new customer's username to a text file
            saveNewCustomerUsername(customer.getUsername().strip());
        }
    }

    private void saveNewCustomerUsername(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("new_customers.txt", true))) {
            writer.write(username);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing username to new customer file: " + e.getMessage());
        }
    }

    private void readUsernamesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("new_customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usernames.add(line.strip());
            }
        } catch (IOException e) {
            System.out.println("Unable to read usernames from file: " + e.getMessage());
        }
    }

    private void calculateProductTypeDiscount() {
        int clothingCount = 0;
        int electronicsCount = 0;
        for (Product product : productsInCart) {
            if (product instanceof Clothing) {
                clothingCount++;
            } else if (product instanceof Electronics) {
                electronicsCount++;
            }
        }
        double discount = shoppingCartObject.calculateTotalPrice(productsInCart) * 0.20;
        if(clothingCount >= 3){
            productTypeDiscountAmountLabel.setText("Three Items In The Same Category Discount: - £" + String.format("%.2f", discount));
            productTypeDiscount = true;
        } else if (electronicsCount >= 3){
            productTypeDiscountAmountLabel.setText("Three Items In The Same Category Discount: - £" + String.format("%.2f", discount));
            productTypeDiscount = true;
        }
    }

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
        costDetails();
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

    public void costDetails() {
        // Calculate the total price of all products in the cart
        double totalPrice = shoppingCartObject.calculateTotalPrice(productsInCart);
        // Update the total label with the calculated total price
        totalLabel.setText("Total: £" + totalPrice);
        calculateProductTypeDiscount();
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
        finalTotalLabel.setText(" Final total: £" + finalPrice);
    }
}