package DDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import BLL.CategoriasProd;
import BLL.Producto;

public class ControllerProducto {

    // crea producto (el id lo genera MySQL)
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
            return false;
        }
    }


    // Busca producto por id y devuelve el objeto Producto (o null si no existe)
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
            e.printStackTrace();
        }
        return producto;
    }


    // Edita los datos de un producto existente
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
            return false;
        }
    }


    // elimina un producto
    public static boolean eliminarProducto(String idProducto) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "DELETE FROM producto WHERE id_productos = ?"
            );
            stmt.setString(1, idProducto);
            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar: el producto tiene ventas o compras asociadas");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // muestra los productos
    public static String obtenerListaProductos() {
        String lista = "";

        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto ORDER BY nombre"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista += "ID: " + rs.getInt("id_productos") +
                         " | " + rs.getString("nombre") +
                         " | $" + rs.getDouble("precio") +
                         " | Stock: " + rs.getInt("stock") +
                         " | " + rs.getString("categoria") +
                         " | Talle: " + rs.getString("talle") + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
 // productos con stock menor a 20
    public static String obtenerListaBajoStock() {
        String lista = "";

        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto WHERE stock < 20 ORDER BY stock"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista += "ID: " + rs.getInt("id_productos") +
                         " | " + rs.getString("nombre") +
                         " | " + rs.getString("categoria") +
                         " | Talle: " + rs.getString("talle") +
                         " | Stock: " + rs.getInt("stock") + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
           return lista; }
    
    
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
    

 // trae todos los productos como objetos  (para ver en las compras y ventas)
    public static LinkedList<Producto> obtenerProductos() {
        LinkedList<Producto> productos = new LinkedList<Producto>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM producto ORDER BY nombre"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriasProd categoria = null;
                String textoCategoria = rs.getString("categoria");
                for (CategoriasProd c : CategoriasProd.values()) {
                    if (c.name().equalsIgnoreCase(textoCategoria)) {
                        categoria = c;
                    }
                }

                Producto p = new Producto(
                    String.valueOf(rs.getInt("id_productos")),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    categoria,
                    rs.getString("talle")
                );
                productos.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;  }
    
    
    
   
    
    
}