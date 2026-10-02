package BLL;

import javax.swing.JOptionPane;

public class Validacion {

    // validacion numeros enteros (si cancela, vuelve a preguntar)
    public static int ValidarNum(String mensaje) {
        String dato;
        boolean flag;
        do {
            flag = true;
            dato = JOptionPane.showInputDialog(mensaje);
            if (dato == null || dato.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El dato no puede estar vacío.");
                flag = false;
            } else if (dato.length() > 9) {
                JOptionPane.showMessageDialog(null, "El número es demasiado largo.");
                flag = false;
            } else {
                for (int i = 0; i < dato.length(); i++) {
                    if (!Character.isDigit(dato.charAt(i))) {
                        JOptionPane.showMessageDialog(null, "Solo puede ingresar números enteros");
                        flag = false;
                        break;
                    }
                }
            }
        } while (flag == false);
        return Integer.parseInt(dato);
    }

    // numeros enteros: si toca Cancelar devuelve -1
    public static int ValidarNumCancelable(String mensaje) {
        String dato;
        boolean flag;
        do {
            flag = true;
            dato = JOptionPane.showInputDialog(mensaje);
            if (dato == null) {
                return -1; // canceló
            }
            if (dato.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El dato no puede estar vacío.");
                flag = false;
            } else if (dato.length() > 9) {
                JOptionPane.showMessageDialog(null, "El número es demasiado largo.");
                flag = false;
            } else {
                for (int i = 0; i < dato.length(); i++) {
                    if (!Character.isDigit(dato.charAt(i))) {
                        JOptionPane.showMessageDialog(null, "Solo puede ingresar números enteros");
                        flag = false;
                        break;
                    }
                }
            }
        } while (flag == false);
        return Integer.parseInt(dato);
    }
    
    

    // numero entero dentro de un rango (si cancela, vuelve a preguntar)
    public static int ValidarRango(String mensaje, int min, int max) {
        int numero;
        do {
            numero = ValidarNum(mensaje);
            if (numero < min || numero > max) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un número entre " + min + " y " + max + ".");
            }
        } while (numero < min || numero > max);
        return numero;
    }

    // numero entero dentro de un rango: si toca Cancelar devuelve -1
    public static int ValidarRangoCancelable(String mensaje, int min, int max) {
        int numero;
        do {
            numero = ValidarNumCancelable(mensaje);
            if (numero == -1) {
                return -1; // canceló
            }
            if (numero < min || numero > max) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un número entre " + min + " y " + max + ".");
            }
        } while (numero < min || numero > max);
        return numero;
    }

    // solo letras 
    public static String ValidarLetras(String mensaje) {
        String dato;
        boolean flag;
        do {
            flag = true;
            dato = JOptionPane.showInputDialog(mensaje);
            if (dato == null || dato.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El dato no puede estar vacío.");
                flag = false;
            } else {
                for (int i = 0; i < dato.length(); i++) {
                    if (!Character.isLetter(dato.charAt(i)) && dato.charAt(i) != ' ') {
                        JOptionPane.showMessageDialog(null, "Solo puede ingresar letras");
                        flag = false;
                        break;
                    }
                }
            }
        } while (flag == false);
        return dato.trim(); }
    
    

    // texto (cualquier caracter, pero no vacio)
    public static String ValidarTexto(String mensaje) {
        String dato;
        do {
            dato = JOptionPane.showInputDialog(mensaje);
            if (dato == null || dato.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El dato no puede estar vacío.");
            }
        } while (dato == null || dato.trim().isEmpty());
        return dato.trim();
    }
    
    
    // texto (cualquier caracter, no vacio): si toca Cancelar devuelve null
    public static String ValidarTextoCancelable(String mensaje) {
        String dato;
        do {
            dato = JOptionPane.showInputDialog(mensaje);
            if (dato == null) {
                return null; // canceló
            }
            if (dato.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El dato no puede estar vacío.");
            }
        } while (dato.trim().isEmpty());
        return dato.trim();
    }
    
    
}