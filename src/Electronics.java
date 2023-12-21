public class Electronics extends Product{
    // declaring "Electronics" class specific instance variables
    private String brand;
    private int warrantyPeriod;

    // constructor for the "Electronics" class
    public Electronics(String productID, String productName, int itemsInStock, double price, String brand, int warrantyPeriod) {
        super(productID, productName, itemsInStock, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // getters and setters for the instance variables
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}