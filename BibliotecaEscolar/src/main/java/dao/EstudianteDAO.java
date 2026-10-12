
package dao;

import conexion.DatabaseConnection;
import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteDAO {

    public boolean guardar(Estudiante estudiante) throws SQLException {

        Connection con = DatabaseConnection.conectar();

        try {
            con.setAutoCommit(false);

            String sqlUsuario = "INSERT INTO USUARIOS " +
                    "(NOMBRE, RUT, CORREO, CONTRASENA, ROL) " +
                    "VALUES (?, ?, ?, ?, 'ESTUDIANTE')";

            int idUsuario;

            try (PreparedStatement ps = con.prepareStatement(
                    sqlUsuario, new String[]{"ID"})) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCorreo());
                ps.setString(4,
                    UsuarioDAO.encriptar(estudiante.getContrasena()));

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException(
                            "No se pudo obtener el ID del usuario"
                        );
                    }

                    idUsuario = rs.getInt(1);
                }
            }

            String sqlEstudiante = "INSERT INTO ESTUDIANTES " +
                    "(NOMBRE, RUT, CURSO, CORREO, ID_USUARIO) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlEstudiante)) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCurso());
                ps.setString(4, estudiante.getCorreo());
                ps.setInt(5, idUsuario);

                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;

        } finally {
            con.close();
        }
    }

    public ArrayList<Estudiante> listar() throws SQLException {

        ArrayList<Estudiante> lista =
                new ArrayList<Estudiante>();

        String sql = "SELECT ID, NOMBRE, RUT, CURSO, " +
                "CORREO, ID_USUARIO FROM ESTUDIANTES ORDER BY ID";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Estudiante estudiante = new Estudiante();

                estudiante.setIdEstudiante(rs.getInt("ID"));
                estudiante.setIdUsuario(rs.getInt("ID_USUARIO"));
                estudiante.setNombre(rs.getString("NOMBRE"));
                estudiante.setRut(rs.getString("RUT"));
                estudiante.setCurso(rs.getString("CURSO"));
                estudiante.setCorreo(rs.getString("CORREO"));

                lista.add(estudiante);
            }
        }

        return lista;
    }

    public boolean actualizar(Estudiante estudiante)
            throws SQLException {

        Connection con = DatabaseConnection.conectar();

        try {
            con.setAutoCommit(false);

            String sqlEstudiante = "UPDATE ESTUDIANTES SET " +
                    "NOMBRE=?, RUT=?, CURSO=?, CORREO=? WHERE ID=?";

            int filas;

            try (PreparedStatement ps =
                    con.prepareStatement(sqlEstudiante)) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCurso());
                ps.setString(4, estudiante.getCorreo());
                ps.setInt(5, estudiante.getIdEstudiante());

                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                con.rollback();
                return false;
            }

            String sqlUsuario = "UPDATE USUARIOS SET " +
                    "NOMBRE=?, RUT=?, CORREO=? " +
                    "WHERE ID=(SELECT ID_USUARIO FROM ESTUDIANTES " +
                    "WHERE ID=?)";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlUsuario)) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCorreo());
                ps.setInt(4, estudiante.getIdEstudiante());

                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;

        } finally {
            con.close();
        }
    }

    public boolean eliminar(int id) throws SQLException {

        Connection con = DatabaseConnection.conectar();

        try {
            con.setAutoCommit(false);

            int idUsuario = 0;

            String sqlBuscar =
                    "SELECT ID_USUARIO FROM ESTUDIANTES WHERE ID=?";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlBuscar)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    idUsuario = rs.getInt("ID_USUARIO");
                    if (rs.wasNull()) {
                        idUsuario = 0;
                    }
                }
            }

            String sqlEliminarEstudiante =
                    "DELETE FROM ESTUDIANTES WHERE ID=?";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlEliminarEstudiante)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            if (idUsuario > 0) {

                String sqlEliminarUsuario =
                        "DELETE FROM USUARIOS WHERE ID=?";

                try (PreparedStatement ps =
                        con.prepareStatement(sqlEliminarUsuario)) {

                    ps.setInt(1, idUsuario);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;

        } finally {
            con.close();
        }
    }

    public int buscarIdPorUsuario(int idUsuario)
            throws SQLException {

        String sql = "SELECT ID FROM ESTUDIANTES WHERE ID_USUARIO=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        }

        return -1;
    }
}
