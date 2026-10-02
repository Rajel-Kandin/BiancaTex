package BLL;
import javax.swing.JOptionPane;

public class Contador extends Usuario {

	
	public Contador(int idUsuario, String dni, String nombre, String email, Boolean activo) {
	    super(idUsuario, 3, dni, nombre, email, activo);}
	 
	
	@Override
    public void verMenu() {
        String[] opciones = {
            "Ver cuenta corriente",
            "Salir" };
        
        
        
        int seleccion;
		do { 
			  seleccion = JOptionPane.showOptionDialog(null,   "Usuario: " + this.getNombre() + " \n\n Menú Contador - Elija una opción",
		                "BiancaTex", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,  null, opciones, opciones[0]);

        	
        switch (seleccion) {
        case 0: verCuentaCorriente(); break;}	
		} while (seleccion!=1);}
        
       
        
        public void verCuentaCorriente() {}
       
        
    }
	
	
	
