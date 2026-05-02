package com.parcial.app.Excepcion;

public class PermisoNotFoundException extends RuntimeException {
    
    public PermisoNotFoundException(String id, String mensaje) {
        super("Permiso con ID " + id + " no encontrado: " + mensaje);
    }
    
    public PermisoNotFoundException(String id) {
        super("Permiso con ID " + id + " no encontrado");
    }
}