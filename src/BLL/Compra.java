package BLL;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import DDL.ControllerCompra;
import DDL.ControllerProducto;
import DDL.ControllerProveedor;

public class Compra extends Movimiento {

	private int idCompra;
	private Double total;
	private Proveedor proveedor;
	private Usuario usuario;
	private static LinkedList<Compra> compras = new LinkedList<Compra>();

	public Compra(int idMovimiento, LocalDate fecha, Double monto, int idCompra, Double total, Proveedor proveedor, Usuario usuario) {
		super(idMovimiento, fecha, monto);
		this.idCompra = idCompra;
		this.total = total;
		this.proveedor = proveedor;
		this.usuario = usuario;
	}

	public int getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static LinkedList<Compra> getCompras() {
		return compras;
	}

	public static void setCompras(LinkedList<Compra> compras) {
		Compra.compras = compras;
	}

	public String VerInfo() {
		return "Compra [idCompra=" + idCompra + ", total=" + total + ", proveedor=" + proveedor + ", usuario=" + usuario + "]";
	}

	// registrar compra
	public static void registrarCompra(Usuario usuarioLogueado) {

		// seleccionar proveedor 
		Proveedor proveedor = seleccionarProveedor();
		if (proveedor == null) return; 

		//seleccionar producto y cantidad
		LinkedList<DetalleCompra> detalleTemporal = new LinkedList<DetalleCompra>();
		boolean seguirAgregando = true;

		while (seguirAgregando) {
			Producto producto = seleccionarProducto();
			if (producto == null) return; //

			String cantidadTexto = JOptionPane.showInputDialog("Cantidad de \"" + producto.getNombre() + "\" a comprar:");
			if (cantidadTexto == null) return;

			int cantidad;
			try {
				cantidad = Integer.parseInt(cantidadTexto);
				if (cantidad <= 0) {
					JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0");
					continue;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "La cantidad no es un número válido");
				continue;
			}

			// creo y guardo idDetalleCompra e idCompra 
			detalleTemporal.add(new DetalleCompra(0, cantidad, producto, 0));

			int seguir = JOptionPane.showConfirmDialog(null, "¿Quiere agregar otro producto a la compra?",
					"Agregar producto", JOptionPane.YES_NO_OPTION);
			seguirAgregando = (seguir == JOptionPane.YES_OPTION);
		}

		if (detalleTemporal.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La compra no tiene productos cargados, se cancela");
			return;
		}

		// ingresa el total
		String totalTexto = JOptionPane.showInputDialog("Monto total de la compra:");
		if (totalTexto == null) return;

		Double total;
		try {
			total = Double.parseDouble(totalTexto);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "El monto no es un número válido");
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Confirmar compra a " + proveedor.getNombre() + " por $" + total + "?",
				"Confirmar compra", JOptionPane.YES_NO_OPTION);
		if (confirmacion != JOptionPane.YES_OPTION) return;

		LocalDate fecha = LocalDate.now();

		//guardo el id del usuario que regsitro la compra
		int idCompra = ControllerCompra.crearCompra(fecha, total, proveedor.getCuit(), usuarioLogueado.getIdUsuario());
		if (idCompra == -1) {
			JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la compra");
			return;
		}

		// guardo  detalleCompra y suma al stock de cada producto
		for (DetalleCompra detalle : detalleTemporal) {
			detalle.setIdCompra(idCompra);
			ControllerCompra.crearDetalleCompra(detalle.getCantidad(), detalle.getProducto().getIdProducto(), idCompra);

			Producto producto = detalle.getProducto();
			int nuevoStock = producto.getStockActual() + detalle.getCantidad();
			ControllerProducto.actualizarStock(producto.getIdProducto(), nuevoStock);
			producto.setStockActual(nuevoStock);
		}

		// TODO RFA11: cuando esté armado CuentaCorriente, generar acá un egreso por "total"

		Compra nuevaCompra = new Compra(0, fecha, total, idCompra, total, proveedor, usuarioLogueado);
		compras.add(nuevaCompra);

