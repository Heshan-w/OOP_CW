import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("r u manager or customer");
        Scanner scanner = new Scanner(System.in);
        WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
        westminsterShoppingManager.displayManagerConsole();
        System.out.println("Thank you for using Westminster Shopping Manager!");
//        westminsterShoppingManager.printProductList();
//        System.out.println();
//        System.out.println();}
    }
}
