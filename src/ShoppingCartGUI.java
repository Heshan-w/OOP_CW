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

        JTable shoppingCartTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(shoppingCartTable);

        for (Product product : shoppingCartItems) {
            Object[] rowData = new Object[6];
            rowData[0] = product.getProductID();
            rowData[1] = product.getProductName();
            rowData[3] = product.getPrice();

            if (product instanceof Clothing) {
                rowData[2] = "Clothing";
                rowData[4] = "Size: " + ((Clothing) product).getSize() + ", Colour: " + ((Clothing) product).getColour();
            } else if (product instanceof Electronics) {
                rowData[2] = "Electronics";
                rowData[4] = "Brand: " + ((Electronics) product).getBrand() +
                        ", Warranty Period: " + ((Electronics) product).getWarrantyPeriod() + " weeks";
            }

            // Adding the "Available Items" column
            rowData[5] = product.getItemsInStock();

            tableModel.addRow(rowData);
        }

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

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
