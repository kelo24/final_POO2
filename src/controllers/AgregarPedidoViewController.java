package controllers;

/**
 * AgregarPedidoViewController
 */
public class AgregarPedidoViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("AgregarPedidoViewController inicializado");
    }
}