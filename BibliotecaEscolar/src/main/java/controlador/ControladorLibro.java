
package controlador;

import dao.LibroDAO;
import modelo.Libro;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorLibro {

    private LibroDAO dao;

    public ControladorLibro() {
        dao = new LibroDAO();
    }

    public boolean guardar(Libro libro) throws SQLException {
        return dao.guardar(libro);
    }

    public ArrayList<Libro> listar() throws SQLException {
        return dao.listar();
    }

    public boolean actualizar(Libro libro) throws SQLException {
        return dao.actualizar(libro);
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }
}
