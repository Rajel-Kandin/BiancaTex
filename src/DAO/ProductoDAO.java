package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import BLL.CategoriasProd;
import BLL.Producto;

public class ProductoDAO {

    // crea producto 
    public static boolean crearProducto(String nombre, double precio, int stock, CategoriasProd categoria, String talle) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "INSERT INTO producto (nombre, precio, stock, categoria, talle) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setInt(3, stock);
            stmt.setString(4, categoria.name());
            stmt.setString(5, talle);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;    } }


    // Busca producto por id
    public static Producto buscarProductoPorId(String idProducto) {
        Producto producto = null;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto WHERE id_productos = ?"
            );
            stmt.setString(1, idProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CategoriasProd categoria = null;
                String textoCategoria = rs.getString("categoria");
                for (CategoriasProd c : CategoriasProd.values()) {
                    if (c.name().equalsIgnoreCase(textoCategoria)) {
                        categoria = c;
                    }
                }

                producto = new Producto(
                    String.valueOf(rs.getInt("id_productos")),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    categoria,
                    rs.getString("talle")
                );
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return producto;
    }


    // edita producto
    public static boolean editarProducto(String idProducto, String nombre, double precio, int stock, CategoriasProd categoria, String talle) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE producto SET nombre = ?, precio = ?, stock = ?, categoria = ?, talle = ? WHERE id_productos = ?"
            );
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setInt(3, stock);
            stmt.setString(4, categoria.name());
            stmt.setString(5, talle);
            stmt.setString(6, idProducto);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; }
    }


    // desactiva un producto 
    public static boolean desactivarProducto(String idProducto) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE producto SET activo = 0 WHERE id_productos = ? AND activo = 1"
            );
            stmt.setString(1, idProducto);
            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;}
                                     }

    // productos activos con stock menor a 20
    public static String obtenerListaBajoStock() {
        StringBuilder lista = new StringBuilder();

        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto WHERE stock < 20 AND activo = 1 ORDER BY stock"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.append("ID: " + rs.getInt("id_productos") +
                             " | " + rs.getString("nombre") +
                             " | " + rs.getString("categoria") +
                             " | Talle: " + rs.getString("talle") +
                             " | Stock: " + rs.getInt("stock") + "\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return lista.toString();
                                         }
    
    
 // actualiza el stock de producto
    public static boolean actualizarStock(String idProducto, int nuevoStock) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE producto SET stock = ? WHERE id_productos = ?"
            );
            stmt.setInt(1, nuevoStock);
            stmt.setString(2, idProducto);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;   } }
    

 
    // trae todos los productos activos
    public static LinkedList<Producto> obtenerProductos() {
        LinkedList<Producto> productos = new LinkedList<Producto>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto WHERE activo = 1 ORDER BY nombre"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriasProd categoria = null;
                String textoCategoria = rs.getString("categoria");
                for (CategoriasProd c : CategoriasProd.values()) {
                    if (c.name().equalsIgnoreCase(textoCategoria)) {
                        categoria = c;  }}
                Producto p = new Producto(
                    String.valueOf(rs.getInt("id_productos")),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    categoria,
                    rs.getString("talle")
                );
                productos.add(p);}
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();  }
        return productos;  }
    
    
    
   //productos activos
    public static String obtenerListaProductos() {
        return obtenerListaProductos("Todas", "");
                                            }


    //productos activos filtrados por categoría
    public static String obtenerListaProductos(String categoria) {
        return obtenerListaProductos(categoria, "");
                                                    }


    // muestra los productos activos filtrados por categoría y nombre
    public static String obtenerListaProductos(String categoria, String busqueda) {
        StringBuilder lista = new StringBuilder();

        try {
            PreparedStatement stmt;
            if (categoria.equals("Todas")) {
                stmt = Conexion.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM producto WHERE activo = 1 AND nombre LIKE ? ORDER BY nombre"
                );
                stmt.setString(1, "%" + busqueda + "%");
            } else {
                stmt = Conexion.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM producto WHERE activo = 1 AND categoria = ? AND nombre LIKE ? ORDER BY nombre"
                );
                stmt.setString(1, categoria);
                stmt.setString(2, "%" + busqueda + "%");
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.append("ID: " + rs.getInt("id_productos") +
                             " | " + rs.getString("nombre") +
                             " | $" + rs.getDouble("precio") +
                             " | Stock: " + rs.getInt("stock") +
                             " | " + rs.getString("categoria") +
                             " | Talle: " + rs.getString("talle") + "\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return lista.toString();
                                                 }
    
    
    // productos activos filtrados por categoria y por nombre
    public static LinkedList<Producto> obtenerProductosFiltrados(String categoria, String busqueda) {
        LinkedList<Producto> productos = new LinkedList<Producto>();
        try {
            PreparedStatement stmt;
            if (categoria.equals("Todas")) {
                stmt = Conexion.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM producto WHERE activo = 1 AND nombre LIKE ? ORDER BY nombre");
                stmt.setString(1, "%" + busqueda + "%");
            } else {
                stmt = Conexion.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM producto WHERE activo = 1 AND categoria = ? AND nombre LIKE ? ORDER BY nombre");
                stmt.setString(1, categoria);
                stmt.setString(2, "%" + busqueda + "%");
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productos.add(armarProducto(rs)); }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return productos;  }

    // arma un Producto con la fila actual del ResultSet
    private static Producto armarProducto(ResultSet rs) throws SQLException {
        CategoriasProd categoria = null;
        String textoCategoria = rs.getString("categoria");
        for (CategoriasProd c : CategoriasProd.values()) {
            if (c.name().equalsIgnoreCase(textoCategoria)) {
                categoria = c;}  }
        return new Producto(
            String.valueOf(rs.getInt("id_productos")),
            rs.getString("nombre"),
            rs.getDouble("precio"),
            rs.getInt("stock"),
            categoria,
            rs.getString("talle"));
                  }
    
   
    
    
}