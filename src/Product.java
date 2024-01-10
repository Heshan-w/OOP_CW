import java.io.Serializable;

public abstract class Product implements Serializable {
    // declaring the common instance variables for subclasses to inherit and use
    private String productID;
    private String productName;
    private int itemsInStock;
    private double price;

    // constructor for the Product class to be used by subclasses
    public Product(String productID, String productName, int itemsInStock, double price) {
        this.productID = productID;
        this.productName = productName;
        this.itemsInStock = itemsInStock;
        this.price = price;
    }

    // getters and setters for the instance variables
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(int itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
