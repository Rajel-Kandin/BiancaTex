import java.util.LinkedList;

public class CuentaCorriente {

	private Double saldo;
	private static LinkedList<Movimiento> movimientos = new   LinkedList<Movimiento>();
	
	
	public CuentaCorriente(Double saldo) {
		super();
		this.saldo = saldo;
	}
	
	
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public static LinkedList<Movimiento> getMovimientos() {
		return movimientos;
	}
	public static void setMovimientos(LinkedList<Movimiento> movimientos) {
		CuentaCorriente.movimientos = movimientos;
	}


	@Override
	public String toString() {
		return "CuentaCorriente [saldo=" + saldo + "]";
	}


	public LinkedList<Venta> filtrarVentas() {
	    return null;
	}

	public LinkedList<Compra> filtrarCompras() {
	    return null;
	}

	public LinkedList<Ingreso> filtrarIngresos() {
	    return null;
	}

	public LinkedList<Egreso> filtrarEgresos() {
	    return null;
	}
	
	
	

	
	
	
	
	
}
