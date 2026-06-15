package com.licoreria.view;

import com.licoreria.dao.ProductoDAO;
import com.licoreria.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoView extends JFrame {

    ProductoDAO dao = new ProductoDAO();
    DefaultTableModel model = new DefaultTableModel();
    JTable tabla;

    public ProductoView() {
        setTitle("Gestión de Productos");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnActualizar = new JButton("🔄 Actualizar");

        panelSuperior.add(btnAgregar);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnActualizar);
        
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        tabla = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Precio");
        model.addColumn("Stock");
        
        JScrollPane sp = new JScrollPane(tabla);
        add(sp, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel();
        JLabel lblInfo = new JLabel("Total de productos: 0");
        panelInferior.add(lblInfo);
        add(panelInferior, BorderLayout.SOUTH);

        // Action Listeners
        btnAgregar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) return;
            
            String precioStr = JOptionPane.showInputDialog(this, "Precio:");
            if (precioStr == null) return;
            
            String stockStr = JOptionPane.showInputDialog(this, "Stock inicial:");
            if (stockStr == null) return;
            
            try {
                Producto p = new Producto();
                p.setNombre(nombre.trim());
                p.setPrecio(Double.parseDouble(precioStr));
                p.setStock(Integer.parseInt(stockStr));
                dao.insertar(p);
                cargar();
                lblInfo.setText("Total de productos: " + model.getRowCount());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos");
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);
                Producto p = dao.buscarPorId(id);
                
                if (p != null) {
                    String nombre = JOptionPane.showInputDialog(this, "Nombre:", p.getNombre());
                    if (nombre == null) return;
                    
                    String precioStr = JOptionPane.showInputDialog(this, "Precio:", p.getPrecio());
                    if (precioStr == null) return;
                    
                    String stockStr = JOptionPane.showInputDialog(this, "Stock:", p.getStock());
                    if (stockStr == null) return;
                    
                    try {
                        p.setNombre(nombre.trim());
                        p.setPrecio(Double.parseDouble(precioStr));
                        p.setStock(Integer.parseInt(stockStr));
                        dao.actualizar(p);
                        cargar();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Datos inválidos");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar este producto?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.eliminar(id);
                    cargar();
                    lblInfo.setText("Total de productos: " + model.getRowCount());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto");
            }
        });

        btnActualizar.addActionListener(e -> {
            cargar();
            lblInfo.setText("Total de productos: " + model.getRowCount());
        });

        // Cargar datos
        cargar();
        lblInfo.setText("Total de productos: " + model.getRowCount());
    }

    void cargar() {
        model.setRowCount(0);
        List<Producto> lista = dao.listar();
        for (Producto p : lista) {
            model.addRow(new Object[]{
                p.getId(), 
                p.getNombre(), 
                String.format("$%.2f", p.getPrecio()), 
                p.getStock()
            });
        }
    }
}