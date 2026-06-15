package com.licoreria.view;

import com.licoreria.dao.ClienteDAO;
import com.licoreria.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClienteView extends JFrame {

    ClienteDAO dao = new ClienteDAO();
    DefaultTableModel model = new DefaultTableModel();
    JTable tabla;

    public ClienteView() {
        setTitle("Gestión de Clientes");
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
        model.addColumn("DNI");
        model.addColumn("Dirección");
        
        JScrollPane sp = new JScrollPane(tabla);
        add(sp, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel();
        JLabel lblInfo = new JLabel("Total de clientes: 0");
        panelInferior.add(lblInfo);
        add(panelInferior, BorderLayout.SOUTH);

        // Action Listeners
        btnAgregar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
            if (nombre == null || nombre.trim().isEmpty()) return;
            
            String dni = JOptionPane.showInputDialog(this, "DNI:");
            if (dni == null || dni.trim().isEmpty()) return;
            
            String direccion = JOptionPane.showInputDialog(this, "Dirección:");
            if (direccion == null) direccion = "";
            
            Cliente c = new Cliente();
            c.setNombre(nombre.trim());
            c.setDni(dni.trim());
            c.setDireccion(direccion.trim());
            dao.insertar(c);
            cargar();
            lblInfo.setText("Total de clientes: " + model.getRowCount());
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);
                Cliente c = dao.buscarPorId(id);
                
                if (c != null) {
                    String nombre = JOptionPane.showInputDialog(this, "Nombre:", c.getNombre());
                    if (nombre == null) return;
                    
                    String dni = JOptionPane.showInputDialog(this, "DNI:", c.getDni());
                    if (dni == null) return;
                    
                    String direccion = JOptionPane.showInputDialog(this, "Dirección:", c.getDireccion());
                    if (direccion == null) direccion = "";
                    
                    c.setNombre(nombre.trim());
                    c.setDni(dni.trim());
                    c.setDireccion(direccion.trim());
                    dao.actualizar(c);
                    cargar();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar este cliente?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.eliminar(id);
                    cargar();
                    lblInfo.setText("Total de clientes: " + model.getRowCount());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            }
        });

        btnActualizar.addActionListener(e -> {
            cargar();
            lblInfo.setText("Total de clientes: " + model.getRowCount());
        });

        // Cargar datos
        cargar();
        lblInfo.setText("Total de clientes: " + model.getRowCount());
    }

    void cargar() {
        model.setRowCount(0);
        List<Cliente> lista = dao.listar();
        for (Cliente c : lista) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getDni(),
                c.getDireccion()
            });
        }
    }
}