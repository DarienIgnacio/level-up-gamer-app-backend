package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.ProductoService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // para permitir llamadas desde Android/Emulador
public class ProductoController {

    private final ProductoService productoService;
    private final FileStorageService fileStorage;

    public ProductoController(ProductoService productoService, FileStorageService fileStorage) {
        this.productoService = productoService;
        this.fileStorage = fileStorage;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerUno(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") int precio,
            @RequestParam("categoria") String categoria,
            @RequestParam("stock") int stock,
            @RequestPart(value = "imagen", required = false) String imagen
    ) {
        Producto creado = productoService.crearProducto(
                nombre, descripcion, precio, categoria, stock, imagen
        );
        return ResponseEntity
                .created(URI.create("/api/productos/" + creado.getId()))
                .body(creado);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") int precio,
            @RequestParam("categoria") String categoria,
            @RequestParam("stock") int stock,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        Producto actualizado = productoService.actualizarProducto(
                id, nombre, descripcion, precio, categoria, stock, imagen
        );
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/imagenes/{filename:.+}")
    public ResponseEntity<Resource> servirImagen(@PathVariable String filename) {
        Resource resource = fileStorage.loadFileAsResource(filename);
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (Exception ignored) {}
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
