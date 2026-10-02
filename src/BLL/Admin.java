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
	            "Gestionar locales",
	            "Seccion compras",
	            "Seccion ventas",
	            "Cuenta corriente",
	            "Bajo stock",
	           
	            "Salir" };
	        
	        
	        
	        int seleccion;
			do {  seleccion = JOptionPane.showOptionDialog(null,  "Usuario: " + this.getNombre() + "  \n\n  Menú Administrador - Elija una opción",
	        		        "BiancaTex", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
	        	
	        switch (seleccion) {
            case 0: gestionarUsuarios(); break;
            case 1: gestionarProductos(); break;
            case 2: gestionarProveedores(); break;
            case 3: gestionarLocales();break;
            case 4: seccionCompras(); break;
            case 5: seccionVentas(); break;
            case 6: verCuentaCorriente(); break;
            case 7: Producto.verBajoStock(); break;
           }	
			} while (seleccion!=8);}
	        
	    
	    
	    
	    
	       //funcion de crud para usuarios
	    public void gestionarUsuarios() {
	        String[] opcionesUsuarios = {"Crear", "Buscar", "Editar", "Desactivar","Ver usuarios" ,  "Volver"};
	        int seleccionUsuarios;

	        do {  seleccionUsuarios = JOptionPane.showOptionDialog(null, "Crud usuarios - Elija una opción", "Usuarios",
	                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	                    null, opcionesUsuarios, opcionesUsuarios[0]);

	            switch (seleccionUsuarios) {
	                case 0: crearUsuario(); break;
	                case 1: buscarUsuario(); break;
	                case 2: editarUsuario(); break;
	                case 3: desactivarUsuario(); break;
	                case 4: verUsuarios();break;}

	        } while (seleccionUsuarios != 5  && seleccionUsuarios != -1);}
	    
	        

	        public void gestionarProductos() {
	            String[] opcionesProductos = {"Crear", "Buscar", "Editar", "Eliminar","Ver productos",  "Volver"};
		        int seleccionProductos;

		        do {  seleccionProductos = JOptionPane.showOptionDialog(null, "Crud productos - Elija una opción", "Productos",
		                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		                    null, opcionesProductos, opcionesProductos[0]);

		            switch (seleccionProductos) {
		                case 0: Producto.crearProducto(); break;
		                case 1: Producto.buscarProductos(); break;
		                case 2: Producto.editarProducto(); break;
		                case 3: Producto.eliminarProducto(); break;
		                case 4: Producto.verProductos();}

		        } while (seleccionProductos != 5 && seleccionProductos != -1);}
	        	
	        public void gestionarProveedores() {
	        	  String[] opcionesProveedores= {"Crear", "Buscar", "Editar", "Eliminar", "Ver proveedores" , "Volver"};
			        int seleccionProveedores;

			        do {  seleccionProveedores = JOptionPane.showOptionDialog(null, "Crud proveedores - Elija una opción", "Proveedores",
			                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
			                    null, opcionesProveedores, opcionesProveedores[0]);

			            switch (seleccionProveedores) {
			                case 0: Proveedor.crearProveedor(); break;
			                case 1: Proveedor.buscarProveedor(); break;
			                case 2: Proveedor.editarProveedor(); break;
			                case 3: Proveedor.eliminarProveedor(); break;
			                case 4: Proveedor.verProveedores();break;
			                 }

			        } while (seleccionProveedores != 5 && seleccionProveedores != -1);}
	        
	        
	      //funcion de crud para locales
		    public void gestionarLocales() {
		        String[] opcionesUsuarios = {"Crear", "Buscar", "Editar", "Eliminar","Ver locales" ,  "Volver"};
		        int seleccionUsuarios;

		        do {  seleccionUsuarios = JOptionPane.showOptionDialog(null, "Crud locales - Elija una opción", "Locales",
		                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		                    null, opcionesUsuarios, opcionesUsuarios[0]);

		            switch (seleccionUsuarios) {
		                case 0: Local.crearLocal(); break;
		                case 1: Local.buscarLocal(); break;
		                case 2: Local.editarLocal(); break;
		                case 3: Local.eliminarLocal(); break;
		                case 4: Local.verLocales();break;}

		        } while (seleccionUsuarios != 5  && seleccionUsuarios != -1);}
	        
	        
		    public void seccionCompras() {
		    	String[] opcionesCompras = {"Registrar compra" , "Buscar" , "Eliminar" , "Ver compras", "Volver"};
		        int seleccionCompras;

		        do {  seleccionCompras = JOptionPane.showOptionDialog(null, "Seccion compras - Elija una opción", "Compras",
		                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		                    null, opcionesCompras, opcionesCompras[0]);

		            switch (seleccionCompras) {
		                case 0: Compra.registrarCompra(this); break;
		                case 1: Compra.buscarCompra(); break;
		                case 2: Compra.eliminarCompra(); break;
		                case 3: Compra.verCompras();}

		        } while (seleccionCompras != 4 && seleccionCompras != -1);}
		    
		    public void seccionVentas() {
		        String[] opcionesVentas = {"Registrar venta", "Buscar", "Eliminar", "Ver ventas", "Volver"};
		        int seleccionVentas;

		        do {
		            seleccionVentas = JOptionPane.showOptionDialog(null, "Seccion ventas - Elija una opción", "Ventas",
		                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		                    null, opcionesVentas, opcionesVentas[0]);

		            switch (seleccionVentas) {
		                case 0: Venta.registrarVenta(this); break;
		                case 1: Venta.buscarVenta(); break;
		                case 2: Venta.eliminarVenta(); break;
		                case 3: Venta.verVentas(); break;
		            }

		        } while (seleccionVentas != 4 && seleccionVentas != -1); }
		    	
		    
	
	     
	        public void verCuentaCorriente() {}
	   
	        
	        
	    }

	       
	
	
	
