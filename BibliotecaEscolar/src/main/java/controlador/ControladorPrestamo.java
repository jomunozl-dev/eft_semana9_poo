
package controlador;

import dao.PrestamoDAO;
import modelo.Prestamo;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorPrestamo {

    private static final PrestamoDAO dao = new PrestamoDAO();

    public boolean guardar(Prestamo prestamo)
            throws SQLException {
        return dao.guardar(prestamo);
    }

    public boolean devolver(int idPrestamo)
            throws SQLException {
        return dao.devolver(idPrestamo);
    }

    public ArrayList<Prestamo> listar()
            throws SQLException {
        return dao.listar();
    }

    public ArrayList<Prestamo> historial(int idEstudiante)
            throws SQLException {
        return dao.historial(idEstudiante);
    }

    public ArrayList<Object[]> librosMasPrestados()
            throws SQLException {
        return dao.librosMasPrestados();
    }

    public boolean eliminar(int id)
            throws SQLException {
        return dao.eliminar(id);
    }

    public boolean actualizar(Prestamo prestamo)
            throws SQLException {
        return dao.actualizar(prestamo);
    }
}
