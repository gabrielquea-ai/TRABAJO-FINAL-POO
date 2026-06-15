package com.licoreria.dao;

import com.licoreria.model.Venta;
import com.licoreria.model.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    
    public int insertar(Venta venta) {
        String sql = "INSERT INTO venta(cliente_id, total) VALUES(?, ?)";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, venta.getClienteId());
            ps.setDouble(2, venta.getTotal());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public void insertarDetalle(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_venta(venta_id, producto_id, cantidad, subtotal) VALUES(?,?,?,?)";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, detalle.getVentaId());
            ps.setInt(2, detalle.getProductoId());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getSubtotal());
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nombre as cliente_nombre FROM venta v " +
                    "LEFT JOIN cliente c ON v.cliente_id = c.id ORDER BY v.fecha DESC";
        
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setClienteNombre(rs.getString("cliente_nombre"));
                v.setFecha(rs.getTimestamp("fecha"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<DetalleVenta> obtenerDetallesVenta(int ventaId) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.*, p.nombre as producto_nombre FROM detalle_venta dv " +
                    "JOIN producto p ON dv.producto_id = p.id " +
                    "WHERE dv.venta_id = ?";
        
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, ventaId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DetalleVenta dv = new DetalleVenta();
                dv.setId(rs.getInt("id"));
                dv.setVentaId(rs.getInt("venta_id"));
                dv.setProductoId(rs.getInt("producto_id"));
                dv.setProductoNombre(rs.getString("producto_nombre"));
                dv.setCantidad(rs.getInt("cantidad"));
                dv.setSubtotal(rs.getDouble("subtotal"));
                lista.add(dv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}