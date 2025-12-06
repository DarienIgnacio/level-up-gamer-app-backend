package com.example.demo.service;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Obtener uno
    public Optional<Producto> obtenerPorId(Long id) {
    return productoRepository.findById(id);
    }

    // Crear o guardar
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // =============================
    // 🔥 Actualizar producto
    // =============================
    public Producto actualizarProducto(Long id, Producto datosActualizados) {
        return productoRepository.findById(id)
                .map(producto -> {

                    producto.setNombre(datosActualizados.getNombre());
                    producto.setDescripcion(datosActualizados.getDescripcion());
                    producto.setPrecio(datosActualizados.getPrecio());
                    producto.setImagen(datosActualizados.getImagen());
                    producto.setCategoria(datosActualizados.getCategoria());

                    // 🔥 AQUI FALLABA: método NO EXISTÍA
                    producto.setStock(datosActualizados.getStock());

                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID " + id));
    }

    // Eliminar
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
