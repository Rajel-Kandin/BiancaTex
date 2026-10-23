package BLL;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import DDL.ControllerProducto;
import DDL.ControllerVenta;
import repository.Validaciones;

public class Venta extends Movimiento {

    private int idVenta;
    private Double descuento;
    private Double subtotal;
    private Double total;
    private Usuario usuario;
    private String nombreCliente;   
    private String telefonoCliente; 

    public Venta(int idMovimiento, LocalDate fecha, Double monto, int idVenta, Double descuento, Double subtotal,
            Double total, Usuario usuario) {
        super(idMovimiento, fecha, monto);
        this.idVenta = idVenta;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.total = total;
        this.usuario = usuario;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    public String getNombreCliente() {
        return nombreCliente;
    }

    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

   
    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    
    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String VerInfo() {
        return "Venta [idVenta=" + idVenta + ", descuento=" + descuento + ", subtotal=" + subtotal + ", total=" + total  + ", usuario=" + usuario + "]";
    }

    private static Producto seleccionarProducto() {
        LinkedList<Producto> productos = ControllerProducto.obtenerProductos();

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos cargados. Se cancela la venta");
            return null;
        }

        String[] opciones = new String[productos.size()];
        for (int i = 0; i < productos.size(); i++) {
            opciones[i] = productos.get(i).getNombre() + " (Talle: " + productos.get(i).getTalle()
                    + " - Precio: $" + String.format("%.2f", productos.get(i).getPrecio()) + ")";}
        

        String seleccion = (String) JOptionPane.showInputDialog(
                null, "Seleccioná el producto:", "Producto", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == null) return null;

        int index = Arrays.asList(opciones).indexOf(seleccion);
        return productos.get(index); }

    
    
    public static void registrarVenta(Usuario usuario) {
        String nombreCliente = JOptionPane.showInputDialog("Nombre del cliente:");
        while (nombreCliente != null && nombreCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
            nombreCliente = JOptionPane.showInputDialog("Nombre del cliente:"); }
        if (nombreCliente == null) {
            JOptionPane.showMessageDialog(null, "Venta cancelada.");
            return; }

        String telefonoCliente = Validaciones.validarNumeros("Teléfono del cliente:");
        if (telefonoCliente == null) {
            JOptionPane.showMessageDialog(null, "Venta cancelada.");
            return; }

        nombreCliente = nombreCliente.trim();
        telefonoCliente = telefonoCliente.trim();

        LinkedList<DetalleVenta> detalles = new LinkedList<DetalleVenta>();
        double subtotal = 0;
        boolean agregando = true;

        // agregar productos
        while (agregando) {
            Producto producto = seleccionarProducto();

            if (producto == null) {
                agregando = false;
            } else {
                // me fijo si el producto ya estaba cargado en esta venta
                DetalleVenta yaCargado = null;
                for (DetalleVenta d : detalles) {
                    if (d.getProducto().getIdProducto().equals(producto.getIdProducto())) {
                        yaCargado = d; } }

                int cantidadCargada = 0;
                if (yaCargado != null) {
                    cantidadCargada = yaCargado.getCantidad(); }

                int disponible = producto.getStockActual() - cantidadCargada;

                if (disponible <= 0) {
                    JOptionPane.showMessageDialog(null, "No hay stock disponible de ese producto.");
                } else {
                    Integer cantidad = Validaciones.validarEntero("Cantidad (disponible: " + disponible + "):");
                    while (cantidad != null && cantidad > disponible) {
                        JOptionPane.showMessageDialog(null, "No hay stock suficiente. Disponible: " + disponible);
                        cantidad = Validaciones.validarEntero("Cantidad (disponible: " + disponible + "):"); }

                    if (cantidad != null) {
                        if (yaCargado != null) {
                            // si ya estaba, sumo la cantidad y recalculo su subtotal
                            yaCargado.setCantidad(cantidadCargada + cantidad);
                            yaCargado.setSubtotal(yaCargado.getPrecioUnitario() * yaCargado.getCantidad());
                        } else {
                            detalles.add(new DetalleVenta(0, cantidad, producto.getPrecio(),
                                    producto.getPrecio() * cantidad, producto)); }

                        // calcula subtotal
                        subtotal += producto.getPrecio() * cantidad;

                        int otro = JOptionPane.showConfirmDialog(null,
                                "Producto agregado.\nSubtotal actual: $" + String.format("%.2f", subtotal) +
                                "\n\n¿Agregar otro producto?", "Venta", JOptionPane.YES_NO_OPTION);
                        if (otro != JOptionPane.YES_OPTION) {
                            agregando = false; } }
                } } }

        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se agregó ningún producto. Venta cancelada.");
            return; }

