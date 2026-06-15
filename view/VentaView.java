package com.licoreria.view;

import com.licoreria.dao.*;
import com.licoreria.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentaView extends JFrame {
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();
    
    private DefaultTableModel modelDetalle = new DefaultTableModel();
    private JTable tablaDetalle;
    private JComboBox<Cliente> comboClientes;
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;
    private JLabel lblTotal;
    private double total = 0.0;
    
    public VentaView() {
        setTitle("Nueva Venta");
        setSize(700, 550);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Panel superior: Cliente
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelSuperior.setPreferredSize(new Dimension(0, 100));
        
        panelSuperior.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        cargarClientes();
        panelSuperior.add(comboClientes);
        
        JButton btnNuevoCliente = new JButton("Nuevo Cliente");
        btnNuevoCliente.addActionListener(e -> {
            new ClienteView().setVisible(true);
        });
        panelSuperior.add(btnNuevoCliente);
        
        JButton btnRefrescarClientes = new JButton("🔄");
        btnRefrescarClientes.addActionListener(e -> cargarClientes());
        panelSuperior.add(btnRefrescarClientes);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central: Agregar productos
        JPanel panelCentro = new JPanel(new BorderLayout());
        
        // Panel para agregar productos
        JPanel panelAgregar = new JPanel(new FlowLayout());
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));
        
        comboProductos = new JComboBox<>();
        cargarProductos();
        panelAgregar.add(new JLabel("Producto:"));
        panelAgregar.add(comboProductos);
        
        panelAgregar.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField("1", 5);
        panelAgregar.add(txtCantidad);
        
        JButton btnAgregar = new JButton("➕ Agregar");
        btnAgregar.addActionListener(e -> agregarProducto());
        panelAgregar.add(btnAgregar);
        
        JButton btnRefrescarProductos = new JButton("🔄");
        btnRefrescarProductos.addActionListener(e -> cargarProductos());
        panelAgregar.add(btnRefrescarProductos);
        
        panelCentro.add(panelAgregar, BorderLayout.NORTH);
        
        // Tabla de detalles
        tablaDetalle = new JTable(modelDetalle);
        modelDetalle.addColumn("Producto");
        modelDetalle.addColumn("Cantidad");
        modelDetalle.addColumn("Precio Unit.");
        modelDetalle.addColumn("Subtotal");
        
        JScrollPane scroll = new JScrollPane(tablaDetalle);
        panelCentro.add(scroll, BorderLayout.CENTER);
        
        add(panelCentro, BorderLayout.CENTER);
        
        // Panel inferior: Total y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel lblTotalTexto = new JLabel("TOTAL: $");
        lblTotalTexto.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotalTexto);
        
        lblTotal = new JLabel("0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.BLUE);
        panelTotal.add(lblTotal);
        
        panelInferior.add(panelTotal, BorderLayout.NORTH);
        
        JPanel panelBotones = new JPanel();
        JButton btnEliminarProducto = new JButton("❌ Quitar Producto");
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        panelBotones.add(btnEliminarProducto);
        
        JButton btnFinalizar = new JButton("✅ Finalizar Venta");
        btnFinalizar.addActionListener(e -> finalizarVenta());
        panelBotones.add(btnFinalizar);
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void cargarClientes() {
        comboClientes.removeAllItems();
        List<Cliente> clientes = clienteDAO.listar();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }
    
    private void cargarProductos() {
        comboProductos.removeAllItems();
        List<Producto> productos = productoDAO.listar();
        for (Producto p : productos) {
            if (p.getStock() > 0) { // Solo mostrar productos con stock
                comboProductos.addItem(p);
            }
        }
    }
    
    private void agregarProducto() {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles");
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                return;
            }
            
            // Verificar stock disponible
            if (cantidad > producto.getStock()) {
                JOptionPane.showMessageDialog(this, 
                    "Stock insuficiente\n" +
                    "Disponible: " + producto.getStock() + " unidades\n" +
                    "Producto: " + producto.getNombre());
                return;
            }
            
            double subtotal = cantidad * producto.getPrecio();
            
            // Actualizar stock temporalmente en el objeto (no en BD todavía)
            producto.setStock(producto.getStock() - cantidad);
            
            // Actualizar comboBox
            cargarProductos();
            
            // Agregar a la tabla
            modelDetalle.addRow(new Object[]{
                producto.getNombre(),
                cantidad,
                String.format("$%.2f", producto.getPrecio()),
                String.format("$%.2f", subtotal)
            });
            
            // Actualizar total
            total += subtotal;
            lblTotal.setText(String.format("%.2f", total));
            
            txtCantidad.setText("1");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido");
        }
    }
    
    private void eliminarProducto() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila >= 0) {
            // Obtener datos del producto eliminado
            String nombreProducto = (String) modelDetalle.getValueAt(fila, 0);
            int cantidad = Integer.parseInt(modelDetalle.getValueAt(fila, 1).toString()
                .replace("$", "").replaceAll("[^\\d]", ""));
            double subtotal = Double.parseDouble(modelDetalle.getValueAt(fila, 3).toString()
                .replace("$", ""));
            
            // Buscar producto para restaurar stock
            Producto producto = productoDAO.buscarPorNombre(nombreProducto);
            if (producto != null) {
                producto.setStock(producto.getStock() + cantidad);
                cargarProductos();
            }
            
            // Eliminar de la tabla
            modelDetalle.removeRow(fila);
            
            // Actualizar total
            total -= subtotal;
            lblTotal.setText(String.format("%.2f", total));
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
        }
    }
    
    private void finalizarVenta() {
        if (modelDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue productos a la venta");
            return;
        }
        
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }
        
        // Confirmar venta
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Confirmar venta por $" + String.format("%.2f", total) + "?",
            "Confirmar Venta",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Crear venta
        Venta venta = new Venta();
        venta.setClienteId(cliente.getId());
        venta.setTotal(total);
        
        int ventaId = ventaDAO.insertar(venta);
        if (ventaId > 0) {
            // Guardar detalles
            for (int i = 0; i < modelDetalle.getRowCount(); i++) {
                String nombreProducto = (String) modelDetalle.getValueAt(i, 0);
                int cantidad = Integer.parseInt(modelDetalle.getValueAt(i, 1).toString());
                double subtotal = Double.parseDouble(modelDetalle.getValueAt(i, 3).toString()
                    .replace("$", ""));
                
                // Buscar producto por nombre
                Producto p = productoDAO.buscarPorNombre(nombreProducto);
                if (p != null) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVentaId(ventaId);
                    detalle.setProductoId(p.getId());
                    detalle.setCantidad(cantidad);
                    detalle.setSubtotal(subtotal);
                    
                    ventaDAO.insertarDetalle(detalle);
                    
                    // Actualizar stock definitivo en BD
                    productoDAO.actualizarStock(p.getId(), p.getStock());
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "✅ Venta registrada exitosamente\n" +
                "📋 Número de venta: " + ventaId + "\n" +
                "💰 Total: $" + String.format("%.2f", total) + "\n" +
                "👤 Cliente: " + cliente.getNombre());
            
            // Resetear venta
            modelDetalle.setRowCount(0);
            total = 0.0;
            lblTotal.setText("0.00");
            cargarProductos();
            
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta");
        }
    }
}