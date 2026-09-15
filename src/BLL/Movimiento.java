package BLL;
import java.time.LocalDate;

public abstract  class   Movimiento {

	private int idMovimiento;
	private LocalDate fecha;
	private Double monto;
	
	
	public Movimiento(int idMovimiento, LocalDate fecha, Double monto) {
		super();
		this.idMovimiento = idMovimiento;
		this.fecha = fecha;
		this.monto = monto;
	}
	public int getIdMovimiento() {
		return idMovimiento;
	}
	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	
	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", fecha=" + fecha + ", monto=" + monto + "]";
	}
	
	public abstract String  VerInfo();
	
	
	
}
