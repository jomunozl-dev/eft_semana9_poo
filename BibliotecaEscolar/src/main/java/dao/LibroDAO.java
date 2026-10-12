
package dao;

import conexion.DatabaseConnection;
import modelo.Libro;

import java.sql.*;
import java.util.ArrayList;

public class LibroDAO {

    public boolean guardar(Libro libro) throws SQLException {

        String sql = "INSERT INTO LIBROS " +
                "(TITULO, AUTOR, ISBN, EDITORIAL, STOCK, ID_CATEGORIA) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            return ps.executeUpdate() > 0;
        }
    }

    public ArrayList<Libro> listar() throws SQLException {

        ArrayList<Libro> lista = new ArrayList<Libro>();

        String sql = "SELECT ID, TITULO, AUTOR, ISBN, " +
                "EDITORIAL, STOCK, ID_CATEGORIA " +
                "FROM LIBROS ORDER BY ID";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Libro libro = new Libro();

                libro.setIdLibro(rs.getInt("ID"));
                libro.setTitulo(rs.getString("TITULO"));
                libro.setAutor(rs.getString("AUTOR"));
                libro.setIsbn(rs.getString("ISBN"));
                libro.setEditorial(rs.getString("EDITORIAL"));
                libro.setStock(rs.getInt("STOCK"));
                libro.setIdCategoria(rs.getInt("ID_CATEGORIA"));

                lista.add(libro);
            }
        }

        return lista;
    }

    public boolean actualizar(Libro libro) throws SQLException {

        String sql = "UPDATE LIBROS SET " +
                "TITULO=?, AUTOR=?, ISBN=?, EDITORIAL=?, " +
                "STOCK=?, ID_CATEGORIA=? WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getIdLibro());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM LIBROS WHERE ID=?";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
