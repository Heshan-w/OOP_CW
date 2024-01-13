import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

// the class that creates the GUI for the customer
// extends JFrame to create a new frame
public class CustomerGUI extends JFrame {
    // declaring a "User" type object to store the customer details
    private final User customer;
    // declaring a "WestminsterShoppingManager" type object to store the WestminsterShoppingManager object
    private final WestminsterShoppingManager wsmObject;
    // declaring a "ShoppingCart" type object to store the ShoppingCart object
    private final ShoppingCart shoppingCartObject;
    // the list that stores the products currently available in the store
    private final List<Product> availableProducts;
    // a list to temporarily store the selected products
    private final List<Product> shoppingCartItems;
    // "DefaultTableModel" type object used to create a table with the following column names
    private final DefaultTableModel tableModel;
    // "JTextArea" type object used to display the details of the selected product
    private final JTextArea detailsTextArea;


    // constructor for the "CustomerGUI" class
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
        // setting the default close operation to "DISPOSE_ON_CLOSE" to close the current frame only
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // setting the size of the frame
        setSize(1500, 600);

        // creating a new "DefaultTableModel" object
        tableModel = new DefaultTableModel() {
            // overriding the "isCellEditable" method to make the cells in the table non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // setting the column names for the table
        // the column names are stored in an array of objects and passed as a parameter
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"});

        // creating a new "JTextArea" object
        // this text area is used to display the details of the selected product
        detailsTextArea = new JTextArea();
        // setting the text area to be non-editable
        detailsTextArea.setEditable(false);
        // setting the border for the text area
        // the border is created using the "BorderFactory" class
        detailsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // calling the "initUI" method to initialize the UI of the frame
        initUI();
        // used to center the frame on the screen
        setLocationRelativeTo(null);
    }

