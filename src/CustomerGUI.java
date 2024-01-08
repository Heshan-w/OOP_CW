import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomerGUI extends JFrame {
    // Instance variables for the customer, available products, and shopping cart items
    private User customer;
    private final List<Product> availableProducts;
    private final List<Product> shoppingCartItems;

    // Components for GUI
    // Table model to display products
    private DefaultTableModel tableModel;
    // TextArea to display details of the selected product
    private JTextArea detailsTextArea;
    // ComboBox for sorting options
    private JComboBox<String> sortByComboBox;

    // Constructor for the "CustomerGUI" class
    public CustomerGUI(User customer, List<Product> availableProducts, List<Product> shoppingCartItems) {
        this.customer = customer;
        this.availableProducts = availableProducts;
        this.shoppingCartItems = shoppingCartItems;

        // Setting up the JFrame window
        setTitle("Westminster Shopping Centre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Initializing the GUI components
        initUI();
        setLocationRelativeTo(null);
    }

    // Method to initialize the GUI components
    private void initUI() {
        // Setting the layout of the JFrame window as BorderLayout
        setLayout(new BorderLayout());

        // Filter options for the products
        String[] filterOptions = {"All", "Clothing", "Electronics"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.addActionListener(e -> updateTableAndDetails((String) Objects.requireNonNull(filterComboBox.getSelectedItem())));

        // Sorting options for the products
        String[] sortByOptions = {"Recently Added", "Alphabetical Order"};
        sortByComboBox = new JComboBox<>(sortByOptions);
        sortByComboBox.addActionListener(e -> updateTableAndDetails((String) Objects.requireNonNull(filterComboBox.getSelectedItem())));

        // Creating a JPanel for the control panel (filter and sort options)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(filterComboBox);
        controlPanel.add(new JLabel("Sort By:"));
        controlPanel.add(sortByComboBox);
        add(controlPanel, BorderLayout.NORTH);

        // Initializing the table model for displaying products
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"});

        // Initializing the text area for displaying product details
        detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Updating the table and details based on the initial filter
        updateTableAndDetails("All");

        // Creating a JTable for displaying products
        JTable productsTable = new JTable(tableModel);

        // Cell renderer for center-aligning cells in the table
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Serial
            private static final long serialVersionUID = 1L;

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

        // Setting alignment and border for cells in the table
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        productsTable.setDefaultRenderer(Object.class, cellRenderer);
        productsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Creating a JScrollPane for the table
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));

        // Creating a JPanel for the top part of the JFrame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Creating a JPanel for the bottom part of the JFrame
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.add(new JLabel("Selected Product - Details"), BorderLayout.NORTH);
        bottomPanel.add(detailsTextArea, BorderLayout.CENTER);

        // Adding a ListSelectionListener to the table
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1) {
                detailsTextArea.setText(getProductDetails(selectedRow));
            }
        });

        // Creating a JPanel for the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1) {
                Product selectedProduct = availableProducts.get(selectedRow);
                if (selectedProduct != null) {
                    addToShoppingCart(selectedProduct);
                    JOptionPane.showMessageDialog(null, "Product added to the shopping cart!");
                }
            }
        });
        buttonPanel.add(addToCartButton);

        // Adding components to the bottom panel
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adding panels to the JFrame window
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to update the table and details based on the selected filter
    private void updateTableAndDetails(String filter) {
        // Use sortByComboBox.getSelectedIndex() to determine the selected sorting option
        List<Product> sortedProducts = sortProducts(availableProducts, sortByComboBox.getSelectedIndex());

        // Clear the table
        tableModel.setRowCount(0);

        // Filter the products based on the selected filter
        List<Product> filteredProducts = switch (filter) {
            case "Clothing" ->
                    sortedProducts.stream().filter(product -> product instanceof Clothing).collect(Collectors.toList());
            case "Electronics" ->
                    sortedProducts.stream().filter(product -> product instanceof Electronics).collect(Collectors.toList());
            default -> sortedProducts;
        };

        // Add the filtered products to the table
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

            // Add the product to the table
            tableModel.addRow(rowData);
        }

        // Update the details of the selected product
        int selectedRow = tableModel.getRowCount() > 0 ? 0 : -1;
        if (selectedRow != -1) {
            detailsTextArea.setText(getProductDetails(selectedRow));
        } else {
            detailsTextArea.setText("");
        }
    }

    // Method to get the details of the selected product
    private String getProductDetails(int rowIndex) {
        StringBuilder details = new StringBuilder();
        details.append("Product ID: ").append(tableModel.getValueAt(rowIndex, 0)).append("\n");
        details.append("Category: ").append(tableModel.getValueAt(rowIndex, 2)).append("\n");
        details.append("Name: ").append(tableModel.getValueAt(rowIndex, 1)).append("\n");

        if (tableModel.getValueAt(rowIndex, 4) != null) {
            details.append(tableModel.getValueAt(rowIndex, 4)).append("\n");
        }

        Product selectedProduct = availableProducts.get(rowIndex);
        if (selectedProduct != null) {
            details.append("Available Items: ").append(selectedProduct.getItemsInStock()).append("\n");
        }

        return details.toString();
    }

    // Method to add a product to the shopping cart
    private void addToShoppingCart(Product product) {
        // Implement your logic to add the product to the shopping cart
        // You can update the shopping cart of the customer or perform any other necessary actions
    }

    // Method to sort products based on the sorting option
    private List<Product> sortProducts(List<Product> products, int sortingOption) {
        // Sorting by Recently Added (sortingOption = 0) or Alphabetical Order (sortingOption = 1)
        if (sortingOption == 0) {
            // Sort by the original order (do nothing)
            return products;
        } else {
            // Sort alphabetically by product name
            return products.stream()
                    .sorted(Comparator.comparing(Product::getProductName))
                    .collect(Collectors.toList());
        }
    }
}
