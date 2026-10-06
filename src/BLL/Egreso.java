package BLL;
import java.time.LocalDate;

public class Egreso extends Manual {

	public Egreso(int idMovimiento, LocalDate fecha, Double monto, String descripcion) {
		super(idMovimiento, fecha, monto, descripcion);
	}

	public  String VerInfo() {
		return "Egreso #" + getIdMovimiento() + " | " + getFecha() + " | " + getDescripcion() + " | -$" + getMonto();}
	

	
	
	
	
}
