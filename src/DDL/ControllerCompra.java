package DDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import BLL.DetalleCompra;
import BLL.Producto;

public class ControllerCompra {

	
	//registra la compra
	public static int crearCompra(LocalDate fecha, Double total, String cuit, int idUsuario) {
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"INSERT INTO compra (fecha, total, fk_cuit, fk_usuario) VALUES (?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setDate(1, java.sql.Date.valueOf(fecha));
			stmt.setDouble(2, total);
			stmt.setString(3, cuit);
			stmt.setInt(4, idUsuario);

			int filas = stmt.executeUpdate();
			if (filas == 0) {
				stmt.close();
				return -1;
			}

			ResultSet rs = stmt.getGeneratedKeys();
			int idGenerado = -1;
			if (rs.next()) {
				idGenerado = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			return idGenerado;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	//registra el detalle
	public static boolean crearDetalleCompra(int cantidad, String idProducto, int idCompra) {
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"INSERT INTO detalleCompra (cantidad, fk_producto, fk_compra) VALUES (?, ?, ?)"
			);
			stmt.setInt(1, cantidad);
			stmt.setString(2, idProducto);
			stmt.setInt(3, idCompra);

			int filas = stmt.executeUpdate();
			stmt.close();
			return filas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean existeCompra(int idCompra) {
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "SELECT id_compra FROM compra WHERE id_compra = ?"
	        );
	        stmt.setInt(1, idCompra);
	        ResultSet rs = stmt.executeQuery();
	        boolean existe = rs.next();
	        rs.close();
	        stmt.close();
	        return existe;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	//buscar
	public static String obtenerCompraTexto(int idCompra) {
	    String texto = "";
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "SELECT compra.id_compra, compra.fecha, compra.total, " +
	            "proveedor.nombre AS proveedor_nombre, usuario.nombre AS usuario_nombre " +
	            "FROM compra " +
	            "INNER JOIN proveedor ON compra.fk_cuit = proveedor.cuit " +
	            "INNER JOIN usuario ON compra.fk_usuario = usuario.id_usuario " +
	            "WHERE compra.id_compra = ?"
	        );
	        stmt.setInt(1, idCompra);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            texto += "Compra #" + rs.getInt("id_compra") +
	                     " | Fecha: " + rs.getDate("fecha") +
	                     " | Proveedor: " + rs.getString("proveedor_nombre") +
	                     " | Registrada por: " + rs.getString("usuario_nombre") +
	                     " | Total pagado: $" + rs.getDouble("total") + "\n";
	            texto += obtenerDetalleCompra(idCompra);
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return texto;
	}

	public static LinkedList<DetalleCompra> obtenerDetalleCompraObjetos(int idCompra) {
	    LinkedList<DetalleCompra> detalles = new LinkedList<DetalleCompra>();
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "SELECT id_detalleCompra, cantidad, fk_producto FROM detalleCompra WHERE fk_compra = ?"
	        );
	        stmt.setInt(1, idCompra);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Producto producto = ControllerProducto.buscarProductoPorId(String.valueOf(rs.getInt("fk_producto")));
	            detalles.add(new DetalleCompra(rs.getInt("id_detalleCompra"), rs.getInt("cantidad"), producto, idCompra));
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return detalles;}
	
	
	
	//elimina el detalle
	public static boolean eliminarDetallesDeCompra(int idCompra) {
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "DELETE FROM detalleCompra WHERE fk_compra = ?"
	        );
	        stmt.setInt(1, idCompra);
	        stmt.executeUpdate();
	        stmt.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//elimina la compra
	public static boolean eliminarCompra(int idCompra) {
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "DELETE FROM compra WHERE id_compra = ?"
	        );
	        stmt.setInt(1, idCompra);
	        int filas = stmt.executeUpdate();
	        stmt.close();
	        return filas > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	//ver compras
    public static String obtenerListaCompras() {
        String lista = "";
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT compra.id_compra, compra.fecha, compra.total, " +
                "proveedor.nombre AS proveedor_nombre, usuario.nombre AS usuario_nombre " +
                "FROM compra " +
                "INNER JOIN proveedor ON compra.fk_cuit = proveedor.cuit " +
                "INNER JOIN usuario ON compra.fk_usuario = usuario.id_usuario " +
                "ORDER BY compra.fecha DESC");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idCompra = rs.getInt("id_compra");
                lista += "Compra #" + idCompra +
                         " | Fecha: " + rs.getDate("fecha") +
                         " | Proveedor: " + rs.getString("proveedor_nombre") +
                         " | Registrada por: " + rs.getString("usuario_nombre") +
                         " | Total pagado: $" + rs.getDouble("total") + "\n";
                lista += obtenerDetalleCompra(idCompra);
                lista += "----------------------------------------\n"; }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return lista;}

    private static String obtenerDetalleCompra(int idCompra) {
        String detalle = "";
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT detalleCompra.cantidad, producto.nombre " +
                "FROM detalleCompra " +
                "INNER JOIN producto ON detalleCompra.fk_producto = producto.id_productos " +
                "WHERE detalleCompra.fk_compra = ?"
            );
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                detalle += "    - " + rs.getInt("cantidad") + " x " + rs.getString("nombre") + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalle;
    }
    
}