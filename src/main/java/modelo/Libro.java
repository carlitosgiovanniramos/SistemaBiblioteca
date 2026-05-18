/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Lenovo LOQ
 */
public class Libro {

    private String codigo;
    private String titulo;
    private String cedulaAutor;
    private String nombreAutor;

    public Libro() {
    }

    public Libro(String codigo, String titulo, String cedulaAutor, String nombreAutor) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.cedulaAutor = cedulaAutor;
        this.nombreAutor = nombreAutor;
    }

    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCedulaAutor() {
        return cedulaAutor;
    }

    public void setCedulaAutor(String cedulaAutor) {
        this.cedulaAutor = cedulaAutor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }
    
    

}
