
package Main;

import controllers.HomeController;
import controllers.LoginViewController;

/**
 *
 * @author PC
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Ejecutar la aplicación a través del HomeController
        java.awt.EventQueue.invokeLater(() -> {
            HomeController homeController = HomeController.getInstance();
            homeController.run();
        });
    }
}
