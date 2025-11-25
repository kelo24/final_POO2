/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio {
    
    private List<Usuario> usuarios;
    
    public UsuarioRepositorio() {
        inicializarUsuarios();
    }
    
   
    private void inicializarUsuarios() {
        usuarios = new ArrayList<>();
        
       
        usuarios.add(new Usuario("admin", "admin123", "Administrador", "ADMIN"));
        usuarios.add(new Usuario("vendedor", "venta123", "Usuario Vendedor", "VENDEDOR"));
        usuarios.add(new Usuario("logistica", "log123", "Usuario Logística", "LOGISTICA"));
        usuarios.add(new Usuario("inventario", "inv123", "Usuario Inventario", "INVENTARIO"));
    }
    
    
    public Usuario findByUsername(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }
    
   
    public boolean validarCredenciales(String username, String password) {
        Usuario usuario = findByUsername(username);
        if (usuario != null) {
            return usuario.getPassword().equals(password);
        }
        return false;
    }
    
 
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }
    
   
    public boolean agregarUsuario(Usuario usuario) {
       
        if (findByUsername(usuario.getUsername()) != null) {
            return false;
        }
        return usuarios.add(usuario);
    }
}