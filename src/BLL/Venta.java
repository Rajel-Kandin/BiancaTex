package BLL;
import java.time.LocalDate;

public  class Venta extends Movimiento {

	private int idVenta;
	private Double descuento;
	private Double subtotal;
	private Double total;
	private Usuario usuario;
	
	public Venta(int idMovimiento, LocalDate fecha, Double monto, int idVenta, Double descuento, Double subtotal,
			Double total, Usuario usuario) {
		super(idMovimiento, fecha, monto);
		this.idVenta = idVenta;
		this.descuento = descuento;
		this.subtotal = subtotal;
		this.total = total;
		this.usuario = usuario;
	}

	public int getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}

	public Double getDescuento() {
		return descuento;
	}

	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	public String VerInfo() {
		return "Venta [idVenta=" + idVenta + ", descuento=" + descuento + ", subtotal=" + subtotal + ", total=" + total
				+ ", usuario=" + usuario + "]";
	}
	
	
	
	
  
	
	
	
	
	
	
}
