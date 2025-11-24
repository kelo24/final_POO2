package controllers;

/**
 * HomeController - Controlador principal de la aplicación
 * Gestiona y coordina los demás controllers
 */
public class HomeController {
    
    private static HomeController instance;
    
    /**
     * Constructor privado para Singleton
     */
    private HomeController() {
        System.out.println("HomeController inicializado");
    }
    
    /**
     * Obtiene la instancia única del HomeController (Singleton)
     * @return instancia de HomeController
     */
    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }
    
    /**
     * Ejecuta la aplicación
     * Muestra el LoginView
     */
    public void run() {
        System.out.println("Iniciando aplicación...");
        
        // Mostrar LoginView
        LoginViewController loginController = new LoginViewController();
        loginController.initialize();
        loginController.showLoginView();
    }
    
    /**
     * Cierra sesión y vuelve al login
     */
    public void logout() {
        System.out.println("Cerrando sesión...");
        
        // Cerrar todas las ventanas abiertas
        // Mostrar LoginView nuevamente
        
        LoginViewController loginController = new LoginViewController();
        loginController.initialize();
        loginController.showLoginView();
    }
    
    // Aquí puedes agregar métodos para gestionar otros controllers
    // Por ejemplo:
    // public PedidoController getPedidoController() { ... }
    // public ClienteController getClienteController() { ... }
    // public InventarioController getInventarioController() { ... }
}