
package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame {

    private Usuario usuario;

    public VentanaPrincipal(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Biblioteca Escolar - " + usuario.getNombre());
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

        JLabel titulo = new JLabel(
            "Usuario: " + usuario.getNombre() +
            " | Rol: " + usuario.getRol(),
            SwingConstants.CENTER
        );

        JButton btnLibros = new JButton("Gestión de libros");
        JButton btnEstudiantes = new JButton("Gestión de estudiantes");
        JButton btnCategorias = new JButton("Gestión de categorías");
        JButton btnPrestamos = new JButton("Préstamos y devoluciones");
        JButton btnReportes = new JButton("Reportes");
        JButton btnSalir = new JButton("Cerrar sesión");

        panel.add(titulo);
        panel.add(btnLibros);
        panel.add(btnEstudiantes);
        panel.add(btnCategorias);
        panel.add(btnPrestamos);
        panel.add(btnReportes);
        panel.add(btnSalir);

        add(panel);

        boolean admin = usuario.getRol().equals("BIBLIOTECARIO");

        btnEstudiantes.setVisible(admin);
        btnCategorias.setVisible(admin);

        btnLibros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaLibros(admin).setVisible(true);
            }
        });

        btnEstudiantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaEstudiantes().setVisible(true);
            }
        });

        btnCategorias.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaCategorias().setVisible(true);
            }
        });

        btnPrestamos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaPrestamos(usuario).setVisible(true);
            }
        });

        btnReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaReportes(usuario).setVisible(true);
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VentanaLogin().setVisible(true);
            }
        });
    }
}
