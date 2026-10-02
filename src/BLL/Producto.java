package BLL;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import DDL.ControllerProducto;

public class Producto {

	private String IdProducto;
	private String nombre;
	private Double precio;
	private int stockActual;
	private  CategoriasProd categoria;
	private String talle;
	private static LinkedList<Producto> productos = new   LinkedList<Producto>();



	public Producto(String idProducto, String nombre, Double precio, int stockActual, CategoriasProd categoria,
			String talle) {
		super();
		IdProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.stockActual = stockActual;
		this.categoria = categoria;
		this.talle = talle;
	}




	public String getIdProducto() {
		return IdProducto;
	}


	public void setIdProducto(String idProducto) {
		IdProducto = idProducto;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public int getStockActual() {
		return stockActual;
	}


	public void setStockActual(int stockActual) {
		this.stockActual = stockActual;
	}


	public static LinkedList<Producto> getProductos() {
		return productos;
	}


	public static void setProductos(LinkedList<Producto> productos) {
		Producto.productos = productos;
	}
	
	


	public CategoriasProd getCategoria() {
		return categoria;
	}


	public void setCategoria(CategoriasProd categoria) {
		this.categoria = categoria;
	}

	
	
	
	public String getTalle() {
		return talle;
	}


	public void setTalle(String talle) {
		this.talle = talle;
	}
	
	


	@Override
	public String toString() {
		return "Producto [IdProducto=" + IdProducto + ", nombre=" + nombre + ", precio=" + precio + ", stockActual="
				+ stockActual + ", categoria=" + categoria + ", talle=" + talle + "]";
	}
	
	
	// crear producto
	// crear producto
	public static void crearProducto() {
	    String nombre = JOptionPane.showInputDialog("Nombre del producto:");
	    if (nombre == null) return;
	    String precioTexto = JOptionPane.showInputDialog("Precio:");
	    if (precioTexto == null) return;
	    String stockTexto = JOptionPane.showInputDialog("Stock inicial:");
	    if (stockTexto == null) return;
	    String talle = JOptionPane.showInputDialog("Talle:");
	    if (talle == null) return;
	    CategoriasProd categoria = CategoriasProd.ElegirCategoria();
	    if (categoria == null) return;

	    try {
	        double precio = Double.parseDouble(precioTexto);
	        int stock = Integer.parseInt(stockTexto);

	        boolean creado = ControllerProducto.crearProducto(nombre, precio, stock, categoria, talle);
	        if (creado) {
	            JOptionPane.showMessageDialog(null, "Producto creado correctamente");
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(null, "El precio o el stock no son números válidos");
	    }
	}


	// buscar producto
	public static void buscarProductos() {
	    String id = JOptionPane.showInputDialog("Código del producto a buscar:");
	    if (id == null) return;

	    Producto p = ControllerProducto.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;
	    }

	    JOptionPane.showMessageDialog(null,
	        "Nombre: " + p.getNombre() +
	        "\nPrecio: $" + p.getPrecio() +
	        "\nStock: " + p.getStockActual() +
	        "\nCategoría: " + p.getCategoria() +
	        "\nTalle: " + p.getTalle());
	}


	// editar producto
	public static void editarProducto() {
	    String id = JOptionPane.showInputDialog("Código del producto a editar:");
	    if (id == null) return;

	    Producto p = ControllerProducto.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;
	    }

	    String nuevoNombre = JOptionPane.showInputDialog("Nombre del producto:", p.getNombre());
	    if (nuevoNombre == null) return;
	    String precioTexto = JOptionPane.showInputDialog("Precio:", p.getPrecio());
	    if (precioTexto == null) return;
	    String stockTexto = JOptionPane.showInputDialog("Stock:", p.getStockActual());
	    if (stockTexto == null) return;
	    String nuevoTalle = JOptionPane.showInputDialog("Talle:", p.getTalle());
	    if (nuevoTalle == null) return;
	    CategoriasProd nuevaCategoria = CategoriasProd.ElegirCategoria();
	    if (nuevaCategoria == null) return;

	    try {
	        double nuevoPrecio = Double.parseDouble(precioTexto);
	        int nuevoStock = Integer.parseInt(stockTexto);

	        boolean editado = ControllerProducto.editarProducto(id, nuevoNombre, nuevoPrecio, nuevoStock, nuevaCategoria, nuevoTalle);
	        if (editado) {
	            JOptionPane.showMessageDialog(null, "Producto editado correctamente");
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(null, "El precio o el stock no son números válidos");
	    }
	}


	// eliminar producto
	public static void eliminarProducto() {
	    String id = JOptionPane.showInputDialog("Código del producto a eliminar:");
	    if (id == null) return;

	    Producto p = ControllerProducto.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;
	    }

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar " + p.getNombre() + "?",
	            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	    if (confirmacion == JOptionPane.YES_OPTION) {
	        boolean eliminado = ControllerProducto.eliminarProducto(id);
	        if (eliminado) {
	            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
	        }
	    }
	}


	// ver productos
	public static void verProductos() {
	    String lista = ControllerProducto.obtenerListaProductos();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay productos cargados");
	        return;
	    }
	    JOptionPane.showMessageDialog(null, lista, "Productos", JOptionPane.PLAIN_MESSAGE);
	}


	// ver bajo stock
	public static void verBajoStock() {
	    String lista = ControllerProducto.obtenerListaBajoStock();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay productos con bajo stock");
	        return;
	    }
	    JOptionPane.showMessageDialog(null, lista, "Bajo stock (menos de 20 unidades)", JOptionPane.PLAIN_MESSAGE);
	}


	public void stockSuficiente() {}
	public void sumarStock() {}
	public void descontarStock(){}
	

	
	
	
}
