package BLL;

import javax.swing.JOptionPane;

import DAO.LocalDAO;

public class Local {

	private int idLocal;
	private String direccion;

	public Local(int idLocal, String direccion) {
		this.idLocal = idLocal;
		this.direccion = direccion;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Local [idLocal=" + idLocal + ", direccion=" + direccion + "]";
	}


	// crear local
	public static void crearLocal() {
		String direccion = JOptionPane.showInputDialog("Dirección del nuevo local:");
		if (direccion == null) return;
		if (direccion.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "La dirección es obligatoria");
			return;}

		boolean creado = LocalDAO.crearLocal(direccion.trim());
		if (creado) {
			JOptionPane.showMessageDialog(null, "Local creado correctamente");}
	}


	// editar local
	public static void editarLocal(int idLocal) {
		Local l = LocalDAO.buscarLocalPorId(idLocal);
		if (l == null) {
			JOptionPane.showMessageDialog(null, "No se encontró ningún local con ese ID");
			return;}

		String nuevaDireccion = JOptionPane.showInputDialog("Dirección:", l.getDireccion());
		if (nuevaDireccion == null) return;
		if (nuevaDireccion.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "La dirección es obligatoria");
			return;}

		l.setDireccion(nuevaDireccion.trim());
		boolean editado = LocalDAO.editarLocal(l);
		if (editado) {
			JOptionPane.showMessageDialog(null, "Local editado correctamente");}
	}


	// eliminar local 
	public static void eliminarLocal(int idLocal) {
		Local l = LocalDAO.buscarLocalPorId(idLocal);
		if (l == null) {
			JOptionPane.showMessageDialog(null, "No se encontró ningún local con ese ID");
			return;}

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Seguro que querés eliminar el local de " + l.getDireccion() + "?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean eliminado = LocalDAO.desactivarLocal(idLocal);
			if (eliminado) {
				JOptionPane.showMessageDialog(null, "Local eliminado correctamente");}}
	}
}
