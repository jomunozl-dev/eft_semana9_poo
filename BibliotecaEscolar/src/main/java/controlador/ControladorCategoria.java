
package controlador;

import dao.CategoriaDAO;
import modelo.Categoria;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorCategoria {

    private CategoriaDAO dao;

    public ControladorCategoria() {
        dao = new CategoriaDAO();
    }

    public boolean guardar(Categoria c) throws SQLException {
        return dao.guardar(c);
    }

    public ArrayList<Categoria> listar() throws SQLException {
        return dao.listar();
    }

    public boolean actualizar(Categoria c) throws SQLException {
        return dao.actualizar(c);
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }
}
