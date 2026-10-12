
package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorUsuario {

    private UsuarioDAO dao;

    public ControladorUsuario() {
        dao = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String correo, String clave)
            throws SQLException {
        return dao.iniciarSesion(correo, clave);
    }

    public boolean guardar(Usuario usuario) throws SQLException {
        return dao.guardar(usuario);
    }

    public ArrayList<Usuario> listar() throws SQLException {
        return dao.listar();
    }

    public boolean actualizar(Usuario usuario) throws SQLException {
        return dao.actualizar(usuario);
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }
}
