
package dao;

import conexion.DatabaseConnection;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class UsuarioDAO {

    public static String encriptar(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] datos = md.digest(
                clave.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder resultado = new StringBuilder();

            for (int i = 0; i < datos.length; i++) {
                resultado.append(
                    String.format("%02x", datos[i] & 0xff)
                );
            }

            return resultado.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean guardar(Usuario usuario) throws SQLException {

        String sql = "INSERT INTO USUARIOS " +
                "(NOMBRE, RUT, CORREO, CONTRASENA, ROL) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, encriptar(usuario.getContrasena()));
            ps.setString(5, usuario.getRol());

            return ps.executeUpdate() > 0;
        }
    }

    public Usuario iniciarSesion(String correo, String contrasena)
            throws SQLException {

        String sql = "SELECT ID, NOMBRE, RUT, CORREO, ROL " +
                "FROM USUARIOS WHERE CORREO=? AND CONTRASENA=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, encriptar(contrasena));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("ID"));
                    usuario.setNombre(rs.getString("NOMBRE"));
                    usuario.setRut(rs.getString("RUT"));
                    usuario.setCorreo(rs.getString("CORREO"));
                    usuario.setRol(rs.getString("ROL"));

                    return usuario;
                }
            }
        }

        return null;
    }

    public ArrayList<Usuario> listar() throws SQLException {

        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        String sql = "SELECT ID, NOMBRE, RUT, CORREO, ROL " +
                "FROM USUARIOS ORDER BY ID";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();

                usuario.setIdUsuario(rs.getInt("ID"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setRut(rs.getString("RUT"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setRol(rs.getString("ROL"));

                lista.add(usuario);
            }
        }

        return lista;
    }

    public boolean actualizar(Usuario usuario) throws SQLException {

        String sql = "UPDATE USUARIOS SET " +
                "NOMBRE=?, RUT=?, CORREO=?, ROL=? WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getRol());
            ps.setInt(5, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM USUARIOS WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
