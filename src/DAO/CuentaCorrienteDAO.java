package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import javax.swing.table.DefaultTableModel;

public class CuentaCorrienteDAO {

    // guarda un movimiento 
    public static boolean registrarMovimiento(LocalDate fecha, double monto, String descripcion, int idUsuario,
            Integer idCompra, Integer idVenta) {
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO cuentacorriente (fecha, monto, descripcion, fk_usuario, fk_compra, fk_venta) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setDouble(2, monto);
            stmt.setString(3, descripcion);
            stmt.setInt(4, idUsuario);

            if (idCompra == null) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, idCompra);}

            if (idVenta == null) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, idVenta); }

            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  } }
    

    // true si ya hay algún movimiento cargado
    public static boolean existenMovimientos() {
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM cuentacorriente");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;}
        } catch (Exception e) {
            e.printStackTrace(); }
        return false; }


    // calcula saldo actual 
    public static double obtenerSaldo() {
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT IFNULL(SUM(monto), 0) FROM cuentacorriente");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);  }
        } catch (Exception e) {
            e.printStackTrace(); }
        return 0;}


    // carga la tabla
    public static void cargarTablaMovimientos(DefaultTableModel modelo, String filtro) {
        modelo.setRowCount(0);
        String where = "";
        switch (filtro) {
            case "Ventas":   where = "WHERE c.fk_venta IS NOT NULL "; break;
            case "Compras":  where = "WHERE c.fk_compra IS NOT NULL "; break;
            case "Ingresos": where = "WHERE c.fk_venta IS NULL AND c.fk_compra IS NULL AND c.monto > 0 "; break;
            case "Egresos":  where = "WHERE c.fk_venta IS NULL AND c.fk_compra IS NULL AND c.monto < 0 "; break;
        }
        String sql = "SELECT c.id_movimiento, c.fecha, c.monto, c.descripcion, c.fk_venta, c.fk_compra, u.nombre "
                   + "FROM cuentacorriente c "
                   + "LEFT JOIN usuario u ON u.id_usuario = c.fk_usuario "
                   + where
                   + "ORDER BY c.fecha DESC, c.id_movimiento DESC";
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");

                String tipo;
                if (rs.getObject("fk_venta") != null) {
                    tipo = "Venta #" + rs.getInt("fk_venta");
                } else if (rs.getObject("fk_compra") != null) {
                    tipo = "Compra #" + rs.getInt("fk_compra");
                } else if ("Saldo inicial".equals(descripcion)) {
                    tipo = "Saldo inicial";
                } else if (monto > 0) {
                    tipo = "Ingreso";
                } else {
                    tipo = "Egreso"; }

                String montoTexto = (monto >= 0 ? "+ $" : "- $") + String.format("%.2f", Math.abs(monto));

                modelo.addRow(new Object[] {
                    rs.getInt("id_movimiento"),
                    rs.getString("fecha"),
                    tipo,
                    descripcion,
                    montoTexto,
                    rs.getString("nombre")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace(); }}


    // muestra  los ingresos  egresos  
    public static String obtenerListaManuales() {
        StringBuilder texto = new StringBuilder();
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM cuentacorriente WHERE fk_compra IS NULL AND fk_venta IS NULL "
                    + "AND descripcion <> 'Saldo inicial' ORDER BY id_movimiento");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double monto = rs.getDouble("monto");
                String montoTexto;
                if (monto >= 0) {
                    montoTexto = "+$" + String.format("%.2f", monto);
                } else {
                    montoTexto = "-$" + String.format("%.2f", -monto); }

                texto.append("#" + rs.getInt("id_movimiento") + "  |  " + rs.getDate("fecha") + "  |  "
                        + rs.getString("descripcion") + "  |  " + montoTexto + "\n"); }
        } catch (Exception e) {
            e.printStackTrace();  }
        return texto.toString();   }


    // elimina un movimiento manual 
    public static boolean eliminarMovimientoManual(int idMovimiento) {
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM cuentacorriente WHERE id_movimiento = ? AND fk_compra IS NULL "
                    + "AND fk_venta IS NULL AND descripcion <> 'Saldo inicial'");
            stmt.setInt(1, idMovimiento);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false; }}
    
    
    
}