		JOptionPane.showMessageDialog(null, "Compra registrada correctamente");
	}

	// selecciona un proveedor menu   
	private static Proveedor seleccionarProveedor() {
		LinkedList<Proveedor> proveedores = ControllerProveedor.obtenerProveedores();

		if (proveedores.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay proveedores cargados. Se cancela la compra");
			return null;
		}

		String[] opciones = new String[proveedores.size()];
		for (int i = 0; i < proveedores.size(); i++) {
			opciones[i] = proveedores.get(i).getNombre() + " (CUIT: " + proveedores.get(i).getCuit() + ")";
		}

		String seleccion = (String) JOptionPane.showInputDialog(
				null,
				"Seleccioná el proveedor:",
				"Proveedor",
				JOptionPane.QUESTION_MESSAGE,
				null,
				opciones,
				opciones[0]
		);

		if (seleccion == null) return null;

		int index = Arrays.asList(opciones).indexOf(seleccion);
		return proveedores.get(index);
	}

	// selecciona un producto de un menu  
	private static Producto seleccionarProducto() {
		LinkedList<Producto> productos = ControllerProducto.obtenerProductos();

		if (productos.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay productos cargados. Se cancela la compra");
			return null;
		}

		String[] opciones = new String[productos.size()];
		for (int i = 0; i < productos.size(); i++) {
			opciones[i] = productos.get(i).getNombre() + " (Talle: " + productos.get(i).getTalle()
					+ " - Stock: " + productos.get(i).getStockActual() + ")";
		}

		String seleccion = (String) JOptionPane.showInputDialog(
				null, "Seleccioná el producto:","Producto",JOptionPane.QUESTION_MESSAGE,null,	opciones,opciones[0]);

		if (seleccion == null) return null; // canceló

		int index = Arrays.asList(opciones).indexOf(seleccion);
		return productos.get(index);
	}



	// buscar compra
	public static void buscarCompra() {
	    String idTexto = JOptionPane.showInputDialog("Número de compra a buscar:");
	    if (idTexto == null) return;

	    int idCompra;
	    try {
	        idCompra = Integer.parseInt(idTexto);
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(null, "El número de compra no es válido");
	        return;
	    }

	    String texto = ControllerCompra.obtenerCompraTexto(idCompra);
	    if (texto.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No se encontró ninguna compra con ese número");
	        return;
	    }
	    JOptionPane.showMessageDialog(null, texto, "Compra #" + idCompra, JOptionPane.PLAIN_MESSAGE);}

	

	
	// eliminar compra (revierte el stock sumado)
	public static void eliminarCompra() {
	    String idTexto = JOptionPane.showInputDialog("Número de compra a eliminar:");
	    if (idTexto == null) return;

	    int idCompra;
	    try {
	        idCompra = Integer.parseInt(idTexto);
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(null, "El número de compra no es válido");
	        return;
	    }

	    if (!ControllerCompra.existeCompra(idCompra)) {
	        JOptionPane.showMessageDialog(null, "No se encontró ninguna compra con ese número");
	        return;
	    }

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar la compra #" + idCompra + "? Se revertirá el stock sumado",
	            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    LinkedList<DetalleCompra> detalle = ControllerCompra.obtenerDetalleCompraObjetos(idCompra);
	    for (DetalleCompra d : detalle) {
	        Producto producto = d.getProducto();
	        int stockRevertido = producto.getStockActual() - d.getCantidad();
	        ControllerProducto.actualizarStock(producto.getIdProducto(), stockRevertido);
	    }

	    ControllerCompra.eliminarDetallesDeCompra(idCompra);
	    ControllerCompra.eliminarCompra(idCompra);

	    //  cuando esté armado CuentaCorriente, revertir también el egreso asociado

	    Compra aEliminar = null;
	    for (Compra c : compras) {
	        if (c.getIdCompra() == idCompra) {
	            aEliminar = c;
	            break;
	        }
	    }
	    if (aEliminar != null) {
	        compras.remove(aEliminar);
	    }

	    JOptionPane.showMessageDialog(null, "Compra eliminada correctamente");}


	// ver compras
	public static void verCompras() {
	    String lista = ControllerCompra.obtenerListaCompras();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay compras registradas");
	        return;
	    }
	    JOptionPane.showMessageDialog(null, lista, "Compras", JOptionPane.PLAIN_MESSAGE);
	}

	
	
	
	
	
	
	
}
