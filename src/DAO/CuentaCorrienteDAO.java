package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;

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
    

 // lista de movimientos de cc  con  filtros 
    public static String obtenerListaMovimientos(String filtro) {
        StringBuilder texto = new StringBuilder();
        double saldo = 0;
        double total = 0;

        String sql = "SELECT * FROM cuentacorriente";
        switch (filtro) {
            case "Ventas":
                sql += " WHERE fk_venta IS NOT NULL";
                break;
            case "Compras":
                sql += " WHERE fk_compra IS NOT NULL";
                break;
            case "Ingresos":
                sql += " WHERE fk_venta IS NULL AND fk_compra IS NULL AND monto > 0";
                break;
            case "Egresos":
                sql += " WHERE fk_venta IS NULL AND fk_compra IS NULL AND monto < 0";
                break;
        }
        sql += " ORDER BY id_movimiento";

        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double monto = rs.getDouble("monto");
                saldo += monto;
                total += monto;

                String tipo;
                if (rs.getObject("fk_venta") != null) {
                    tipo = "Venta #" + rs.getInt("fk_venta");
                } else if (rs.getObject("fk_compra") != null) {
                    tipo = "Compra #" + rs.getInt("fk_compra");
                } else {
                    tipo = rs.getString("descripcion"); }

                String montoTexto;
                if (monto >= 0) {
                    montoTexto = "+$" + String.format("%.2f", monto);
                } else {
                    montoTexto = "-$" + String.format("%.2f", -monto);}

                texto.append(rs.getDate("fecha") + "  |  " + tipo + "  |  " + montoTexto);
                if (filtro.equals("Todos")) {
                    texto.append("  |  Saldo: $" + String.format("%.2f", saldo)); }
                texto.append("\n");}

            if (texto.length() > 0 && !filtro.equals("Todos")) {
                texto.append("\n TOTAL " + filtro.toUpperCase() + ": $" + String.format("%.2f", Math.abs(total)));}
        } catch (Exception e) {
            e.printStackTrace();}
        return texto.toString(); }
    
    

    
    
    // muestra solo los ingresos y egresos manuales 
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