package models;
import java.io.Serializable;

public class InfoEnvio implements Serializable {
    
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccion;
    private String estado = "PENDIENTE";
    private String transportadora, sucursal, nTracking, cTracking;

    public String getDepartamento(){return departamento;}
    public void setDepartamento(String v){departamento=v;}
    public String getProvincia(){return provincia;}
    public void setProvincia(String v){provincia=v;}
    public String getDistrito(){return distrito;}
    public void setDistrito(String v){distrito=v;}
    public String getDireccion(){return direccion;}
    public void setDireccion(String v){direccion=v;}

    public String getEstado(){return estado;}
    public void setEstado(String e){estado=e;}
    public String getTransportadora(){return transportadora;}
    public void setTransportadora(String t){transportadora=t;}
    public String getSucursal(){return sucursal;}
    public void setSucursal(String s){sucursal=s;}
    public String getnTracking(){return nTracking;}
    public void setnTracking(String n){nTracking=n;}
    public String getcTracking(){return cTracking;}
    public void setcTracking(String c){cTracking=c;}
}