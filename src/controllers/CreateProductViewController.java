package controllers;

/**
 * CreateProductViewController
 */
public class CreateProductViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("CreateProductViewController inicializado");
    }
}