        // pregunta descuento
        double descuento = 0;
        int resp = JOptionPane.showConfirmDialog(null,
                "Subtotal: $" + String.format("%.2f", subtotal) + "\n¿Desea aplicar un descuento sobre el total?",
                "Descuento", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            Double porcentaje = Validaciones.validarDouble("Ingrese el porcentaje de descuento (1 a 100):");
            while (porcentaje != null && porcentaje > 100) {
                JOptionPane.showMessageDialog(null, "El descuento no puede ser mayor a 100%.");
                porcentaje = Validaciones.validarDouble("Ingrese el porcentaje de descuento (1 a 100):"); }

            if (porcentaje == null) {
                JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return; }
            descuento = porcentaje; }

        // calcula total final
        double total = subtotal - (subtotal * descuento / 100);
        int confirmar = JOptionPane.showConfirmDialog(null,
                "Cliente: " + nombreCliente +
                "\n Subtotal: $" + String.format("%.2f", subtotal) +
                "\n Descuento: " + descuento + "%" +
                "\n TOTAL: $" + String.format("%.2f", total) +
                "\n\n ¿Confirmar venta?", "Confirmar venta", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Venta cancelada.");
            return; }

        // guarda la venta
        int idVenta = ControllerVenta.guardarVenta(usuario.getIdUsuario(), descuento, subtotal, total,
                nombreCliente, telefonoCliente, detalles);

        if (idVenta > 0) {
            JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");

            // genera el ticket
            Ticket.generarTxt(idVenta, usuario.getNombre(), nombreCliente, telefonoCliente,
                    detalles, subtotal, descuento, total);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta. No se guardó nada."); }
                               }
    
    

    
    //busco las ventas
    public static void buscarVenta() {
        Integer idVenta = Validaciones.validarEntero("Ingrese el ID de la venta a buscar:");
        if (idVenta == null) return;
        String info = ControllerVenta.obtenerVenta(idVenta);

        if (info == null) {
            JOptionPane.showMessageDialog(null, "No existe una venta con ese ID.");
        } else {
            JOptionPane.showMessageDialog(null, info);  }}
    
    
 
    //elimina la venta y su ticket
    public static void eliminarVenta() {
        Integer idVenta = Validaciones.validarEntero("Ingrese el ID de la venta a eliminar:");
        if (idVenta == null) return;

        String info = ControllerVenta.obtenerVenta(idVenta);

        if (info == null) {
            JOptionPane.showMessageDialog(null, "No existe una venta con ese ID.");
            return;  }

        int resp = JOptionPane.showConfirmDialog(null,
                info + "\n\nSe devolverá el stock de los productos.\n¿Eliminar esta venta?",
                "Eliminar venta", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            if (ControllerVenta.eliminarVenta(idVenta)) {

                // borro el ticket de esa venta para que no quede como válido
                File ticket = new File("tickets", "ticket_" + idVenta + ".txt");
                if (ticket.exists()) {
                    ticket.delete(); }

                JOptionPane.showMessageDialog(null, "Venta eliminada, stock devuelto y ticket borrado.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la venta. No se modificó nada.");}}}
     
    

    //ver ventas
    public static void verVentas() {
        JOptionPane.showMessageDialog(null, ControllerVenta.listarVentas()); }
    
    
    //ver tickets 
    public static void verTickets() {
        File carpeta = new File("tickets");

        if (!carpeta.exists() || carpeta.listFiles() == null) {
            JOptionPane.showMessageDialog(null, "No hay tickets generados.");
            return;}

        LinkedList<String> nombres = new LinkedList<>();
        for (File archivo : carpeta.listFiles()) {
            if (archivo.getName().endsWith(".txt")) {
                nombres.add(archivo.getName());  } }

        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tickets generados.");
            return;}

        String[] opciones = nombres.toArray(new String[0]);
        String elegido = (String) JOptionPane.showInputDialog(null, "Seleccione un ticket:",
                "Tickets", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (elegido == null) {
            return;  }

        String contenido = "";
        try {
            Scanner lector = new Scanner(new File(carpeta, elegido));
            while (lector.hasNextLine()) {
                contenido += lector.nextLine() + "\n";
            }
            lector.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el ticket.");
            return; }

        JOptionPane.showMessageDialog(null, contenido, elegido, JOptionPane.INFORMATION_MESSAGE); }
    
    
    //obtener nombres de los tickets
    public static LinkedList<String> obtenerTickets() {
        LinkedList<String> nombres = new LinkedList<>();
        File carpeta = new File("tickets");

        if (carpeta.exists() && carpeta.listFiles() != null) {
            for (File archivo : carpeta.listFiles()) {
                if (archivo.getName().endsWith(".txt")) {
                    nombres.add(archivo.getName()); } } }
        return nombres; }

    
    //leer el contenido de un ticket
    public static String leerTicket(String nombreArchivo) {
        String contenido = "";
        try {
            Scanner lector = new Scanner(new File("tickets", nombreArchivo));
            while (lector.hasNextLine()) {
                contenido += lector.nextLine() + "\n"; }
            lector.close();
             } catch (Exception e) {
            contenido = "No se pudo abrir el ticket.";}
            return contenido; }
    
    
             }