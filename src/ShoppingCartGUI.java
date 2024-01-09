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

        // Creating an empty panel with a white background
        JPanel blankPanel = new JPanel();
        blankPanel.setBackground(Color.WHITE);

        // Adding the blank panel to the frame
        add(blankPanel, BorderLayout.CENTER);
    }
}
