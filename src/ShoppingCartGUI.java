import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartGUI extends JFrame {
    private User customer;
    private ShoppingCart shoppingCartObject;
    private List<Product> productsInCart;
    private DefaultTableModel tableModel;

    public ShoppingCartGUI(User customer, ShoppingCart shoppingCartObject) {
        this.customer = customer;
        this.shoppingCartObject = shoppingCartObject;

        productsInCart = shoppingCartObject.getSelectedProducts();

        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        initUI();
        setLocationRelativeTo(null);
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

        // Adding the table to the frame
        add(tableScrollPane, BorderLayout.CENTER);

        // Populate the table with product details
        populateCartTable();
    }


    private void populateCartTable() {
        ArrayList<String> displayedProductIDs = new ArrayList<>();
        // Clear existing rows in the table
        tableModel.setRowCount(0);

        // Populate the table with product details
        for (Product product : productsInCart) {
            // Check if the product ID is already in the set
            if (!(displayedProductIDs.contains(product.getProductID()))) {
                Object[] rowData = new Object[3];
                rowData[0] = getProductDetails(product);
                rowData[1] = 1;
                rowData[2] = product.getPrice();
                tableModel.addRow(rowData);
                displayedProductIDs.add(product.getProductID());
            }
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
}
