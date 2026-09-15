package BLL;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import DDL.ControllerLocal;
import DDL.ControllerUsuario;

public class Admin extends Usuario {

	
	public Admin(int idUsuario, String dni, String nombre, String email, Boolean activo) {
	    super(idUsuario, 1, dni, nombre, email, activo);}


	    @Override
	    public void verMenu() {
	        String[] opciones = {
	            "Gestionar usuarios",
	            "Gestionar productos",
	            "Gestionar proveedores",
	            "Registrar venta",
	            "Registrar compra",
	            "Cuenta corriente",
	            "Bajo stock",
	            "Ver locales",
	            "Salir" };
	        
	        
	        
	        int seleccion;
			do { 
	        	 seleccion = JOptionPane.showOptionDialog( null,  "Menú Administrador - Elija una opción",  "BiancaTex",  JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
		                null, opciones, opciones[0]);
	        	
	        switch (seleccion) {
            case 0: gestionarUsuarios(); break;
            case 1: gestionarProductos(); break;
            case 2: gestionarProveedores(); break;
            case 3: registrarVenta(); break;
            case 4: registrarCompra(); break;
            case 5: verCuentaCorriente(); break;
            case 6: verBajoStock(); break;
            case 7: obtenerLocal();}	
			} while (seleccion!=8);}
	        
	    
	    
	    public void crearUsuario() {
	        String dni = JOptionPane.showInputDialog("DNI del nuevo usuario:");
	        if (dni == null) return;
	        String nombre = JOptionPane.showInputDialog("Nombre y apellido:");
	        if (nombre == null) return;
	        String email;
	        do { email = JOptionPane.showInputDialog("Email:");
	            if (email == null) return;
	            if (!email.endsWith("@biancatex.com")) {
	                JOptionPane.showMessageDialog(null, "El email debe terminar en @biancatex.com");
	                email = null; } } while (email == null);
	        String[] opcionesTipo = {"Admin", "Empleado", "Contador"};
	        int tipoSeleccionado = JOptionPane.showOptionDialog(null, "Tipo de usuario", "Crear usuario",
	                0, 0, null, opcionesTipo, opcionesTipo[0]);
	        if (tipoSeleccionado == -1) return;
	        int tipo = tipoSeleccionado + 1; // Admin=1, Empleado=2, Contador=3
	        Integer fkLocal = null;
	        if (tipo == 2) { // Empleado necesita local
	            fkLocal = seleccionarLocal();
	            if (fkLocal == null) {
	                JOptionPane.showMessageDialog(null, "Hay que asignar un local para un Empleado");
	                return;  } }

	        boolean creado = ControllerUsuario.crearUsuario(dni, nombre, email, tipo, fkLocal);
	        if (creado) {
	            JOptionPane.showMessageDialog(null, "Usuario creado correctamente");
	                                    }}

	    private Integer seleccionarLocal() {
	        LinkedList<Local> locales = ControllerLocal.obtenerLocales();
	        if (locales.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "No hay locales cargados");
	            return null;}

	        String[] opciones = new String[locales.size()];
	        for (int i = 0; i < locales.size(); i++) {
	            opciones[i] = locales.get(i).getDireccion(); }

	        int seleccionado = JOptionPane.showOptionDialog(null, "Seleccione el local", "Local",
	                0, 0, null, opciones, opciones[0]);
	        if (seleccionado == -1) return null;

