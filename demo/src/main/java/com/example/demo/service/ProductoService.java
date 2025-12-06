package com.example.demo.service;

import com.example.demo.dto.ProductoDto;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Iterable<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto actualizarProducto(Long id, ProductoDto dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));

        producto.setNombre(dto.nombre);
        producto.setDescripcion(dto.descripcion);
        producto.setPrecio(dto.precio);
        producto.setImagen(dto.imagen);
        producto.setCategoria(dto.categoria);
        producto.setstock(dto.stock);

        return productoRepository.save(producto);
    }
}
