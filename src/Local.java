
public class Local {

	private int IdLocal;
	private String direccion;
	
	public Local(int idLocal, String direccion) {
		super();
		IdLocal = idLocal;
		this.direccion = direccion;
	}

	public int getIdLocal() {
		return IdLocal;
	}

	public void setIdLocal(int idLocal) {
		IdLocal = idLocal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Local [IdLocal=" + IdLocal + ", direccion=" + direccion + "]";
	}
	
	
	
	
	
}