	        return locales.get(seleccionado).getIdLocal();}
	    
	   //buscar usuario
	    public void buscarUsuario() {
	    	   String dni = JOptionPane.showInputDialog("DNI del usuario a buscar:");
	    	    if (dni == null) return;

	    	    Object[] datos = ControllerUsuario.buscarUsuarioPorDni(dni);
	    	    if (datos == null) {
	    	        JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese DNI");
	    	        return;
	    	    }

	    	    String tipoTexto;
	    	    switch ((int) datos[4]) {
	    	        case 1: tipoTexto = "Admin"; break;
	    	        case 2: tipoTexto = "Empleado"; break;
	    	        case 3: tipoTexto = "Contador"; break;
	    	        default: tipoTexto = "Desconocido";
	    	    }

	    	    JOptionPane.showMessageDialog(null,   "Nombre: " + datos[2] + "\n Email: " + datos[3] +  "\n Tipo: " + tipoTexto +  "\n Activo: " + datos[6]);
	    }
	    
	    
	    //editar usuario
	    public void editarUsuario() {
	        String dni = JOptionPane.showInputDialog("DNI del usuario a editar:");
	        if (dni == null) return;

	        Object[] datos = ControllerUsuario.buscarUsuarioPorDni(dni);
	        if (datos == null) {
	            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese DNI");
	            return;
	        }

	        int idUsuario = (int) datos[0];
	        String nuevoNombre = JOptionPane.showInputDialog("Nombre y apellido:", datos[2]);
	        if (nuevoNombre == null) return;
	        String nuevoEmail;
	        do {
	            nuevoEmail = JOptionPane.showInputDialog("Email (debe terminar en @biancatex.com):", datos[3]);
	            if (nuevoEmail == null) return;
	            if (!nuevoEmail.endsWith("@biancatex.com")) {
	                JOptionPane.showMessageDialog(null, "El email debe terminar en @biancatex.com");
	                nuevoEmail = null;
	            }
	        } while (nuevoEmail == null);

	        String[] opcionesTipo = {"Admin", "Empleado", "Contador"};
	        int tipoSeleccionado = JOptionPane.showOptionDialog(null, "Nuevo tipo de usuario", "Editar usuario",
	                0, 0, null, opcionesTipo, opcionesTipo[(int) datos[4] - 1]);
	        if (tipoSeleccionado == -1) return;

	        int nuevoTipo = tipoSeleccionado + 1;
	        Integer nuevoFkLocal = null;

	        if (nuevoTipo == 2) {
	            nuevoFkLocal = seleccionarLocal();
	            if (nuevoFkLocal == null) {
	                JOptionPane.showMessageDialog(null, "Hay que asignar un local para un Empleado");
	                return;}}

	        boolean editado = ControllerUsuario.editarUsuario(idUsuario, dni, nuevoNombre, nuevoEmail, nuevoTipo, nuevoFkLocal);
	        if (editado) {
	            JOptionPane.showMessageDialog(null, "Usuario editado correctamente");}}

	    
	    
	    //elimina // desactiva usuario
	    public void eliminarUsuario() {
	        String dni = JOptionPane.showInputDialog("DNI del usuario a eliminar:");
	        if (dni == null) return;

	        Object[] datos = ControllerUsuario.buscarUsuarioPorDni(dni);
	        if (datos == null) {
	            JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese DNI");
	            return;}

	        int idUsuario = (int) datos[0];

	        int confirmacion = JOptionPane.showConfirmDialog(null,
	                "¿Seguro que querés eliminar (desactivar) a " + datos[2] + "?",
	                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	        if (confirmacion == JOptionPane.YES_OPTION) {
	            boolean eliminado = ControllerUsuario.eliminarUsuario(idUsuario);
	            if (eliminado) {
	                JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente");
	            } }}
	    
	    
	       //funcion de crud para usuarios
	    public void gestionarUsuarios() {
	        String[] opcionesUsuarios = {"Crear", "Buscar", "Editar", "Eliminar", "Volver"};
	        int seleccionUsuarios;

	        do {  seleccionUsuarios = JOptionPane.showOptionDialog(null, "Crud usuarios - Elija una opción", "Usuarios",
	                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	                    null, opcionesUsuarios, opcionesUsuarios[0]);

	            switch (seleccionUsuarios) {
	                case 0: crearUsuario(); break;
	                case 1: buscarUsuario(); break;
	                case 2: editarUsuario(); break;
	                case 3: eliminarUsuario(); break;}

	        } while (seleccionUsuarios != 4 && seleccionUsuarios != -1);}
	  
	        public void obtenerLocal(){
	        	LinkedList<Local> locales = ControllerLocal.obtenerLocales();
	        	if (locales.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "No hay locales cargados");}

		        String[] opciones = new String[locales.size()];
		        for (int i = 0; i < locales.size(); i++) {
		            opciones[i] = locales.get(i).getDireccion(); }
		         JOptionPane.showOptionDialog(null, "Locales ", "Local",
		                0, 0, null, opciones, opciones[0]);}

	        public void gestionarProductos() {}
	        public void registrarVenta() {}
	        public void registrarCompra() {}
	        public void verCuentaCorriente() {}
	        public void verBajoStock() {}
	        public void gestionarProveedores() {}
	        
	    }

	       
	
	
	
