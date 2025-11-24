package controllers;

/**
 * PaymentInfoViewController
 */
public class PaymentInfoViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("PaymentInfoViewController inicializado");
    }
}