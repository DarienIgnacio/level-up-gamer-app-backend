package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")   // Permitir Android acceder a la API
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ============================
    // REGISTRO
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        Usuario nuevo = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(request.getPassword())
                .rut(request.getRut())
                .esAdmin(false)  // Puedes cambiarlo si quieres roles
                .build();

        Usuario registrado = usuarioService.registrar(nuevo);

        return ResponseEntity.ok(registrado);
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(usuario);
    }


    // ============================
    // CLASES DTO Internas
    // ============================

    @Data
    public static class RegisterRequest {
        private String nombre;
        private String email;
        private String password;
        private String rut;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
