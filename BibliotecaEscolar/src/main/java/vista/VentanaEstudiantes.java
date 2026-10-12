
package vista;

import controlador.ControladorEstudiante;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class VentanaEstudiantes extends JFrame {

    private ControladorEstudiante controlador =
            new ControladorEstudiante();

    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtRut = new JTextField();
    private JTextField txtCorreo = new JTextField();
    private JTextField txtCurso = new JTextField();
    private JPasswordField txtContrasena = new JPasswordField();

    private DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Nombre", "RUT", "Correo", "Curso"}, 0
    );

    public VentanaEstudiantes() {

        setTitle("Gestión de Estudiantes");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(6, 2));

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("RUT:"));
        formulario.add(txtRut);

        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtContrasena);

        formulario.add(new JLabel("Curso:"));
        formulario.add(txtCurso);

        JTable tabla = new JTable(modelo);

        JPanel botones = new JPanel();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnListar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    Estudiante estudiante = obtenerEstudiante();

                    if (estudiante.getContrasena().trim().isEmpty()) {
                        throw new Exception(
                            "Debe ingresar una contraseña"
                        );
                    }

                    controlador.guardar(estudiante);

                    JOptionPane.showMessageDialog(
                        VentanaEstudiantes.this,
                        "Estudiante registrado correctamente"
                    );

                    cargar();

                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    controlador.actualizar(obtenerEstudiante());
                    cargar();

                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    int id = Integer.parseInt(txtId.getText());
                    controlador.eliminar(id);
                    cargar();

                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });

        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargar();
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int fila = tabla.getSelectedRow();

                if (fila >= 0) {
                    txtId.setText(
                        tabla.getValueAt(fila, 0).toString()
                    );
                    txtNombre.setText(
                        tabla.getValueAt(fila, 1).toString()
                    );
                    txtRut.setText(
                        tabla.getValueAt(fila, 2).toString()
                    );
                    txtCorreo.setText(
                        tabla.getValueAt(fila, 3).toString()
                    );
                    txtCurso.setText(
                        tabla.getValueAt(fila, 4).toString()
                    );

                    txtContrasena.setText("");
                }
            }
        });

        cargar();
    }

    private Estudiante obtenerEstudiante() {

        Estudiante estudiante = new Estudiante();

        if (!txtId.getText().trim().isEmpty()) {
            estudiante.setIdEstudiante(
                Integer.parseInt(txtId.getText())
            );
        }

        estudiante.setNombre(txtNombre.getText().trim());
        estudiante.setRut(txtRut.getText().trim());
        estudiante.setCorreo(txtCorreo.getText().trim());
        estudiante.setCurso(txtCurso.getText().trim());

        estudiante.setContrasena(
            new String(txtContrasena.getPassword())
        );

        estudiante.setRol("ESTUDIANTE");

        return estudiante;
    }

    private void cargar() {

        try {
            modelo.setRowCount(0);

            for (Estudiante estudiante : controlador.listar()) {

                modelo.addRow(new Object[]{
                    estudiante.getIdEstudiante(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCorreo(),
                    estudiante.getCurso()
                });
            }

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void mostrarError(Exception e) {
        JOptionPane.showMessageDialog(
            this, e.getMessage()
        );
    }
}
