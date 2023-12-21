public interface ShoppingManager {
    // declaring "ShoppingManager" interface specific methods
    // no access modifier specified as all methods in an interface are public by default

    // method to add a product to a data structure(ie: List) specified in a class implementing this interface
    void addProduct();
    // method to remove a product from a data structure specified in a class implementing this interface
    void removeProduct();
    // method to print the list of products in a data structure specified in a class implementing this interface
    void printProductList();
    // method to save products to a file from a data structure specified in a class implementing this interface
    void saveToFile();
    // method to load products from a file to a data structure specified in a class implementing this interface
    void loadFromFile();
}
