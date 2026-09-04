import javax.swing.JOptionPane;

public class Empleado  extends  Usuario{

	private Local local;
	 

	 public Empleado(int idUsuario, String dni, String nombre, String email, Boolean activo,
			Local local) {
		super(idUsuario, 2 , dni, nombre, email, activo);
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
	        String[] opciones = {
	            "Registrar venta",
	            "Ver stock de productos",
	            "Bajo stock",
	            "Salir" };
	        
	        
	        
	        int seleccion;
			do { 
	        	 seleccion = JOptionPane.showOptionDialog( null,  "Menú Administrador - Elija una opción",  "BiancaTex",  JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
		                null, opciones, opciones[0]);
	        	
	        switch (seleccion) {
         case 0: registrarVenta(); break;
         case 1: VerStockProductos(); break;
         case 2: verBajoStock(); break;}
			} while (seleccion!=3);}
	        
	       
	   
	        public void registrarVenta() {}
	        public void VerStockProductos() {}
	        public void verBajoStock() {}
	
	
}
