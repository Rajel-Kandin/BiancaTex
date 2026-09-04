import java.util.LinkedList;

public class Producto {

	private String IdProducto;
	private String nombre;
	private Double precio;
	private int stockActual;
	private static LinkedList<Producto> productos = new   LinkedList<Producto>();
	
	
	public Producto(String idProducto, String nombre, Double precio, int stockActual) {
		super();
		IdProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.stockActual = stockActual;
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


	@Override
	public String toString() {
		return "Producto [IdProducto=" + IdProducto + ", nombre=" + nombre + ", precio=" + precio + ", stockActual="
				+ stockActual + "]";
	}
	
	
	public void StockSuficiente() {}
	public void SumarStock() {}
	public void  DescontarStock(){}
	
	public void crearProducto() {}
	public void editarProducto() {}
	public void eliminarProducto() {}
	public void verProductos() {}
	
	
	
}
