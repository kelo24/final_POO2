package controllers;

/**
 * DashboardViewController
 */
public class DashboardViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("DashboardViewController inicializado");
    }
}