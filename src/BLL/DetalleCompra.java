package BLL;

public class DetalleCompra {

	private int idDetalleCompra;
	private int cantidad;
	private Producto producto;
	private int idCompra;

	public DetalleCompra(int idDetalleCompra, int cantidad, Producto producto, int idCompra) {
		this.idDetalleCompra = idDetalleCompra;
		this.cantidad = cantidad;
		this.producto = producto;
		this.idCompra = idCompra;
	}

	public int getIdDetalleCompra() {
		return idDetalleCompra;
	}

	public void setIdDetalleCompra(int idDetalleCompra) {
		this.idDetalleCompra = idDetalleCompra;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	@Override
	public String toString() {
		return "DetalleCompra [cantidad=" + cantidad + ", producto=" + producto.getNombre() + "]";
	}
}