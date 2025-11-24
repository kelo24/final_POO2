package controllers;

/**
 * LoginViewController
 */
public class LoginViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("LoginViewController inicializado");
    }
}