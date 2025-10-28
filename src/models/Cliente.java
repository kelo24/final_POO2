package models;

import java.io.Serializable;

public class Cliente implements Serializable {
    
    // Attributes
    private String dni;
    private String nombre;
    private String telefono;
    
    // Constructor
    public Cliente() {}
    public Cliente(String dni, String nombre, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
    // SETTER AND GETTER
    public String getDni() {return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    // Override para pruebas
    @Override public String toString() { return dni + " - " + nombre; }
}