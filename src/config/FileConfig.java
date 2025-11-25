package config;

import java.io.File;

/**
 * FileConfig - Configuración de rutas de archivos de datos
 */
public class FileConfig {
    
    // Carpeta principal de datos
    private static final String DATA_FOLDER = "data";
    
    // Archivos de datos
    public static final String PEDIDOS_FILE = DATA_FOLDER + File.separator + "pedidos.dat";
    public static final String CLIENTES_FILE = DATA_FOLDER + File.separator + "clientes.dat";
    public static final String PRODUCTOS_FILE = DATA_FOLDER + File.separator + "productos.dat";
    public static final String INVENTARIO_FILE = DATA_FOLDER + File.separator + "inventario.dat";
    
    /**
     * Inicializa la estructura de carpetas y archivos
     */
    public static void inicializar() {
        File dataDir = new File(DATA_FOLDER);
        
        // Crear carpeta data si no existe
        if (!dataDir.exists()) {
            if (dataDir.mkdir()) {
                System.out.println("Carpeta 'data' creada exitosamente");
            }
        }
        
        // Crear archivos si no existen
        crearArchivoSiNoExiste(PEDIDOS_FILE);
        crearArchivoSiNoExiste(CLIENTES_FILE);
        crearArchivoSiNoExiste(PRODUCTOS_FILE);
        crearArchivoSiNoExiste(INVENTARIO_FILE);
    }
    
    /**
     * Crea un archivo vacío si no existe
     */
    private static void crearArchivoSiNoExiste(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("Archivo creado: " + filePath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al crear archivo " + filePath + ": " + e.getMessage());
        }
    }
}