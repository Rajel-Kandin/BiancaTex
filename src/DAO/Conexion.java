package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexion {

	//de manera remota
	private static String URL ="jdbc:mysql://b6bb2p9uv4x0rs85ifrh-mysql.services.clever-cloud.com:3306/b6bb2p9uv4x0rs85ifrh";
	private static String USER = "uukognpjp4av0ws7";
	private static String PASSWORD ="JwqcAwKvP7dFmidMoWR4";


	//de manera local
	/*private static String URL = "jdbc:mysql://localhost:3306/biancatex";
	private static String USER = "root";
	private static String PASSWORD = "";*/


	private static Connection conect;
	private static Conexion instance;

	private Conexion() {
		try {
			conect = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Se conectó");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"No se pudo conectar a la base de datos.\n"
					+ "Verificá que MySQL esté iniciado y que los datos de conexión sean correctos.\n\n"
					+ "Detalle: " + e.getMessage(),
					"Error de conexión", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}}

	public static Conexion getInstance() {
		if (instance == null) {
			instance = new Conexion();
		}
		return instance;
	}

	public Connection getConnection() {
		return conect;
	}

}
