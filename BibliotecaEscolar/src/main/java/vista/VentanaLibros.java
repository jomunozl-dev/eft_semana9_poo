
package vista;

import controlador.ControladorLibro;
import modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VentanaLibros extends JFrame {

    private JTextField txtId, txtTitulo, txtAutor;
    private JTextField txtEditorial, txtIsbn, txtCategoria, txtStock;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ControladorLibro controlador;

    public VentanaLibros(boolean admin) {

        controlador = new ControladorLibro();

        setTitle("Gestión de Libros");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(7, 2));

        txtId = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtEditorial = new JTextField();
        txtIsbn = new JTextField();
        txtCategoria = new JTextField();
        txtStock = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);
        formulario.add(new JLabel("Autor:"));
        formulario.add(txtAutor);
        formulario.add(new JLabel("Editorial:"));
        formulario.add(txtEditorial);
        formulario.add(new JLabel("ISBN:"));
        formulario.add(txtIsbn);
        formulario.add(new JLabel("ID Categoría:"));
        formulario.add(txtCategoria);
        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        modeloTabla = new DefaultTableModel(
            new String[]{
                "ID", "Título", "Autor", "Editorial",
                "ISBN", "Categoría", "Stock"
            }, 0
        );

        tabla = new JTable(modeloTabla);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        JPanel botones = new JPanel();

        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnListar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnGuardar.setEnabled(admin);
        btnActualizar.setEnabled(admin);
        btnEliminar.setEnabled(admin);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.guardar(obtenerLibro());
                    listar();
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.actualizar(obtenerLibro());
                    listar();
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.eliminar(
                        Integer.parseInt(txtId.getText())
                    );
                    listar();
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listar();
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();

                if (fila >= 0) {
                    txtId.setText(tabla.getValueAt(fila, 0).toString());
                    txtTitulo.setText(tabla.getValueAt(fila, 1).toString());
                    txtAutor.setText(tabla.getValueAt(fila, 2).toString());
                    txtEditorial.setText(String.valueOf(
                        tabla.getValueAt(fila, 3)));
                    txtIsbn.setText(tabla.getValueAt(fila, 4).toString());
                    txtCategoria.setText(tabla.getValueAt(fila, 5).toString());
                    txtStock.setText(tabla.getValueAt(fila, 6).toString());
                }
            }
        });

        listar();
    }

    private Libro obtenerLibro() {
        Libro libro = new Libro();

        if (!txtId.getText().trim().isEmpty()) {
            libro.setIdLibro(Integer.parseInt(txtId.getText()));
        }

        libro.setTitulo(txtTitulo.getText());
        libro.setAutor(txtAutor.getText());
        libro.setEditorial(txtEditorial.getText());
        libro.setIsbn(txtIsbn.getText());
        libro.setIdCategoria(Integer.parseInt(txtCategoria.getText()));
        libro.setStock(Integer.parseInt(txtStock.getText()));

        return libro;
    }

    private void listar() {
        try {
            modeloTabla.setRowCount(0);

            ArrayList<Libro> libros = controlador.listar();

            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getEditorial(),
                    libro.getIsbn(),
                    libro.getIdCategoria(),
                    libro.getStock()
                });
            }

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void mostrarError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
