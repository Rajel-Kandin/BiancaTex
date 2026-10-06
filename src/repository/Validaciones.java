package repository;

import javax.swing.JOptionPane;

public class Validaciones {

    // Entero mayor a 0
    public static Integer validarEntero(String mensaje) {
        String entrada;
        int numero = 0;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje);
            if (entrada == null) return null; // Cancelar

            try {
                numero = Integer.parseInt(entrada.trim());
                if (numero > 0) {
                    valido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El número tiene que ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresá solo números enteros.");
            }
        } while (!valido);
        return numero;
    }

    // Número con decimales mayor a 0 
    public static Double validarDouble(String mensaje) {
        String entrada;
        double numero = 0;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje);
            if (entrada == null) return null; // Cancelar

            try {
                numero = Double.parseDouble(entrada.trim().replace(",", "."));
                if (numero > 0) {
                    valido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El número tiene que ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresá un número válido (ej: 1500 o 1500,50).");
            }
        } while (!valido);
        return numero;
    }

    // Solo letras 
    public static String validarLetras(String mensaje) {
        String entrada;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje);
            if (entrada == null) return null; // Cancelar

            entrada = entrada.trim();
            valido = !entrada.isEmpty();
            for (int i = 0; i < entrada.length(); i++) {
                char c = entrada.charAt(i);
                if (!Character.isLetter(c) && c != ' ') {
                    valido = false;
                }
            }
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Ingresá solo letras.");
            }
        } while (!valido);
        return entrada;
    }
    
    
    // Solo números (CUIT, teléfono)
    public static String validarNumeros(String mensaje) {
        String entrada;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje);
            if (entrada == null) return null; // Cancelar

            entrada = entrada.trim();
            valido = !entrada.isEmpty();
            for (int i = 0; i < entrada.length(); i++) {
                char c = entrada.charAt(i);
                if (!Character.isDigit(c)) {
                    valido = false;
                }
            }
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Ingresá solo números.");
            }
        } while (!valido);
        return entrada;
    }
    
    
}