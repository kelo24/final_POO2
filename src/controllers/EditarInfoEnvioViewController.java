package controllers;

/**
 * EditarInfoEnvioViewController
 */
public class EditarInfoEnvioViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("EditarInfoEnvioViewController inicializado");
    }
}