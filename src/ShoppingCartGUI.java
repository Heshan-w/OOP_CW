import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShoppingCartGUI extends JFrame {
    // declaring instance variables
    private User customer;
    // declaring a list to store the products added to the shopping cart
    private List<Product> shoppingCartItems;
    // declaring a table model to be used by the JTable
    private DefaultTableModel tableModel;

    public ShoppingCartGUI(User customer, List<Product> shoppingCartItems) {
        this.customer = customer;
        this.shoppingCartItems = shoppingCartItems;

        setTitle("Shopping Cart");
        // setting the default close operation to "DISPOSE_ON_CLOSE" to close only the current window
        // and not the entire application
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        // calling the method to initialize the GUI
        initUI();
        // centering the GUI on the screen
        setLocationRelativeTo(null);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info", "Available Items"});

        // creating a JTable with the table model
        JTable shoppingCartTable = new JTable(tableModel);
        // setting the table to be scrollable
        JScrollPane tableScrollPane = new JScrollPane(shoppingCartTable);

        // adding the products in the shopping cart to the table
        for (Product product : shoppingCartItems) {
            // declaring an array to store the data of each product
            Object[] rowData = new Object[6];
            // adding the data of each product to the array
            rowData[0] = product.getProductID();
            // checking if the product is an instance of the "Clothing" class
            rowData[1] = product.getProductName();
            // adding the product category to the table
            rowData[3] = product.getPrice();

            // checking if the product is an instance of the "Clothing" class
            if (product instanceof Clothing) {
                // adding the product category to the table
                rowData[2] = "Clothing";
                // adding the product info to the table
                rowData[4] = "Size: " + ((Clothing) product).getSize() + ", Colour: " + ((Clothing) product).getColour();
            // checking if the product is an instance of the "Electronics" class
            } else if (product instanceof Electronics) {
                // adding the product category to the table
                rowData[2] = "Electronics";
                // adding the product info to the table
                rowData[4] = "Brand: " + ((Electronics) product).getBrand() +
                        ", Warranty Period: " + ((Electronics) product).getWarrantyPeriod() + " weeks";
            }

            // Adding the "Available Items" column
            rowData[5] = product.getItemsInStock();

            // adding the product data to the table
            tableModel.addRow(rowData);
        }

        // creating a panel to hold the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        // adding the table to the panel
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // creating a panel to hold the "Checkout" button
        add(tablePanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("TestUser", "TestPassword");
            WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
            westminsterShoppingManager.addProduct();
            westminsterShoppingManager.addProduct();
//            List<Product> shoppingCartItems = westminsterShoppingManager.getShoppingCart(testUser);

//            ShoppingCartGUI shoppingCartGUI = new ShoppingCartGUI(testUser, shoppingCartItems);
//            shoppingCartGUI.setVisible(true);
        });
    }
}