    // method to initialize the UI of the frame
    private void initUI() {
        // setting the layout of the frame to "BorderLayout"
        setLayout(new BorderLayout());

        // creating a new "JComboBox" object(the drop-down menu for the users to select)
        // this combo box is used to filter the products based on their category
        JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"All", "Clothing", "Electronics"});
        // this combo box is used to sort the products
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Recently Added", "Alphabetical Order"});

        // adding action listeners to the combo boxes, a lambda expression is used to implement the action listener
        // the "updateTableAndDetails" method is called with the selected filter and sort options as parameters
        filterComboBox.addActionListener(e -> updateTableAndDetails((String) filterComboBox.getSelectedItem(), (String) sortComboBox.getSelectedItem()));
        sortComboBox.addActionListener(e -> updateTableAndDetails((String) filterComboBox.getSelectedItem(), (String) sortComboBox.getSelectedItem()));

        // creating a new "JTable" object which will display the products available in the store(inventory)
        JTable productsTable = new JTable(tableModel);
        // creating a new "DefaultTableCellRenderer" object to add borders to the cells in the table
        DefaultTableCellRenderer cellRenderer = createCellRenderer();
        // setting the custom cell renderer for each column
        productsTable.setDefaultRenderer(Object.class, cellRenderer);

        // creating a new "JScrollPane" object to add a scroll bar to the table
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        // setting the preferred size of the scroll pane
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        // setting the border for the scroll pane
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // creating a new "JPanel" object to add the table scroll pane to the top of the frame
        JPanel topPanel = new JPanel(new BorderLayout());
        // adding the table scroll pane to the center of the topPanel
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        // creating a new "JPanel" object to add the bottom panel to the bottom of the frame
        JPanel bottomPanel = createBottomPanel(productsTable);

        // adding the control panel, top panel, and bottom panel to the frame
        add(createControlPanel(filterComboBox, sortComboBox), BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // calling the "updateTableAndDetails" method with the default filter and sort options as parameters
        updateTableAndDetails("All", "Recently Added");

        // Add a selection listener directly to the table to update details when a new row is selected
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            // Check if a row is selected
            if (selectedRow != -1) {
                // Update the details text area with the details of the selected product
                detailsTextArea.setText(getProductDetails(selectedRow));
            }
        });
    }

    // method to create the control panel which contains the filter and sort dropdown boxes
    private JPanel createControlPanel(JComboBox<String> filterComboBox, JComboBox<String> sortComboBox) {
        // Create the main control panel with a FlowLayout
        JPanel controlPanel = new JPanel(new FlowLayout());
        // Add a label and the filter JComboBox to the main control panel
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(filterComboBox);

        // Create a sort panel with a FlowLayout
        JPanel sortPanel = new JPanel(new FlowLayout());
        // Add a label and the sort JComboBox to the sort panel
        sortPanel.add(new JLabel("Sort By:"));
        sortPanel.add(sortComboBox);

        // Add the sort panel to the main control panel
        controlPanel.add(sortPanel);

        // Create a button to open the shopping cart
        JButton shoppingCartButton = new JButton("Open Shopping Cart");
        // Add an action listener to the button to open the shopping cart
        shoppingCartButton.addActionListener(e -> openShoppingCart());

        // Add the shopping cart button to the main control panel
        controlPanel.add(shoppingCartButton);

        return controlPanel;
    }

    private JPanel createBottomPanel(JTable productsTable) {
        // Create the bottom panel with BorderLayout and padding
        JPanel bottomPanel = new JPanel(new BorderLayout());
        // Set the padding for the bottom panel
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Add a label to the top of the bottom panel
        bottomPanel.add(new JLabel("Selected Product - Details"), BorderLayout.NORTH);
        // Add the details text area to the center of the bottom panel
        bottomPanel.add(detailsTextArea, BorderLayout.CENTER);

        // Create a button to add the selected product to the shopping cart
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        // Add an action listener to the button to add the selected product to the shopping cart
        addToCartButton.addActionListener(e -> {
            // Get the selected row from the table
            int selectedRow = productsTable.getSelectedRow();
            addToShoppingCart(selectedRow);
        });

        // Create a panel to center the button
        JPanel addToCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addToCartButtonPanel.add(addToCartButton);
        bottomPanel.add(addToCartButtonPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    // method to create a custom cell renderer to add borders to each side of the cells
    private DefaultTableCellRenderer createCellRenderer() {
        // return a new DefaultTableCellRenderer object
        return new DefaultTableCellRenderer() {
            // overriding the "getTableCellRendererComponent" method to add borders to each side of the cells
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // creating a new "Component" object
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // setting the border for the component
                ((JComponent) component).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                return component;
            }
        };
    }

    // method to update the table and details text area based on the selected filter and sort options
    private void updateTableAndDetails(String filter, String sort) {
        // Clear the table
        tableModel.setRowCount(0);

        // Sort and filter the products based on the selected options
        List<Product> sortedProducts = switch (sort) {
            // Sort the products based on the selected option
            case "Alphabetical Order" -> availableProducts.stream().sorted((p1, p2) -> p1.getProductName().compareToIgnoreCase(p2.getProductName())).collect(Collectors.toList());
            default -> availableProducts;
        };

        // Filter the products based on the selected option
        List<Product> filteredProducts = switch (filter) {
            // if the filter option is Clothing, filter the products to only include products of type Clothing
            case "Clothing" -> sortedProducts.stream().filter(product -> product instanceof Clothing).collect(Collectors.toList());
            case "Electronics" -> sortedProducts.stream().filter(product -> product instanceof Electronics).collect(Collectors.toList());
            // finally if its "All", return the sorted products
            default -> sortedProducts;
        };

        // Add the filtered and sorted products to the table
        for (Product product : filteredProducts) {
            // Create a new row with the product details
            Object[] rowData = new Object[5];
            // Set the product ID, product name, and price in the row data
            rowData[0] = product.getProductID();
            rowData[1] = product.getProductName();
            rowData[3] = product.getPrice();

            // Check the type of the product to determine the category (Clothing or Electronics)
            if (product instanceof Clothing) {
                // If the product is Clothing, set the category as "Clothing"
                rowData[2] = "Clothing";
                // Set additional details specific to Clothing (Size and Colour)
                rowData[4] = "Size: " + ((Clothing) product).getSize() + ", Colour: " + ((Clothing) product).getColour();
            } else if (product instanceof Electronics) {
                // If the product is Electronics, set the category as "Electronics"
                rowData[2] = "Electronics";
                // Set additional details specific to Electronics (Brand and Warranty Period)
                rowData[4] = "Brand: " + ((Electronics) product).getBrand() +
                        ", Warranty Period: " + ((Electronics) product).getWarrantyPeriod() + " weeks";
            }

            // Add the row data to the table
            tableModel.addRow(rowData);
        }

        // Get the index of the selected row. If no rows are present, set selectedRow to -1.
        int selectedRow = tableModel.getRowCount() > 0 ? 0 : -1;
        // Check if a row is selected
        if (selectedRow != -1) {
            // Retrieve and display the details of the selected product in the details text area
            detailsTextArea.setText(getProductDetails(selectedRow));
        } else {
            // If no row is selected, clear the details text area
            detailsTextArea.setText("");
        }
    }

    private String getProductDetails(int rowIndex) {
        StringBuilder details = new StringBuilder();
        // Append basic product details to the StringBuilder
        details.append("Product ID: ").append(tableModel.getValueAt(rowIndex, 0)).append("\n");
        details.append("Category: ").append(tableModel.getValueAt(rowIndex, 2)).append("\n");
        details.append("Name: ").append(tableModel.getValueAt(rowIndex, 1)).append("\n");

        // Check if additional details are available and append them
        if (tableModel.getValueAt(rowIndex, 4) != null) {
            details.append(tableModel.getValueAt(rowIndex, 4)).append("\n");
        }

        // Find the selected product in availableProducts and append its available items
        Object selectedProductID = tableModel.getValueAt(rowIndex, 0);
        availableProducts.stream()
                .filter(product -> product.getProductID().equals(selectedProductID))
                .findFirst().ifPresent(selectedProduct -> details.append("Available Items: ").append(selectedProduct.getItemsInStock()).append("\n"));

        return details.toString();
    }

    private void addToShoppingCart(int rowIndex) {
        // Check if rowIndex is valid and within the availableProducts size
        if (rowIndex >= 0 && rowIndex < availableProducts.size()) {
            // Retrieve the selected product from availableProducts
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
                    product.setItemsInStock(product.getItemsInStock() - 1);
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
        // Check if the shopping cart is empty
        if (shoppingCartItems.isEmpty()) {
            // if so display a message saying so and avoid launching the shopping cart GUI
            JOptionPane.showMessageDialog(null, "Shopping cart is empty!");
            return;
        }
        // if the shopping cart is not empty, launch the shopping cart GUI
        SwingUtilities.invokeLater(() -> {
            // create a new "ShoppingCartGUI" object and pass the customer and shoppingCartObject as parameters
            ShoppingCartGUI shoppingCartGUI = new ShoppingCartGUI(customer, shoppingCartObject);
            // set the visibility of the shopping cart GUI to true
            shoppingCartGUI.setVisible(true);
        });
    }
}
