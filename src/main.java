
import java.awt.Image;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

	public class main {

		public static void main(String[] args) {
			
			
			String[] menuIngreso= {"Ingresar" ,  "Salir" };	
			String nombreIngresado;
			int elegidoIngreso;
			
			cargarUsuarios();
			do {
				 //JOptionPane.showMessageDialog(null, "" , "", JOptionPane.INFORMATION_MESSAGE,(RedimensionarImg("BiancaTex.png" , 800 , 400)));
				elegidoIngreso=JOptionPane.showOptionDialog(null, "Bienvenido al sistema de BiancaTex"   ,  "Ingreso", 0, 0, null, menuIngreso, menuIngreso[0]);
		    switch (elegidoIngreso) {
			    
		    case 0:
		        nombreIngresado = JOptionPane.showInputDialog("Ingrese su usuario:");
		        if (nombreIngresado == null) { break; }

		        Usuario usuarioLogueado = Usuario.login(nombreIngresado);
		        if (usuarioLogueado != null) {
		            usuarioLogueado.verMenu();
		           
		        } else {
		            JOptionPane.showMessageDialog(null, "Usuario no encontrado");}
		        
		   break;}
		        
		             }while(elegidoIngreso!=1);}
		    
			
		
		
		
		
		private static LinkedList<Usuario> usuarios = new LinkedList<Usuario>();

		public static void cargarUsuarios() {
			usuarios.add(new Admin(1, "25369741", "salomon jakim","salomonjakim@biancatex.com", true));
			usuarios.add(new Admin(2 , "36941257", "jose rossi","josirossi@biancatex.com", true));

			usuarios.add(new Empleado(3, "28741663", "marcelo gonzalez", "marcelogonzalez@biancatex.com", true, null));
			usuarios.add(new Empleado(4, "37961358", "martin romero", "martinromero@biancatex.com", true, null));
			usuarios.add(new Empleado(5, "41339114", "daniel gomez", "danielgomez@biancatex.com", true, null));
			
			usuarios.add(new Contador(6, "31677822", "Ferran diaz","ferrandiaz@biancatex.com", true));}
		
		
	
	
		//funcion para redimensionar img 
		 public static ImageIcon RedimensionarImg(String nombreImg , int ancho , int alto) {
			 ImageIcon imagen = new ImageIcon (main.class.getResource(nombreImg));
			 Image recortado =  imagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
			 imagen = new ImageIcon (recortado);
			 return imagen; }
			
	
		
		}

