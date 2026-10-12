
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
import java.util.Calendar;

public class VentanaPrestamos extends JFrame {

    private ControladorPrestamo controlador =
            new ControladorPrestamo();

    private ControladorEstudiante controladorEstudiante =
            new ControladorEstudiante();

    private Usuario usuario;
    private boolean admin;

    private JTextField txtEstudiante = new JTextField();
    private JTextField txtLibro = new JTextField();
    private JTextField txtPrestamo = new JTextField();

    private JButton btnPrestar = new JButton("Registrar préstamo");
    private JButton btnDevolver = new JButton("Devolver");

    private DefaultTableModel modelo = new DefaultTableModel(
        new String[]{
            "ID", "Estudiante", "Libro", "Fecha préstamo",
            "Vencimiento", "Fecha devolución", "Devuelto", "Atraso"
        }, 0
    );

    public VentanaPrestamos(Usuario usuario) {

        this.usuario = usuario;
        this.admin = "BIBLIOTECARIO".equals(usuario.getRol());

        setTitle("Préstamos y Devoluciones");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(3, 2));

        formulario.add(new JLabel("ID Estudiante:"));
        formulario.add(txtEstudiante);

        formulario.add(new JLabel("ID Libro:"));
        formulario.add(txtLibro);

        formulario.add(new JLabel("ID Préstamo:"));
        formulario.add(txtPrestamo);

        if (!admin) {
            txtEstudiante.setEditable(false);
        }

        JTable tabla = new JTable(modelo);

        JPanel botones = new JPanel();

        JButton btnListar = new JButton("Listar");

        botones.add(btnPrestar);
        botones.add(btnDevolver);
        botones.add(btnListar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnPrestar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarPrestamo();
            }
        });

        btnDevolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                devolverPrestamo();
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
                    txtPrestamo.setText(
                        tabla.getValueAt(fila, 0).toString()
                    );
                }
            }
        });

        cargar();
    }

    private int obtenerIdEstudiante() throws Exception {

        if (admin) {
            return Integer.parseInt(txtEstudiante.getText());
        }

        int id = controladorEstudiante.buscarIdPorUsuario(
            usuario.getIdUsuario()
        );

        if (id == -1) {
            throw new Exception(
                "El usuario no tiene un registro de estudiante"
            );
        }

        return id;
    }

    private void registrarPrestamo() {

        try {
            final int idEstudiante = obtenerIdEstudiante();

            final int idLibro = Integer.parseInt(
                txtLibro.getText()
            );

            btnPrestar.setEnabled(false);

            Thread hilo = new Thread(new Runnable() {

                public void run() {

                    try {
                        Prestamo prestamo = new Prestamo();

                        prestamo.setIdEstudiante(idEstudiante);
                        prestamo.setIdLibro(idLibro);

                        controlador.guardar(prestamo);

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {

                                JOptionPane.showMessageDialog(
                                    VentanaPrestamos.this,
                                    "Préstamo registrado correctamente"
                                );

                                btnPrestar.setEnabled(true);
                                cargar();
                            }
                        });

                    } catch (final Exception ex) {

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {

                                JOptionPane.showMessageDialog(
                                    VentanaPrestamos.this,
                                    ex.getMessage()
                                );

                                btnPrestar.setEnabled(true);
                            }
                        });
                    }
                }
            });

            hilo.start();

        } catch (Exception e) {
            btnPrestar.setEnabled(true);
            mostrarError(e);
        }
    }

    private void devolverPrestamo() {

        try {
            int idPrestamo = Integer.parseInt(
                txtPrestamo.getText()
            );

            if (!admin) {

                int idEstudiante = obtenerIdEstudiante();
                boolean pertenece = false;

                ArrayList<Prestamo> historial =
                    controlador.historial(idEstudiante);

                for (Prestamo p : historial) {

                    if (p.getIdPrestamo() == idPrestamo) {
                        pertenece = true;
                        break;
                    }
                }

                if (!pertenece) {
                    throw new Exception(
                        "El préstamo no pertenece al estudiante"
                    );
                }
            }

            controlador.devolver(idPrestamo);

            JOptionPane.showMessageDialog(
                this, "Devolución registrada correctamente"
            );

            cargar();

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void cargar() {

        try {
            modelo.setRowCount(0);

            ArrayList<Prestamo> lista;

            if (admin) {
                lista = controlador.listar();
            } else {
                lista = controlador.historial(
                    obtenerIdEstudiante()
                );
            }

            for (Prestamo p : lista) {

                Calendar calendario = Calendar.getInstance();
                calendario.setTime(p.getFechaPrestamo());
                calendario.add(Calendar.DAY_OF_MONTH, 7);

                java.sql.Date vencimiento = new java.sql.Date(
                    calendario.getTimeInMillis()
                );

                String atraso = "NO";

                long fechaComparacion = System.currentTimeMillis();

                if (p.isDevuelto() &&
                    p.getFechaDevolucion() != null) {

                    fechaComparacion =
                        p.getFechaDevolucion().getTime();
                }

                if (fechaComparacion > vencimiento.getTime()) {
                    atraso = "SI";
                }

                modelo.addRow(new Object[]{
                    p.getIdPrestamo(),
                    p.getIdEstudiante(),
                    p.getIdLibro(),
                    p.getFechaPrestamo(),
                    vencimiento,
                    p.getFechaDevolucion(),
                    p.isDevuelto() ? "S" : "N",
                    atraso
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
