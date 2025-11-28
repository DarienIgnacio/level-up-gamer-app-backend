package com.example.demo.service;

import com.example.demo.dto.CarritoAgregarRequest;
import com.example.demo.dto.CarritoActualizarCantidadRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CarritoItem;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CarritoItemRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    private final CarritoItemRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    public CarritoService(CarritoItemRepository carritoRepo,
                          UsuarioRepository usuarioRepo,
                          ProductoRepository productoRepo) {

        this.carritoRepo = carritoRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    // =========================================================
    // OBTENER CARRITO DE UN USUARIO
    // =========================================================
    public List<CarritoItem> obtenerCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return carritoRepo.findByUsuario(usuario);
    }

    // =========================================================
    // AGREGAR PRODUCTO AL CARRITO
    // =========================================================
    public CarritoItem agregarProducto(CarritoAgregarRequest req) {

        Usuario usuario = usuarioRepo.findById(req.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepo.findById(req.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // ¿Ya existe este producto en el carrito?
        return carritoRepo.findByUsuarioAndProducto(usuario, producto)
                .map(item -> {
                    // Si existe → aumentar cantidad
                    item.setCantidad(item.getCantidad() + req.getCantidad());
                    return carritoRepo.save(item);
                })
                .orElseGet(() -> {
                    // Si no existe → crear nuevo item
                    CarritoItem nuevo = CarritoItem.builder()
                            .usuario(usuario)
                            .producto(producto)
                            .cantidad(req.getCantidad())
                            .build();

                    return carritoRepo.save(nuevo);
                });
    }

    // =========================================================
    // ACTUALIZAR CANTIDAD
    // =========================================================
    public CarritoItem actualizarCantidad(Long itemId, CarritoActualizarCantidadRequest req) {
        CarritoItem item = carritoRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

        item.setCantidad(req.getCantidad());

        return carritoRepo.save(item);
    }

    // =========================================================
    // ELIMINAR ITEM DEL CARRITO
    // =========================================================
    public void eliminarItem(Long itemId) {
        if (!carritoRepo.existsById(itemId)) {
            throw new ResourceNotFoundException("Item no encontrado");
        }
        carritoRepo.deleteById(itemId);
    }

    // =========================================================
    // VACIAR CARRITO
    // =========================================================
    public void vaciarCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        carritoRepo.deleteByUsuario(usuario);
    }
}
