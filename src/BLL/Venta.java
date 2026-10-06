package BLL;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
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

    public String VerInfo() {
        return "Venta [idVenta=" + idVenta + ", descuento=" + descuento + ", subtotal=" + subtotal + ", total=" + total
                + ", usuario=" + usuario + "]";
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
                    + " - Precio: $" + String.format("%.2f", productos.get(i).getPrecio()) + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                null, "Seleccioná el producto:", "Producto", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == null) return null;

        int index = Arrays.asList(opciones).indexOf(seleccion);
        return productos.get(index);
    }

    public static void registrarVenta(Usuario usuario) {
        LinkedList<Integer> ids = new LinkedList<Integer>();
        LinkedList<Integer> cantidades = new LinkedList<Integer>();
        LinkedList<Double> precios = new LinkedList<Double>();
        double subtotal = 0;
        boolean agregando = true;

        // agregar productos
        while (agregando) {
            Producto producto = seleccionarProducto();

            if (producto == null) {
                agregando = false;
            } else {
                int idProd = Integer.parseInt(String.valueOf(producto.getIdProducto()));
                double precio = producto.getPrecio();

                int pos = ids.indexOf(idProd);
                int yaCargado = 0;
                if (pos != -1) {
                    yaCargado = cantidades.get(pos);
                }
                int disponible = producto.getStockActual() - yaCargado;

                if (disponible <= 0) {
                    JOptionPane.showMessageDialog(null, "No hay stock disponible de ese producto.");
                } else {
                    
                    Integer cantidad = Validaciones.validarEntero("Cantidad (disponible: " + disponible + "):");
                    while (cantidad != null && cantidad > disponible) {
                        JOptionPane.showMessageDialog(null, "No hay stock suficiente. Disponible: " + disponible);
                        cantidad = Validaciones.validarEntero("Cantidad (disponible: " + disponible + "):");
                    }

                    if (cantidad != null) {
                        if (pos != -1) {
                            cantidades.set(pos, yaCargado + cantidad);
                        } else {
                            ids.add(idProd);
                            cantidades.add(cantidad);
                            precios.add(precio);
                        }

                        // calcula subtotal
                        subtotal += precio * cantidad;

                        int otro = JOptionPane.showConfirmDialog(null,
                                "Producto agregado.\nSubtotal actual: $" + String.format("%.2f", subtotal) +
                                "\n\n¿Agregar otro producto?", "Venta", JOptionPane.YES_NO_OPTION);
                        if (otro != JOptionPane.YES_OPTION) {
                            agregando = false;
                        }
                    }
                }
            }
        }

        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se agregó ningún producto. Venta cancelada.");
            return;
        }

        // pregunta descuento
        double descuento = 0;
        int resp = JOptionPane.showConfirmDialog(null,
                "Subtotal: $" + String.format("%.2f", subtotal) + "\n¿Desea aplicar un descuento sobre el total?",
                "Descuento", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            
            Double porcentaje = Validaciones.validarDouble("Ingrese el porcentaje de descuento (1 a 100):");
            while (porcentaje != null && porcentaje > 100) {
                JOptionPane.showMessageDialog(null, "El descuento no puede ser mayor a 100%.");
                porcentaje = Validaciones.validarDouble("Ingrese el porcentaje de descuento (1 a 100):");
            }

            if (porcentaje == null) {
                JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return;
            }
            descuento = porcentaje;
        }

        // calcula total final
        double total = subtotal - (subtotal * descuento / 100);

        int confirmar = JOptionPane.showConfirmDialog(null,
                "Subtotal: $" + String.format("%.2f", subtotal) +
                "\nDescuento: " + descuento + "%" +
                "\nTOTAL: $" + String.format("%.2f", total) +
                "\n\n¿Confirmar venta?", "Confirmar venta", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Venta cancelada.");
            return;
        }

        // guarda la venta
        boolean ok = ControllerVenta.guardarVenta(usuario.getIdUsuario(), descuento, subtotal, total, ids, cantidades, precios);

        if (ok) {
            JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta. No se guardó nada.");
        }
    }

    public static void buscarVenta() {
       
        Integer idVenta = Validaciones.validarEntero("Ingrese el ID de la venta a buscar:");
        if (idVenta == null) return;

        String info = ControllerVenta.obtenerVenta(idVenta);

        if (info == null) {
            JOptionPane.showMessageDialog(null, "No existe una venta con ese ID.");
        } else {
            JOptionPane.showMessageDialog(null, info);
        }
    }

    public static void eliminarVenta() {
        
        Integer idVenta = Validaciones.validarEntero("Ingrese el ID de la venta a eliminar:");
        if (idVenta == null) return;

        String info = ControllerVenta.obtenerVenta(idVenta);

        if (info == null) {
            JOptionPane.showMessageDialog(null, "No existe una venta con ese ID.");
            return;
        }

        int resp = JOptionPane.showConfirmDialog(null,
                info + "\n\nSe devolverá el stock de los productos.\n¿Eliminar esta venta?",
                "Eliminar venta", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            if (ControllerVenta.eliminarVenta(idVenta)) {
                JOptionPane.showMessageDialog(null, "Venta eliminada y stock devuelto.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la venta. No se modificó nada.");
            }
        }
    }

    public static void verVentas() {
        JOptionPane.showMessageDialog(null, ControllerVenta.listarVentas());
    }
}