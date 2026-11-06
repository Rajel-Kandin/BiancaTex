package BLL;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DAO.ProductoDAO;
import repository.Validaciones;

public class Producto {

	private String IdProducto;
	private String nombre;
	private Double precio;
	private int stockActual;
	private  CategoriasProd categoria;
	private String talle;


	public Producto(String idProducto, String nombre, Double precio, int stockActual, CategoriasProd categoria,
			String talle) {
		super();
		IdProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.stockActual = stockActual;
		this.categoria = categoria;
		this.talle = talle;
	}




	public String getIdProducto() {
		return IdProducto;
	}


	public void setIdProducto(String idProducto) {
		IdProducto = idProducto;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public int getStockActual() {
		return stockActual;
	}


	public void setStockActual(int stockActual) {
		this.stockActual = stockActual;
	}
	


	public CategoriasProd getCategoria() {
		return categoria;
	}


	public void setCategoria(CategoriasProd categoria) {
		this.categoria = categoria;
	}

	
	
	
	public String getTalle() {
		return talle;
	}


	public void setTalle(String talle) {
		this.talle = talle;
	}
	
	


	@Override
	public String toString() {
		return "Producto [IdProducto=" + IdProducto + ", nombre=" + nombre + ", precio=" + precio + ", stockActual="
				+ stockActual + ", categoria=" + categoria + ", talle=" + talle + "]";
	}
	
	
	// crear producto
	public static void crearProducto() {
	    String nombre = Validaciones.validarLetras("Nombre del producto:");
	    if (nombre == null) return;
	    Double precio = Validaciones.validarDouble("Precio:");
	    if (precio == null) return;
	    Integer stock = Validaciones.validarEnteroCero("Stock inicial:");
	    if (stock == null) return;
	    String talle = Validaciones.validarLetras("Talle:");
	    if (talle == null) return;
	    CategoriasProd categoria = CategoriasProd.ElegirCategoria();
	    if (categoria == null) return;

	    boolean creado = ProductoDAO.crearProducto(nombre, precio, stock, categoria, talle);
	    if (creado) {
	        JOptionPane.showMessageDialog(null, "Producto creado correctamente"); }
	                                 }


	// buscar producto
	public static void buscarProductos() {
	    String id = Validaciones.validarNumeros("Código del producto a buscar:");
	    if (id == null) return;

	    Producto p = ProductoDAO.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;
	    }

	    JOptionPane.showMessageDialog(null,
	        "Nombre: " + p.getNombre() +
	        "\nPrecio: $" + p.getPrecio() +
	        "\nStock: " + p.getStockActual() +
	        "\nCategoría: " + p.getCategoria() +
	        "\nTalle: " + p.getTalle());
	}


	// editar producto
	public static void editarProducto() {
	    String id = Validaciones.validarNumeros("Código del producto a editar:");
	    if (id == null) return;

	    Producto p = ProductoDAO.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return; }
	    String nuevoNombre = Validaciones.validarLetras("Nombre del producto:", p.getNombre());
	    if (nuevoNombre == null) return;
	    Double nuevoPrecio = Validaciones.validarDouble("Precio:", String.valueOf(p.getPrecio()));
	    if (nuevoPrecio == null) return;
	    Integer nuevoStock = Validaciones.validarEnteroCero("Stock:", String.valueOf(p.getStockActual()));
	    if (nuevoStock == null) return;
	    String nuevoTalle = Validaciones.validarLetras("Talle:", p.getTalle());
	    if (nuevoTalle == null) return;
	    CategoriasProd nuevaCategoria = CategoriasProd.ElegirCategoria(p.getCategoria());
	    if (nuevaCategoria == null) return;

	    boolean editado = ProductoDAO.editarProducto(id, nuevoNombre, nuevoPrecio, nuevoStock, nuevaCategoria, nuevoTalle);
	    if (editado) {
	        JOptionPane.showMessageDialog(null, "Producto editado correctamente");}
	    }

	
	

	// eliminar producto
	public static void eliminarProducto() {
	    String id = Validaciones.validarNumeros("Código del producto a eliminar:");
	    if (id == null) return;

	    Producto p = ProductoDAO.buscarProductoPorId(id);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese código");
	        return;}

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar " + p.getNombre() + "?",
	            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	    if (confirmacion == JOptionPane.YES_OPTION) {
	        boolean eliminado = ProductoDAO.desactivarProducto(id);
	        if (eliminado) {
	            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente"); } }
	}


	// ver productos, filtros y búsqueda
	public static void verProductos() {
	    CategoriasProd[] categorias = CategoriasProd.values();
	    String[] filtros = new String[categorias.length + 1];
	    filtros[0] = "Todas";
	    for (int i = 0; i < categorias.length; i++) {
	        filtros[i + 1] = categorias[i].name(); }

	    String filtro = "Todas";
	    String busqueda = "";
	    boolean seguir = true;

	    while (seguir) {
	        String lista = ProductoDAO.obtenerListaProductos(filtro, busqueda);

	        if (lista.isEmpty()) {
	            if (filtro.equals("Todas") && busqueda.isEmpty()) {
	                JOptionPane.showMessageDialog(null, "No hay productos cargados");
	                return; }
	            lista = "No se encontraron productos."; }

	        JComboBox<String> combo = new JComboBox<>(filtros);
	        combo.setSelectedItem(filtro);
	        JTextField campoBusqueda = new JTextField(busqueda);

	        JTextArea areaLista = new JTextArea(lista);
	        areaLista.setEditable(false);
	        areaLista.setCaretPosition(0);
	        JScrollPane scroll = new JScrollPane(areaLista);
	        scroll.setPreferredSize(new Dimension(600, 350));

	        Object[] mensaje = {"Categoría:", combo, "Buscar por nombre:", campoBusqueda, " ", scroll};

	        int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Productos - " + filtro,
	                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);

	        String nuevoFiltro = (String) combo.getSelectedItem();
	        String nuevaBusqueda = campoBusqueda.getText().trim();

	        if (opcion != JOptionPane.OK_OPTION) {
	            seguir = false;
	        } else if (!nuevoFiltro.equals(filtro) || !nuevaBusqueda.equals(busqueda)) {
	            filtro = nuevoFiltro;
	            busqueda = nuevaBusqueda;
	        } else if (!filtro.equals("Todas") || !busqueda.isEmpty()) {
	            filtro = "Todas";
	            busqueda = "";
	        } else {
	            seguir = false; } }
	}


	// ver bajo stock
	public static void verBajoStock() {
	    String lista = ProductoDAO.obtenerListaBajoStock();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay productos con bajo stock");
	        return;
	    }

	    JTextArea areaLista = new JTextArea(lista);
	    areaLista.setEditable(false);
	    areaLista.setCaretPosition(0);
	    JScrollPane scroll = new JScrollPane(areaLista);
	    scroll.setPreferredSize(new Dimension(600, 350));

	    JOptionPane.showMessageDialog(null, scroll, "Bajo stock (menos de 20 unidades)", JOptionPane.PLAIN_MESSAGE);
	}


	

	
	
	
}
