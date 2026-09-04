import java.util.LinkedList;

import javax.swing.JOptionPane;

public abstract class Usuario implements Menu{

	private int idUsuario;
	private int tipoUsuario;
	private String dni;
	private String nombre;
	private String mail;
	private Boolean activo;
	private static LinkedList<Usuario> usuarios = new   LinkedList<Usuario>();
	


	public Usuario(int idUsuario, int tipoUsuario, String dni, String nombre, String email, Boolean activo) {
	    this.idUsuario = idUsuario;
	    this.tipoUsuario = tipoUsuario;
	    this.dni = dni;
	    this.nombre = nombre;
	    this.mail = email;
	    this.activo = activo;
	    usuarios.add(this);
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

	
	 

		public static Usuario login(String nombre) {
			for (Usuario usuario : usuarios) {
				if (usuario.getNombre().toLowerCase().equals(nombre.toLowerCase())) {
					return usuario;}}
			return null;}

	

	

	
	
	
	
	
	
	
}
	
	
	
	
	
	
	
	

