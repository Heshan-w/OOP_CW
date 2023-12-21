import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    // declaring a list of "Product" type objects to store products selected by the user
    private final List<Product> selectedProducts;

    // constructor for the "ShoppingCart" class
    public ShoppingCart() {
        this.selectedProducts = new ArrayList<>();
    }

    // method to add a product to the "selectedProducts" list
    public void addProduct(Product product){
        selectedProducts.add(product);
    }

    // method to remove a product from the "selectedProducts" list
    public void removeProduct(Product product){
        selectedProducts.remove(product);
    }

    // method to print the total price of the products in the "selectedProducts" list
    public double calculateTotalPrice(){
        double totalPrice = 0;
        for (Product product : selectedProducts) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}
