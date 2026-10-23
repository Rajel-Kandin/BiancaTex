package BLL;
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
	        String[] opciones = { "Seccion ventas",  "Ver productos",  "Bajo stock",  "Salir" };
	        
	        int seleccion;
			do { 
				 seleccion = JOptionPane.showOptionDialog(null,  "Usuario: " + this.getNombre() + "\n  \n\n  Menú Empleado - Elija una opción",
			                "BiancaTex", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
	        	
	        	
	        switch (seleccion) {
         case 0: seccionVentas(); break;
         case 1: Producto.verProductos(); break;
         case 2:Producto.verBajoStock(); break;}
			} while (seleccion != 3 && seleccion != -1);}
	        
	       
	 public void seccionVentas() {
	        String[] opcionesVentas = {"Registrar venta", "Buscar",  "Ver ventas", "Volver"};
	        int seleccionVentas;

	        do {
	            seleccionVentas = JOptionPane.showOptionDialog(null, "Seccion ventas - Elija una opción", "Ventas",
	                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	                    null, opcionesVentas, opcionesVentas[0]);

	            switch (seleccionVentas) {
	                case 0: Venta.registrarVenta(this); break;
	                case 1: Venta.buscarVenta(); break;
	                case 2: Venta.verVentas(); break;
	            }

	        } while (seleccionVentas != 3 && seleccionVentas != -1); }
	    	
	        
	
	
}
