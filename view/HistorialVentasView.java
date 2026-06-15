package com.licoreria.view;

import com.licoreria.dao.VentaDAO;
import com.licoreria.model.Venta;
import com.licoreria.model.DetalleVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistorialVentasView extends JFrame {
    
    private VentaDAO ventaDAO = new VentaDAO();
    private DefaultTableModel modelVentas = new DefaultTableModel();
    private DefaultTableModel modelDetalles = new DefaultTableModel();
    private JTable tablaVentas;
    private JTable tablaDetalles;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public HistorialVentasView() {
        setTitle("Historial de Ventas");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Panel superior: título
        JLabel titulo = new JLabel("HISTORIAL DE VENTAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);
        
        // Panel central dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // Panel superior: Ventas
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("Ventas"));
        
        tablaVentas = new JTable(modelVentas);
        modelVentas.addColumn("ID");
        modelVentas.addColumn("Fecha");
        modelVentas.addColumn("Cliente");
        modelVentas.addColumn("Total");
        
        JScrollPane scrollVentas = new JScrollPane(tablaVentas);
        panelVentas.add(scrollVentas, BorderLayout.CENTER);
        
        JPanel panelBotonesVentas = new JPanel();
        JButton btnActualizar = new JButton("🔄 Actualizar");
        JButton btnVerDetalles = new JButton("🔍 Ver Detalles");
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        
        panelBotonesVentas.add(btnActualizar);
        panelBotonesVentas.add(btnVerDetalles);
        panelBotonesVentas.add(btnImprimir);
        panelVentas.add(panelBotonesVentas, BorderLayout.SOUTH);
        
        // Panel inferior: Detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de Venta"));
        
        tablaDetalles = new JTable(modelDetalles);
        modelDetalles.addColumn("Producto");
        modelDetalles.addColumn("Cantidad");
        modelDetalles.addColumn("Precio Unit.");
        modelDetalles.addColumn("Subtotal");
        
        JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);
        
        splitPane.setTopComponent(panelVentas);
        splitPane.setBottomComponent(panelDetalles);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior: estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total de ventas: 0 | Total recaudado: $0.00");
        panelEstadisticas.add(lblEstadisticas);
        add(panelEstadisticas, BorderLayout.SOUTH);
        
        // Action Listeners
        btnActualizar.addActionListener(e -> cargarVentas(lblEstadisticas));
        
        btnVerDetalles.addActionListener(e -> {
            int fila = tablaVentas.getSelectedRow();
            if (fila >= 0) {
                int ventaId = (int) modelVentas.getValueAt(fila, 0);
                cargarDetallesVenta(ventaId);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una venta");
            }
        });
        
        btnImprimir.addActionListener(e -> {
            int fila = tablaVentas.getSelectedRow();
            if (fila >= 0) {
                int ventaId = (int) modelVentas.getValueAt(fila, 0);
                imprimirTicket(ventaId);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una venta para imprimir");
            }
        });
        
        // Cargar datos iniciales
        cargarVentas(lblEstadisticas);
    }
    
    private void cargarVentas(JLabel lblEstadisticas) {
        modelVentas.setRowCount(0);
        List<Venta> ventas = ventaDAO.listarVentas();
        
        double totalRecaudado = 0;
        for (Venta v : ventas) {
            modelVentas.addRow(new Object[]{
                v.getId(),
                sdf.format(v.getFecha()),
                v.getClienteNombre(),
                String.format("$%.2f", v.getTotal())
            });
            totalRecaudado += v.getTotal();
        }
        
        lblEstadisticas.setText(
            "Total de ventas: " + ventas.size() + 
            " | Total recaudado: $" + String.format("%.2f", totalRecaudado)
        );
    }
    
    private void cargarDetallesVenta(int ventaId) {
        modelDetalles.setRowCount(0);
        List<DetalleVenta> detalles = ventaDAO.obtenerDetallesVenta(ventaId);
        
        for (DetalleVenta dv : detalles) {
            modelDetalles.addRow(new Object[]{
                dv.getProductoNombre(),
                dv.getCantidad(),
                String.format("$%.2f", dv.getSubtotal() / dv.getCantidad()),
                String.format("$%.2f", dv.getSubtotal())
            });
        }
    }
    
    private void imprimirTicket(int ventaId) {
        // Simulación de impresión - muestra en un diálogo
        List<DetalleVenta> detalles = ventaDAO.obtenerDetallesVenta(ventaId);
        List<Venta> ventas = ventaDAO.listarVentas();
        
        Venta ventaSeleccionada = null;
        for (Venta v : ventas) {
            if (v.getId() == ventaId) {
                ventaSeleccionada = v;
                break;
            }
        }
        
        if (ventaSeleccionada == null) return;
        
        StringBuilder ticket = new StringBuilder();
        ticket.append("══════════════════════════════════\n");
        ticket.append("        LICORERÍA EL BUEN SABOR\n");
        ticket.append("══════════════════════════════════\n");
        ticket.append("Ticket #: ").append(ventaId).append("\n");
        ticket.append("Fecha: ").append(sdf.format(ventaSeleccionada.getFecha())).append("\n");
        ticket.append("Cliente: ").append(ventaSeleccionada.getClienteNombre()).append("\n");
        ticket.append("══════════════════════════════════\n");
        
        double total = 0;
        for (DetalleVenta dv : detalles) {
            double precioUnit = dv.getSubtotal() / dv.getCantidad();
            ticket.append(String.format("%-20s %3d x $%6.2f\n", 
                dv.getProductoNombre(), dv.getCantidad(), precioUnit));
            ticket.append(String.format("                     $%8.2f\n", dv.getSubtotal()));
            total += dv.getSubtotal();
        }
        
        ticket.append("══════════════════════════════════\n");
        ticket.append(String.format("TOTAL:             $%8.2f\n", total));
        ticket.append("══════════════════════════════════\n");
        ticket.append("      ¡GRACIAS POR SU COMPRA!\n");
        ticket.append("══════════════════════════════════\n");
        
        JTextArea textArea = new JTextArea(ticket.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Ticket de Venta", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}