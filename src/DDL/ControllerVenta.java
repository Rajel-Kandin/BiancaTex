package DDL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;

import BLL.DetalleVenta;

public class ControllerVenta {

    // guarda venta, el detalle y descuenta stock. Devuelve el id de la venta (0 si falló)
    public static int guardarVenta(int idUsuario, double descuento, double subtotal, double total,
            String nombreCliente, String telefonoCliente, LinkedList<DetalleVenta> detalles) {

        Connection con = null;
        try {
            con = Conexion.getInstance().getConnection();
            con.setAutoCommit(false);

            PreparedStatement psVenta = con.prepareStatement(
                "INSERT INTO venta (fecha, descuento, subTotal, total, fk_usuario, nombre_cliente, telefono_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            psVenta.setDate(1, Date.valueOf(LocalDate.now()));
            psVenta.setDouble(2, descuento);
            psVenta.setDouble(3, subtotal);
            psVenta.setDouble(4, total);
            psVenta.setInt(5, idUsuario);
            psVenta.setString(6, nombreCliente);
            psVenta.setString(7, telefonoCliente);
            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();
            rs.next();
            int idVenta = rs.getInt(1);

            for (DetalleVenta d : detalles) {
                String idProducto = d.getProducto().getIdProducto();

                PreparedStatement psDetalle = con.prepareStatement(
                    "INSERT INTO detalleVenta (cantidad, precio_uni, subTotal_item, fk_venta, fk_producto) VALUES (?, ?, ?, ?, ?)");
                psDetalle.setInt(1, d.getCantidad());
                psDetalle.setDouble(2, d.getPrecioUnitario());
                psDetalle.setDouble(3, d.getSubtotal());
                psDetalle.setInt(4, idVenta);
                psDetalle.setString(5, idProducto);
                psDetalle.executeUpdate();

                PreparedStatement psStock = con.prepareStatement(
                    "UPDATE producto SET stock = stock - ? WHERE id_productos = ? AND stock >= ?");
                psStock.setInt(1, d.getCantidad());
                psStock.setString(2, idProducto);
                psStock.setInt(3, d.getCantidad());
                if (psStock.executeUpdate() == 0) {
                    throw new SQLException("Stock insuficiente");
                } }

            // en cuenta corriente: ingreso por el total de la venta
            PreparedStatement psCuenta = con.prepareStatement(
                "INSERT INTO cuentacorriente (fecha, monto, descripcion, fk_usuario, fk_compra, fk_venta) VALUES (?, ?, ?, ?, NULL, ?)");
            psCuenta.setDate(1, Date.valueOf(LocalDate.now()));
            psCuenta.setDouble(2, total);
            psCuenta.setString(3, "Venta");
            psCuenta.setInt(4, idUsuario);
            psCuenta.setInt(5, idVenta);
            psCuenta.executeUpdate();

            con.commit();
            return idVenta;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
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
                "SELECT v.fecha, v.descuento, v.subTotal, v.total, v.nombre_cliente, v.telefono_cliente, u.nombre " +
                "FROM venta v JOIN usuario u ON v.fk_usuario = u.id_usuario " +
                "WHERE v.id_venta = ?");
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                texto = "VENTA #" + idVenta +
                        "\nFecha: " + rs.getDate("fecha") +
                        "\nRegistrada por: " + rs.getString("nombre") +
                        "\nCliente: " + rs.getString("nombre_cliente") +
                        "\nTeléfono: " + rs.getString("telefono_cliente") +
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
                rsDet.close();
                psDet.close();

                texto += "\nSubtotal: $" + rs.getDouble("subTotal") +
                         "\nDescuento: " + rs.getDouble("descuento") + "%" +
                         "\nTOTAL: $" + rs.getDouble("total");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return texto; }


    // lista de todas las ventas
    public static String listarVentas() {
        String lista = "";
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT venta.id_venta, venta.fecha, venta.subTotal, venta.descuento, venta.total, " +
                "venta.nombre_cliente, venta.telefono_cliente, " +
                "usuario.nombre AS usuario_nombre " +
                "FROM venta " +
                "INNER JOIN usuario ON venta.fk_usuario = usuario.id_usuario " +
                "ORDER BY venta.fecha DESC");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idVenta = rs.getInt("id_venta");
                lista += "Venta #" + idVenta +
                         " | Fecha: " + rs.getDate("fecha") +
                         " | Registrada por: " + rs.getString("usuario_nombre") +
                         " | Cliente: " + rs.getString("nombre_cliente") +
                         " | Subtotal: $" + rs.getDouble("subTotal") +
                         " | Descuento: " + rs.getDouble("descuento") + "%" +
                         " | Total cobrado: $" + rs.getDouble("total") + "\n";
                lista += obtenerDetalleVenta(idVenta);
                lista += "----------------------------------------\n"; }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }

        if (lista.equals("")) {
            lista = "No hay ventas registradas."; }
        return lista; }
    
    
    
    // detalle de productos de una venta
    public static String obtenerDetalleVenta(int idVenta) {
        String detalle = "";
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT producto.nombre, producto.talle, detalleVenta.cantidad, " +
                "detalleVenta.precio_uni, detalleVenta.subTotal_item " +
                "FROM detalleVenta " +
                "INNER JOIN producto ON detalleVenta.fk_producto = producto.id_productos " +
                "WHERE detalleVenta.fk_venta = ?");
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                detalle += "   - " + rs.getString("nombre") +
                           " (talle " + rs.getString("talle") + ")" +
                           " x" + rs.getInt("cantidad") +
                           " | $" + rs.getDouble("precio_uni") + " c/u" +
                           " | Subtotal: $" + rs.getDouble("subTotal_item") + "\n"; }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return detalle; }



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

            //en  cuenta corriente : borrar el movimiento de esta venta
            PreparedStatement psDelCuenta = con.prepareStatement("DELETE FROM cuentacorriente WHERE fk_venta = ?");
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
            return true; }

        catch (SQLException e) {
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
                    con.setAutoCommit(true);    }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }} }


}