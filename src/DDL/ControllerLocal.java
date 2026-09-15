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
    
    
}