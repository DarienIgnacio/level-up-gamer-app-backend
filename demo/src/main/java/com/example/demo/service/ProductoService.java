package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public Producto crearProducto(Producto p) {
        // Aquí la imagen ya viene como String URL
        return repo.save(p);
    }

    public Producto actualizarProducto(Long id, Producto datos) {
        Producto existing = obtenerPorId(id);

        existing.setNombre(datos.getNombre());
        existing.setDescripcion(datos.getDescripcion());
        existing.setPrecio(datos.getPrecio());
        existing.setCategoria(datos.getCategoria());
        existing.setStock(datos.getStock());
        existing.setImagen(datos.getImagen()); // URL directa

        return repo.save(existing);
    }

    public void eliminarProducto(Long id) {
        Producto existing = obtenerPorId(id);
        repo.delete(existing);
    }
}
