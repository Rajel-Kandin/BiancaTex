package BLL;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import DAO.CompraDAO;
import DAO.ProductoDAO;
import DAO.ProveedorDAO;
import repository.Validaciones;

public class Compra extends Movimiento {

	private int idCompra;
	private Double total;
	private Proveedor proveedor;
	private Usuario usuario;

	private byte[] factura;

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

	public byte[] getFactura() {
		return factura;
	}

	public void setFactura(byte[] factura) {
		this.factura = factura;
	}

	public String VerInfo() {
		return "Compra [idCompra=" + idCompra + ", total=" + total + ", proveedor=" + proveedor + ", usuario=" + usuario + "]";
	}



	// metodo registrar compra
	public static void registrarCompra(Usuario usuarioLogueado) {

		// seleccionar proveedor
		Proveedor proveedor = seleccionarProveedor();
		if (proveedor == null) {
			return;}

		// seleccionar producto y cantidad
		LinkedList<DetalleCompra> detalleTemporal = new LinkedList<DetalleCompra>();
		boolean seguirAgregando = true;

		while (seguirAgregando) {
			Producto producto = seleccionarProducto();
			if (producto == null) return;

			Integer cantidad = Validaciones.validarEntero("Cantidad de \"" + producto.getNombre() + "\" a comprar:");
			if (cantidad == null) return;

			detalleTemporal.add(new DetalleCompra(0, cantidad, producto, 0));

			int seguir = JOptionPane.showConfirmDialog(null, "¿Quiere agregar otro producto a la compra?",
					"Agregar producto", JOptionPane.YES_NO_OPTION);
			seguirAgregando = (seguir == JOptionPane.YES_OPTION);}

		if (detalleTemporal.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La compra no tiene productos cargados, se cancela");
			return;}

		// ingresa el total
		Double total = Validaciones.validarDouble("Monto total de la compra:");
		if (total == null) return;

		// factura 
		byte[] factura = null;
		int quiereFactura = JOptionPane.showConfirmDialog(null,
				"¿Querés agregar la factura de la compra? (opcional)",
				"Factura", JOptionPane.YES_NO_OPTION);
		if (quiereFactura == JOptionPane.YES_OPTION) {
			factura = seleccionarFactura();}

		String textoFactura = (factura != null) ? "Con factura adjunta" : "Sin factura";

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Confirmar compra a " + proveedor.getNombre() + " por $" + total + "?\n" + textoFactura,
				"Confirmar compra", JOptionPane.YES_NO_OPTION);
		if (confirmacion != JOptionPane.YES_OPTION) return;

		LocalDate fecha = LocalDate.now();

		// todo junto en una transaccion la  compra, detalle, stock y cuenta corriente
		int idCompra = CompraDAO.registrarCompraCompleta(fecha, total, proveedor.getCuit(),
				usuarioLogueado.getIdUsuario(), detalleTemporal, factura);

