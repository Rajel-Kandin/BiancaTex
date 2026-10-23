package DDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import BLL.Proveedor;

public class ControllerProveedor {

    // crea proveedor
    public static boolean crearProveedor(String cuit, String nombre, String direccion, String telefono) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "INSERT INTO proveedor (cuit, nombre, direccion, telefono) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, cuit);
            stmt.setString(2, nombre);
            stmt.setString(3, direccion);
            stmt.setString(4, telefono);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Ya existe un proveedor con ese CUIT");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  } }


    // Busca proveedor por CUIT 
    public static Proveedor buscarProveedorPorCuit(String cuit) {
        Proveedor proveedor = null;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM proveedor WHERE cuit = ?"
            );
            stmt.setString(1, cuit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                proveedor = new Proveedor(
                    rs.getString("nombre"),
                    rs.getString("cuit"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                );
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedor; }


    // Edita los datos de un proveedor 
    public static boolean editarProveedor(String cuit, String nombre, String direccion, String telefono) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE proveedor SET nombre = ?, direccion = ?, telefono = ? WHERE cuit = ?"
            );
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.setString(4, cuit);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;   } }

    

 // desactiva un proveedor (borrado lógico)
    public static boolean desactivarProveedor(String cuit) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE proveedor SET activo = 0 WHERE cuit = ? AND activo = 1"
            );
            stmt.setString(1, cuit);
            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; }
                                      }
    
    
    // muestra los proveedores activos
    public static String obtenerListaProveedores() {
        String lista = "";

        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM proveedor WHERE activo = 1 ORDER BY nombre"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista += "CUIT: " + rs.getString("cuit") +
                         " | " + rs.getString("nombre") +
                         " | Dirección: " + rs.getString("direccion") +
                         " | Tel: " + rs.getString("telefono") + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    // trae los proveedores 
    public static LinkedList<Proveedor> obtenerProveedores() {
        LinkedList<Proveedor> proveedores = new LinkedList<Proveedor>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM proveedor WHERE activo = 1 ORDER BY nombre"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proveedor p = new Proveedor(
                    rs.getString("nombre"),
                    rs.getString("cuit"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                );
                proveedores.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }}