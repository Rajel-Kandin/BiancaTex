package DDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import BLL.Local;

public class ControllerLocal {

    public static LinkedList<Local> obtenerLocales() {
        LinkedList<Local> locales = new LinkedList<>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "SELECT * FROM local"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                locales.add(new Local(rs.getInt("id_local"), rs.getString("direccion")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locales;
    }

    public static Local buscarLocalPorId(int idLocal) {
        Local local = null;
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return local;
    }

    public static boolean crearLocal(String direccion) {
        boolean exito = false;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "INSERT INTO local (direccion) VALUES (?)"
            );
            stmt.setString(1, direccion);
            exito = stmt.executeUpdate() > 0;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    public static boolean editarLocal(Local local) {
        boolean exito = false;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "UPDATE local SET direccion = ? WHERE id_local = ?"
            );
            stmt.setString(1, local.getDireccion());
            stmt.setInt(2, local.getIdLocal());
            exito = stmt.executeUpdate() > 0;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    public static boolean eliminarLocal(int idLocal) {
        boolean exito = false;
        try {
            PreparedStatement stmt = Conexion.getInstance().getConnection().prepareStatement(
                "DELETE FROM local WHERE id_local = ?"
            );
            stmt.setInt(1, idLocal);
            exito = stmt.executeUpdate() > 0;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }
}