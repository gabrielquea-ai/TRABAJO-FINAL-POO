package com.licoreria.view;

import com.licoreria.dao.UsuarioDAO;
import javax.swing.*;

public class LoginView extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginView() {
        setTitle("Login - Sistema Licorería");
        setSize(350, 220);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel titulo = new JLabel("SISTEMA DE LICORERÍA");
        titulo.setBounds(80, 10, 200, 25);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        add(titulo);

        JLabel l1 = new JLabel("Usuario:");
        l1.setBounds(30, 50, 80, 25);
        add(l1);

        txtUser = new JTextField();
        txtUser.setBounds(120, 50, 180, 25);
        add(txtUser);

        JLabel l2 = new JLabel("Clave:");
        l2.setBounds(30, 85, 80, 25);
        add(l2);

        txtPass = new JPasswordField();
        txtPass.setBounds(120, 85, 180, 25);
        add(txtPass);

        JButton btn = new JButton("Ingresar");
        btn.setBounds(120, 130, 100, 30);
        add(btn);

        // Enter key listener
        txtPass.addActionListener(e -> btn.doClick());

        btn.addActionListener(e -> {
            UsuarioDAO dao = new UsuarioDAO();
            String u = txtUser.getText();
            String p = new String(txtPass.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            if (dao.login(u, p)) {
                new MenuView().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o clave incorrectos");
            }
        });
    }
}