		if (idCompra == -1) {
			JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la compra. No se guardó ningún dato.");
			return;}
		JOptionPane.showMessageDialog(null, "Compra #" + idCompra + " registrada correctamente");
	                            }


	// elige una imagen 
	private static byte[] seleccionarFactura() {
		JFileChooser chooser = new JFileChooser();

		// filtra lo q ves, solo imágenes
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Imágenes (JPG, PNG, JPEG)", "jpg", "jpeg", "png");
		chooser.setFileFilter(filter);

		int option = chooser.showOpenDialog(null);
		if (option != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "No se seleccionó factura. La compra sigue sin factura.");
			return null;}
		File archivo = chooser.getSelectedFile();

		// Validación manual extra
		String nombreArchivo = archivo.getName().toLowerCase();
		if (!(nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png"))) {
			JOptionPane.showMessageDialog(null, "Solo se permiten archivos JPG, JPEG o PNG. La compra sigue sin factura.");
			return null;}

		// limite de 5 MB
		if (archivo.length() > 5 * 1024 * 1024) {
			JOptionPane.showMessageDialog(null, "La imagen pesa más de 5 MB. Elegí una más liviana. La compra sigue sin factura.");
			return null;}

		try {
			byte[] bytes = Files.readAllBytes(archivo.toPath());
			JOptionPane.showMessageDialog(null, "Factura cargada correctamente.");
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al leer la imagen. La compra sigue sin factura.");
			return null;}
	}


	// selecciona un proveedor menu
	public static Proveedor seleccionarProveedor() {
		Proveedor elegido = null;
		boolean seguir = true;
		do {
			LinkedList<Proveedor> proveedores = ProveedorDAO.obtenerProveedores();
			String[] opciones = new String[proveedores.size() + 1];
			opciones[0] = "Crear nuevo proveedor";
			for (int i = 0; i < proveedores.size(); i++) {
				opciones[i + 1] = proveedores.get(i).getNombre() + " - CUIT: " + proveedores.get(i).getCuit();}

			String opcion = (String) JOptionPane.showInputDialog(null, "Elija el proveedor:", "Proveedor",
					JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

			if (opcion == null) {
				seguir = false;
			} else if (opcion.equals(opciones[0])) {
				Proveedor.crearProveedor();
			} else {
				for (int i = 1; i < opciones.length; i++) {
					if (opciones[i].equals(opcion)) {
						elegido = proveedores.get(i - 1);}}
				seguir = false;	}} while (seguir);
		return elegido;
	}


	// selecciona un producto de un menu
	private static Producto seleccionarProducto() {
		LinkedList<Producto> productos = ProductoDAO.obtenerProductos();
		if (productos.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay productos cargados. Se cancela la compra");
			return null;}

		String[] opciones = new String[productos.size()];
		for (int i = 0; i < productos.size(); i++) {
			opciones[i] = productos.get(i).getNombre() + " (Talle: " + productos.get(i).getTalle()
					+ " - Stock: " + productos.get(i).getStockActual() + ")";}

		String seleccion = (String) JOptionPane.showInputDialog(
				null, "Seleccioná el producto:", "Producto", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (seleccion == null) return null;

		int index = Arrays.asList(opciones).indexOf(seleccion);
		return productos.get(index);
	                       }



	// buscar compra
	public static void buscarCompra() {
		Integer idCompra = Validaciones.validarEntero("Número de compra a buscar:");
		if (idCompra == null) return;

		String texto = CompraDAO.obtenerCompraTexto(idCompra);
		if (texto.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontró ninguna compra con ese número");
			return;}

		// en lado izquierdo: muestra datos de la compra 
		JTextArea txtInfo = new JTextArea(texto.replace(" | ", "\n"));
		txtInfo.setEditable(false);
		txtInfo.setLineWrap(true);
		txtInfo.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(txtInfo);

		// del lado derecho:  esta la foto 
		JLabel lblFactura = new JLabel();
		lblFactura.setHorizontalAlignment(JLabel.CENTER);
		mostrarImagen(lblFactura, CompraDAO.obtenerFactura(idCompra));

		// divide la pagina
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(700, 350));
		panel.add(scroll);
		panel.add(lblFactura);

		JOptionPane.showMessageDialog(null, panel, "Compra #" + idCompra, JOptionPane.PLAIN_MESSAGE);
	}


	// redimensiona la imagen
	private static void mostrarImagen(JLabel lbl, byte[] imagenBytes) {
		if (imagenBytes != null && imagenBytes.length > 0) {
			ImageIcon icon = new ImageIcon(imagenBytes);
			int ancho = icon.getIconWidth();
			int alto = icon.getIconHeight();
			if (ancho > 0 && alto > 0) {
				double escala = Math.min(330.0 / ancho, 330.0 / alto);
				Image img = icon.getImage().getScaledInstance((int) (ancho * escala), (int) (alto * escala), Image.SCALE_SMOOTH);
				lbl.setIcon(new ImageIcon(img));
				return;}}
		lbl.setIcon(null);
		lbl.setText("Sin factura");
	}


	// eliminar compra (revierte stock y cuenta corriente)
	public static void eliminarCompra() {
		Integer idCompra = Validaciones.validarEntero("Número de compra a eliminar:");
		if (idCompra == null) return;

		if (!CompraDAO.existeCompra(idCompra)) {
			JOptionPane.showMessageDialog(null, "No se encontró ninguna compra con ese número");
			return;}

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Seguro que querés eliminar la compra #" + idCompra + "? Se revertirá el stock sumado",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		if (confirmacion != JOptionPane.YES_OPTION) return;

		LinkedList<DetalleCompra> detalle = CompraDAO.obtenerDetalleCompraObjetos(idCompra);
		if (CompraDAO.eliminarCompraCompleta(idCompra, detalle)) {
			JOptionPane.showMessageDialog(null, "Compra #" + idCompra + " eliminada correctamente");
		} else {JOptionPane.showMessageDialog(null,
					"No se pudo eliminar la compra. Puede que parte del stock ya se haya vendido. No se modificó nada.");}
	}



	// ver compras
	public static void verCompras() {
		String lista = CompraDAO.obtenerListaCompras();
		if (lista.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay compras registradas");
			return;}
		JOptionPane.showMessageDialog(null, lista, "Compras", JOptionPane.PLAIN_MESSAGE);}

	
}