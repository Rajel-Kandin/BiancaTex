package GUI;

import javax.swing.JOptionPane;

import BLL.Usuario;
import DDL.ControllerUsuario;
import repository.Validaciones;

public class main {

    public static void main(String[] args) {

        String[] menuIngreso = {"Ingresar", "Salir"};
        String emailIngresado;
        int elegidoIngreso;

        do {
            elegidoIngreso = JOptionPane.showOptionDialog(null, "Bienvenido al sistema de BiancaTex",
                    "Ingreso", 0, 0, null, menuIngreso, menuIngreso[0]);

            switch (elegidoIngreso) {
                case 0:
                    emailIngresado = Validaciones.validarEmail("Ingrese su email:");
                    if (emailIngresado == null) {
                        break; }

                    String passwordIngresada = JOptionPane.showInputDialog("Ingrese su contraseña:");
                    if (passwordIngresada == null) {
                        break; }

                    if (passwordIngresada.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía.");
                        break; }

                    Usuario usuarioLogueado = ControllerUsuario.login(emailIngresado, passwordIngresada);
                    if (usuarioLogueado != null) {
                        usuarioLogueado.verMenu(); }
                    break; }
        } while (elegidoIngreso != 1 && elegidoIngreso != JOptionPane.CLOSED_OPTION); }
    
    
}