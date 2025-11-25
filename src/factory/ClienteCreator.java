/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
    package factory;

    import models.Cliente;
    import factory.Entidad;

    public class ClienteCreator extends EntidadCreator {

        private String dni;
        private String nombre;
        private String telefono;

        public ClienteCreator(String dni, String nombre, String telefono) {
            this.dni = dni;
            this.nombre = nombre;
            this.telefono = telefono;
        }

        @Override
        public Entidad createEntidad() {
            return (Entidad) new Cliente(dni, nombre, telefono);
        }
    }
