package BLL;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import DAO.LocalDAO;
import DAO.UsuarioDAO;
import repository.Validaciones;

public abstract class Usuario implements Menu {

    private int idUsuario;
    private int tipoUsuario;
    private String dni;
    private String nombre;
    private String mail;
    private Boolean activo;

    public Usuario(int idUsuario, int tipoUsuario, String dni, String nombre, String email, Boolean activo) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.dni = dni;
        this.nombre = nombre;
        this.mail = email;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return mail;
    }

    public void setEmail(String email) {
        this.mail = email;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", tipoUsuario=" + tipoUsuario + ", dni=" + dni + ", nombre="
                + nombre + ", mail=" + mail + ", activo=" + activo + "]";
    }
    

    public String getTipoTexto() {
        switch (tipoUsuario) {
            case 1: return "Admin";
            case 2: return "Empleado";
            case 3: return "Contador";
            default: return "Desconocido";  } }
    

	// crear usuario
	public void crearUsuario() {
		String dni = Validaciones.validarDni("DNI del nuevo usuario:");
		if (dni == null) return;
		if (UsuarioDAO.existeDni(dni)) {
			JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese DNI");
			return;}

		String nombre = Validaciones.validarLetras("Nombre y apellido:");
		if (nombre == null) return;

		String email = Validaciones.validarEmail("Email de la empresa:");
		if (email == null) return;
		if (UsuarioDAO.existeEmail(email, 0)) {
			JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese email");
			return;}

		String password = JOptionPane.showInputDialog("Contraseña del nuevo usuario:");
		if (password == null) return;

		String[] opcionesTipo = {"Admin", "Empleado", "Contador"};
		int tipoSeleccionado = JOptionPane.showOptionDialog(null, "Tipo de usuario", "Crear usuario",
				0, 0, null, opcionesTipo, opcionesTipo[0]);
		if (tipoSeleccionado == -1) return;
		int tipo = tipoSeleccionado + 1; // Admin=1, Empleado=2, Contador=3

		Integer fkLocal = null;
		if (tipo == 2) { // Empleado tiene q tener local
			fkLocal = seleccionarLocal();
			if (fkLocal == null) {
				JOptionPane.showMessageDialog(null, "Hay que asignar un local para un Empleado");
				return;	}}

		boolean creado = UsuarioDAO.crearUsuario(dni, nombre, email, password, tipo, fkLocal);
		if (creado) {
			JOptionPane.showMessageDialog(null, "Usuario creado correctamente");}
	                             }
	
    
    private Integer seleccionarLocal() {
        LinkedList<Local> locales = LocalDAO.obtenerLocales();
        if (locales.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay locales cargados");
            return null; }

        String[] opciones = new String[locales.size()];
        for (int i = 0; i < locales.size(); i++) {
            opciones[i] = locales.get(i).getDireccion(); }

        int seleccionado = JOptionPane.showOptionDialog(null, "Seleccione el local", "Local",
                0, 0, null, opciones, opciones[0]);
        if (seleccionado == -1) return null;
        return locales.get(seleccionado).getIdLocal(); }
    

    // buscar usuario
    public void buscarUsuario() {
        Integer idUsuario = Validaciones.validarEntero("ID del usuario a buscar:");
        if (idUsuario == null) return;

        Usuario u = UsuarioDAO.buscarUsuarioPorId(idUsuario);
        if (u == null) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese ID");
            return; }

        JOptionPane.showMessageDialog(null,
                "ID: " + u.getIdUsuario() +
                "\nNombre: " + u.getNombre() +
                "\nDNI: " + u.getDni() +
                "\nEmail: " + u.getEmail() +
                "\nTipo: " + u.getTipoTexto() +
                "\nActivo: " + (u.getActivo() ? "Sí" : "No"));}
    

	// editar usuario
	public void editarUsuario() {
		Integer idUsuario = Validaciones.validarEntero("ID del usuario a editar:");
		if (idUsuario == null) return;

		Usuario u = UsuarioDAO.buscarUsuarioPorId(idUsuario);
		if (u == null) {
			JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese ID");
			return;}

		String nuevoNombre = Validaciones.validarLetras("Nombre y apellido:", u.getNombre());
		if (nuevoNombre == null) return;

		String nuevoEmail = Validaciones.validarEmail("Email (debe terminar en @biancatex.com):", u.getEmail());
		if (nuevoEmail == null) return;
		if (UsuarioDAO.existeEmail(nuevoEmail, idUsuario)) {
			JOptionPane.showMessageDialog(null, "Ya existe otro usuario con ese email");
			return;}
		String[] opcionesTipo = {"Admin", "Empleado", "Contador"};
		int tipoSeleccionado = JOptionPane.showOptionDialog(null,
				"Tipo actual: " + u.getTipoTexto() + "\nElegí el tipo de usuario:", "Editar usuario",
				0, 0, null, opcionesTipo, opcionesTipo[u.getTipoUsuario() - 1]);
		if (tipoSeleccionado == -1) return;

		int nuevoTipo = tipoSeleccionado + 1;
		Integer nuevoFkLocal = null;

		if (nuevoTipo == 2) {
			nuevoFkLocal = seleccionarLocal();
			if (nuevoFkLocal == null) {
				JOptionPane.showMessageDialog(null, "Hay que asignar un local para un Empleado");
				return;	}}
		boolean editado = UsuarioDAO.editarUsuario(idUsuario, u.getDni(), nuevoNombre, nuevoEmail, nuevoTipo, nuevoFkLocal);
		if (editado) {
			JOptionPane.showMessageDialog(null, "Usuario editado correctamente");}
	                                            }

    // desactivar usuario
    public void desactivarUsuario() {
        Integer idUsuario = Validaciones.validarEntero("ID del usuario a desactivar:");
        if (idUsuario == null) return;

        Usuario u = UsuarioDAO.buscarUsuarioPorId(idUsuario);
        if (u == null) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese ID");
            return;}

        // no puede desactivarse a sí mismo
        if (u.getIdUsuario() == this.getIdUsuario()) {
            JOptionPane.showMessageDialog(null, "No podés desactivar tu propio usuario");
            return;}

        // no se puede desactivar si ya esta desactivado
        if (!u.getActivo()) {
            JOptionPane.showMessageDialog(null, u.getNombre() + " ya está desactivado");
            return; }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Seguro que querés desactivar a " + u.getNombre() + "?",
                "Confirmar desactivación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean desactivado = UsuarioDAO.desactivarUsuario(u.getIdUsuario());
            if (desactivado) {
                JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente");  }}
                                            }

    // activar usuario
    public void activarUsuario() {
        Integer idUsuario = Validaciones.validarEntero("ID del usuario a activar:");
        if (idUsuario == null) return;

        Usuario u = UsuarioDAO.buscarUsuarioPorId(idUsuario);
        if (u == null) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese ID");
            return; }

        // no puede activarse a sí mismo
        if (u.getIdUsuario() == this.getIdUsuario()) {
            JOptionPane.showMessageDialog(null, "No podés activar tu propio usuario");
            return;}

        // no se puede activar uno que ya está activo
        if (u.getActivo()) {
            JOptionPane.showMessageDialog(null, u.getNombre() + " ya está activo");
            return; }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Seguro que querés activar a " + u.getNombre() + "?",
                "Confirmar activación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean activado = UsuarioDAO.activarUsuario(u.getIdUsuario());
            if (activado) {
                JOptionPane.showMessageDialog(null, "Usuario activado correctamente"); }  }}
    

    // ver usuarios
    public void verUsuarios() {
        String lista = UsuarioDAO.obtenerListaUsuarios();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios cargados");
            return;}
        JOptionPane.showMessageDialog(null, lista, "Usuarios", JOptionPane.PLAIN_MESSAGE);}
    
    
}
	
	
	
	
	
	
	

