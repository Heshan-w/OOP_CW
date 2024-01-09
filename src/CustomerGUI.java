import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerGUI extends JFrame {
    // declaring instance variables
    private User customer;
    private final List<Product> availableProducts;
    private final List<Product> shoppingCartItems;
    // DefaultTableModel is used to display the products in a table
    private DefaultTableModel tableModel;
    // JTextArea is used to display the details of the selected product
    private JTextArea detailsTextArea;
    private JPanel bottomPanel;

    // constructor for the "CustomerGUI" class
    public CustomerGUI(User customer, List<Product> availableProducts, List<Product> shoppingCartItems) {
        this.customer = customer;
        this.availableProducts = availableProducts;
        this.shoppingCartItems = shoppingCartItems;

        // setting the title of the JFrame window as "Westminster Shopping Centre"
        setTitle("Westminster Shopping Centre");
        // setting the default close operation of the JFrame window as "EXIT_ON_CLOSE"
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // setting the size of the JFrame window as 1000 x 600
        setSize(1500, 600);

        // calling the "initUI" method to initialise the GUI components
        initUI();
        // setting the location of the JFrame window to the center of the screen
        setLocationRelativeTo(null);
    }

    // method to initialise the GUI components
    // method to initialise the GUI components
// method to initialise the GUI components
// method to initialise the GUI components
    private void initUI() {
        // setting the layout of the JFrame window as "BorderLayout"
        setLayout(new BorderLayout());

        // declaring and initialising a JComboBox to display the filter options
        String[] filterOptions = {"All", "Clothing", "Electronics"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        // adding an ActionListener to the JComboBox
        // the "updateTableAndDetails" method is called when the user selects a filter option
        filterComboBox.addActionListener(e -> updateTableAndDetails((String) filterComboBox.getSelectedItem()));

        // declaring and initialising a JComboBox to display the sorting options
        String[] sortOptions = {"Recently Added", "Alphabetical Order"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        // adding an ActionListener to the JComboBox
        // the "updateTableAndDetails" method is called when the user selects a sorting option
        sortComboBox.addActionListener(e -> updateTableAndDetails(filterComboBox.getSelectedItem().toString(), (String) sortComboBox.getSelectedItem()));

        // declaring and initialising a DefaultTableModel to display the products in a table
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // setting the column names of the table
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"});

        // declaring and initialising a JTextArea to display the details of the selected product
        detailsTextArea = new JTextArea();
        // setting the JTextArea as non-editable
        detailsTextArea.setEditable(false);
        // setting the JTextArea as non-focusable
        detailsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // calling the "updateTableAndDetails" method to update the table and the details of the selected product
        updateTableAndDetails("All");

        // declaring and initialising a JTable to display the products, it displays the products in a table
        // the "tableModel" is updated when the user selects a filter or sorting option
        JTable productsTable = new JTable(tableModel);

        // declaring and initialising a DefaultTableCellRenderer to render the cells of the JTable
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // setting the alignment of the cells to center
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // setting the border of the cells
                ((JComponent) component).setBorder(BorderFactory.createCompoundBorder(
                        // setting the border of the cells as a line border
                        BorderFactory.createLineBorder(Color.BLACK),
                        // setting the border of the cells as an empty border with 5px padding
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                return component;
            }
        };

        // setting the alignment of the cells to center
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        productsTable.setDefaultRenderer(Object.class, cellRenderer);
        // setting the border color of the cells to black
        productsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // declaring and initialising a JScrollPane to display the JTable
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        // setting the preferred size of the JScrollPane
        tableScrollPane.setPreferredSize(new Dimension(0, 300));

        // declaring and initialising a JPanel to display the JTable
        JPanel topPanel = new JPanel(new BorderLayout());
        // the JPanel is added to the center of the JFrame window
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        // declaring and initialising a JPanel to display the JTextArea
        bottomPanel = new JPanel(new BorderLayout());
        // setting the border of the JPanel as an empty border with 10px padding
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // adding a JLabel to the JPanel
        bottomPanel.add(new JLabel("Selected Product - Details"), BorderLayout.NORTH);
        // adding the JTextArea to the JPanel
        bottomPanel.add(detailsTextArea, BorderLayout.CENTER);

        // Add a ListSelectionListener to the table
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            // Update the details of the selected product
            int selectedRow = productsTable.getSelectedRow();
            // Check if a row is selected
            if (selectedRow != -1) {
                detailsTextArea.setText(getProductDetails(selectedRow));
            }
        });

        // declaring and initialising a JPanel to display the JComboBox
        JPanel controlPanel = new JPanel(new FlowLayout());
        // adding a JLabel to the JPanel and naming it "Filter:"
        controlPanel.add(new JLabel("Filter:"));
        // adding the JComboBox to the JPanel
        controlPanel.add(filterComboBox);

        // declaring and initialising a JPanel to display the sorting JComboBox
        JPanel sortPanel = new JPanel(new FlowLayout());
        // adding a JLabel to the JPanel and naming it "Sort By:"
        sortPanel.add(new JLabel("Sort By:"));
        // adding the sorting JComboBox to the JPanel
        sortPanel.add(sortComboBox);

        // adding the sortPanel to the controlPanel
        controlPanel.add(sortPanel);

        // declaring and initialising a JPanel to display the "Add to Shopping Cart" button
        JPanel addToCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // declaring and initialising a JButton to add the selected product to the shopping cart
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        // adding an ActionListener to the JButton
        addToCartButton.addActionListener(e -> {
            // Add your logic here for adding the selected product to the shopping cart
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1) {
                Product selectedProduct = availableProducts.get(selectedRow);
                if (selectedProduct != null) {
                    // Add the selected product to the shopping cart
                    addToShoppingCart(selectedProduct);
                    JOptionPane.showMessageDialog(null, "Product added to the shopping cart!");
                }
            }
        });
        // adding the JButton to the JPanel
        addToCartButtonPanel.add(addToCartButton);
        // adding the JPanel to the JFrame window
        bottomPanel.add(addToCartButtonPanel, BorderLayout.SOUTH);

        // declaring and initialising a JPanel to display the "Open Shopping Cart" button
        JPanel shoppingCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // declaring and initialising a JButton to launch the shopping cart
        JButton shoppingCartButton = new JButton("Open Shopping Cart");
        // adding an ActionListener to the JButton
        shoppingCartButton.addActionListener(e -> openShoppingCart());
        // adding the JButton to the JPanel
        shoppingCartButtonPanel.add(shoppingCartButton);
        // adding the JPanel to the JFrame window
        controlPanel.add(shoppingCartButtonPanel);

        // adding the JPanels to the JFrame window
        add(controlPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }




    // method to update the table and the details of the selected product
    private void updateTableAndDetails(String filter) {
        updateTableAndDetails(filter, "Recently Added");
    }

    // method to update the table and the details of the selected product
    private void updateTableAndDetails(String filter, String sort) {
        // Clear the table
        tableModel.setRowCount(0);

        // Sort the products based on the selected sorting option
        List<Product> sortedProducts = switch (sort) {
            // If the selected sorting option is "Alphabetical Order", then sort the products alphabetically
            case "Alphabetical Order" -> availableProducts.stream().sorted((p1, p2) -> p1.getProductName().compareToIgnoreCase(p2.getProductName())).collect(Collectors.toList());
            // If the selected sorting option is "Recently Added", then display the products in their original order
            default -> availableProducts;
        };

        // Filter the products based on the selected filter
        List<Product> filteredProducts = switch (filter) {
            // If the selected filter is "Clothing", then display only the clothing products
            case "Clothing" ->
                    sortedProducts.stream().filter(product -> product instanceof Clothing).collect(Collectors.toList());
            // If the selected filter is "Electronics", then display only the electronics products
            case "Electronics" ->
                    sortedProducts.stream().filter(product -> product instanceof Electronics).collect(Collectors.toList());
            // If the selected filter is "All", then display all the products
            default -> sortedProducts;
        };

        // Add the filtered products to the table
        for (Product product : filteredProducts) {
            // Add the product details to the table
            // The "tableModel" is updated when the user selects a filter or sorting option
            Object[] rowData = new Object[5];
            // The "Product ID" column
            rowData[0] = product.getProductID();
            // The "Name" column
            rowData[1] = product.getProductName();
            // The "Category" column
            rowData[3] = product.getPrice();

            // The "Info" column
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
        // if a row is selected, then update the details of the selected product
        if (selectedRow != -1) {
            detailsTextArea.setText(getProductDetails(selectedRow));
            // if not selected, then clear the details of the selected product
        } else {
            detailsTextArea.setText("");
        }
    }

    // method to get the details of the selected product
    private String getProductDetails(int rowIndex) {
        // Get the details of the selected product
        StringBuilder details = new StringBuilder();
        // The "Product ID" column
        details.append("Product ID: ").append(tableModel.getValueAt(rowIndex, 0)).append("\n");
        // The "Category" column
        details.append("Category: ").append(tableModel.getValueAt(rowIndex, 2)).append("\n");
        // The "Name" column
        details.append("Name: ").append(tableModel.getValueAt(rowIndex, 1)).append("\n");

        // The "Info" column
        if (tableModel.getValueAt(rowIndex, 4) != null) {
            details.append(tableModel.getValueAt(rowIndex, 4)).append("\n");
        }

        // The "Available Items" column
        Product selectedProduct = availableProducts.get(rowIndex);
        if (selectedProduct != null) {
            details.append("Available Items: ").append(selectedProduct.getItemsInStock()).append("\n");
        }

        return details.toString();
    }

    private void addToShoppingCart(Product product) {
        // Implement your logic to add the product to the shopping cart
        // You can update the shopping cart of the customer or perform any other necessary actions
    }

    private void openShoppingCart() {
        // Create and display the shopping cart GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            ShoppingCartGUI shoppingCartGUI = new ShoppingCartGUI(customer, shoppingCartItems);
            shoppingCartGUI.setVisible(true);
        });
    }
}