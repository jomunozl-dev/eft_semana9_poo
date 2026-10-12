
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexion a Oracle Cloud Autonomous Database
 * mediante Wallet.
 *
 * 
 */

public class DatabaseConnection {

    private static DatabaseConnection instancia;

    // Ruta del Wallet
    private static final String WALLET_PATH =
            "/home/user/NetBeansProjects/BibliotecaEscolar/Wallet_evsumativa3"; //modificar ubicacion

    // URL de Oracle Cloud
    private static final String URL =
            "jdbc:oracle:thin:@evsumativa3_high?TNS_ADMIN="
            + WALLET_PATH;

    // Credenciales de Oracle Cloud
    private static final String USER = "ADMIN";

    private static final String PASSWORD = "duocUC$2026$";

    // Constructor privado
    private DatabaseConnection() {
    }

    // Singleton
    public static synchronized DatabaseConnection getInstance() {

        if (instancia == null) {
            instancia = new DatabaseConnection();
        }

        return instancia;
    }

    // Obtener conexion
    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    // Metodo alternativo compatible con los DAO
    public static Connection conectar() throws SQLException {

        return getInstance().getConnection();
    }
}
