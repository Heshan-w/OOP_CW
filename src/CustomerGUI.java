import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerGUI extends JFrame {
    private User customer;
    private final List<Product> availableProducts;

    public CustomerGUI(User customer, List<Product> availableProducts) {
        this.customer = customer;
        this.availableProducts = availableProducts;

        setTitle("Customer GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        initUI();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Create a JTable with a DefaultTableModel
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Product Name", "Price"});

        // Populate the table with availableProducts
        for (Product product : availableProducts) {
            tableModel.addRow(new Object[]{product.getProductID(), product.getProductName(), product.getPrice()});
        }

        JTable productsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // For testing purposes
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("TestUser", "TestPassword");
            WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
            westminsterShoppingManager.addProduct();
            westminsterShoppingManager.addProduct();
            List<Product> availableProducts = westminsterShoppingManager.getStoreInventory();

            CustomerGUI customerGUI = new CustomerGUI(testUser, availableProducts);
            customerGUI.setVisible(true);
        });
    }
}
