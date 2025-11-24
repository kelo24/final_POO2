
package controllers;

/**
 *
 * @author kelvincitopkmz
 */
public class HomeController {
    
    private static HomeController instance;
    
    private HomeController() {
    }
    
    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }
    
    public void initialize() {
        System.out.println("HomeController inicializado");
    }
}
