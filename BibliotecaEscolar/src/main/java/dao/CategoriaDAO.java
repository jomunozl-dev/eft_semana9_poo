
package dao;

import conexion.DatabaseConnection;
import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;

public class CategoriaDAO {

    public boolean guardar(Categoria categoria) throws SQLException {

        String sql = "INSERT INTO CATEGORIAS (NOMBRE) VALUES (?)";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            return ps.executeUpdate() > 0;
        }
    }

    public ArrayList<Categoria> listar() throws SQLException {

        ArrayList<Categoria> lista = new ArrayList<Categoria>();

        String sql = "SELECT ID, NOMBRE FROM CATEGORIAS ORDER BY ID";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();

                categoria.setIdCategoria(rs.getInt("ID"));
                categoria.setNombre(rs.getString("NOMBRE"));

                lista.add(categoria);
            }
        }

        return lista;
    }

    public boolean actualizar(Categoria categoria)
            throws SQLException {

        String sql = "UPDATE CATEGORIAS SET NOMBRE=? WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM CATEGORIAS WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
