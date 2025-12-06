package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {

        Optional<Producto> resultado = productoService.obtenerPorId(id);

        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto datosActualizados) {

        try {
            Producto actualizado = productoService.actualizarProducto(id, datosActualizados);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(
            @PathVariable Long id,
            @RequestParam int stock) {

        Optional<Producto> resultado = productoService.obtenerPorId(id);

        if (resultado.isPresent()) {
            Producto p = resultado.get();
            p.setStock(stock);
            productoService.guardarProducto(p);
            return ResponseEntity.ok(p);
        } else {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {

        Optional<Producto> resultado = productoService.obtenerPorId(id);

        if (resultado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        productoService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado");
    }
}
