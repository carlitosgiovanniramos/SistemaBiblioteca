/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo LOQ
 */
public class Conexion {

    public static Connection conectar() {
        Connection cn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/biblioteca", "root", "");
            System.out.println((cn == null) ? "no valio" : "valio");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return cn;
    }
}
