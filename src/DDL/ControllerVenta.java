package DDL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;

public class ControllerVenta {

    // guarda venta + detalle + descuenta stock 
    public static boolean guardarVenta(int idUsuario, double descuento, double subtotal, double total,
            LinkedList<Integer> ids, LinkedList<Integer> cantidades, LinkedList<Double> precios) {

        Connection con = null;
        try {
            con = Conexion.getInstance().getConnection();
            con.setAutoCommit(false);

            PreparedStatement psVenta = con.prepareStatement(
                "INSERT INTO venta (fecha, descuento, subTotal, total, fk_usuario) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            psVenta.setDate(1, Date.valueOf(LocalDate.now()));
            psVenta.setDouble(2, descuento);
            psVenta.setDouble(3, subtotal);
            psVenta.setDouble(4, total);
            psVenta.setInt(5, idUsuario);
            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();
            rs.next();
            int idVenta = rs.getInt(1);

            for (int i = 0; i < ids.size(); i++) {
                PreparedStatement psDetalle = con.prepareStatement(
                    "INSERT INTO detalleVenta (cantidad, precio_uni, subTotal_item, fk_venta, fk_producto) VALUES (?, ?, ?, ?, ?)");
                psDetalle.setInt(1, cantidades.get(i));
                psDetalle.setDouble(2, precios.get(i));
                psDetalle.setDouble(3, precios.get(i) * cantidades.get(i));
                psDetalle.setInt(4, idVenta);
                psDetalle.setInt(5, ids.get(i));
                psDetalle.executeUpdate();

                PreparedStatement psStock = con.prepareStatement(
                    "UPDATE producto SET stock = stock - ? WHERE id_productos = ? AND stock >= ?");
                psStock.setInt(1, cantidades.get(i));
                psStock.setInt(2, ids.get(i));
                psStock.setInt(3, cantidades.get(i));
                if (psStock.executeUpdate() == 0) {
                    throw new SQLException("Stock insuficiente");
                }
            }

            // CUENTA CORRIENTE: ingreso por el total de la venta (
            PreparedStatement psCuenta = con.prepareStatement(
                "INSERT INTO cuentaCorriente (fecha, monto, descripcion, fk_usuario, fk_compra, fk_venta) VALUES (?, ?, ?, ?, NULL, ?)");
            psCuenta.setDate(1, Date.valueOf(LocalDate.now()));
            psCuenta.setDouble(2, total);
            psCuenta.setString(3, "Venta");
            psCuenta.setInt(4, idUsuario);
            psCuenta.setInt(5, idVenta);
            psCuenta.executeUpdate();

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();}}}
    
           
    // devuelve el texto de una venta con su detalle
    public static String obtenerVenta(int idVenta) {
        String texto = null;
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT v.fecha, v.descuento, v.subTotal, v.total, u.nombre " +
                "FROM venta v JOIN usuario u ON v.fk_usuario = u.id_usuario " +
                "WHERE v.id_venta = ?");
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                texto = "VENTA #" + idVenta +
                        "\nFecha: " + rs.getDate("fecha") +
                        "\nRegistrada por: " + rs.getString("nombre") +
                        "\n\nProductos:\n";

                PreparedStatement psDet = con.prepareStatement(
                    "SELECT p.nombre, p.talle, d.cantidad, d.precio_uni, d.subTotal_item " +
                    "FROM detalleVenta d JOIN producto p ON d.fk_producto = p.id_productos " +
                    "WHERE d.fk_venta = ?");
                psDet.setInt(1, idVenta);
                ResultSet rsDet = psDet.executeQuery();
                while (rsDet.next()) {
                    texto += "- " + rsDet.getString("nombre") + " (" + rsDet.getString("talle") + ") x"
                            + rsDet.getInt("cantidad") + " a $" + rsDet.getDouble("precio_uni")
                            + " = $" + rsDet.getDouble("subTotal_item") + "\n";
                }

                texto += "\nSubtotal: $" + rs.getDouble("subTotal") +
                         "\nDescuento: " + rs.getDouble("descuento") + "%" +
                         "\nTOTAL: $" + rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return texto;
    }

    // lista de todas las ventas en texto
    public static String listarVentas() {
        String texto = "";
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT v.id_venta, v.fecha, v.descuento, v.total, u.nombre " +
                "FROM venta v JOIN usuario u ON v.fk_usuario = u.id_usuario " +
                "ORDER BY v.id_venta");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                texto += "#" + rs.getInt("id_venta") + " - " + rs.getDate("fecha") +
                         " - " + rs.getString("nombre") +
                         " - Desc: " + rs.getDouble("descuento") + "%" +
                         " - Total: $" + rs.getDouble("total") + "\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (texto.equals("")) {
            texto = "No hay ventas registradas.";
        }
        return texto; }

    
    
    // elimina la venta, su detalle y devuelve el stock 
    public static boolean eliminarVenta(int idVenta) {
        Connection con = null;
        try {
            con = Conexion.getInstance().getConnection();
            con.setAutoCommit(false);

            
            // devolver stock
            PreparedStatement psSel = con.prepareStatement(
                "SELECT fk_producto, cantidad FROM detalleVenta WHERE fk_venta = ?");
            psSel.setInt(1, idVenta);
            ResultSet rs = psSel.executeQuery();
            while (rs.next()) {
                PreparedStatement psStock = con.prepareStatement(
                    "UPDATE producto SET stock = stock + ? WHERE id_productos = ?");
                psStock.setInt(1, rs.getInt("cantidad"));
                psStock.setInt(2, rs.getInt("fk_producto"));
                psStock.executeUpdate();
            }

            // CUENTA CORRIENTE: borrar el movimiento de esta venta (antes de borrar la venta por la FK)
            PreparedStatement psDelCuenta = con.prepareStatement("DELETE FROM cuentaCorriente WHERE fk_venta = ?");
            psDelCuenta.setInt(1, idVenta);
            psDelCuenta.executeUpdate();

            // borrar detalle y venta
            PreparedStatement psDelDet = con.prepareStatement("DELETE FROM detalleVenta WHERE fk_venta = ?");
            psDelDet.setInt(1, idVenta);
            psDelDet.executeUpdate();

            PreparedStatement psDelVenta = con.prepareStatement("DELETE FROM venta WHERE id_venta = ?");
            psDelVenta.setInt(1, idVenta);
            psDelVenta.executeUpdate();

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }} }
    
    
}
