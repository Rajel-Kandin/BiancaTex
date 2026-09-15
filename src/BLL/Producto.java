package BLL;
import java.util.LinkedList;

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
		this.categoria = CategoriasProd.ElegirCategoria();
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




	public void stockSuficiente() {}
	public void sumarStock() {}
	public void  descontarStock(){}
	
	public void crearProducto() {}
	public void editarProducto() {}
	public void eliminarProducto() {}
	public void verProductos() {}
	
	
	
}
