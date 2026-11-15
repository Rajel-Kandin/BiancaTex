package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReporteDAO {

	//empleados q mas ventas tuvieron el ultimo mes 
    public static String empleadoConMasVentas() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT u.nombre, COUNT(v.id_venta) AS cantidad, SUM(v.total) AS recaudado "
                   + "FROM venta v "
                   + "JOIN usuario u ON u.id_usuario = v.fk_usuario "
                   + "WHERE u.fk_tipoDeUsuario = 2 "
                   + "AND v.fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) "
                   + "GROUP BY u.id_usuario, u.nombre "
                   + "ORDER BY cantidad DESC";
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            sb.append("EMPLEADOS CON MÁS VENTAS (último mes)\n\n");
            int puesto = 1;
            while (rs.next()) {
                sb.append(puesto).append(". ")
                  .append(rs.getString("nombre"))
                  .append(" - Ventas: ").append(rs.getInt("cantidad"))
                  .append(" - Recaudado: $").append(String.format("%.2f", rs.getDouble("recaudado")))
                  .append("\n");
                puesto++;}
            if (puesto == 1) {
                sb.append("No hay ventas de empleados en el último mes.");  }
            rs.close();
            ps.close();
        } catch (Exception e) {
            sb.append("Error al generar el reporte: ").append(e.getMessage());}
        return sb.toString();  }

    
    // 5 productos mas vendido en ulitmo mes 
    public static String productosMasVendidos() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT p.nombre, p.talle, SUM(d.cantidad) AS unidades "
                   + "FROM detalleventa d "
                   + "JOIN producto p ON p.id_productos = d.fk_producto "
                   + "JOIN venta v ON v.id_venta = d.fk_venta "
                   + "WHERE v.fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) "
                   + "GROUP BY p.id_productos, p.nombre, p.talle "
                   + "ORDER BY unidades DESC "
                   + "LIMIT 5";
        try {
            Connection con = Conexion.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            sb.append("TOP 5 PRODUCTOS MÁS VENDIDOS (último mes)\n\n");
            int puesto = 1;
            while (rs.next()) {
                sb.append(puesto).append(". ")
                  .append(rs.getString("nombre"))
                  .append(" (talle ").append(rs.getString("talle")).append(")")
                  .append(" - Unidades: ").append(rs.getInt("unidades"))
                  .append("\n");
                puesto++;}
            if (puesto == 1) {
                sb.append("No hay ventas en el último mes.");  }
            rs.close();
            ps.close();
        } catch (Exception e) {
            sb.append("Error al generar el reporte: ").append(e.getMessage()); }
        return sb.toString(); }

    
    // Balance último mes
    public static String balance() {
        StringBuilder sb = new StringBuilder();
        // Egreso = sale plata    Ingreso = entro plata
        String sqlMes = "SELECT "
                      + "SUM(CASE WHEN fk_compra IS NULL AND monto > 0 THEN monto ELSE 0 END) AS ingresos, "
                      + "SUM(CASE WHEN fk_compra IS NOT NULL OR monto < 0 THEN ABS(monto) ELSE 0 END) AS egresos "
                      + "FROM cuentacorriente "
                      + "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        String sqlTotal = "SELECT "
                        + "SUM(CASE WHEN fk_compra IS NULL AND monto > 0 THEN monto ELSE 0 END) "
                        + "- SUM(CASE WHEN fk_compra IS NOT NULL OR monto < 0 THEN ABS(monto) ELSE 0 END) AS saldo "
                        + "FROM cuentacorriente";
        try {
            Connection con = Conexion.getInstance().getConnection();

            PreparedStatement ps = con.prepareStatement(sqlMes);
            ResultSet rs = ps.executeQuery();
            double ingresos = 0;
            double egresos = 0;
            if (rs.next()) {
                ingresos = rs.getDouble("ingresos");
                egresos = rs.getDouble("egresos");
            }
            rs.close();
            ps.close();

            ps = con.prepareStatement(sqlTotal);
            rs = ps.executeQuery();
            double saldo = 0;
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
            rs.close();
            ps.close();

            double resultado = ingresos - egresos;

            sb.append("BALANCE (último mes)\n\n");
            sb.append("Ingresos: $").append(String.format("%.2f", ingresos)).append("\n");
            sb.append("Egresos:  $").append(String.format("%.2f", egresos)).append("\n");
            sb.append("Resultado: $").append(String.format("%.2f", resultado));
            sb.append(resultado >= 0 ? "  (ganancia)" : "  (pérdida)").append("\n\n");
            sb.append("Saldo actual de la cuenta corriente: $").append(String.format("%.2f", saldo));
        } catch (Exception e) {
            sb.append("Error al generar el reporte: ").append(e.getMessage());
        }
        return sb.toString();
    }
}