package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProductoController(
            ProductoService productoService,
            ProductoRepository productoRepository
    ) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    // =========================
    // CRUD ADMIN
    // =========================

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto datosActualizados
    ) {
        try {
            Producto actualizado =
                    productoService.actualizarProducto(id, datosActualizados);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        if (productoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
        productoService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    // =========================
    // COMPRA (USUARIO)
    // =========================

    @PutMapping("/{id}/disminuir-stock")
    public ResponseEntity<?> disminuirStock(
            @PathVariable Long id,
            @RequestParam int cantidad
    ) {
        return productoRepository.findById(id)
                .map(producto -> {
                    if (producto.getStock() < cantidad) {
                        return ResponseEntity
                                .badRequest()
                                .body("Stock insuficiente");
                    }

                    producto.setStock(producto.getStock() - cantidad);
                    productoRepository.save(producto);

                    return ResponseEntity.ok(producto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
