
package Main;

import controllers.HomeController;
import controllers.LoginViewController;

/**
 *
 * @author PC
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Inicializar la estructura de archivos
    config.FileConfig.inicializar();
    
    // Ejecutar la aplicación a través del HomeController
    java.awt.EventQueue.invokeLater(() -> {
        HomeController homeController = HomeController.getInstance();
        homeController.run();
    });
    }
}
