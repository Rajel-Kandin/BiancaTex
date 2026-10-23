package BLL;

import javax.swing.JOptionPane;

import DDL.ControllerProducto;
import repository.Validaciones;

public class Producto {

	private String IdProducto;
	private String nombre;
	private Double precio;
	private int stockActual;
	private  CategoriasProd categoria;
	private String talle;


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
	public static void crearProducto() {
	    String nombre = Validaciones.validarLetras("Nombre del producto:");
	    if (nombre == null) return;
	    Double precio = Validaciones.validarDouble("Precio:");
	    if (precio == null) return;
	    String stockTexto = Validaciones.validarNumeros("Stock inicial:");
	    if (stockTexto == null) return;
	    if (stockTexto.length() > 9) {
	        JOptionPane.showMessageDialog(null, "El stock es demasiado grande."); return; }
	    int stock = Integer.parseInt(stockTexto);
	    String talle = Validaciones.validarLetras("Talle:");
	    if (talle == null) return;
	    CategoriasProd categoria = CategoriasProd.ElegirCategoria();
	    if (categoria == null) return;

	    boolean creado = ControllerProducto.crearProducto(nombre, precio, stock, categoria, talle);
	    if (creado) {
	        JOptionPane.showMessageDialog(null, "Producto creado correctamente");
	    }
	}


	// buscar producto
	public static void buscarProductos() {
	    String id = Validaciones.validarNumeros("Código del producto a buscar:");
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
	    String id = Validaciones.validarNumeros("Código del producto a editar:");
	    if (id == null) return;

	    Producto p = ControllerProducto.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return; }


	    String nuevoNombre = Validaciones.validarLetras("Nombre del producto:", p.getNombre());
	    if (nuevoNombre == null) return;
	    Double nuevoPrecio = Validaciones.validarDouble("Precio:", String.valueOf(p.getPrecio()));
	    if (nuevoPrecio == null) return;
	    String stockTexto = Validaciones.validarNumeros("Stock:", String.valueOf(p.getStockActual()));
	    if (stockTexto == null) return;
	    if (stockTexto.length() > 9) {
	        JOptionPane.showMessageDialog(null, "El stock es demasiado grande.");
	        return; }
	    int nuevoStock = Integer.parseInt(stockTexto);
	    String nuevoTalle = Validaciones.validarLetras("Talle:", p.getTalle());
	    if (nuevoTalle == null) return;
	    CategoriasProd nuevaCategoria = CategoriasProd.ElegirCategoria(p.getCategoria());
	    if (nuevaCategoria == null) return;

	    boolean editado = ControllerProducto.editarProducto(id, nuevoNombre, nuevoPrecio, nuevoStock, nuevaCategoria, nuevoTalle);
	    if (editado) {
	        JOptionPane.showMessageDialog(null, "Producto editado correctamente");}
	                    }

	
	
	

	// eliminar producto
	public static void eliminarProducto() {
	    String id = Validaciones.validarNumeros("Código del producto a eliminar:");
	    if (id == null) return;

	    Producto p = ControllerProducto.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;}

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar " + p.getNombre() + "?",
	            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	    if (confirmacion == JOptionPane.YES_OPTION) {
	        boolean eliminado = ControllerProducto.desactivarProducto(id);
	        if (eliminado) {
	            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente"); } }
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



	

	
	
	
}
