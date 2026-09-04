
public class DetalleCompra {

	private int idDetalleCompra;
	private  int cantidad;
	
	public DetalleCompra(int idDetalleCompra, int cantidad) {
		super();
		this.idDetalleCompra = idDetalleCompra;
		this.cantidad = cantidad;
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
	

	public String VerInfo() {
		return "DetalleCompra [idDetalleCompra=" + idDetalleCompra + ", cantidad=" + cantidad + "]";
	}
	

	
	
}
