package GUI;

import java.awt.Image;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import BLL.Usuario;
import BLL.Validacion;
import DDL.ControllerUsuario;
import repository.Hashing;

public class main {

    private static LinkedList<Usuario> usuarios = new LinkedList<Usuario>();

    public static void main(String[] args) {

        String[] menuIngreso = {"Ingresar", "Salir"};
        String nombreIngresado;
        int elegidoIngreso;

        do {
            elegidoIngreso = JOptionPane.showOptionDialog(null, "Bienvenido al sistema de BiancaTex",
                    "Ingreso", 0, 0, null, menuIngreso, menuIngreso[0]);

            switch (elegidoIngreso) {
                case 0:
                    nombreIngresado = Validacion.ValidarTextoCancelable("Ingrese su usuario:");
                    if (nombreIngresado == null) {
                        break; }
                    nombreIngresado = nombreIngresado.toLowerCase();

                    String passwordIngresada = Validacion.ValidarTextoCancelable("Ingrese su contraseña:");
                    if (passwordIngresada == null) {
                        break; }

                    Usuario usuarioLogueado = ControllerUsuario.login(nombreIngresado, passwordIngresada);
                    if (usuarioLogueado != null) {
                        usuarioLogueado.verMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                    }
                    break;
            }
        } while (elegidoIngreso != 1 && elegidoIngreso != JOptionPane.CLOSED_OPTION);
    }

    /*	public static void cargarUsuarios() {
            usuarios.add(new Admin(1, "25369741", "salomon jakim","salomonjakim@biancatex.com", true));  -- 1234
            usuarios.add(new Admin(2 , "36941257", "jose rossi","josirossi@biancatex.com", true));      -- 5555

            usuarios.add(new Empleado(3, "28741663", "marcelo gonzalez", "marcelogonzalez@biancatex.com", true, null)); -- 1111
            usuarios.add(new Empleado(4, "37961358", "martin romero", "martinromero@biancatex.com", true, null));       --3333
            usuarios.add(new Empleado(5, "41339114", "daniel gomez", "danielgomez@biancatex.com", true, null));         --2026

            usuarios.add(new Contador(6, "31677822", "Ferran diaz","ferrandiaz@biancatex.com", true));}              --contador*/

    //funcion para redimensionar img
    public static ImageIcon RedimensionarImg(String nombreImg, int ancho, int alto) {
        ImageIcon imagen = new ImageIcon(main.class.getResource(nombreImg));
        Image recortado = imagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        imagen = new ImageIcon(recortado);
        return imagen;
    }
}
		
