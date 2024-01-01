import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShoppingCartGUI extends JFrame {
    private User customer;
    private List<Product> shoppingCartItems;
    private DefaultTableModel tableModel;

    public ShoppingCartGUI(User customer, List<Product> shoppingCartItems) {
        this.customer = customer;
        this.shoppingCartItems = shoppingCartItems;

        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        initUI();
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
