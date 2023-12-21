public class Clothing extends  Product{
    // declaring "Clothing" class specific instance variables
    private String size;
    private String colour;

    // constructor for the "Clothing" class
    public Clothing(String productID, String productName, int itemsInStock, double price, String size, String colour) {
        super(productID, productName, itemsInStock, price);
        this.size = size;
        this.colour = colour;
    }

    // getters and setters for the instance variables
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
