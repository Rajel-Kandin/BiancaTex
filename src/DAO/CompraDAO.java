package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.LinkedList;
import BLL.DetalleCompra;
import BLL.Producto;

public class CompraDAO {

	// registra compra + detalles + stock + cuenta corriente con transaccion
	public static int registrarCompraCompleta(LocalDate fecha, Double total, String cuit, int idUsuario,
			LinkedList<DetalleCompra> detalles, byte[] factura) {

		Connection con = Conexion.getInstance().getConnection();
		int idCompra = -1;

		try {
			con.setAutoCommit(false);

			// compra (con factura opcional)
			PreparedStatement stmtCompra = con.prepareStatement(
				"INSERT INTO compra (fecha, total, fk_cuit, fk_usuario, factura) VALUES (?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
			stmtCompra.setDate(1, java.sql.Date.valueOf(fecha));
			stmtCompra.setDouble(2, total);
			stmtCompra.setString(3, cuit);
			stmtCompra.setInt(4, idUsuario);
			if (factura != null) {
				stmtCompra.setBytes(5, factura);
			} else {
				stmtCompra.setNull(5, Types.BLOB);
			}
			stmtCompra.executeUpdate();

			ResultSet rs = stmtCompra.getGeneratedKeys();
			if (rs.next()) {
				idCompra = rs.getInt(1);
			}
			rs.close();
			stmtCompra.close();

			if (idCompra == -1) {
				throw new SQLException("No se generó el id de la compra");
			}

			//  detalle y suma de stock
			PreparedStatement stmtDetalle = con.prepareStatement(
				"INSERT INTO detalleCompra (cantidad, fk_producto, fk_compra) VALUES (?, ?, ?)");
			PreparedStatement stmtStock = con.prepareStatement(
				"UPDATE producto SET stock = stock + ? WHERE id_productos = ?");

			for (DetalleCompra d : detalles) {
				stmtDetalle.setInt(1, d.getCantidad());
				stmtDetalle.setString(2, d.getProducto().getIdProducto());
				stmtDetalle.setInt(3, idCompra);
				stmtDetalle.executeUpdate();

				stmtStock.setInt(1, d.getCantidad());
				stmtStock.setString(2, d.getProducto().getIdProducto());
				if (stmtStock.executeUpdate() == 0) {
					throw new SQLException("No se encontró el producto " + d.getProducto().getIdProducto());
				}}
			stmtDetalle.close();
			stmtStock.close();

			// egreso en cuenta corriente
			PreparedStatement stmtCC = con.prepareStatement(
				"INSERT INTO cuentacorriente (fecha, monto, descripcion, fk_usuario, fk_compra, fk_venta) VALUES (?, ?, ?, ?, ?, NULL)");
			stmtCC.setDate(1, java.sql.Date.valueOf(fecha));
			stmtCC.setDouble(2, -total);
			stmtCC.setString(3, "Compra");
			stmtCC.setInt(4, idUsuario);
			stmtCC.setInt(5, idCompra);
			stmtCC.executeUpdate();
			stmtCC.close();

			con.commit();
			return idCompra;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();}
			return -1;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();}}
	}
	
	
	// elimina compra: resta stock + borra movimiento + detalle + compra con transaccion
	public static boolean eliminarCompraCompleta(int idCompra, LinkedList<DetalleCompra> detalles) {
		Connection con = Conexion.getInstance().getConnection();

		try {
			con.setAutoCommit(false);

			//  revertir stock 
			PreparedStatement stmtStock = con.prepareStatement(
				"UPDATE producto SET stock = stock - ? WHERE id_productos = ? AND stock >= ?");
			for (DetalleCompra d : detalles) {
				stmtStock.setInt(1, d.getCantidad());
				stmtStock.setString(2, d.getProducto().getIdProducto());
				stmtStock.setInt(3, d.getCantidad());
				if (stmtStock.executeUpdate() == 0) {
					throw new SQLException("Stock insuficiente para revertir el producto " + d.getProducto().getNombre());
				}
			}
			stmtStock.close();

			//  movimiento de cuenta corriente
			PreparedStatement stmtCC = con.prepareStatement(
				"DELETE FROM cuentacorriente WHERE fk_compra = ?");
			stmtCC.setInt(1, idCompra);
			stmtCC.executeUpdate();
			stmtCC.close();

			//  detalle
			PreparedStatement stmtDetalle = con.prepareStatement(
				"DELETE FROM detalleCompra WHERE fk_compra = ?");
			stmtDetalle.setInt(1, idCompra);
			stmtDetalle.executeUpdate();
			stmtDetalle.close();

			// compra
			PreparedStatement stmtCompra = con.prepareStatement(
				"DELETE FROM compra WHERE id_compra = ?");
			stmtCompra.setInt(1, idCompra);
			if (stmtCompra.executeUpdate() == 0) {
				throw new SQLException("No se encontró la compra " + idCompra);}
			stmtCompra.close();
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();}return false;

		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();	}}
	                               }
	

	public static boolean existeCompra(int idCompra) {
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"SELECT id_compra FROM compra WHERE id_compra = ?");
			stmt.setInt(1, idCompra);
			ResultSet rs = stmt.executeQuery();
			boolean existe = rs.next();
			rs.close();
			stmt.close();
			return existe;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;}
	}

	// buscar
	public static String obtenerCompraTexto(int idCompra) {
		String texto = "";
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"SELECT compra.id_compra, compra.fecha, compra.total, " +
				"proveedor.nombre AS proveedor_nombre, usuario.nombre AS usuario_nombre " +
				"FROM compra " +
				"INNER JOIN proveedor ON compra.fk_cuit = proveedor.cuit " +
				"INNER JOIN usuario ON compra.fk_usuario = usuario.id_usuario " +
				"WHERE compra.id_compra = ?");
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
			e.printStackTrace();}
		return texto;
	}
	

	public static LinkedList<DetalleCompra> obtenerDetalleCompraObjetos(int idCompra) {
		LinkedList<DetalleCompra> detalles = new LinkedList<DetalleCompra>();
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"SELECT id_detalleCompra, cantidad, fk_producto FROM detalleCompra WHERE fk_compra = ?");
			stmt.setInt(1, idCompra);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Producto producto = ProductoDAO.buscarProductoPorId(String.valueOf(rs.getInt("fk_producto")));
				detalles.add(new DetalleCompra(rs.getInt("id_detalleCompra"), rs.getInt("cantidad"), producto, idCompra));	}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();}
		return detalles;
	}

	// ver compras con el detalle
	public static String obtenerListaCompras() {
		StringBuilder lista = new StringBuilder();
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
				lista.append("Compra #" + idCompra +
						" | Fecha: " + rs.getDate("fecha") +
						" | Proveedor: " + rs.getString("proveedor_nombre") +
						" | Registrada por: " + rs.getString("usuario_nombre") +
						" | Total pagado: $" + rs.getDouble("total") + "\n");
				lista.append(obtenerDetalleCompra(idCompra));
				lista.append("----------------------------------------\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();}
		return lista.toString();
	                                      }

	
	// detalle  de la compra
	private static String obtenerDetalleCompra(int idCompra) {
		StringBuilder detalle = new StringBuilder();
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"SELECT detalleCompra.cantidad, producto.nombre " +
				"FROM detalleCompra " +
				"INNER JOIN producto ON detalleCompra.fk_producto = producto.id_productos " +
				"WHERE detalleCompra.fk_compra = ?");
			stmt.setInt(1, idCompra);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				detalle.append("    - " + rs.getInt("cantidad") + " x " + rs.getString("nombre") + "\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();}
		return detalle.toString();}
	
	// trae la imagen de la factura 
	public static byte[] obtenerFactura(int idCompra) {
		byte[] factura = null;
		try {
			PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
				"SELECT factura FROM compra WHERE id_compra = ?");
			stmt.setInt(1, idCompra);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				factura = rs.getBytes("factura");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();}
		return factura;}


}