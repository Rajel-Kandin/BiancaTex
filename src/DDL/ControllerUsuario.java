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
import repository.Hashing;
public class ControllerUsuario {

	
	//ingresar
	public static Usuario login(String nombre, String password) {
	    Usuario usuario = null;
	    try {
	        PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
	            "SELECT * FROM usuario WHERE nombre = ?"
	        );
	        stmt.setString(1, nombre);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next() && Hashing.verificar(password, rs.getString("password"))) {
	            int id = rs.getInt("id_usuario");
	            String dniBd = rs.getString("dni");
	            String nombreBd = rs.getString("nombre");
	            String email = rs.getString("email");
	            int tipo = rs.getInt("fk_tipoDeUsuario");
	            boolean activo = rs.getBoolean("activo");

	            if (tipo == 1) {
	                usuario = new Admin(id, dniBd, nombreBd, email, activo);
	            } else if (tipo == 2) {
	                Local local = obtenerLocal(rs.getInt("fk_local"));
	                usuario = new Empleado(id, dniBd, nombreBd, email, activo, local);
	            } else if (tipo == 3) {
	                usuario = new Contador(id, dniBd, nombreBd, email, activo);
	            }
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return usuario;
	}
    
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
    public static boolean crearUsuario(String dni, String nombre, String email, String password, int tipo, Integer fkLocal) {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "INSERT INTO usuario (dni, nombre, email, password, fk_tipoDeUsuario, fk_local, activo) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.setString(4, Hashing.hash(password));
            stmt.setInt(5, tipo);

            if (fkLocal != null) {
                stmt.setInt(6, fkLocal);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER); }

            stmt.setBoolean(7, true); // activo por defecto
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
    public static Usuario buscarUsuarioPorDni(String dni) {    	Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE dni = ?";

        try (PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int id          = rs.getInt("id_usuario");
                    String dniBd    = rs.getString("dni");
                    String nombre   = rs.getString("nombre");
                    String email    = rs.getString("email");
                    int tipo        = rs.getInt("fk_tipoDeUsuario");
                    boolean activo  = rs.getBoolean("activo");

                    switch (tipo) {
                        case 1:
                            usuario = new Admin(id, dniBd, nombre, email, activo);
                            break;
                        case 2:
                            int idLocal = rs.getInt("fk_local"); // 0 si es null
                            Local local = obtenerLocal(idLocal); // ajustá si tu método tiene otra firma
                            usuario = new Empleado(id, dniBd, nombre, email, activo, local);
                            break;
                        case 3:
                            usuario = new Contador(id, dniBd, nombre, email, activo);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario; }

    
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

    
    // desactivar (activo = 0) 
    public static boolean desactivarUsuario(int idUsuario) {
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
    
    
 // muestra los usuarios
    public static String obtenerListaUsuarios() {
        String lista = "";

        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.email, t.tipo, u.fk_local, u.activo " +
                     "FROM usuario u " +
                     "JOIN tipoDeUsuario t ON u.fk_tipoDeUsuario = t.id_tipoDeUsuario " +
                     "ORDER BY u.nombre";

        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int fkLocal = rs.getInt("fk_local");
                String local = rs.wasNull() ? "-" : String.valueOf(fkLocal);
                String activo = rs.getBoolean("activo") ? "Sí" : "No";

                lista += "ID: " + rs.getInt("id_usuario") +
                         " | DNI: " + rs.getString("dni") +
                         " | " + rs.getString("nombre") +
                         " | " + rs.getString("email") +
                         " | " + rs.getString("tipo") +
                         " | Local: " + local +
                         " | Activo: " + activo + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
   
	
}
