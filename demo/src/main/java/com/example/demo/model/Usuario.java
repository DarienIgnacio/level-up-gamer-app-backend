package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "usuario",
    uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rut;

    @Column(nullable = false)
    private boolean esAdmin;
}
