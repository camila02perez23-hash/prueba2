// RolNotFoundException.java
package com.parcial.app.Excepcion;

public class RolNotFoundException extends RuntimeException {
    public RolNotFoundException(String message) {
        super(message);
    }
    
    public RolNotFoundException(String id, String mensaje) {
        super("Rol con ID " + id + " no encontrado: " + mensaje);
    }
}