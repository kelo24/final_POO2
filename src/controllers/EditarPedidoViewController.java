package controllers;


public class EditarPedidoViewController {
    
    private HomeController homeController;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("EditarPedidoViewController inicializado");
    }
}