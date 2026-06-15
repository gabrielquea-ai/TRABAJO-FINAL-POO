package com.licoreria.model;

import java.util.Date;

public class Venta {
    private int id;
    private int clienteId;
    private String clienteNombre;
    private Date fecha;
    private double total;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}