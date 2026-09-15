package BLL;
import java.time.LocalDate;

public class Compra extends Movimiento {

	private int idCompra;
	private Double total;
	private Proveedor proveedor;
	
	public Compra(int idMovimiento, LocalDate fecha, Double monto, int idCompra, Double total, Proveedor proveedor) {
		super(idMovimiento, fecha, monto);
		this.idCompra = idCompra;
		this.total = total;
		this.proveedor = proveedor;
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

	
	public String VerInfo() {
		return "Compra [idCompra=" + idCompra + ", total=" + total + ", proveedor=" + proveedor + "]";
	}
	
	
	
	
	
	
	
}
