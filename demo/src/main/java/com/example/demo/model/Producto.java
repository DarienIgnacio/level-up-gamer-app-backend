package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private int precio;
    private String imagen;
    private String categoria;
    private int stock;

    public Producto() {
    }

    public Producto(Long id, String nombre, String descripcion, int precio, String imagen, String categoria, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.categoria = categoria;
        this.stock = stock;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getPrecio() {
        return precio;
    }
    public String getImagen() {
        return imagen;
    }
    public String getCategoria() {
        return categoria;
    }
    public int getStock() {
        return stock;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
