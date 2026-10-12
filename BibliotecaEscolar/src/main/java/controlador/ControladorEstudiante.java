
package controlador;

import dao.EstudianteDAO;
import modelo.Estudiante;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorEstudiante {

    private EstudianteDAO dao;

    public ControladorEstudiante() {
        dao = new EstudianteDAO();
    }

    public boolean guardar(Estudiante e) throws SQLException {
        return dao.guardar(e);
    }

    public ArrayList<Estudiante> listar() throws SQLException {
        return dao.listar();
    }

    public boolean actualizar(Estudiante e) throws SQLException {
        return dao.actualizar(e);
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }

    public int buscarIdPorUsuario(int idUsuario)
            throws SQLException {
        return dao.buscarIdPorUsuario(idUsuario);
    }
}
