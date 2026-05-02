package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetallesServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        
        System.out.println("Buscando usuario: " + correo);
        
        Usuario usuario = usuarioRepositorio.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        
        System.out.println("Usuario encontrado: " + usuario.getCorreo());
        System.out.println("Rol en BD: " + usuario.getRolNombre());
        
        // Determinar el rol (por defecto USER)
        String rol = usuario.getRolNombre();
        if (rol == null || rol.isEmpty()) {
            rol = "USER";
        }
        
        // Crear autoridades
        List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
        autoridades.add(new SimpleGrantedAuthority("ROLE_" + rol));
        
        System.out.println("Autoridad asignada: ROLE_" + rol);
        
        return new User(
            usuario.getCorreo(),
            usuario.getPasswordHash(),
            autoridades
        );
    }
}