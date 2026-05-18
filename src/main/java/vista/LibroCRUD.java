/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import conexion.Conexion;
import dao.AutorDAO;
import dao.LibroDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import modelo.Autor;
import modelo.Libro;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Lenovo LOQ
 */
public class LibroCRUD extends javax.swing.JFrame {

    /**
     * Creates new form libroCRUD
     */
    DefaultTableModel modeloLibro;

    public LibroCRUD() {
        initComponents();

        cargarColumnasTabla();
        listarLibros();
        cargarAutoresCombo();
        SeleccionarFilaLibro();
        cargarAutoresComboBusqueda();
    }

    public void cargarColumnasTabla() {
        String[] columnas = {"Codigo", "Titulo", "Autor"};
        modeloLibro = new DefaultTableModel(null, columnas);
        jtblLibro.setModel(modeloLibro);
    }

    public void listarLibros() {

        modeloLibro.setRowCount(0);

        LibroDAO dao = new LibroDAO();

        ArrayList<Libro> lista = dao.listarLibrosConAutor();

        for (Libro libro : lista) {

            Object[] fila = {
                libro.getCodigo(),
                libro.getTitulo(),
                libro.getNombreAutor()
            };

            modeloLibro.addRow(fila);
        }
    }

    public void buscarLibrosPorAutorCombo() {

        Object seleccionado = jcmbBusquedaCombo.getSelectedItem();

        if (seleccionado == null) {
            listarLibros();
            return;
        }

        modeloLibro.setRowCount(0);

        String datoCombo = seleccionado.toString();

        String cedulaAutor = datoCombo.split(" - ")[0];

        LibroDAO dao = new LibroDAO();

        ArrayList<Libro> lista = dao.buscarLibrosPorAutor(cedulaAutor);

        for (Libro libro : lista) {

            Object[] fila = {
                libro.getCodigo(),
                libro.getTitulo(),
                libro.getNombreAutor()
            };

            modeloLibro.addRow(fila);
        }
    }

    public void guardarLibro() {

        String codigo = jtxtCodigoLibro.getText().trim();
        String titulo = jtxtTituloLibro.getText().trim();

        String datoCombo = jcmbAutor.getSelectedItem().toString();
        String cedulaAutor = datoCombo.split(" - ")[0];

        Libro libro = new Libro(codigo, titulo, cedulaAutor, "");

        LibroDAO dao = new LibroDAO();

        if (dao.guardarLibro(libro)) {
            JOptionPane.showMessageDialog(this, "Libro guardado correctamente");
            listarLibros();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar libro");
        }
    }

    public void editarLibro() {

        String codigo = jtxtCodigoLibro.getText().trim();
        String titulo = jtxtTituloLibro.getText().trim();

        String datoCombo = jcmbAutor.getSelectedItem().toString();
        String cedulaAutor = datoCombo.split(" - ")[0];

        Libro libro = new Libro(codigo, titulo, cedulaAutor, "");

        LibroDAO dao = new LibroDAO();

        if (dao.editarLibro(libro)) {
            JOptionPane.showMessageDialog(this, "Libro editado correctamente");
            listarLibros();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al editar libro");
        }
    }

    public void eliminarLibro() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el libro?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            String codigo = jtxtCodigoLibro.getText().trim();

            LibroDAO dao = new LibroDAO();

            if (dao.eliminarLibro(codigo)) {

                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");

                listarLibros();

                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            }
        }
    }

    public void cargarAutoresCombo() {

        jcmbAutor.removeAllItems();

        AutorDAO dao = new AutorDAO();

        ArrayList<Autor> lista = dao.listarAutores();

        for (Autor autor : lista) {

            jcmbAutor.addItem(
                    autor.getCedula() + " - " + autor.getNombre()
            );
        }
    }

    public void cargarAutoresComboBusqueda() {

        jcmbBusquedaCombo.removeAllItems();

        AutorDAO dao = new AutorDAO();

        ArrayList<Autor> lista = dao.listarAutores();

        for (Autor autor : lista) {

            jcmbBusquedaCombo.addItem(
                    autor.getCedula() + " - " + autor.getNombre()
            );
        }
        jcmbBusquedaCombo.setSelectedIndex(-1);
    }

    public void limpiarCampos() {

        jtxtCodigoLibro.setText("");
        jtxtTituloLibro.setText("");

        if (jcmbAutor.getItemCount() > 0) {
            jcmbAutor.setSelectedIndex(0);
        }

        jtxtCodigoLibro.setEnabled(true);

        jtblLibro.clearSelection();
    }

    public void SeleccionarFilaLibro() {
        jtblLibro.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblLibro.getSelectedRow() != -1) {
                    int fila = jtblLibro.getSelectedRow();
                    jtxtCodigoLibro.setText(jtblLibro.getValueAt(fila, 0).toString());
                    String nombreAutorTabla = jtblLibro.getValueAt(fila, 2).toString();

                    for (int i = 0; i < jcmbAutor.getItemCount(); i++) {

                        String item = jcmbAutor.getItemAt(i);

                        if (item.contains(nombreAutorTabla)) {
                            jcmbAutor.setSelectedIndex(i);
                            break;
                        }
                    }
                    jtxtTituloLibro.setText(jtblLibro.getValueAt(fila, 1).toString());
                    jtxtCodigoLibro.setEnabled(false);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtxtCodigoLibro = new javax.swing.JTextField();
        jtxtTituloLibro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblLibro = new javax.swing.JTable();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jcmbAutor = new javax.swing.JComboBox<>();
        jcmbBusquedaCombo = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setText("Buscar por Cedula Autor");

        jLabel1.setText("Codigo Libro");

        jtxtCodigoLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCodigoLibroActionPerformed(evt);
            }
        });

        jtxtTituloLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTituloLibroActionPerformed(evt);
            }
        });

        jLabel3.setText("Autor Libro");

        jLabel2.setText("Titulo del Libro");

        jtblLibro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblLibro);

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jcmbAutor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jcmbBusquedaCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcmbBusquedaCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jcmbBusquedaComboMouseClicked(evt);
            }
        });
        jcmbBusquedaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmbBusquedaComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtxtTituloLibro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtxtCodigoLibro, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jcmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbBusquedaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnNuevo)
                    .addComponent(jbtnGuardar)
                    .addComponent(jbtnEditar)
                    .addComponent(jbtnEliminar)
                    .addComponent(jbtnCancelar))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcmbBusquedaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtxtCodigoLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtTituloLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbtnNuevo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbtnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbtnEditar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbtnEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnCancelar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtCodigoLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCodigoLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodigoLibroActionPerformed

    private void jtxtTituloLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTituloLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTituloLibroActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        // TODO add your handling code here:
        guardarLibro();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        // TODO add your handling code here:
        editarLibro();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
        listarLibros();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jcmbBusquedaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmbBusquedaComboActionPerformed
        // TODO add your handling code here:\
        buscarLibrosPorAutorCombo();
    }//GEN-LAST:event_jcmbBusquedaComboActionPerformed

    private void jcmbBusquedaComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcmbBusquedaComboMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jcmbBusquedaComboMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LibroCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LibroCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LibroCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibroCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LibroCRUD().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JComboBox<String> jcmbAutor;
    private javax.swing.JComboBox<String> jcmbBusquedaCombo;
    private javax.swing.JTable jtblLibro;
    private javax.swing.JTextField jtxtCodigoLibro;
    private javax.swing.JTextField jtxtTituloLibro;
    // End of variables declaration//GEN-END:variables
}
