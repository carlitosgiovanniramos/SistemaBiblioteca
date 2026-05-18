/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mysql.cj.protocol.Resultset;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Autor;

/**
 *
 * @author Lenovo LOQ
 */
public class AutorDAO {

    public ArrayList<Autor> listarAutores() {
        ArrayList<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autor";

        try {
            Connection cc = Conexion.conectar();
            PreparedStatement psd = cc.prepareStatement(sql);
            ResultSet rs = psd.executeQuery();

            while (rs.next()) {
                Autor autor = new Autor();

                autor.setCedula(rs.getString("autCedula"));
                autor.setNombre(rs.getString("autNombre"));
                autor.setApellido(rs.getString("autApellido"));
                autor.setNacionalidad(rs.getString("autNacionalidad"));
                autor.setTelefono(rs.getString("autTelefono"));

                lista.add(autor);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar autores: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Autor> buscarAutorPorCedula(String cedula) {

        ArrayList<Autor> lista = new ArrayList<>();

        String sql = "SELECT * FROM autor WHERE autCedula = ?";

        try {

            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Autor autor = new Autor();

                autor.setCedula(rs.getString("autCedula"));
                autor.setNombre(rs.getString("autNombre"));
                autor.setApellido(rs.getString("autApellido"));
                autor.setNacionalidad(rs.getString("autNacionalidad"));
                autor.setTelefono(rs.getString("autTelefono"));

                lista.add(autor);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar autor: " + e.getMessage());
        }

        return lista;
    }

    public boolean guardarAutor(Autor autor) {
        String sql = "INSERT INTO autor (autCedula, autNombre, autApellido, autNacionalidad, autTelefono) VALUES(?,?,?,?,?)";
        try {
            Connection cc = Conexion.conectar();
            PreparedStatement psd = cc.prepareStatement(sql);

            psd.setString(1, autor.getCedula());
            psd.setString(2, autor.getNombre());
            psd.setString(3, autor.getApellido());
            psd.setString(4, autor.getNacionalidad());
            psd.setString(5, autor.getTelefono());

            psd.executeUpdate();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar autor: " + e.getMessage());
            return false;
        }
    }

    public boolean editarAutor(Autor autor) {
        String sql = "UPDATE autor SET autNombre=?, autApellido=?, autNacionalidad=?, autTelefono=? WHERE autCedula=?";

        try {
            Connection cc = Conexion.conectar();
            PreparedStatement psd = cc.prepareStatement(sql);

            psd.setString(1, autor.getNombre());
            psd.setString(2, autor.getApellido());
            psd.setString(3, autor.getNacionalidad());
            psd.setString(4, autor.getTelefono());
            psd.setString(5, autor.getCedula());

            psd.executeUpdate();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    public boolean eliminarAutor(Autor autor) {
        String sql = "DELETE FROM autor WHERE autCedula=?";

        try {
            Connection cc = Conexion.conectar();
            PreparedStatement psd = cc.prepareStatement(sql);

            psd.setString(1, autor.getCedula());

            psd.executeUpdate();
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }
}
