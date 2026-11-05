package BLL;

import javax.swing.JOptionPane;

import DAO.ProveedorDAO;
import repository.Validaciones;

public class Proveedor {

	private String nombre;
	private String cuit;
	private String direccion;
	private String telefono;
	
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
		this.cuit = cuit;
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




	@Override
	public String toString() {
		return "Proveedor [nombre=" + nombre + ", Cuit=" + cuit + ", direccion=" + direccion + ", telefono=" + telefono	+ "]";}
	

	// crear proveedor
	public static void crearProveedor() {
		String cuit = Validaciones.validarCuit("CUIT del nuevo proveedor:");
		if (cuit == null) return;
		if (ProveedorDAO.existeCuit(cuit)) {
			JOptionPane.showMessageDialog(null, "Ya existe un proveedor con ese CUIT");
			return;}

		String nombre = Validaciones.validarLetras("Nombre del proveedor:");
		if (nombre == null) return;
		String direccion = JOptionPane.showInputDialog("Dirección:");
		if (direccion == null) return;
		if (direccion.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "La dirección es obligatoria");
			return;}
		String telefono = Validaciones.validarNumeros("Teléfono:");
		if (telefono == null) return;
		boolean creado = ProveedorDAO.crearProveedor(cuit, nombre, direccion.trim(), telefono);
		if (creado) {
			JOptionPane.showMessageDialog(null, "Proveedor creado correctamente");}
	                                   }


	// buscar proveedor
	public static void buscarProveedor() {
	    String cuit = Validaciones.validarCuit("CUIT del proveedor a buscar:");
	    if (cuit == null) return;

	    Proveedor p = ProveedorDAO.buscarProveedorPorCuit(cuit);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún proveedor con ese CUIT");
	        return; }

	    JOptionPane.showMessageDialog(null,
	        "Nombre: " + p.getNombre() +
	        "\n CUIT: " + p.getCuit() +
	        "\n Dirección: " + p.getDireccion() +
	        "\n Teléfono: " + p.getTelefono());}


	// editar proveedor
	public static void editarProveedor() {
		String cuit = Validaciones.validarCuit("CUIT del proveedor a editar:");
		if (cuit == null) return;

		Proveedor p = ProveedorDAO.buscarProveedorPorCuit(cuit);
		if (p == null) {
			JOptionPane.showMessageDialog(null, "No se encontró ningún proveedor con ese CUIT");
			return;}
		String nuevoNombre = Validaciones.validarLetras("Nombre del proveedor:", p.getNombre());
		if (nuevoNombre == null) return;
		String nuevaDireccion = JOptionPane.showInputDialog("Dirección:", p.getDireccion());
		if (nuevaDireccion == null) return;
		if (nuevaDireccion.trim().isEmpty()) {JOptionPane.showMessageDialog(null, "La dirección es obligatoria");
			return;}
		String nuevoTelefono = Validaciones.validarNumeros("Teléfono:", p.getTelefono());
		if (nuevoTelefono == null) return;
		boolean editado = ProveedorDAO.editarProveedor(cuit, nuevoNombre, nuevaDireccion.trim(), nuevoTelefono);
		if (editado) {JOptionPane.showMessageDialog(null, "Proveedor editado correctamente");}
	                                   }

	
	// eliminar proveedor
	public static void eliminarProveedor() {
	    String cuit = Validaciones.validarCuit("CUIT del proveedor a eliminar:");
		if (cuit == null) return;

	    Proveedor p = ProveedorDAO.buscarProveedorPorCuit(cuit);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún proveedor con ese CUIT");
	        return;}

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar a " + p.getNombre() + "?",
	            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	    if (confirmacion == JOptionPane.YES_OPTION) {
	        boolean eliminado = ProveedorDAO.desactivarProveedor(cuit);
	        if (eliminado) {
	            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
	        }}
	                    }


	// ver proveedores
	public static void verProveedores() {
	    String lista = ProveedorDAO.obtenerListaProveedores();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay proveedores cargados");
	        return; }
	    JOptionPane.showMessageDialog(null, lista, "Proveedores", JOptionPane.PLAIN_MESSAGE);
                }
	
	
	
	
}
