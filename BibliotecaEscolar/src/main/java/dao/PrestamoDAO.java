
package dao;

import conexion.DatabaseConnection;
import modelo.Prestamo;

import java.sql.*;
import java.util.ArrayList;

public class PrestamoDAO {

    public synchronized boolean guardar(Prestamo prestamo)
            throws SQLException {

        Connection con = DatabaseConnection.conectar();

        try {
            con.setAutoCommit(false);

            String sqlStock = "UPDATE LIBROS SET STOCK=STOCK-1 " +
                    "WHERE ID=? AND STOCK>0";

            int filas;

            try (PreparedStatement ps =
                    con.prepareStatement(sqlStock)) {

                ps.setInt(1, prestamo.getIdLibro());
                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                throw new SQLException(
                    "El libro no existe o no tiene stock disponible"
                );
            }

            String sqlPrestamo = "INSERT INTO PRESTAMOS " +
                    "(ID_ESTUDIANTE, ID_LIBRO, FECHA_PRESTAMO, DEVUELTO) " +
                    "VALUES (?, ?, SYSDATE, 'N')";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlPrestamo)) {

                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());

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

    public synchronized boolean devolver(int idPrestamo)
            throws SQLException {

        Connection con = DatabaseConnection.conectar();

        try {
            con.setAutoCommit(false);

            int idLibro;

            String sqlBuscar = "SELECT ID_LIBRO FROM PRESTAMOS " +
                    "WHERE ID=? AND DEVUELTO='N' FOR UPDATE";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlBuscar)) {

                ps.setInt(1, idPrestamo);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                            "El préstamo no existe o ya fue devuelto"
                        );
                    }

                    idLibro = rs.getInt("ID_LIBRO");
                }
            }

            String sqlDevolver = "UPDATE PRESTAMOS SET " +
                    "DEVUELTO='S', FECHA_DEVOLUCION=SYSDATE " +
                    "WHERE ID=? AND DEVUELTO='N'";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlDevolver)) {

                ps.setInt(1, idPrestamo);
                ps.executeUpdate();
            }

            String sqlStock =
                    "UPDATE LIBROS SET STOCK=STOCK+1 WHERE ID=?";

            try (PreparedStatement ps =
                    con.prepareStatement(sqlStock)) {

                ps.setInt(1, idLibro);
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

    public ArrayList<Prestamo> listar() throws SQLException {

        ArrayList<Prestamo> lista = new ArrayList<Prestamo>();

        String sql = "SELECT ID, ID_ESTUDIANTE, ID_LIBRO, " +
                "FECHA_PRESTAMO, FECHA_DEVOLUCION, DEVUELTO " +
                "FROM PRESTAMOS ORDER BY ID DESC";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Prestamo prestamo = new Prestamo();

                prestamo.setIdPrestamo(rs.getInt("ID"));
                prestamo.setIdEstudiante(rs.getInt("ID_ESTUDIANTE"));
                prestamo.setIdLibro(rs.getInt("ID_LIBRO"));
                prestamo.setFechaPrestamo(
                    rs.getDate("FECHA_PRESTAMO")
                );
                prestamo.setFechaDevolucion(
                    rs.getDate("FECHA_DEVOLUCION")
                );
                prestamo.setDevuelto(
                    "S".equals(rs.getString("DEVUELTO"))
                );

                lista.add(prestamo);
            }
        }

        return lista;
    }

    public ArrayList<Prestamo> historial(int idEstudiante)
            throws SQLException {

        ArrayList<Prestamo> historial =
                new ArrayList<Prestamo>();

        for (Prestamo p : listar()) {
            if (p.getIdEstudiante() == idEstudiante) {
                historial.add(p);
            }
        }

        return historial;
    }

    public ArrayList<Object[]> librosMasPrestados()
            throws SQLException {

        ArrayList<Object[]> lista = new ArrayList<Object[]>();

        String sql = "SELECT L.TITULO, COUNT(*) AS TOTAL " +
                "FROM PRESTAMOS P " +
                "INNER JOIN LIBROS L ON P.ID_LIBRO=L.ID " +
                "GROUP BY L.ID, L.TITULO " +
                "ORDER BY TOTAL DESC";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Object[]{
                    rs.getString("TITULO"),
                    rs.getInt("TOTAL")
                });
            }
        }

        return lista;
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM PRESTAMOS " +
                "WHERE ID=? AND DEVUELTO='S'";

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Prestamo prestamo)
            throws SQLException {

        // Los préstamos activos solo cambian mediante devolver().
        // No se permite modificar libro o estudiante porque
        // eso afectaría el control de inventario.
        return false;
    }
}
