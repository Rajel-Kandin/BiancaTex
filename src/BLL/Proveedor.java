package BLL;
import java.util.LinkedList;

public class Proveedor {

	private String nombre;
	private String cuit;
	private String direccion;
	private String telefono;
	private static LinkedList<Proveedor> proveedores = new   LinkedList<Proveedor>();
	
	
	public Proveedor(String nombre, String cuit, String direccion, String telefono) {
		super();
		this.nombre = nombre;
		this.cuit = cuit;
		this.direccion = direccion;
		this.telefono = telefono;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCuit() {
		return cuit;
	}


	public void setCuit(String cuit) {
		cuit = cuit;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public static LinkedList<Proveedor> getProveedores() {
		return proveedores;
	}


	public static void setProveedores(LinkedList<Proveedor> proveedores) {
		Proveedor.proveedores = proveedores;
	}


	@Override
	public String toString() {
		return "Proveedor [nombre=" + nombre + ", Cuit=" + cuit + ", direccion=" + direccion + ", telefono=" + telefono
				+ "]";
	}
	

	public void crearProveedor() {}
	public void editarProveedor() {}
	public void eliminarProveedor() {}
	public void verProveedores() {}
	
	
}
