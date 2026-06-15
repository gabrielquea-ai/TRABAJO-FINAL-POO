package com.licoreria.view;

import javax.swing.*;

public class MenuView extends JFrame {

    public MenuView() {
        setTitle("Menú Principal - Sistema Licorería");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel titulo = new JLabel("MENÚ PRINCIPAL");
        titulo.setBounds(140, 20, 150, 30);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        add(titulo);

        JButton b1 = new JButton("📦 Productos");
        JButton b2 = new JButton("👥 Clientes");
        JButton b3 = new JButton("💰 Nueva Venta");
        JButton b4 = new JButton("📋 Ver Ventas");
        JButton b5 = new JButton("🚪 Salir");

        b1.setBounds(120, 60, 150, 35);
        b2.setBounds(120, 100, 150, 35);
        b3.setBounds(120, 140, 150, 35);
        b4.setBounds(120, 180, 150, 35);
        b5.setBounds(120, 220, 150, 35);

        add(b1); add(b2); add(b3); add(b4); add(b5);

        b1.addActionListener(e -> new ProductoView().setVisible(true));
        b2.addActionListener(e -> new ClienteView().setVisible(true));
        b3.addActionListener(e -> new VentaView().setVisible(true));
        b4.addActionListener(e -> new HistorialVentasView().setVisible(true));
        b5.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea salir?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }
}