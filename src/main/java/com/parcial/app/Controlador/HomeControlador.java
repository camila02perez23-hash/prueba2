package com.parcial.app.Controlador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControlador {
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/panel";
    }
    
    @GetMapping("/panel")
    public String panelUsuario() {
        return "panelUsuario";
    }
}