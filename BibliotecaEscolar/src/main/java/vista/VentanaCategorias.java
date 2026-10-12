
package vista;

import controlador.ControladorCategoria;
import modelo.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class VentanaCategorias extends JFrame {

    private ControladorCategoria controlador =
            new ControladorCategoria();

    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();

    private DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Nombre"}, 0
    );

    public VentanaCategorias() {

        setTitle("Gestión de Categorías");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(2, 2));
        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        JTable tabla = new JTable(modelo);

        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton editar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton listar = new JButton("Listar");

        botones.add(guardar);
        botones.add(editar);
        botones.add(eliminar);
        botones.add(listar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        guardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.guardar(
                        new Categoria(0, txtNombre.getText())
                    );
                    cargar();
                } catch (Exception ex) {
                    error(ex);
                }
            }
        });

        editar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.actualizar(new Categoria(
                        Integer.parseInt(txtId.getText()),
                        txtNombre.getText()
                    ));
                    cargar();
                } catch (Exception ex) {
                    error(ex);
                }
            }
        });

        eliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.eliminar(
                        Integer.parseInt(txtId.getText())
                    );
                    cargar();
                } catch (Exception ex) {
                    error(ex);
                }
            }
        });

        listar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargar();
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();

                if (fila >= 0) {
                    txtId.setText(tabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                }
            }
        });

        cargar();
    }

    private void cargar() {
        try {
            modelo.setRowCount(0);

            for (Categoria c : controlador.listar()) {
                modelo.addRow(new Object[]{
                    c.getIdCategoria(), c.getNombre()
                });
            }

        } catch (Exception e) {
            error(e);
        }
    }

    private void error(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
