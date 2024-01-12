import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerGUI extends JFrame {
    private final User customer;
    private final WestminsterShoppingManager wsmObject;
    private final ShoppingCart shoppingCartObject;
    // the list that stores the products currently available in the store
    private final List<Product> availableProducts;
    // a list to temporarily store the selected products
    private final List<Product> shoppingCartItems;
    private final DefaultTableModel tableModel;
    private final JTextArea detailsTextArea;

    public CustomerGUI(User customer, WestminsterShoppingManager wsmObject, ShoppingCart shoppingCartObject) {
        this.customer = customer;
        this.wsmObject = wsmObject;
        this.shoppingCartObject = shoppingCartObject;

        // assign products available in store.
        // done by accessing the product list updated in the WestminsterShoppingManager object
        availableProducts = wsmObject.getStoreInventory();
        // get the shopping cart list from the ShoppingCart class
        shoppingCartItems = shoppingCartObject.getSelectedProducts();

        // set the title, default close operation, and size of the frame
        setTitle("Westminster Shopping Centre");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 600);

        // create a new table model with the following column names
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"});

        detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        initUI();
        setLocationRelativeTo(null);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"All", "Clothing", "Electronics"});
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Recently Added", "Alphabetical Order"});

        filterComboBox.addActionListener(e -> updateTableAndDetails((String) filterComboBox.getSelectedItem(), (String) sortComboBox.getSelectedItem()));
        sortComboBox.addActionListener(e -> updateTableAndDetails((String) filterComboBox.getSelectedItem(), (String) sortComboBox.getSelectedItem()));

        JTable productsTable = new JTable(tableModel);
        DefaultTableCellRenderer cellRenderer = createCellRenderer();
        productsTable.setDefaultRenderer(Object.class, cellRenderer);

        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel(productsTable);

        add(createControlPanel(filterComboBox, sortComboBox), BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateTableAndDetails("All", "Recently Added");

        // Add a selection listener directly to the table to update details when a new row is selected
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1) {
                detailsTextArea.setText(getProductDetails(selectedRow));
            }
        });
    }

    private JPanel createControlPanel(JComboBox<String> filterComboBox, JComboBox<String> sortComboBox) {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(filterComboBox);

        JPanel sortPanel = new JPanel(new FlowLayout());
        sortPanel.add(new JLabel("Sort By:"));
        sortPanel.add(sortComboBox);

        controlPanel.add(sortPanel);

        JButton shoppingCartButton = new JButton("Open Shopping Cart");
        shoppingCartButton.addActionListener(e -> openShoppingCart());

        controlPanel.add(shoppingCartButton);

        return controlPanel;
    }

    private JPanel createBottomPanel(JTable productsTable) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.add(new JLabel("Selected Product - Details"), BorderLayout.NORTH);
        bottomPanel.add(detailsTextArea, BorderLayout.CENTER);

        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            addToShoppingCart(selectedRow);
        });

        JPanel addToCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addToCartButtonPanel.add(addToCartButton);
        bottomPanel.add(addToCartButtonPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private DefaultTableCellRenderer createCellRenderer() {
        return new DefaultTableCellRenderer() {
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
    }

    private void updateTableAndDetails(String filter, String sort) {
        tableModel.setRowCount(0);

        List<Product> sortedProducts = switch (sort) {
            case "Alphabetical Order" -> availableProducts.stream().sorted((p1, p2) -> p1.getProductName().compareToIgnoreCase(p2.getProductName())).collect(Collectors.toList());
            default -> availableProducts;
        };

        List<Product> filteredProducts = switch (filter) {
            case "Clothing" -> sortedProducts.stream().filter(product -> product instanceof Clothing).collect(Collectors.toList());
            case "Electronics" -> sortedProducts.stream().filter(product -> product instanceof Electronics).collect(Collectors.toList());
            default -> sortedProducts;
        };

        for (Product product : filteredProducts) {
            Object[] rowData = new Object[5];
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

            tableModel.addRow(rowData);
        }

        int selectedRow = tableModel.getRowCount() > 0 ? 0 : -1;
        if (selectedRow != -1) {
            detailsTextArea.setText(getProductDetails(selectedRow));
        } else {
            detailsTextArea.setText("");
        }
    }

    private String getProductDetails(int rowIndex) {
        StringBuilder details = new StringBuilder();
        details.append("Product ID: ").append(tableModel.getValueAt(rowIndex, 0)).append("\n");
        details.append("Category: ").append(tableModel.getValueAt(rowIndex, 2)).append("\n");
        details.append("Name: ").append(tableModel.getValueAt(rowIndex, 1)).append("\n");

        if (tableModel.getValueAt(rowIndex, 4) != null) {
            details.append(tableModel.getValueAt(rowIndex, 4)).append("\n");
        }

        Object selectedProductID = tableModel.getValueAt(rowIndex, 0);
        availableProducts.stream()
                .filter(product -> product.getProductID().equals(selectedProductID))
                .findFirst().ifPresent(selectedProduct -> details.append("Available Items: ").append(selectedProduct.getItemsInStock()).append("\n"));

        return details.toString();
    }

    private void addToShoppingCart(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < availableProducts.size()) {
            Product selectedProduct = availableProducts.get(rowIndex);
            Product productToAdd;

            // Check the type of the selected product and create an instance accordingly
            if (selectedProduct instanceof Clothing clothing) {
                productToAdd = new Clothing(
                        clothing.getProductID(),
                        clothing.getProductName(),
                        clothing.getItemsInStock(),
                        clothing.getPrice(),
                        clothing.getSize(),
                        clothing.getColour()
                );
            } else if (selectedProduct instanceof Electronics electronics) {
                productToAdd = new Electronics(
                        electronics.getProductID(),
                        electronics.getProductName(),
                        electronics.getItemsInStock(),
                        electronics.getPrice(),
                        electronics.getBrand(),
                        electronics.getWarrantyPeriod()
                );
            } else {
                // Handle other product types as needed
                return;
            }

            // Add the newly created Product object to the shoppingCartItems list
            shoppingCartItems.add(productToAdd);
            // Add the newly created Product object to the selectedProducts list in shoppingCart class
            shoppingCartObject.setSelectedProducts(shoppingCartItems);;

            // Update the items in stock for the item added to cart
            String productID = productToAdd.getProductID();
            for (Product product : availableProducts) {
                if (product.getProductID().equals(productID)) {
                    System.out.println(product.getItemsInStock());  // debugging statement REMOVE FINALLY
                    product.setItemsInStock(product.getItemsInStock() - 1);
                    System.out.println(product.getItemsInStock());  // debugging statement REMOVE FINALLY
                    break;
                }
            }
            // save the changes made to the store inventory into the file
            wsmObject.saveToFile();
            // update the UI or perform other actions as needed
            JOptionPane.showMessageDialog(null, "Product added to the shopping cart!");
        }
    }

    private void openShoppingCart() {
        SwingUtilities.invokeLater(() -> {
            ShoppingCartGUI shoppingCartGUI = new ShoppingCartGUI(customer, shoppingCartObject);
            shoppingCartGUI.setVisible(true);
        });
    }
}
