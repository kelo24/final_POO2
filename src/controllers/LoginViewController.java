package controllers;

import views.LoginView;
import views.DashboardView;

/**
 * LoginViewController - Controlador para la lógica de negocio del Login
 */
public class LoginViewController {
    
    private LoginView loginView;
    
    public void initialize() {
        HomeController.getInstance();
        System.out.println("LoginViewController inicializado");
    }
    
    /**
     * Muestra la vista de login
     */
    public void showLoginView() {
        if (loginView == null) {
            loginView = new LoginView();
            loginView.setController(this);
        }
        loginView.setVisible(true);
        System.out.println("LoginView mostrado");
    }
    
    /**
     * Valida las credenciales de login
     * @param username nombre de usuario
     * @param password contraseña
     * @return true si las credenciales son válidas
     */
    public boolean validateLogin(String username, String password) {
        // Validación básica: campos no vacíos
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Aquí va tu lógica de validación real:
        // - Consultar base de datos
        // - Validar contra archivo
        // - Llamar a un servicio de autenticación
        
        // Ejemplo simple (CAMBIAR EN PRODUCCIÓN):
        return username.equals("admin") && password.equals("1234");
    }
    
    /**
     * Procesa el login exitoso
     * @param username nombre del usuario que inició sesión
     */
    public void onLoginSuccess(String username) {
        System.out.println("Login exitoso para usuario: " + username);
        
        // Cerrar la vista de login
        if (loginView != null) {
            loginView.dispose();
        }
        
        // Crear y mostrar DashboardView
        DashboardView dashboard = new DashboardView();
        dashboard.initializeVentasTable();
        dashboard.initializeLogisticaTable();
        dashboard.initializeInventarioTable();
        dashboard.initializeReportesTable();
        
        // IMPORTANTE: Cargar datos iniciales después de inicializar las tablas
        dashboard.cargarDatosIniciales();
        
        dashboard.setVisible(true);
    }
    
    /**
     * Procesa el login fallido
     */
    public void onLoginFailure() {
        System.out.println("Intento de login fallido");
        
        // Aquí puedes agregar lógica adicional:
        // - Registrar intento fallido
        // - Incrementar contador de intentos
        // - Bloquear cuenta después de X intentos
        // - etc.
    }
}