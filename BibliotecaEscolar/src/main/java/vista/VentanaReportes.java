
package vista;

import controlador.ControladorPrestamo;
import controlador.ControladorEstudiante;
import modelo.Prestamo;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VentanaReportes extends JFrame {

    private ControladorPrestamo controlador =
            new ControladorPrestamo();

    private ControladorEstudiante controladorEstudiante =
            new ControladorEstudiante();

    private Usuario usuario;

    private DefaultTableModel modelo = new DefaultTableModel();

    public VentanaReportes(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Reportes de Biblioteca");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable tabla = new JTable(modelo);

        JPanel botones = new JPanel();

        JButton btnMas = new JButton("Libros más prestados");
        JButton btnHistorial = new JButton("Historial estudiante");
        JButton btnActivos = new JButton("Préstamos activos");

        botones.add(btnMas);
        botones.add(btnHistorial);
        botones.add(btnActivos);

        add(botones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnMas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarMasPrestados();
            }
        });

        btnHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarHistorial();
            }
        });

        btnActivos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarActivos();
            }
        });
    }

    private boolean esAdmin() {
        return "BIBLIOTECARIO".equals(usuario.getRol());
    }

    private void mostrarMasPrestados() {

        if (!esAdmin()) {
            JOptionPane.showMessageDialog(
                this, "Reporte exclusivo del bibliotecario"
            );
            return;
        }

        try {
            modelo.setColumnIdentifiers(
                new String[]{"Libro", "Total préstamos"}
            );
            modelo.setRowCount(0);

            for (Object[] fila : controlador.librosMasPrestados()) {
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void mostrarHistorial() {

        try {
            int idEstudiante;

            if (esAdmin()) {

                String dato = JOptionPane.showInputDialog(
                    this, "Ingrese ID del estudiante"
                );

                if (dato == null) {
                    return;
                }

                idEstudiante = Integer.parseInt(dato);

            } else {

                idEstudiante =
                    controladorEstudiante.buscarIdPorUsuario(
                        usuario.getIdUsuario()
                    );
            }

            modelo.setColumnIdentifiers(
                new String[]{
                    "ID", "Libro", "Fecha préstamo",
                    "Fecha devolución", "Devuelto"
                }
            );

            modelo.setRowCount(0);

            for (Prestamo p : controlador.historial(idEstudiante)) {

                modelo.addRow(new Object[]{
                    p.getIdPrestamo(),
                    p.getIdLibro(),
                    p.getFechaPrestamo(),
                    p.getFechaDevolucion(),
                    p.isDevuelto() ? "S" : "N"
                });
            }

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void mostrarActivos() {

        try {
            modelo.setColumnIdentifiers(
                new String[]{
                    "ID", "Estudiante", "Libro", "Fecha préstamo"
                }
            );

            modelo.setRowCount(0);

            ArrayList<Prestamo> lista;

            if (esAdmin()) {
                lista = controlador.listar();
            } else {
                int idEstudiante =
                    controladorEstudiante.buscarIdPorUsuario(
                        usuario.getIdUsuario()
                    );

                lista = controlador.historial(idEstudiante);
            }

            for (Prestamo p : lista) {

                if (!p.isDevuelto()) {

                    modelo.addRow(new Object[]{
                        p.getIdPrestamo(),
                        p.getIdEstudiante(),
                        p.getIdLibro(),
                        p.getFechaPrestamo()
                    });
                }
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
