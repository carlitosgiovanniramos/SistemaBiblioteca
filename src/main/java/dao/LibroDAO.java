/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelo.Libro;

/**
 *
 * @author Lenovo LOQ
 */
public class LibroDAO {

    public ArrayList<Libro> listarLibrosConAutor() {

        ArrayList<Libro> lista = new ArrayList<>();

        String sql = "SELECT l.codLibro, l.titLibro, a.autNombre "
                + "FROM libro l "
                + "INNER JOIN autor a ON l.cedAutLibro = a.autCedula";

        try {

            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Libro libro = new Libro();

                libro.setCodigo(rs.getString("codLibro"));
                libro.setTitulo(rs.getString("titLibro"));
                libro.setNombreAutor(rs.getString("autNombre"));

                lista.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al listar libros con autor: " + e.getMessage());
        }

        return lista;
    }

    public boolean guardarLibro(Libro libro) {

        String sql = "INSERT INTO libro (codLibro, titLibro, cedAutLibro) VALUES (?, ?, ?)";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, libro.getCodigo());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getCedulaAutor());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al guardar libro: " + e.getMessage());
            return false;
        }
    }

    public boolean editarLibro(Libro libro) {

        String sql = "UPDATE libro SET titLibro = ?, cedAutLibro = ? WHERE codLibro = ?";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getCedulaAutor());
            ps.setString(3, libro.getCodigo());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al editar libro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLibro(String codigo) {

        String sql = "DELETE FROM libro WHERE codLibro = ?";

        try {

            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, codigo);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Libro> buscarLibrosPorAutor(String cedulaAutor) {

        ArrayList<Libro> lista = new ArrayList<>();

        String sql = "SELECT l.codLibro, l.titLibro, a.autNombre "
                + "FROM libro l "
                + "INNER JOIN autor a ON l.cedAutLibro = a.autCedula "
                + "WHERE l.cedAutLibro = ?";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, cedulaAutor);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();

                libro.setCodigo(rs.getString("codLibro"));
                libro.setTitulo(rs.getString("titLibro"));
                libro.setNombreAutor(rs.getString("autNombre"));

                lista.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor: " + e.getMessage());
        }

        return lista;
    }
}
