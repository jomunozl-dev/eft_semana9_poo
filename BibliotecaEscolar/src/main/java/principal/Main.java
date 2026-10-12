
package principal;

import vista.VentanaLogin;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                VentanaLogin ventana = new VentanaLogin();
                ventana.setVisible(true);
            }
        });
    }
}
