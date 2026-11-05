package BLL;

import GUI.InterfazEmpleado;

public class Empleado extends Usuario {

	private Local local;

	public Empleado(int idUsuario, String dni, String nombre, String email, Boolean activo,
			Local local) {
		super(idUsuario, 2, dni, nombre, email, activo);
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	@Override
	public void verMenu() {
		InterfazEmpleado ventana = new InterfazEmpleado(this);
		ventana.setVisible(true);
	}
}
