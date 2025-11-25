package models;

import java.io.Serializable;

public class InfoEnvio implements Serializable {
    
    // Attributes
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccion;
    private String estadoEnvio = "SIN REGISTRO"; // ✅ Cambiado de "PENDIENTE" a "SIN REGISTRO"
    private String transportadora, sucursal, nTracking, cTracking;

    // SETTER AND GETTER
    public String getDepartamento(){return departamento;}
    public void setDepartamento(String v){departamento = v;}
    public String getProvincia(){return provincia;}
    public void setProvincia(String v){provincia = v;}
    public String getDistrito(){return distrito;}
    public void setDistrito(String v){distrito = v;}
    public String getDireccion(){return direccion;}
    public void setDireccion(String v){direccion = v;}

    // ✅ Renombrar métodos para claridad
    public String getEstadoEnvio(){return estadoEnvio;}
    public void setEstadoEnvio(String e){estadoEnvio = e;}
    
    public String getTransportadora(){return transportadora;}
    public void setTransportadora(String t){transportadora = t;}
    public String getSucursal(){return sucursal;}
    public void setSucursal(String s){sucursal = s;}
    public String getnTracking(){return nTracking;}
    public void setnTracking(String n){nTracking = n;}
    public String getcTracking(){return cTracking;}
    public void setcTracking(String c){cTracking = c;}
    
    // ✅ MANTENER getEstado() para compatibilidad (delegando a getEstadoEnvio)
    public String getEstado(){return estadoEnvio;}
    public void setEstado(String e){estadoEnvio = e;}
}