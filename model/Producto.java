package com.licoreria.model;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre=nombre;}

    public double getPrecio(){return precio;}
    public void setPrecio(double precio){this.precio=precio;}

    public int getStock(){return stock;}
    public void setStock(int stock){this.stock=stock;}
    
    @Override
    public String toString() {
        return nombre + " - $" + precio + " (Stock: " + stock + ")";
    }
}