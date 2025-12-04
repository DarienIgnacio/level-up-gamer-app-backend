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
    private final FileStorageService fileStorage;

    public ProductoService(ProductoRepository repo, FileStorageService fileStorage) {
        this.repo = repo;
        this.fileStorage = fileStorage;
    }

    public Producto crearProducto(
            String nombre,
            String descripcion,
            int precio,
            String categoria,
            int stock,
            String imagen
    ) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setStock(stock);
        p.setImagen(imagen);

        return repo.save(p);
    }

    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    public Producto actualizarProducto(
            Long id,
            String nombre,
            String descripcion,
            int precio,
            String categoria,
            int stock,
            MultipartFile imagen
    ) {
        Producto existing = obtenerPorId(id);
        existing.setNombre(nombre);
        existing.setDescripcion(descripcion);
        existing.setPrecio(precio);
        existing.setCategoria(categoria);
        existing.setStock(stock);

        if (imagen != null && !imagen.isEmpty()) {
            if (existing.getImagen() != null) {
                fileStorage.deleteFile(existing.getImagen());
            }
            String fileName = fileStorage.storeFile(imagen);
            existing.setImagen(fileName);
        }

        return repo.save(existing);
    }

    public void eliminarProducto(Long id) {
        Producto existing = obtenerPorId(id);
        if (existing.getImagen() != null) {
            fileStorage.deleteFile(existing.getImagen());
        }
        repo.delete(existing);
    }
}
