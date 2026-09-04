import java.time.LocalDate;

public abstract class Manual extends Movimiento{

	private String descripcion;

	public Manual(int idMovimiento, LocalDate fecha, Double monto, String descripcion) {
		super(idMovimiento, fecha, monto);
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	public String VerInfo() {
		return "Manual [descripcion=" + descripcion + "]";
	}
	

	
	
	
	
	
	
	
}
