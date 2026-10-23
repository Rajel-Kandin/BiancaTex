package BLL;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Ticket {

	// guardo en la carpeta tickets
	private static final String CARPETA = "tickets";

	public static void generarTxt(int idVenta, String vendedor, String nombreCliente, String telefonoCliente,
			LinkedList<DetalleVenta> detalles, double subtotal, double descuento, double total) {

		// 1) Creamos la carpeta si no existe
		File carpeta = new File(CARPETA);
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}

		// 2) Armamos el archivo
		File archivo = new File(carpeta, "ticket_" + idVenta + ".txt");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// 3) try-with-resources: el archivo se cierra solo al terminar
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {

			writer.write("==============================================");
			writer.newLine();
			writer.write("           BIANCATEX - TICKET N° " + idVenta);
			writer.newLine();
			writer.write("==============================================");
			writer.newLine();
			writer.write("Fecha:    " + LocalDate.now().format(formato));
			writer.newLine();
			writer.write("Registrada por:  " + vendedor);
			writer.newLine();
			writer.newLine();
			writer.write("DATOS DEL CLIENTE");
			writer.newLine();
			writer.write("Nombre:   " + nombreCliente);
			writer.newLine();
			writer.write("Teléfono: " + telefonoCliente);
			writer.newLine();
			writer.newLine();
			writer.write("----------------------------------------------");
			writer.newLine();
			writer.write(String.format("%-20s %5s %9s %9s", "Producto  ", "Cant  ", "P.Unit  ", "  Subtotal"));
			writer.newLine();
			writer.write("----------------------------------------------");
			writer.newLine();

			// 4) Una línea por cada producto vendido
			for (DetalleVenta d : detalles) {
				String nombre = d.getProducto().getNombre() + " T:" + d.getProducto().getTalle();
				if (nombre.length() > 20) {
					nombre = nombre.substring(0, 20);
				}
				writer.write(String.format("%-20s %5d %9.2f %9.2f",
						nombre, d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()));
				writer.newLine();
			}

			writer.write("----------------------------------------------");
			writer.newLine();
			writer.write(String.format("%-30s %15.2f", "Subtotal $", subtotal));
			writer.newLine();
			if (descuento > 0) {
				writer.write(String.format("%-30s %15.2f", "Descuento " + descuento + "% $", -(subtotal * descuento / 100)));
				writer.newLine();
			}
			writer.write(String.format("%-30s %15.2f", "TOTAL $", total));
			writer.newLine();
			writer.write("==============================================");
			writer.newLine();
			writer.write("         ¡Gracias por su compra!");
			writer.newLine();

			JOptionPane.showMessageDialog(null, "Ticket generado en:\n" + archivo.getAbsolutePath());

			// 5) Abrimos el archivo con el programa por defecto (Bloc de notas)
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(archivo);}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No se pudo generar el ticket");
			e.printStackTrace();}}
	
	
	
	                             }