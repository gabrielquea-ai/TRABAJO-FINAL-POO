package com.licoreria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/licoreria_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASS = "9606"; // Cambia esta contraseña

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: No se encontró el Driver MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ ERROR de conexión a MySQL.");
            e.printStackTrace();
        }
        return null;
    }
}