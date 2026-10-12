
package vista;

import controlador.ControladorUsuario;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private ControladorUsuario controlador;

    public VentanaLogin() {

        controlador = new ControladorUsuario();

        setTitle("Biblioteca Escolar - Login");
        setSize(400, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        txtCorreo = new JTextField();
        txtClave = new JPasswordField();
        btnIngresar = new JButton("Ingresar");

        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtClave);
        panel.add(new JLabel(""));
        panel.add(btnIngresar);

        add(panel);

        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        try {
            Usuario usuario = controlador.iniciarSesion(
                txtCorreo.getText().trim(),
                new String(txtClave.getPassword())
            );

            if (usuario != null) {
                JOptionPane.showMessageDialog(
                    this, "Bienvenido " + usuario.getNombre()
                );

                new VentanaPrincipal(usuario).setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(
                    this, "Correo o contraseña incorrectos"
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
