import javax.swing.JOptionPane;

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
            case 6: verBajoStock(); break;}	
			} while (seleccion!=7);}
	        
	       
	        public void gestionarUsuarios() {}
	        public void gestionarProductos() {}
	        public void registrarVenta() {}
	        public void registrarCompra() {}
	        public void verCuentaCorriente() {}
	        public void verBajoStock() {}
	        public void gestionarProveedores() {}
	        
	    }

	       
	
	
	
