package com.example.demo.repository;

import com.example.demo.model.CarritoItem;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    // Todos los items en el carrito de un usuario
    List<CarritoItem> findByUsuario(Usuario usuario);

    // Buscar si ya existe un item (usuario + producto)
    Optional<CarritoItem> findByUsuarioAndProducto(Usuario usuario, Producto producto);

    // Eliminar todos los items del usuario
    void deleteByUsuario(Usuario usuario);
}
