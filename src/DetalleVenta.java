
public class DetalleVenta {

	private int idDetalleVenta;
	private int cantidad;
	private Double precioUnitario;
	private Double subtotal;
	
	

	public DetalleVenta(int idDetalleVenta, int cantidad, Double precioUnitario, Double subtotal) {
		super();
		this.idDetalleVenta = idDetalleVenta;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subtotal = subtotal;
	}

	public int getIdDetalleVenta() {
		return idDetalleVenta;
	}

	public void setIdDetalleVenta(int idDetalleVenta) {
		this.idDetalleVenta = idDetalleVenta;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	
	public String VerInfo() {
		return "DetalleCompra [idDetalleVenta=" + idDetalleVenta + ", cantidad=" + cantidad + ", precioUnitario="
				+ precioUnitario + ", subtotal=" + subtotal + "]";
	}
	
	
	
	
}
