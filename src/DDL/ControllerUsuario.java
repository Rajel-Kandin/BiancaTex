package DDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import BLL.Admin;
import BLL.Contador;
import BLL.Empleado;
import BLL.Local;
import BLL.Usuario;
public class ControllerUsuario {

	
	//ingresar
    public static Usuario login(String nombre) {
        Usuario usuario = null;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM usuario WHERE nombre = ?"
            );
            stmt.setString(1, nombre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean activo = rs.getBoolean("activo");
                if (!activo) {
                    rs.close();
                    stmt.close();
                    return null; // usuario baneado
                }

                int idUsuario = rs.getInt("id_usuario");
                String dni = rs.getString("dni");
                String nombreDb = rs.getString("nombre");
                String email = rs.getString("email");
                int tipo = rs.getInt("fk_tipoDeUsuario");
                int fkLocal = rs.getInt("fk_local"); // queda en 0 si es NULL

                switch (tipo) {
                    case 1:
                        usuario = new Admin(idUsuario, dni, nombreDb, email, activo);
                        break;
                    case 2:
                        Local local = obtenerLocal(fkLocal);
                        usuario = new Empleado(idUsuario, dni, nombreDb, email, activo, local);
                        break;
                    case 3:
                        usuario = new Contador(idUsuario, dni, nombreDb, email, activo);
                        break;
                } }

            rs.close();
            stmt.close(); } 
        
        catch (SQLException e) {
            e.printStackTrace(); }
        return usuario; }

    
    //obtiene el local
    private static Local obtenerLocal(int idLocal) throws SQLException {
        Local local = null;
        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
            "SELECT * FROM local WHERE id_local = ?"
        );
        stmt.setInt(1, idLocal);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            local = new Local(rs.getInt("id_local"), rs.getString("direccion"));
        }
        rs.close();
        stmt.close();
        return local; }
    
    
    
    //crea usuario
    public static boolean crearUsuario(String dni, String nombre, String email, int tipo, Integer fkLocal) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "INSERT INTO usuario (dni, nombre, email, fk_tipoDeUsuario, fk_local, activo) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.setInt(4, tipo);

            if (fkLocal != null) {
                stmt.setInt(5, fkLocal);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER); }

            stmt.setBoolean(6, true); // activo por defecto
            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese DNI o email");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  }}
    
    
 // Busca usuario por DNI y devuelve sus datos
    public static Object[] buscarUsuarioPorDni(String dni) {
        Object[] datos = null;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM usuario WHERE dni = ?"
            );
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                datos = new Object[]{
                    rs.getInt("id_usuario"),
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getInt("fk_tipoDeUsuario"),
                    rs.getObject("fk_local"), // puede venir null
                    rs.getBoolean("activo")
                };}
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); }
        return datos; }

    // Edita los datos de un usuario existente
    public static boolean editarUsuario(int idUsuario, String dni, String nombre, String email, int tipo, Integer fkLocal) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE usuario SET dni = ?, nombre = ?, email = ?, fk_tipoDeUsuario = ?, fk_local = ? WHERE id_usuario = ?" );
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.setInt(4, tipo);

            if (fkLocal != null) {
                stmt.setInt(5, fkLocal);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.setInt(6, idUsuario);

            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Ya existe otro usuario con ese DNI o email");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;}}

    
    // "Eliminar" = desactivar (activo = 0), no borrar la fila 
    public static boolean eliminarUsuario(int idUsuario) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE usuario SET activo = 0 WHERE id_usuario = ?"
            );
            stmt.setInt(1, idUsuario);
            int filas = stmt.executeUpdate();
            stmt.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; }}
   
	
}
